package com.gilt.gfc.aws.cloudwatch.periodic.metric.aggregator

import org.specs2.mutable.Specification

class StatsSpec
  extends Specification {

  "Stats" should {
    "addSample value" in {
      val s = Stats(sampleCount = 0, sum = 0, min = 0, max = 25.99)
      s.max shouldEqual 25.99
      s.min shouldEqual 0
      s.sampleCount shouldEqual 0
      s.sum shouldEqual 0
      val s2 = s.addSample(5.321)
      s2.max shouldEqual 25.99
      s2.min shouldEqual 0
      s2.sampleCount shouldEqual 1
      s2.sum shouldEqual 5.321
      s.equals(s2) shouldEqual false
    }

  }
}
