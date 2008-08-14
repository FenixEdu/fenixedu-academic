package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink.Target;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class JournalIssueRenderer extends OutputRenderer {

    private String linkFormat;
    private boolean moduleRelative;
    private boolean contextRelative;
    private boolean blankTarget;

    public boolean isBlankTarget() {
	return blankTarget;
    }

    public void setBlankTarget(boolean blankTarget) {
	this.blankTarget = blankTarget;
    }

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public String getLinkFormat() {
	return linkFormat;
    }

    public void setLinkFormat(String linkFormat) {
	this.linkFormat = linkFormat;
    }

    public boolean isModuleRelative() {
	return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
	this.moduleRelative = moduleRelative;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new JournalIssueLayout();
    }

    public class JournalIssueLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    JournalIssue issue = (JournalIssue) object;
	    StringBuilder presentationText = new StringBuilder();
	    presentationText.append(issue.getScientificJournal().getName()).append(" - ").append(issue.getVolume());
	    if (issue.getNumber() != null && issue.getNumber().length() > 0) {
		presentationText.append(" (").append(issue.getNumber()).append(")");
	    }

	    presentationText.append(" ");

	    if (issue.getYear() != null) {
		presentationText.append(issue.getYear());
	    }

	    HtmlLink link = new HtmlLink();
	    link.setModuleRelative(isModuleRelative());
	    link.setContextRelative(isContextRelative());
	    if (isBlankTarget()) {
		link.setTarget(Target.BLANK);
	    }
	    link.setText(presentationText.toString());
	    link.setUrl(RenderUtils.getFormattedProperties(getLinkFormat(), issue));

	    return link;

	}

    }
}
