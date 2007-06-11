package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * Provides the most basic presentation of all. This renderer is designed to
 * present strings and can convert some strings to links if they have the
 * correct format.
 * 
 * @author cfgi
 */
public class StringRenderer extends OutputRenderer {

	private boolean isLink;

	private String linkText;

	private boolean escaped;

	private boolean newlineAware;

	private Integer length;

	public StringRenderer() {
		super();

		setEscaped(true);
		setNewlineAware(true);
	}

	public boolean isEscaped() {
		return this.escaped;
	}

	/**
	 * Indicates that the string to be presented should be escaped. This means
	 * that any HTML characters will be replaced by the corresponding entities.
	 * 
	 * @property
	 */
	public void setEscaped(boolean escaped) {
		this.escaped = escaped;

		if (!escaped) {
			setNewlineAware(false);
		}
	}

	public boolean isNewlineAware() {
		return this.newlineAware;
	}

	/**
	 * Indicates if all the newlines should be replaced by &lt;br/&gt;.
	 * 
	 * @property
	 */
	public void setNewlineAware(boolean newlineAware) {
		this.newlineAware = newlineAware;
	}

	public boolean isLink() {
		return this.isLink;
	}

	/**
	 * This property indicates that the text to be presented should be
	 * considered a link. If the text actually has a link format, that is, is
	 * either a url or a mail address then a link is presented.
	 * 
	 * <p>
	 * For example the string <code>"http://www.somewhere.net"</code> would be
	 * presented as <a href="http://www.somewhere.net">http://www.somewhere.net</a>
	 * and <code>jane.doe@somewhere.net</code> as <a
	 * href="mailto:jane.doe@somewhere.net"> jane.doe@somewhere.net</a>.
	 * 
	 * @property
	 */
	public void setLink(boolean makeLink) {
		this.isLink = makeLink;
	}

	public String getLinkText() {
		return this.linkText;
	}

	/**
	 * If this property is specifyed then the generated link will have the value
	 * given instead.
	 * 
	 * @property
	 */
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public Integer getLength() {
		return length;
	}

	/**
	 * If this property is provided the string will be truncated at length and a
	 * tooltip will be placed on the string.
	 * 
	 * @property
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	protected Layout getLayout(Object object, Class type) {
		return new Layout() {

			@Override
			public HtmlComponent createComponent(Object object, Class type) {
				if (object == null) {
					return new HtmlText();
				}

				String string = String.valueOf(object);

				if (!isLink() || string == null) {
					HtmlComponent component = null;
					if (getLength() != null && string.length() > getLength()) {
						component = new HtmlText(string.substring(0, getLength()) + "...", isEscaped(),
								isNewlineAware());
						component.setTitle(HtmlText.escape(string));
					} else {
						component = new HtmlText(string, isEscaped(), isNewlineAware());
					}

					return component;
				} else {
					HtmlLink link = new HtmlLink();
					link.setContextRelative(false);

					String text = (getLinkText() == null) ? string : getLinkText();

					if (getLength() != null && text.length() > getLength()) {
						link.setTitle(HtmlText.escape(text));
						text = text.substring(0, getLength()) + "...";
					}

					link.setText(HtmlText.escape(text));

					// heuristic to distinguish between email and other urls
					if (string
							.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$")) {
						link.setUrl("mailto:" + string);
					} else {
						link.setUrl(string);
					}

					return link;
				}
			}

			@Override
			public void applyStyle(HtmlComponent component) {
				component.setClasses(getClasses());
				component.setStyle(getStyle());
				
				if (component.getTitle() == null) {
					component.setTitle(getTitle());
				}
			}
		};
	}
}
