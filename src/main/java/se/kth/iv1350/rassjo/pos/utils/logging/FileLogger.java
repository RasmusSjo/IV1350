package se.kth.iv1350.rassjo.pos.utils.logging;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code FileLogger} class logs messages to a log file in the {@code logs}
 * directory located in the root directory of the project.
 */
public class FileLogger {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String LOG_OUTPUT_FORMAT = "%s %s [%s]: %s%n";
    private static final String INDENT = "    ";
    private static final String BASE_PATH = "logs/";
    private static final String LOG_INFO_FILE = BASE_PATH + "info-";
    private static final String LOG_ERROR_FILE = BASE_PATH + "error-";
    private static final String LOG_FILE_EXTENSION = ".log";

    /**
     * The singleton instance of the {@link FileLogger} class.
     */
    private static final FileLogger INSTANCE = new FileLogger();

    private final PrintWriter infoLogWriter;
    private final PrintWriter errorLogWriter;

    /**
     * Constructs a new {@link FileLogger} instance. Appends new logs to the log file for
     * the current day, or if there isn't a log file for the current day, a new file is created.
     *
     * @throws UncheckedIOException if an I/O error occurs during initialization.
     */
    private FileLogger() {
        String LOG_INFO_FILE_PATH = LOG_INFO_FILE + DATE_FORMAT.format(LocalDateTime.now()) + LOG_FILE_EXTENSION;
        String LOG_ERROR_FILE_PATH = LOG_ERROR_FILE + DATE_FORMAT.format(LocalDateTime.now()) + LOG_FILE_EXTENSION;
        try {
            Files.createDirectories(Path.of(BASE_PATH));
            infoLogWriter = new PrintWriter(new FileOutputStream(LOG_INFO_FILE_PATH, true), true);
            errorLogWriter = new PrintWriter(new FileOutputStream(LOG_ERROR_FILE_PATH, true), true);
        } catch (IOException e) {
            throw new UncheckedIOException("An error occurred trying to initialise the file logger.", e);
        }
    }

    /**
     * Retrieves the singleton instance of the {@link FileLogger} class.
     *
     * @return The singleton instance of the {@link FileLogger}.
     */
    public static FileLogger getInstance() {
        return INSTANCE;
    }

    /**
     * Logs a {@link Level#DEBUG DEBUG}-level message intended for debugging purposes.
     *
     * @param message the message to be logged
     */
    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    /**
     * Logs a {@link Level#INFO INFO}-level message intended for general application progress.
     *
     * @param message the message to be logged
     */
    public void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a {@link Level#WARN WARN}-level message intended for potentially harmful situations.
     *
     * @param message the message to be logged
     */
    public void warn(String message) {
        log(Level.WARN, message);
    }

    /**
     * Logs a {@link Level#ERROR ERROR}-level message intended for serious
     * issues that may affect application execution.
     *
     * @param message the message to be logged
     */
    public void error(String message) {
        log(Level.ERROR, message);
    }

    /**
     * Logs a {@link Level#ERROR ERROR}-level message along with a throwable that provides additional context about
     * the error. This is typically used for serious issues that may affect application execution.
     *
     * @param message the message to be logged, describing the error.
     * @param t       the {@code Throwable} associated with the error, providing additional details such as a stack trace.
     */
    public void error(String message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    private void log(Level level, String message) {
        log(level, message, null);
    }

    private void log(Level level, String message, Throwable t) {
        String time = LocalDateTime.now().format(TIME_FORMAT);
        String threadName = Thread.currentThread().getName();

        if (level == Level.INFO || level == Level.DEBUG) {
            infoLogWriter.printf(LOG_OUTPUT_FORMAT, time, level, threadName, message);
            return;
        }

        errorLogWriter.printf(LOG_OUTPUT_FORMAT, time, level, threadName, message);
        if (t != null) {
            StringWriter stackTraceHolder = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTraceHolder));

            for (String line : stackTraceHolder.toString().split("\n")) {
                errorLogWriter.printf(INDENT + line + "\n");
            }
        }
    }
}
