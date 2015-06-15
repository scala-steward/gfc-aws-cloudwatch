# gfc-aws-cloudwatch

A thin Scala wrapper around AWS CloudWatch Java client.

## How to use

Add dependency to build.sbt:
```scala
libraryDependencies += "com.gilt" % "gfc-aws-cloudwatch" % "0.3.0"
```

Quick metric aggregator example
(less flexible than CW APIs but can save costs when you have high-frequency events):
```scala

  val SubServiceMetricBuilder = {
    CloudWatchMetricDataAggregator.builder.withMetricNamespace("TopLevelNamespace")
  }

  val Count = SubServiceMetricBuilder.withUnit(StandardUnit.Count)

  val SuccessCount = Count.withMetricName("success")

  def kind = new Dimension().withName("kind")

  val SuccessfulCreate = SuccessCount.addDimensions(kind.withValue("create"))

  val SuccessfulUpdate = SuccessCount.addDimensions(kind.withValue("update"))

  val SuccessfulCreateFoo = SuccessfulCreate.enterMetricNamespace("foo").start()
  val SuccessfulUpdateFoo = SuccessfulUpdate.enterMetricNamespace("foo").start()

  val SuccessfulCreateBar = SuccessfulCreate.enterMetricNamespace("bar").start()
  val SuccessfulUpdateBar = SuccessfulUpdate.enterMetricNamespace("bar").start()

..........

  SuccessfulCreateFoo.increment()
  SuccessfulUpdateFoo.increment()
  .....

```

Quick example:
```scala
implicit
object FooMetricToCloudWatchMetricsData
  extends ToCloudWatchMetricsData[Foo] {

  override
  def toMetricData( t: Foo
                  ): Seq[MetricDatum] = {
    // convert to metric data
  }
}

// ..........

CloudWatchMetricsClient("TopLevelNamespace").
  enterNamespace("foo"). // optionally enter more specific namespace
  putMetricData(someFoo)


implicit
object FooMetricToCloudWatchLogsData
  extends ToCloudWatchLogsData[Foo] {

  override
  def toLogEvents( t: Foo
                  ): Seq[InputLogEvent] = {
    // convert to log events
  }
}

// ..........

CloudWatchLogsClient("TopLevelNamespace").
  enterNamespace("foo"). // optionally enter more specific namespace
  putLogData("streamName", someFoo)

```

## License
Copyright 2015 Gilt Groupe, Inc.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
