package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.Participation.ResearchActivityParticipationRole;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlLink.Target;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer provides a way of presenting a list of 
 * net.sourceforge.fenixedu.domain.research.activity.Participation
 * objects.
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
     * Defines a link format, see layout link for more
     * information about this property.
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
	    Map<Person, List<ResearchActivityParticipationRole>> participationsMap = new HashMap<Person, List<ResearchActivityParticipationRole>>();

	    for (Participation participation : participations) {
		if (participationsMap.containsKey(participation.getParty())) {
		    participationsMap.get(participation.getParty()).add(participation.getRole());
		} else {
		    ArrayList<ResearchActivityParticipationRole> roles = new ArrayList<ResearchActivityParticipationRole>();
		    roles.add(participation.getRole());
		    participationsMap.put((Person) participation.getParty(), roles);
		}
	    }

	    HtmlInlineContainer container = new HtmlInlineContainer();

	    Set<Person> keySet = participationsMap.keySet();
	    int keySetSize = keySet.size();

	    for (Person person : keySet) {
		container.addChild(getPersonComponent(person));
		if (isShowRoles()) {
		    container.addChild(new HtmlText(" ("));
		    List<ResearchActivityParticipationRole> roleList = participationsMap.get(person);
		    int size = roleList.size();
		    for (ResearchActivityParticipationRole role : roleList) {
			container.addChild(new HtmlText(RenderUtils.getEnumString(role)));
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
		HtmlLink link = new HtmlLink();
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
