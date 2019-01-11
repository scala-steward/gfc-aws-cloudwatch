package com.gilt.gfc.aws.cloudwatch.periodic.metric

import software.amazon.awssdk.services.cloudwatch.model.MetricDatum
import com.gilt.gfc.aws.cloudwatch.ToCloudWatchMetricsData


package object aggregator {

  type NamespacedMetricDatum = (String, MetricDatum)


  // Un-wraps values produced by groupBy()
  implicit object SeqNamespacedMetricDatumToCWMetricData
    extends ToCloudWatchMetricsData[Seq[NamespacedMetricDatum]] {

    override
    def toMetricData( data: Seq[(String, MetricDatum)]
                    ): Seq[MetricDatum] = {
      data.map(_._2)
    }
  }

}
