package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink.Target;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * This renderer provides a way of presenting a list of
 * net.sourceforge.fenixedu.domain.research.activity.Participation objects.
 * <p>
 * Roles displayment is configurable
 * 
 * @author pcma
 */

public class ParticipationsRenderer extends OutputRenderer {

    private String linkFormat;

    private String subSchema;

    private String subLayout;

    private boolean moduleRelative;

    private boolean contextRelative;

    private boolean showRoles;

    private boolean showRoleMessage;

    private boolean contextAvailable;

    public boolean iscontextAvailable() {
	return contextAvailable;
    }

    public void setContextAvailable(boolean hasContext) {
	this.contextAvailable = hasContext;
    }

    public boolean isShowRoleMessage() {
	return showRoleMessage;
    }

    public void setShowRoleMessage(boolean showRoleMessage) {
	this.showRoleMessage = showRoleMessage;
    }

    public boolean isShowRoles() {
	return showRoles;
    }

    /**
     * If roles will or not be displayed
     * 
     * @property
     */
    public void setShowRoles(boolean showRoles) {
	this.showRoles = showRoles;
    }

    public String getSubLayout() {
	return subLayout;
    }

    /**
     * Defines a sublayout for each participator
     * 
     * @property
     */
    public void setSubLayout(String layout) {
	this.subLayout = layout;
    }

    public String getSubSchema() {
	return subSchema;
    }

    /**
     * Difines a subSchema for each participator
     * 
     * @property
     */
    public void setSubSchema(String schema) {
	this.subSchema = schema;
    }

    public String getLinkFormat() {
	return linkFormat;
    }

    /**
     * Defines a link format, see layout link for more information about this
     * property.
     * 
     * @property
     */
    public void setLinkFormat(String linkFormat) {
	this.linkFormat = linkFormat;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new ParticipationLayout();
    }

    public class ParticipationLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    List<Participation> participations = (List<Participation>) object;
	    Map<Person, List<Participation>> participationsMap = new HashMap<Person, List<Participation>>();

	    for (Participation participation : participations) {
		if (participationsMap.containsKey(participation.getParty())) {
		    participationsMap.get(participation.getParty()).add(participation);
		} else {
		    ArrayList<Participation> participationsList = new ArrayList<Participation>();
		    participationsList.add(participation);
		    participationsMap.put((Person) participation.getParty(), participationsList);
		}
	    }

	    HtmlInlineContainer container = new HtmlInlineContainer();

	    Set<Person> keySet = participationsMap.keySet();
	    int keySetSize = keySet.size();

	    for (Person person : keySet) {
		container.addChild(getPersonComponent(person));
		if (isShowRoles()) {
		    container.addChild(new HtmlText(" ("));
		    List<Participation> participationList = participationsMap.get(person);
		    int size = participationList.size();
		    for (Participation participation : participationList) {
			container.addChild(new HtmlText(RenderUtils.getEnumString(participation.getRole())));
			if (showRoleMessage && participation.getRoleMessage() != null
				&& !participation.getRoleMessage().isEmpty()) {
			    container.addChild(new HtmlText(" - "));
			    container.addChild(new HtmlText(participation.getRoleMessage().getContent()));
			}

			if (participation instanceof ScientificJournalParticipation) {
			    ScientificJournalParticipation journalParticipation = (ScientificJournalParticipation) participation;

			    if (journalParticipation.getBeginDate() != null || journalParticipation.getEndDate() != null) {
				container.addChild(new HtmlText(" - "));
			    }
			    if (journalParticipation.getBeginDate() != null) {
				container.addChild(new HtmlText(" "
					+ RenderUtils.getResourceString("RESEARCHER_RESOURCES", "label.date.from") + " "));
				container.addChild(new HtmlText(journalParticipation.getBeginDate().toString("dd-MM-yyyy")));
			    }
			    if (journalParticipation.getEndDate() != null) {
				container.addChild(new HtmlText(" "
					+ RenderUtils.getResourceString("RESEARCHER_RESOURCES", "label.date.to") + " "));
				container.addChild(new HtmlText(journalParticipation.getEndDate().toString("dd-MM-yyyy")));
			    }
			}

			if (size > 1) {
			    container.addChild(new HtmlText(", "));
			    size--;
			}
		    }
		    container.addChild(new HtmlText(")"));
		}
		if (keySetSize > 1) {
		    container.addChild(new HtmlText(", "));
		    keySetSize--;
		}
	    }

	    container.setIndented(false);
	    return container;
	}

	private HtmlComponent getPersonComponent(Person person) {
	    HtmlComponent component;

	    Schema findSchema = RenderKit.getInstance().findSchema(getSubSchema());
	    HtmlComponent personComponent = renderValue(person, findSchema, getSubLayout());

	    if (!person.isHomePageAvailable()) {
		component = personComponent;
	    } else {
		HtmlLink link = iscontextAvailable() ? 
			new HtmlLinkWithPreprendedComment(ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX) : 
			new HtmlLinkWithPreprendedComment(ChecksumRewriter.NO_CHECKSUM_PREFIX);

		link.setTarget(Target.BLANK);
		link.setModuleRelative(isModuleRelative());
		link.setContextRelative(isContextRelative());
		link.setUrl(RenderUtils.getFormattedProperties(getLinkFormat(), person));
		link.setBody(personComponent);

		component = link;

	    }
	    return component;
	}

    }

    public boolean isContextRelative() {
	return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
	this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
	return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
	this.moduleRelative = moduleRelative;
    }

}
