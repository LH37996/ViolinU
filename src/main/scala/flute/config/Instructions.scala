package flute.config

import chisel3._
import chisel3.util.BitPat

trait BasicInstructions {
  /*** RV32I ***/
  /** Integer Computational Instructions **/
  /* Interger Register-Immediate Instructions */
  val ADDI   = BitPat("b?????????????????000?????0010011")
  val SLTI   = BitPat("b?????????????????010?????0010011")
  val SLTIU  = BitPat("b?????????????????011?????0010011")
  val ANDI   = BitPat("b?????????????????111?????0010011")
  val ORI    = BitPat("b?????????????????110?????0010011")
  val XORI   = BitPat("b?????????????????100?????0010011")
  val SLLI   = BitPat("b000000???????????001?????0010011")
  val SRLI   = BitPat("b000000???????????101?????0010011")
  val SRAI   = BitPat("b010000???????????101?????0010011")
  val LUI    = BitPat("b?????????????????????????0110111")
  val AUIPC  = BitPat("b?????????????????????????0010111")
  /* Interger Register-Register Instructions */
  val ADD    = BitPat("b0000000??????????000?????0110011")
  val SLT    = BitPat("b0000000??????????010?????0110011")
  val SLTU   = BitPat("b0000000??????????011?????0110011")
  val AND    = BitPat("b0000000??????????111?????0110011")
  val OR     = BitPat("b0000000??????????110?????0110011")
  val XOR    = BitPat("b0000000??????????100?????0110011")
  val SLL    = BitPat("b0000000??????????001?????0110011")  // SLL,NOP
  val SRL    = BitPat("b0000000??????????101?????0110011")
  val SUB    = BitPat("b0100000??????????000?????0110011")
  val SRA    = BitPat("b0100000??????????101?????0110011")
  /** Control Transfer Instructions **/
  /* Unconditional Jumps */
  val JAL    = BitPat("b?????????????????????????1101111")







  /** Logical Instructions **/
  

  val NOR    = BitPat("b000000???????????????00000100111")
  
  
  
  
  /** Arithmetic Instructions **/
  
  val ADDU   = BitPat("b000000???????????????00000100001")
  val ADDIU  = BitPat("b001001??????????????????????????")
  
  val SUBU   = BitPat("b000000???????????????00000100011")
  
  
  
  val MULT   = BitPat("b000000??????????0000000000011000")
  val MULTU  = BitPat("b000000??????????0000000000011001")
  val DIV    = BitPat("b000000??????????0000000000011010")
  val DIVU   = BitPat("b000000??????????0000000000011011")
  /** Branch and Jump Instructions **/
  val BEQ    = BitPat("b000100??????????????????????????")  // BEQ,B
  val BGEZ   = BitPat("b000001?????00001????????????????")
  val BGEZAL = BitPat("b000001?????10001????????????????")  // BGEZAL,BAL
  val BGTZ   = BitPat("b000111?????00000????????????????")
  val BLEZ   = BitPat("b000110?????00000????????????????")
  val BLTZ   = BitPat("b000001?????00000????????????????")
  val BLTZAL = BitPat("b000001?????10000????????????????")
  val BNE    = BitPat("b000101??????????????????????????")
  val J      = BitPat("b000010??????????????????????????")
  
  val JALR   = BitPat("b000000?????00000?????00000001001")
  val JR     = BitPat("b000000?????000000000000000001000")
  /** Load, Store, and Memory Control Instructions **/
  val LB     = BitPat("b100000??????????????????????????")
  val LBU    = BitPat("b100100??????????????????????????")
  val LH     = BitPat("b100001??????????????????????????")
  val LHU    = BitPat("b100101??????????????????????????")
  val LW     = BitPat("b100011??????????????????????????")
  val SW     = BitPat("b101011??????????????????????????")
  val SH     = BitPat("b101001??????????????????????????")
  val SB     = BitPat("b101000??????????????????????????")
  /** Move Instructions **/
  val MFC0   = BitPat("b01000000000??????????00000000???")
  val MTC0   = BitPat("b01000000100??????????00000000???")
  val MFHI   = BitPat("b0000000000000000?????00000010000")
  val MFLO   = BitPat("b0000000000000000?????00000010010")
  val MTHI   = BitPat("b000000?????000000000000000010001")
  val MTLO   = BitPat("b000000?????000000000000000010011")
  /** Shift Instructions **/
  
  
  
  
  
  
  /** Trap Instructions **/
  /** Syscall, currently Halt **/
  val SYSCALL = BitPat("b000000????????????????????001100")
  val BREAK   = BitPat("b000000????????????????????001101")
  val ERET    = BitPat("b01000010000000000000000000011000")
}

object Instructions extends BasicInstructions