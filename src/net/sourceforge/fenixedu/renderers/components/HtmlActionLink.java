package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlActionLink extends HtmlSimpleValueComponent {
    private static final String ACTION_LINK_FIELD_KEY = HtmlActionLink.class.getName() + "_key";

    private static final String ACTION_LINK_FIELD_ID = HtmlActionLink.class.getName() + "_id";

    private String text;

    private HtmlComponent body;

    private boolean activated;

    public HtmlActionLink() {
	super();

	setActivated(false);
    }

    public HtmlActionLink(HtmlComponent body) {
	super();

	setBody(body);
    }

    public HtmlActionLink(String text) {
	this(new HtmlText(text));
    }

    public HtmlComponent getBody() {
	return this.body;
    }

    public void setBody(HtmlComponent body) {
	this.body = body;
    }

    public String getText() {
	return this.text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public boolean isActivated() {
	return this.activated;
    }

    public void setActivated(boolean activated) {
	this.activated = activated;
    }

    @Override
    public void setValue(String value) {
	setActivated(value != null && value.equals(getName()));
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
	HtmlHiddenField hidden = getActionLinkHiddenField(context);

	HtmlLink link = new HtmlLink();
	link.setText(getText());
	link.setBody(getBody());
	link.setUrl("#");
	link.setModuleRelative(false);
	link.setContextRelative(false);

	final String onClickEvent = getOnClick() != null ? getOnClick() : "";
	setOnClick(onClickEvent + " " + "document.getElementById('" + hidden.getId() + "').value='"
		+ getName() + "'; " + "document.getElementById('" + hidden.getId() + "').form.submit();");
	

	HtmlTag tag = super.getOwnTag(context);

	tag.setName(null);
	tag.addChild(hidden.getOwnTag(context));
	
	HtmlTag linkTag = link.getOwnTag(context);
	linkTag.copyAttributes(tag);
	tag.addChild(linkTag);

	return tag;
    }

    private HtmlHiddenField getActionLinkHiddenField(PageContext context) {
	Object hiddenField = context.findAttribute(ACTION_LINK_FIELD_KEY);

	if (hiddenField != null) {
	    return (HtmlHiddenField) hiddenField;
	} else {
	    HtmlHiddenField hidden = new HtmlHiddenField(getName(), null);
	    hidden.setId("____" + ACTION_LINK_FIELD_ID);

	    context.setAttribute(ACTION_LINK_FIELD_KEY, hiddenField, PageContext.REQUEST_SCOPE);
	    return hidden;
	}
    }
}
