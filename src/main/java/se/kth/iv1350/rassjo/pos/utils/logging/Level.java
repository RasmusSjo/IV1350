package se.kth.iv1350.rassjo.pos.utils.logging;

/**
 * Represents the severity levels for logging messages in the application.
 * The levels are used to categorize log messages based on their importance or urgency.
 *
 * @see #DEBUG
 * @see #INFO
 * @see #WARN
 * @see #ERROR
 */
public enum Level {
    /**
     * DEBUG is used for messages providing detailed technical information for debugging.
     */
    DEBUG,
    /**
     * INFO is used for general operational messages that highlight progress.
     */
    INFO,
    /**
     * WARN is used for potentially harmful situations that do not yet prevent operation.
     */
    WARN,
    /**
     * ERROR is used for serious issues that require immediate attention since
     * they can prevent the application from executing properly.
     */
    ERROR
}
