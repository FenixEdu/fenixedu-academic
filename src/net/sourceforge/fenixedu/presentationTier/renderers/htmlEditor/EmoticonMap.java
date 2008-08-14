package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import java.util.HashMap;
import java.util.Map;

public class EmoticonMap {
    private static Map<String, String> emoticons;

    static {
	emoticons = new HashMap<String, String>();

	emoticons.put("cool", "B-)");
	emoticons.put("cry", ":'-(");
	emoticons.put("embarassed", ":-$");
	emoticons.put("foot-in-mouth", ":-!");
	emoticons.put("frown", ":-(");
	emoticons.put("innocent", "O:-)");
	emoticons.put("kiss", ":-*");
	emoticons.put("laughing", ":-D");
	emoticons.put("money-mouth", ":-$");
	emoticons.put("sealed", ":-x");
	emoticons.put("suprised", ":-o");
	emoticons.put("tongue-out", ":-P");
	emoticons.put("undecided", ":-/");
	emoticons.put("wink", ";-)");
	emoticons.put("yell", ":-O");
    }

    public static String getEmoticon(String name) {
	return emoticons.get(name);
    }
}
