
package com.codeiguanas.promptgpt.domain.lint;
public final class LintFinding {
    public enum Level { OK, WARN, ERROR }
    private final Level level;
    private final String message;
    private LintFinding(Level level, String message) { this.level = level; this.message = message; }
    public static LintFinding ok(String msg) { return new LintFinding(Level.OK, msg); }
    public static LintFinding warn(String msg) { return new LintFinding(Level.WARN, msg); }
    public static LintFinding error(String msg) { return new LintFinding(Level.ERROR, msg); }
    public Level level() { return level; }
    public String message() { return message; }
}
