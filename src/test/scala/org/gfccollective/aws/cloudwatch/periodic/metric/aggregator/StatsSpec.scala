package org.gfccollective.aws.cloudwatch.periodic.metric.aggregator

import org.specs2.mutable.Specification
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit.COUNT

class StatsSpec
  extends Specification {

  "Stats" should {
    "addSample sanity check" in {
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

    "statsToCloudWatchMetricData sanity check" in {
      val s = Stats(sampleCount = 3, sum = 30.22, min = 0, max = 99.99)
      val converter = Stats.statsToCloudWatchMetricData("myMetricName", COUNT, Seq.empty)
      val result = converter.toMetricData(s)
      result.size shouldEqual 1
      val datum = result.head
      datum.metricName shouldEqual ("myMetricName")
      datum.unit shouldEqual (COUNT)
      datum.statisticValues.sampleCount shouldEqual 3
      datum.statisticValues.sum shouldEqual 30.22

    }
  }
}
