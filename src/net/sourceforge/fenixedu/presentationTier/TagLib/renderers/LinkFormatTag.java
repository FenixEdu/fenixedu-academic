package net.sourceforge.fenixedu.presentationTier.TagLib.renderers;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.List;

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
    private String target;
    private String module;
    private String contextRelative;
    private String hasContext;

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

    public boolean hasConfirmation() {
        return !empty(this.confirmation);
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    private boolean hasTarget() {
        return !empty(this.target);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    private boolean hasModule() {
        return this.module != null;
    }

    public String getContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(String contextRelative) {
        this.contextRelative = contextRelative;
    }

    public boolean hasContextRelative() {
        return this.contextRelative != null;
    }

    public String getHasContext() {
        return hasContext;
    }

    public void setHasContext(String hasContext) {
        this.hasContext = hasContext;
    }

    public boolean hasContext() {
        return this.hasContext != null;
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
        setLink(parent).setLabel(parent).setCondition(parent).setConfirmation(parent).setOrder(parent).setTarget(parent)
                .setModule(parent).setContextRelative(parent).setHasContext(parent);
    }

    private LinkFormatTag setLink(final PropertyContainerTag parent) {
        parent.addProperty(format("%s(%s)", getLinkType(), getName()), getLink());
        return this;
    }

    /*
     * Add missing args to confirmation? confirmation="label,,args"
     * confirmation="<%="label,bundle," + args %>"
     */
    private LinkFormatTag setConfirmation(PropertyContainerTag parent) throws JspException {

        if (!hasConfirmation()) {
            return this;
        }

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

        // label is required
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
        return getLink().matches(".*\\$\\{.*\\}.*") ? "linkFormat" : "link";
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

    private LinkFormatTag setTarget(final PropertyContainerTag parent) throws JspException {

        if (hasTarget()) {

            final String target = getFormattedTarget();

            if (targetIsValid(target)) {
                throw new JspException("invalid target value. Accepted values: _blank, _self, _parent, _top");
            }

            parent.addProperty(format("target(%s)", getName()), target);
        }

        return this;
    }

    private LinkFormatTag setModule(final PropertyContainerTag parent) throws JspException {

        if (hasModule()) {
            parent.addProperty(format("module(%s)", getName()), module);
        }

        return this;
    }

    private LinkFormatTag setContextRelative(final PropertyContainerTag parent) throws JspException {

        if (hasContextRelative()) {
            parent.addProperty(format("contextRelative(%s)", getName()), contextRelative);
        }

        return this;
    }

    private LinkFormatTag setHasContext(final PropertyContainerTag parent) throws JspException {

        if (hasContext()) {
            parent.addProperty(format("hasContext(%s)", getName()), hasContext);
        }

        return this;
    }

    private String getFormattedTarget() {
        final String target = getTarget();
        return target.startsWith("_") ? target : "_" + target;
    }

    static private List<String> ACCEPTED_TARGETS = Arrays.asList("_blank,_self,_parent,_top".split(","));

    private boolean targetIsValid(String value) {
        return !ACCEPTED_TARGETS.contains(value);
    }

}
