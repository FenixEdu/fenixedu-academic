package net.sourceforge.fenixedu.presentationTier.TagLib.renderers;

import static java.lang.String.format;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu._development.LogLevel;

import org.apache.log4j.Logger;

import pt.ist.fenixWebFramework.renderers.taglib.PropertyContainerTag;

public class LinkFormatTag extends TagSupport {

    static private final long serialVersionUID = 1L;
    static private final Logger logger = Logger.getLogger(LinkFormatTag.class);
    static private final String ELEMENTS_SEPARATOR = ",";

    private String name;
    private String link;
    private String label;
    private String condition;
    private String confirmation;
    private String order;

    public LinkFormatTag() {
    }

    @Override
    public void release() {
	super.release();

	this.name = null;
	this.link = null;
	this.label = null;
	this.condition = null;
	this.order = null;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getLink() {
	return link;
    }

    public void setLink(String link) {
	this.link = link;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public String getCondition() {
	return condition;
    }

    public void setCondition(String condition) {
	this.condition = condition;
    }

    public String getConfirmation() {
	return confirmation;
    }

    public void setConfirmation(String confirmation) {
	this.confirmation = confirmation;
    }

    public String getOrder() {
	return order;
    }

    public void setOrder(String order) {
	this.order = order;
    }

    @Override
    public int doEndTag() throws JspException {
	PropertyContainerTag parent = (PropertyContainerTag) findAncestorWithClass(this, PropertyContainerTag.class);

	if (parent != null) {

	    setProperties(parent);

	} else {
	    if (LogLevel.WARN) {
		logger.warn("property tag was using inside an invalid container");
	    }
	}

	return super.doEndTag();
    }

    private void setProperties(final PropertyContainerTag parent) throws JspException {
	setLink(parent).setLabel(parent).setCondition(parent).setConfirmation(parent).setOrder(parent);
    }

    private LinkFormatTag setLink(final PropertyContainerTag parent) {
	parent.addProperty(format("%s(%s)", getLinkType(), getName()), getLink());
	return this;
    }

    /*
     * Add missing args to confirmation?
     * confirmation="label,,args" confirmation="<%="label,bundle," + args %>"
     * 
     */
    private LinkFormatTag setConfirmation(PropertyContainerTag parent) throws JspException {

	final String[] values = getConfirmation().split(ELEMENTS_SEPARATOR);
	
	if (values.length == 0) {
	    return this;
	}

	final String key = values[0].trim();
	if (empty(key)) {
	    throw new JspException("confirmation is specified but no value found");
	}
	
	setConfirmationKey(parent, key);

	if (values.length > 1) {
	    final String bundle = values[1].trim();

	    if (!empty(bundle)) {
		setConfirmationBundle(parent, bundle);
	    }
	}
	
	return this;
    }
    
    private void setConfirmationKey(final PropertyContainerTag parent, final String key) {
	parent.addProperty(format("confirmationKey(%s)", getName()), key);
    }
    
    private void setConfirmationBundle(final PropertyContainerTag parent, final String bundle) {
	parent.addProperty(format("confirmationBundle(%s)", getName()), bundle);
    }

    private LinkFormatTag setLabel(final PropertyContainerTag parent) throws JspException {

	final String[] values = getLabel().split(ELEMENTS_SEPARATOR);

	if (values.length < 1) {
	    throw new JspException("must define label value");
	}

	final String key = values[0].trim();
	if (empty(key)) {
	    throw new JspException("must define label value");
	}

	setKey(parent, key);

	if (values.length > 1) {
	    final String bundle = values[1].trim();

	    if (!empty(bundle)) {
		setBundle(parent, bundle);
	    }
	}

	return this;
    }

    private void setKey(final PropertyContainerTag parent, final String key) {
	parent.addProperty(format("key(%s)", getName()), key);
    }

    private void setBundle(final PropertyContainerTag parent, final String bundle) {
	parent.addProperty(format("bundle(%s)", getName()), bundle);
    }

    private LinkFormatTag setCondition(final PropertyContainerTag parent) {
	if (!empty(getCondition())) {
	    parent.addProperty(format("%s(%s)", getConditionName(), getName()), getConditionValue());
	}
	return this;
    }

    private LinkFormatTag setOrder(final PropertyContainerTag parent) {
	if (!empty(getOrder())) {
	    parent.addProperty(format("order(%s)", getName()), getOrder());
	}
	return this;
    }

    /*
     * must improve this solution
     */
    private String getLinkType() {
	return getLink().matches(".*=\\$\\{.*\\}.*") ? "linkFormat" : "link";
    }

    /*
     * must improve this solution
     */
    private String getConditionName() {
	return getCondition().startsWith("!") ? "visibleIfNot" : "visibleIf";
    }

    /*
     * must improve this solution
     */
    private String getConditionValue() {
	return getCondition().replace("!", "");
    }

    private boolean empty(String value) {
	return value == null || value.length() == 0;
    }
}
