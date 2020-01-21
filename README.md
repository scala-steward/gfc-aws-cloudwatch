# gfc-aws-cloudwatch [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.gfccollective/gfc-aws-cloudwatch_2.12/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/org.gfccollective/gfc-aws-cloudwatch_2.12) [![Join the chat at https://gitter.im/gilt/gfc](https://badges.gitter.im/gilt/gfc.svg)](https://gitter.im/gilt/gfc?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A thin Scala wrapper around AWS CloudWatch Java client. Part of the [Gilt Foundation Classes](https://github.com/gilt?q=gfc).

## Getting gfc-aws-cloudwatch

The latest version is 1.3.2, which is cross-built against Scala 2.11.x and 2.12.x

Add dependency to build.sbt:
```scala
libraryDependencies += "org.gfccollective" %% "gfc-aws-cloudwatch" % "1.3.2"
```

# Basic usage

Quick metric aggregator example
(less flexible than CW APIs but can save costs when you have high-frequency events):
```scala
  val cwPublisher = CloudWatchMetricsPublisher.start(1 minute)

  val SubServiceMetricBuilder = {
    CloudWatchMetricDataAggregator.builder(cwPublisher).withMetricNamespace("TopLevelNamespace")
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
Copyright 2019 Hudson's Bay Company

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
