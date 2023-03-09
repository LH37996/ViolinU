package flute.core.frontend

import chisel3._
import chisel3.util._

import flute.config.CPUConfig._
import flute.config.BasicInstructions
import flute.util.BitPatCombine

class PreDecoderOutput extends Bundle {
  val predictBT = UInt(addrWidth.W)
  val isBranch  = Bool()
}

class PreDecode extends Module {

  private object Inst extends BasicInstructions {
    def needUpdate = BitPatCombine(Seq(J, JAL))
    def needWait = BitPatCombine(
      Seq(
        BEQ,
        BGEZ,
        BGEZAL,
        BGTZ,
        BLEZ,
        BLTZ,
        BLTZAL,
        BNE,
        JALR,
        JR,
        SYSCALL,
      )
    )
  }

  val io = IO(new Bundle {
    val instruction = Flipped(ValidIO(UInt(instrWidth.W)))
    val pc          = Input(UInt(addrWidth.W))
    val out         = Output(new PreDecoderOutput)
    // val stallReq    = Output(Bool())
  })

  val ins = io.instruction.bits

  when(io.instruction.valid && Inst.needUpdate(io.instruction.bits)) {
    io.out.predictBT := io.pc + Cat(Fill(12, ins(31)), ins(19, 12), ins(20), ins(30, 25), ins(24, 21), 0.U(1.W))
  }.otherwise {
    io.out.predictBT := io.pc + 4.U
  }

  io.out.isBranch := io.instruction.valid &&
    (Inst.needUpdate(io.instruction.bits) || Inst.needWait(io.instruction.bits))
}
