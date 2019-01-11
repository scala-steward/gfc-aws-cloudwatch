package com.gilt.gfc.aws.cloudwatch.periodic.metric.aggregator

import java.time.Instant

import software.amazon.awssdk.services.cloudwatch.model._
import com.gilt.gfc.aws.cloudwatch.ToCloudWatchMetricsData

import scala.collection.JavaConverters._
import scala.language.postfixOps


private[metric]
case class Stats(
  sampleCount: Long
, sum: Double
, min: Double
, max: Double
) {

  def addSample( v: Double
               ): Stats = {
    Stats(
      sampleCount = this.sampleCount + 1L
    , sum = this.sum + v
    , min = this.min.min(v)
    , max = this.max.max(v)
    )
  }
}


private[metric]
object Stats {

  val Zero = Stats(0L, 0, 0, 0)

  val NoData = Zero.copy(sampleCount = 1L) // we send this to CW when there's no metric data to avoid 'insufficient data' state


  def statsToCloudWatchMetricData( metricName: String
                                 , metricUnit: StandardUnit
                                 , metricDimensions: Seq[Seq[Dimension]]
                                 ): ToCloudWatchMetricsData[Stats] = new ToCloudWatchMetricsData[Stats] {

    override
    def toMetricData( s: Stats
                    ): Seq[MetricDatum] = {

      val statsValues = StatisticSet.builder.
        sampleCount(s.sampleCount.toDouble).
        sum(s.sum).
        minimum(s.min).
        maximum(s.max).
        build

      def mdBuilder = MetricDatum.builder. // def, not val, we mutate it
        metricName(metricName).
        unit(metricUnit).
        statisticValues(statsValues).
        timestamp(Instant.now)

      Seq(
        Seq(mdBuilder.build) // publish dimensionless metric
        , metricDimensions.map(ds => mdBuilder.dimensions(ds.asJava).build) // publish same metric with dimensions
      ) flatten
    }
  }
}
