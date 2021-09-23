using log4net;
using System;

namespace json_logging_log4net
{

    class Program
    {
        private static readonly ILog log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType.Name);

        static void Main(string[] args)
        {
            log.Info("starting up");
            log.Debug("debug info");
            log.Warn("warning");
            try
            {
                a();
            }
            catch (Exception e)
            {
                log.Error("error", e);
            }
            log.Info("stopping");
            Console.ReadKey();
        }

        private static void a()
        {
            b();
        }

        private static void b()
        {
            c();
        }

        private static void c()
        {
            throw new Exception("exception");
        }
    }
}
