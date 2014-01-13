package net.sourceforge.fenixedu.domain.research.project;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * Class representing the connection between Project and Party classified by a
 * role
 */
public class ProjectParticipation extends ProjectParticipation_Base {

    public ProjectParticipation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        final Project project = this.getProject();

        this.setProject(null);
        project.sweep();

        this.setParty(null);

        this.setRootDomainObject(null);
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

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasRole() {
        return getRole() != null;
    }

    @Deprecated
    public boolean hasProject() {
        return getProject() != null;
    }

}
