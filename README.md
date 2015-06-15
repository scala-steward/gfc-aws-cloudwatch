# gfc-aws-cloudwatch

A thin Scala wrapper around AWS CloudWatch Java client.

## How to use

Add dependency to build.sbt:
```scala
libraryDependencies += "com.gilt" % "gfc-aws-cloudwatch" % "0.2.0"
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
