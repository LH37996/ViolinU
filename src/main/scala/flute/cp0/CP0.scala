package flute.cp0

import chisel3._
import chisel3.util._

import flute.config.CPUConfig._
import chisel3.stage.ChiselStage

class CP0Read extends Bundle {
  val addr = Input(UInt(5.W))
  val sel  = Input(UInt(3.W))
  val data = Output(UInt(dataWidth.W))
}

class CP0Write extends Bundle {
  val addr   = Input(UInt(5.W))
  val sel    = Input(UInt(3.W))
  val data   = Input(UInt(dataWidth.W))
  val enable = Input(Bool())
}

class CP0WithCommit extends Bundle {
  val pc         = Input(UInt(addrWidth.W))
  val inSlot     = Input(Bool()) // whether the instruction in pc is delay slot
  val exceptions = Input(new ExceptionBundle)
  val completed  = Input(Bool())
}

class CP0 extends Module {
  val io = IO(new Bundle {
    val read    = new CP0Read
    val write   = new CP0Write
    val commit  = new CP0WithCommit
    val hwIntr  = Input(UInt(6.W))
    val intrReq = Output(Bool()) // 例外输出信号
  })

  val badvaddr = new CP0BadVAddr
  val count    = new CP0Count
  val status   = new CP0Status
  val cause    = new CP0Cause
  val epc      = new CP0EPC
  val compare  = new CP0Compare
  val countInc = RegInit(0.B)

  val regs = Seq(
    badvaddr,
    count,
    status,
    cause,
    epc,
    compare,
  )
  val readRes = WireInit(0.U(dataWidth.W))
  regs.foreach(r =>
    when(io.read.addr === r.addr.U && io.read.sel === r.sel.U) {
      readRes := r.reg.asUInt
    }
  )
  io.read.data := readRes

  countInc := !countInc

  val commitWire = io.commit
  // val completedWire = io.commit.completed
  val excVector = VecInit(io.commit.exceptions.asUInt.asBools.map(_ && io.commit.completed))
    .asTypeOf(new ExceptionBundle)

  val hasExc = excVector.asUInt.orR
  val intReqs = for (i <- 0 until 8) yield {
    cause.reg.ip(i) && status.reg.im(i)
  }
  val hasInt = intReqs.foldLeft(0.B)((z, a) => z || a) && status.reg.ie && !status.reg.exl
  val exceptionReqestsNext = 0.B // TODO: 不同类型的异常应当要求从本指令/下一条指令执行
  when(hasInt || hasExc) {
    when(!commitWire.completed) {
      epc.reg := Mux(commitWire.inSlot, commitWire.pc - 4.U, commitWire.pc)
    }.otherwise {
      cause.reg.bd := commitWire.inSlot
      when(hasInt) {
        epc.reg := Mux(commitWire.inSlot, commitWire.pc - 4.U, commitWire.pc)
      }.otherwise {
        when(commitWire.inSlot) {
          epc.reg := commitWire.pc - 4.U
        }.elsewhen(exceptionReqestsNext) {
          epc.reg := commitWire.pc + 4.U
        }.otherwise {
          epc.reg := commitWire.pc
        }
      }
    }
  }

  io.intrReq := hasExc || hasInt

  def wReq(r: CP0BaseReg): Bool = {
    io.write.enable && io.write.addr === r.addr.U && io.write.sel === r.sel.U && !hasInt && !hasExc
  }

  // badvaddr

  // count
  when(wReq(count)) {
    count.reg := io.write.data
  }.otherwise {
    count.reg := count.reg + countInc.asUInt
  }

  // status
  val writeStatusWire = WireInit(io.write.data.asTypeOf(new StatusBundle))
  when(wReq(status)) {
    status.reg.exl := writeStatusWire.exl
    status.reg.ie  := writeStatusWire.ie
    status.reg.im  := writeStatusWire.im
  }

  // cause
  val writeCauseWire = WireInit(io.write.data.asTypeOf(new CauseBundle))
  when(wReq(cause)) {
    for (i <- 0 to 1) yield {
      cause.reg.ip(i) := writeCauseWire.ip(i)
    }
  }
  for (i <- 2 to 6) yield {
    cause.reg.ip(i) := io.hwIntr(i - 2)
  }
  when((wReq(count) || wReq(compare)) && !io.hwIntr(5)) {
    cause.reg.ip(7) := 0.B
  }.elsewhen(io.hwIntr(5) || (count.reg === compare.reg)) {
    cause.reg.ip(7) := 1.B
  }
  when(wReq(count) || wReq(compare)) {
    cause.reg.ti := 0.B
  }.elsewhen(count.reg === compare.reg) {
    cause.reg.ti := 1.B
  }

  // epc
  when(wReq(epc)) {
    epc.reg := io.write.data
  }
}

object CP0Main extends App {
  (new ChiselStage).emitVerilog(new CP0, Array("--target-dir", "target/verilog", "--target:fpga"))
}
