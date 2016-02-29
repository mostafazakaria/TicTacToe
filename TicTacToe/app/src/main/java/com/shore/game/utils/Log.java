package com.shore.game.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class Log {
    public static final String LOG_FILE_UN_HANDLED_EXCEPTIONS = "unhandled_exceptions.error";
    private static final boolean sLoggingEnabled = true;

    public static void i(String tag, String string) {
        if (string != null && sLoggingEnabled) {
            android.util.Log.i(tag, string);
        }
    }

    public static void e(String tag, String string) {
        if (string != null && sLoggingEnabled) {
            android.util.Log.e(tag, string);
        }
    }

    public static void d(String tag, String string) {
        if (string != null && sLoggingEnabled) {
            android.util.Log.d(tag, string);
        }
    }

    public static void v(String tag, String string) {
        if (string != null && sLoggingEnabled) {
            android.util.Log.v(tag, string);
        }
    }

    public static void w(String tag, String string) {
        if (string != null && sLoggingEnabled) {
            android.util.Log.w(tag, string);
        }
    }

    public static void printError(Exception e) {
        if (sLoggingEnabled) {
            // Get the stack trace.
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            String stackTrace = result.toString();
            Log.e("printError", stackTrace);
            printWriter.close();

        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (tag != null && sLoggingEnabled && e != null)
            android.util.Log.e(tag, msg, e.fillInStackTrace());
    }


    public static void writeToFile(String stacktrace, String filename) {
        try {
            // Check the file size if more than 100 MB then remove and create
            // new one
            File file = new File(filename);
            if (file.exists()) {
                double bytes = file.length();
                double kilobytes = (bytes / 1024);
                double megabytes = (kilobytes / 1024);
                if (megabytes >= 100) {
                    file.delete();
                }
            }
            BufferedWriter bos = new BufferedWriter(new FileWriter(filename, true));
            bos.write(new Date().toString() + "\n" + stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}