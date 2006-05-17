/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * Very similar to <code>StringRenderer</code> but presents the '\n' characters as newlines in the html
 * page. This renderer also escapes all html tags, including <code>&lt;br&gt;</code> tags.
 * 
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created on May 15, 2006, 3:46:46
 *         PM
 * 
 */
public class NewlineAwareStringRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		if (object == null) {
		    return new HtmlText();
		}

		String string = String.valueOf(object);

		String[] messageComponents = string.split(System.getProperty("line.separator"));
		HtmlInlineContainer container = new HtmlInlineContainer();

		for (int i = 0; i < messageComponents.length; i++) {
		    String text = messageComponents[i];
		    HtmlText htmlText = new HtmlText(text, true);
		    htmlText.setText(htmlText.getText());
		    HtmlText br = new HtmlText("<br/>",false);
		    
		    container.addChild(htmlText);
		    container.addChild(br);
		}

		return container;

	    }

	};
    }

}