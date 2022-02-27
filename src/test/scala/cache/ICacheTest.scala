package cache

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import fluteutil.BitMode.fromIntToBitModeLong
import config.CpuConfig._

class ICacheTest extends AnyFreeSpec with ChiselScalatestTester with Matchers {
  "test" in {
    test(new ICache("test_data/fetch_icache_test.in")) { c =>
      c.io.addr.valid.poke(1.B)
      c.io.addr.bits.poke(0.U)
      c.clock.step()
      c.io.data.valid.expect(1.B)
      c.io.data.bits(0).expect(0x3801ffff.BM.U)
      c.io.data.bits(1).expect(0x380200ff.U)
    }
  }
}
