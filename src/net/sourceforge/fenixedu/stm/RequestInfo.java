package net.sourceforge.fenixedu.stm;


public class RequestInfo {    
    private static final ThreadLocal<String> REQUEST_URI = new ThreadLocal<String>() {
         protected String initialValue() {
             return "";
         }
    };

    public static String getRequestURI() {
	return REQUEST_URI.get();
    }

    public static void setRequestURI(String uri) {
	REQUEST_URI.set(uri);
    }
}
