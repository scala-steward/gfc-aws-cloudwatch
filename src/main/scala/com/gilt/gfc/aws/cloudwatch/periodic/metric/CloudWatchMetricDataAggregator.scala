package com.gilt.gfc.aws.cloudwatch.periodic.metric

import com.amazonaws.services.cloudwatch.{AmazonCloudWatch, AmazonCloudWatchClientBuilder}
import com.gilt.gfc.aws.cloudwatch.periodic.metric.aggregator.CloudWatchMetricDataAggregatorBuilder

import scala.concurrent.duration.FiniteDuration
import scala.language.postfixOps

/**
 * Maintains state necessary to aggregate metrics on the client side.
 * Interface is different and less flexible than underlying CW API but
 * it results in fewer API calls, which are expensive.
 */
trait CloudWatchMetricDataAggregator {

  /** Handles a single metric datum sample value. */
  def sampleValue(v: Double): Unit

  /** A common use case, increment by 1. */
  def increment(): Unit = { sampleValue(1) }

  /** Stops background tasks. */
  def stop(): Unit
}



object CloudWatchMetricDataAggregator {

  /** Constructs a builder (immutable) that collects all the necessary parameters as well
    * as allows for a partial construction of CloudWatchMetricDataAggregator objects,
    * with common groups of parameters shared by multiple aggregated metrics.
    *
    * N.B. fully constructed CloudWatchMetricDataAggregator instances start to collect
    *      metrics immediately, so a global background task that dumps them to CW needs
    *      to be started first, please call start() somewhere early in your app startup sequence.
    *
    * @return builder instance that you can start customizing
    */
  def builder(publisher: CloudWatchMetricsPublisher): CloudWatchMetricDataAggregatorBuilder = {
    CloudWatchMetricDataAggregatorBuilder().withPublisher(publisher)
  }
}
