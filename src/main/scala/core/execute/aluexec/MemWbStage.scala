package core.execute.aluexec

import chisel3._

import core.components.StageReg
import config.CPUConfig._

class MemWbBundle extends Bundle {
  val control = new Bundle {
    val regWriteEn = Bool()
    val memToReg   = Bool()
  }

  val aluResult    = UInt(dataWidth.W)
  val dataFromMem  = UInt(dataWidth.W)
  val writeRegAddr = UInt(regAddrWidth.W)
}

class MemWbStage extends StageReg(new MemWbBundle) {}
