package flute.core

import chisel3._
import chisel3.util._
import flute.config.CPUConfig._
import flute.core.rename.ArfView
import flute.cp0.CP0

class BetaTop(memoryFile: String = "test_data/imem.in") extends Module {
  val io = IO(new Bundle {
    val hwIntr = Input(UInt(6.W))
    val pc     = Output(UInt(addrWidth.W))
    val arf    = Output(Vec(archRegAmount, UInt(dataWidth.W)))
  })

  val frontend = Module(new Frontend(memoryFile = memoryFile))
  val backend  = Module(new Backend)
  val cp0      = Module(new CP0)

  backend.io.ibuffer <> frontend.io.out
  frontend.io.branchCommit := backend.io.branchCommit
  io.pc                    := frontend.io.pc
  cp0.io.hwIntr            := io.hwIntr
  // TEMP //
  cp0.io.core.read         := DontCare
  cp0.io.core.write        := DontCare
  cp0.io.core.write.enable := 0.B
  // ==== //
  backend.io.cp0IntrReq := cp0.io.core.intrReq
  backend.io.cp0 <> cp0.io.core.commit

  val arfView = Module(new ArfView)
  arfView.io.rmtIn := backend.io.rmt
  arfView.io.prf   := backend.io.prf

  io.arf := arfView.io.arfOut

}
