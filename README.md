# Logging in JSON Format

There are some use cases where it would desirable to have application messages formated as JSON documents. Most popular logging 
packages don't support this, but they are easily extended to handle different layouts.  This repo has examples on how to do this for three
commonly used logging framewoks: Log4J, Log4Net and Logback.

In all these examples, a Layout class is extended to format logging data as a JSON document.  The Layout is then used with a rolling file appender. 
The three examples here are:
* [Log4j](https://github.com/jdremillard/json-logging/tree/main/json-logging-log4j)
* [Log4Net](https://github.com/jdremillard/json-logging/tree/main/json-logging-log4net)
* [Logback](https://github.com/jdremillard/json-logging/tree/main/json-logging-logback)

It may be that you'd like to add application or site specific data to the log messages.  Like a application ID or enviroment name. It's easy enough
to add a new property to the JsonLayout class and add it the configuration file. Like this for log4j:
```
log4j.appender.j=org.apache.log4j.DailyRollingFileAppender
log4j.appender.j.file=./logs/example.json
log4j.appender.j.datePattern='.'yyyy-MM-dd
log4j.appender.j.layout =net.remgant.log4j.JsonLayout
log4j.appender.j.layout.ApplicationID = 1234
log4j.appender.j.layout.EnvironmentName = DEV
```
Or this for log4net:
```
<appender name="Json" type="log4net.Appender.RollingFileAppender">
  <lockingModel type="log4net.Appender.FileAppender+MinimalLock" />
  <staticLogFileName value="false" />
  <file value="Logs\\example-" />
  <appendToFile value="true" />
  <rollingStyle value="Date" />
  <datePattern value="yyyy-MM-dd'.json'" />
  <layout type="Remgant.Log4Net.JsonLayout">
    <ApplicationID>1234</ApplicationID>
    <EnvironmentName>DEV</EnvironmentName>
  </layout>
</appender>
```
If you're using this for logstash, which is a likely use case for this, you could instead add filters to add fields to the document rather than adding properties to the Layout class. For instance, this logstash.conf file would accomplish the same thing:
```
input {
    file {
        path => "/home/user/app/logs/example*.json"
        codec => "json"
        start_position => "beginning"
    }
}
filter {
  mutate {
   add_field => {
      "application_id" => "1234"
      "environment" => "DEV"
    }
 }
 output {
    stdout {
       codec => "json"
    }
 }
```
