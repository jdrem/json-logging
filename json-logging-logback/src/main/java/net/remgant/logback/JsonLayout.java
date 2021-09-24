package net.remgant.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.LayoutBase;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class JsonLayout extends LayoutBase<ILoggingEvent> {

    final private Gson gson;
    final private String hostName;

    public JsonLayout() {
        String hn;
        try {
            hn = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hn = "localhost";
        }
        hostName = hn;
        gson = new Gson();
    }


    @Override
    public String doLayout(ILoggingEvent iLoggingEvent) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.putAll(iLoggingEvent.getMDCPropertyMap());
        builder.put("timestamp", iLoggingEvent.getTimeStamp());
        builder.put("logger", iLoggingEvent.getLoggerName());
        builder.put("level", iLoggingEvent.getLevel().toString());
        builder.put("thread", iLoggingEvent.getThreadName());
        builder.put("message", iLoggingEvent.getFormattedMessage());
        builder.put("host", hostName);
        StackTraceElement[] stackTraceElements = iLoggingEvent.getCallerData();
        builder.put("className", stackTraceElements[stackTraceElements.length - 1].getClassName());
        builder.put("methodName", stackTraceElements[stackTraceElements.length - 1].getMethodName());
        builder.put("fileName", stackTraceElements[stackTraceElements.length - 1].getFileName());
        builder.put("lineNumber", stackTraceElements[stackTraceElements.length - 1].getLineNumber());
        if (iLoggingEvent.getThrowableProxy() != null) {
            IThrowableProxy throwable = iLoggingEvent.getThrowableProxy();
            StringBuilder sb = new StringBuilder();
            sb.append(throwable.getClassName());
            sb.append(": ");
            sb.append(throwable.getMessage());
            sb.append("\n");
            for (StackTraceElementProxy stackTraceElement : throwable.getStackTraceElementProxyArray()) {
                sb.append("\tat ");
                sb.append(stackTraceElement.getStackTraceElement().toString());
                sb.append("\n");
            }
            builder.put("exception", sb.toString());
        }
        return gson.toJson(builder.build()) + "\n";
    }
}
