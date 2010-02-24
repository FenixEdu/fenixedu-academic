package net.sourceforge.fenixedu.presentationTier.TagLib.renderers;

import static java.lang.String.format;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu._development.LogLevel;

import org.apache.log4j.Logger;

import pt.ist.fenixWebFramework.renderers.taglib.PropertyContainerTag;

/**
 * 
 * Add missing options from CollectionRenderer->TableLink
 * 
 * ex:
 * 
 * confirmation="label" confirmation="label,bundle,args",
 * confirmation="label,,args" confirmation="<%="label,bundle," + args %>"
 * 
 * ?
 * 
 */

public class LinkFormatTag extends TagSupport {

    static private final long serialVersionUID = 1L;
    static private final Logger logger = Logger.getLogger(LinkFormatTag.class);

    private String name;
    private String link;
    private String key;
    private String bundle;
    private String condition;
    private String order;

    public LinkFormatTag() {
    }

    @Override
    public void release() {
	super.release();

	this.name = null;
	this.link = null;
	this.key = null;
	this.bundle = null;
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

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getBundle() {
	return bundle;
    }

    public void setBundle(String bundle) {
	this.bundle = bundle;
    }

    public String getCondition() {
	return condition;
    }

    public void setCondition(String condition) {
	this.condition = condition;
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

    private void setProperties(final PropertyContainerTag parent) {
	setLink(parent);
	setKey(parent);
	setBundle(parent);
	setCondition(parent);
	setOrder(parent);
    }

    private void setLink(final PropertyContainerTag parent) {
	parent.addProperty(format("%s(%s)", getLinkType(), getName()), getLink());
    }

    private void setKey(final PropertyContainerTag parent) {
	parent.addProperty(format("key(%s)", getName()), getKey());
    }

    private void setBundle(final PropertyContainerTag parent) {
	if (!empty(getBundle())) {
	    parent.addProperty(format("bundle(%s)", getName()), getBundle());
	}
    }

    private void setCondition(final PropertyContainerTag parent) {
	if (!empty(getCondition())) {
	    parent.addProperty(format("%s(%s)", getConditionName(), getName()), getConditionValue());
	}
    }

    private void setOrder(final PropertyContainerTag parent) {
	if (!empty(getOrder())) {
	    parent.addProperty(format("order(%s)", getName()), getOrder());
	}
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
