package core.pipeline

import chisel3._
import chiseltest._
import core.pipeline.WriteBack
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class WriteBackTest extends AnyFreeSpec with ChiselScalatestTester with Matchers {
  "Write Back Stage Logic" in {
    test(new WriteBack()) { w =>

    }
  }
}