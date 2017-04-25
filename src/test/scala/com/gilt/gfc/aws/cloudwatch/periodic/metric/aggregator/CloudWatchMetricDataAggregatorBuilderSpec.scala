package com.gilt.gfc.aws.cloudwatch.periodic.metric.aggregator

import org.specs2.mutable.Specification

class CloudWatchMetricDataAggregatorBuilderSpec
  extends Specification {

  "Builder" should {
    "allow spaces in namespace" in {
      val space = CloudWatchMetricDataAggregatorBuilder(metricNamespace = Some("Test"))
        .enterMetricNamespace("spaces")

      val manyspaces = CloudWatchMetricDataAggregatorBuilder(metricNamespace = Some("Test"))
        .enterMetricNamespace("many spaces")

      space.metricNamespace shouldNotEqual None
      space.metricNamespace.get shouldEqual "Test / spaces"

      manyspaces.metricNamespace shouldNotEqual None
      manyspaces.metricNamespace.get shouldEqual "Test / many spaces"
    }
  }

}
