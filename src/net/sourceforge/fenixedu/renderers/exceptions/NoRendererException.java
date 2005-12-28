package net.sourceforge.fenixedu.renderers.exceptions;

public class NoRendererException extends RuntimeException {

    public NoRendererException(Class type, String layout) {
        super("No available render for type '" + type.getName() + "' and layout '" + layout + "'");
    }

}
