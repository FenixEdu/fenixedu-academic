package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities.ParserReport;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlPreformattedText;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer is used to present a {@link ParserReport}. The report
 * expressoin is presented and the position of the error is shown.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * this.is.an.&lt;span title=&quot;unexpected token '.'&quot; style=&quot;background: #FAA;&quot;&gt;.&lt;/span&gt;error
 * </pre>
 * <pre>
 * $user.&lt;span title=&quot;method 'address' does not exist&quot; style=&quot;background: #FAA;&quot;&gt;address(home)&lt;/span&gt;.name
 * </pre>
 * 
 * @author cfgi
 */
public class ParserReportRenderer extends OutputRenderer {

    private String bundle;
    
    private String errorClass;
    private String errorStyle;

    protected String getBundle() {
        return this.bundle;
    }

    /**
     * Sets the bundle to be used to localize a localized report message.
     * 
     * @property
     */
    protected void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getErrorClass() {
        return this.errorClass;
    }

    /**
     * Chooses the html classes for the error region.
     * 
     * @property
     */
    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    public String getErrorStyle() {
        return this.errorStyle;
    }

    /**
     * Chooses the css style for the error region.
     * 
     * @property
     */
    public void setErrorStyle(String errorStyle) {
        this.errorStyle = errorStyle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                ParserReport report = (ParserReport) object;

                HtmlContainer container = new HtmlPreformattedText();
                container.setIndented(false);

                String expression = report.getExpression() + " " /* because of 'premature end' errors */; 

                int startIndex = findIndex(report.getStartLine(), report.getStartColumn(), expression);
                int endIndex;

                if (report.hasRangedLineInformation()) {
                    endIndex = findIndex(report.getEndLine(), report.getEndColumn(), expression) + 1;
                } else {
                    endIndex = startIndex + 1;
                }
                
                HtmlText befor = new HtmlText(expression.substring(0, startIndex));
                HtmlText error = new HtmlText(expression.substring(startIndex, endIndex));
                HtmlText after = new HtmlText(expression.substring(endIndex));

                if (error.getText().length() == 0 || error.getText().equals(" ")) {
                    error = new HtmlText("&nbsp;", false);
                }
                
                container.addChild(befor);
                container.addChild(error);
                container.addChild(after);

                error.setClasses(getErrorClass());
                error.setStyle(getErrorStyle());
                error.setTitle(getReportMessage(report));
                
                return container;
            }

            private String getReportMessage(ParserReport report) {
                if (report.isResource()) {
                    String message = RenderUtils.getResourceString(getBundle(), report.getKey(), report.getArgs());
                    return message == null ? report.getKey() : message;
                }
                else {
                    return HtmlText.escape(report.getKey());
                }
            }

            private int findIndex(int lineNumber, int columnNumber, String text) {
                String[] lines = text.split("\n");

                int index = 0;
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];

                    if (i < lineNumber - 1) {
                        index += line.length() + 1 /* "\n".length() */;
                    }

                    if (i == lineNumber - 1) {
                        index += columnNumber - 1;
                    }
                }

                return index;
            }
        };
    }

}
