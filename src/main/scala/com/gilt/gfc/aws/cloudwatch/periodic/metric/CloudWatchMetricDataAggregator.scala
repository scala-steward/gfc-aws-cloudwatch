package com.gilt.gfc.aws.cloudwatch.periodic.metric

import com.gilt.gfc.aws.cloudwatch.periodic.metric.aggregator.CloudWatchMetricDataAggregatorBuilder

import scala.concurrent.duration.FiniteDuration
import scala.language.postfixOps

/**
 *
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

  def builder(): CloudWatchMetricDataAggregatorBuilder = {

    // This is needed to make sure we consume what aggregated metrics start to produce, otherwise
    // system will OOM.
    assert(startedBackgroundTask, "Please call start() before building any aggregated metrics.")

    CloudWatchMetricDataAggregatorBuilder()
  }


  def start( interval: FiniteDuration
           ): Unit = {

    CloudWatchMetricDataAggregatorBuilder.start(interval)

    startedBackgroundTask = true
  }

  @volatile private[this]
  var startedBackgroundTask = false
}
