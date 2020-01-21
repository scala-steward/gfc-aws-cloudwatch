package org.gfccollective.aws.cloudwatch.periodic.metric.aggregator

import org.gfccollective.aws.cloudwatch.ToCloudWatchMetricsData
import org.specs2.mutable.Specification
import software.amazon.awssdk.services.cloudwatch.model.{Dimension, MetricDatum}


class WorkQueueSpec
  extends Specification {

  implicit
  val StringToCloudWatchMetricsData = new ToCloudWatchMetricsData[String] {
    override
    def toMetricData( a: String
                    ): Seq[MetricDatum] = {
      Seq( MetricDatum.builder.unit(a).value(1.0).build )
    }
  }

  "WorkQueue" should {

    "drain empty queue into an empty iterator" in {
      val q = new WorkQueue[String]()
      q.drain().hasNext should beFalse // should not explode
    }

    "drain non-empty queue into an iterator" in {
      val q = new WorkQueue[String]()
      q.enqueue("nsFoo", "foo1")
      q.enqueue("nsFoo", "foo2")
      q.enqueue("nsBar", "bar1")
      q.enqueue("nsBar", "bar2")

      val it = q.drain()

      it.hasNext should beTrue

      val list = it.toList
      list.size shouldEqual(4)
      list(0).shouldEqual(("nsFoo" -> MetricDatum.builder.unit("foo1").value(1.0).build()))
      list(1).shouldEqual(("nsFoo" -> MetricDatum.builder.unit("foo2").value(1.0).build()))
      list(2).shouldEqual(("nsBar" -> MetricDatum.builder.unit("bar1").value(1.0).build()))
      list(3).shouldEqual(("nsBar" -> MetricDatum.builder.unit("bar2").value(1.0).build()))
    }
  }
}
