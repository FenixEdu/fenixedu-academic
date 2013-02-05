package net.sourceforge.fenixedu.util;

import net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor.HtmlToTextConverter;

public class HtmlToTextConverterUtil {

    private final static String REGEX_PATTERN = "\\<.*?>";

    public static String convertToText(String html) {

        // TODO: Move HtmlToTextConverter code to here, so this util does not
        // have to know about renderers
        HtmlToTextConverter converter = new HtmlToTextConverter();
        return (String) converter.convert(String.class, html);
    }

    public static String convertToTextWithRegEx(String html) {
        return html.toString().replaceAll(REGEX_PATTERN, "");
    }

}
