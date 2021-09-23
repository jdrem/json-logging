package net.remgant.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackExample {
    final static private Logger log = LoggerFactory.getLogger(LogbackExample.class);
    public static void main(String[] args) {
        log.info("starting up");
        log.debug("debug info");
        log.warn("warning");
        try {
            a();
        } catch (Exception e) {
            log.error("error", e);
        }
        log.info("stopping");
    }

    private static void a() {
        b();
    }

    private static void b() {
        c();
    }

    private static void c() {
        throw new RuntimeException("exception");
    }
}
