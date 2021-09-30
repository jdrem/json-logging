using log4net.Core;
using Newtonsoft.Json;
using System;
using System.IO;

namespace Remgant.Log4Net
{
    public class JsonLayout : log4net.Layout.LayoutSkeleton
    {
        private readonly static DateTime UnixEpochStart = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
        readonly String HostName;

        public JsonLayout()
        {
            this.HostName = System.Environment.MachineName;
            this.IgnoresException = false;
        }

        public override void ActivateOptions()
        {
        }

        public override void Format(TextWriter writer, LoggingEvent loggingEvent)
        {
            string errorMsg = loggingEvent.RenderedMessage + (!string.IsNullOrEmpty(loggingEvent.GetExceptionString()) ? ": " + loggingEvent.GetExceptionString() : "");
            var msg = new LogTemplate
            {
                // Ticks are 10,000th of seconds, need seconds
                log_time_stamp = (loggingEvent.TimeStampUtc.Ticks - UnixEpochStart.Ticks) / TimeSpan.TicksPerSecond,
                function_name = loggingEvent.LocationInformation.MethodName,
                function_path = loggingEvent.LocationInformation.FileName,
                line_number = loggingEvent.LocationInformation.LineNumber,
                logger_class = loggingEvent.LoggerName,
                message = errorMsg,
                log_level = loggingEvent.Level.Name,
                logged_in_user_id = loggingEvent.UserName,
                thread_name = loggingEvent.ThreadName,
                host = HostName
            };
            writer.WriteLine(msg.ToString());
        }
    }
    internal class LogTemplate
    {
        public string function_name { get; set; } 
        public long log_time_stamp { get; set; }  
        public string logger_class { get; set; }   
        public string function_path { get; set; } 
        public string log_level { get; set; }
        public string app_type { get; set; }
        public string app_environment { get; set; }
        public string message { get; set; }
        public string logged_in_user_id { get; set; }
        public string thread_name { get; set; }
        public string line_number { get; set; }
        public string host { get; set; }

        public override string ToString()
        {
            return JsonConvert.SerializeObject(this, new JsonSerializerSettings { NullValueHandling = NullValueHandling.Ignore });
        }
    }
}
