package com.fenyx.core.console;

/**
 *
 * @author DarkPartizaN
 */
public abstract class ConsoleCommand {

    public String name;
    public String desc;
    public int maxArguments;

    protected abstract void init();
    public abstract void exec(String... args);
    public abstract void print();
}
