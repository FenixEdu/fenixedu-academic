package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * Class representing the connection between Project and Party classified by a
 * role
 */
public class ProjectParticipation extends ProjectParticipation_Base {

    public ProjectParticipation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	final Project project = this.getProject();

	this.removeProject();
	project.sweep();

	this.removeParty();

	this.removeRootDomainObject();
	deleteDomainObject();
    }

    public enum ProjectParticipationType {
	Coordinator, Speaker, Sponsor, Participant;

	public static ProjectParticipationType getDefaultUnitRoleType() {
	    return Sponsor;
	}

	public static ProjectParticipationType getDefaultPersonRoleType() {
	    return Speaker;
	}
    }
}
