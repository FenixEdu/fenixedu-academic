package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * This renderer has the same behaviour as the
 * {@link net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageStringRenderer}
 * but the context is limited to a certain number of characters.
 * 
 * @author cfgi
 */
public class ShortMultiLanguageStringRenderer extends MultiLanguageStringRenderer {

	private Integer length;
	private boolean tooltipShown;

	public ShortMultiLanguageStringRenderer() {
		super();

		this.tooltipShown = true;
	}

	public boolean isTooltipShown() {
		return tooltipShown;
	}

	/**
	 * Chooses if a tooltip is added to the text when text is bigger
	 * than the value given in {@link #setLength(Integer) length}.
	 * 
	 * @property
	 */
	public void setTooltipShown(boolean tooltipShown) {
		this.tooltipShown = tooltipShown;
	}

	public Integer getLength() {
		return this.length;
	}

	/**
	 * Choose the amount of characters displayed. If the original text has more than
	 * the value given the elipses are added.
	 *  
	 * @property
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	protected String getRenderedText(MultiLanguageString mlString) {
		String content = super.getRenderedText(mlString);

		if (content != null && getLength() != null) {
			if (content.length() > getLength()) {
				return content.substring(0, getLength()) + "...";
			}
		}

		return content;
	}

	@Override
	protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
		HtmlComponent component = super.renderComponent(layout, object, type);

		MultiLanguageString mlString = (MultiLanguageString) object;

		String previous = super.getRenderedText(mlString);
		String current = getRenderedText(mlString);

		if (isTooltipShown()) {
			if (!String.valueOf(previous).equals(String.valueOf(current))) {
				component.setTitle(HtmlText.escape(previous));
			}
		}

		return component;
	}
}
