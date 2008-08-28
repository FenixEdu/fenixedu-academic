package net.sourceforge.fenixedu.presentationTier.tiles;

import java.util.HashMap;
import java.util.Map;

public class LayoutLinkInjector {

    public static Map<String, String> fenixLayout2colMap = new HashMap<String, String>();

    public static String getFenixLayout2colMapLinks(final String contextPath) {
	String links = fenixLayout2colMap.get(contextPath);
	if (links == null) {
	    synchronized (fenixLayout2colMap) {
		if (fenixLayout2colMap.containsKey(contextPath)) {
		    links = fenixLayout2colMap.get(contextPath);
		} else {
		    final StringBuilder stringBuilder = new StringBuilder();
		    fillLinks(stringBuilder, contextPath);
		    links = stringBuilder.toString();
		    fenixLayout2colMap.put(contextPath, links);
		}
	    }
	}
	return links;
    }

    private static void fillLinks(final StringBuilder stringBuilder, final String contextPath) {
	addLink(stringBuilder, contextPath, "shortcut icon", "image/ico", null, "/images/favicon.ico");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "screen", "/CSS/layout.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "screen", "/CSS/general.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "screen", "/CSS/color.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "print", "/CSS/print.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "screen", "/CSS/calendar.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", null, "/CSS/dotist_timetables.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "screen, print", "/CSS/dotist_calendars.css");
	addLink(stringBuilder, contextPath, "stylesheet", "text/css", "screen", "/CSS/inquiries_style.css");
	addScript(stringBuilder, contextPath, "text/javascript", "/CSS/scripts/hideButtons.js");
	addScript(stringBuilder, contextPath, "text/javascript", "/CSS/scripts/check.js");
	addScript(stringBuilder, contextPath, "text/javascript", "/CSS/scripts/checkall.js");
    }

    private static void addLink(final StringBuilder stringBuilder, final String contextPath, final String rel,
	    final String type, final String media, final String path) {
	stringBuilder.append("<link rel=\"");
	stringBuilder.append(rel);
	stringBuilder.append("\" type=\"");
	stringBuilder.append(type);
	if (media != null) {
	    stringBuilder.append("\" media=\"");
	    stringBuilder.append(media);
	}
	stringBuilder.append("\" href=\"");
	stringBuilder.append(contextPath);
	stringBuilder.append(path);
	stringBuilder.append("\">\n");
    }

    private static void addScript(final StringBuilder stringBuilder, final String contextPath, final String type, final String path) {
	stringBuilder.append("<script ");
	stringBuilder.append("type=\"");
	stringBuilder.append(type);
	stringBuilder.append("\" src=\"");
	stringBuilder.append(contextPath);
	stringBuilder.append(path);
	stringBuilder.append("\"/>\n");
    }

}
