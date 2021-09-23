package net.remgant.log4j;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class JsonLayout extends Layout {

    Gson gson = new Gson();

    final private String hostName;

    public JsonLayout() {
        String hn;
        try {
            hn = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hn = "localhost";
        }
        hostName = hn;
    }

    @Override
    public String format(LoggingEvent loggingEvent) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("timestamp", loggingEvent.timeStamp);
        builder.put("logger", loggingEvent.getLoggerName());
        builder.put("level", loggingEvent.getLevel().toString());
        builder.put("thread", loggingEvent.getThreadName());
        builder.put("message", loggingEvent.getRenderedMessage());
        if (loggingEvent.getThrowableInformation() != null)
            builder.put("exception", loggingEvent.getThrowableInformation());
        if (loggingEvent.getLocationInformation() != null) {
            LocationInfo locationInfo = loggingEvent.getLocationInformation();
            builder.put("fileName", locationInfo.getFileName());
            builder.put("className", locationInfo.getClassName());
            builder.put("methodName", locationInfo.getMethodName());
            builder.put("lineNumber", locationInfo.getLineNumber());
        }
        builder.put("host", hostName);
        return gson.toJson(builder.build()) + "\n";
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {

    }
}
