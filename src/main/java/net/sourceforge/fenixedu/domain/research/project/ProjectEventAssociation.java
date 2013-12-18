package net.sourceforge.fenixedu.domain.research.project;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.research.activity.EventEdition;

public class ProjectEventAssociation extends ProjectEventAssociation_Base {

    public ProjectEventAssociation() {
        super();
        setRootDomainObject(Bennu.getInstance());

    }

    public void delete() {
        final EventEdition event = this.getEventEdition();
        final Project project = this.getProject();

        this.setEventEdition(null);
        event.sweep();

        this.setProject(null);
        project.sweep();

        this.setRootDomainObject(null);
        deleteDomainObject();
    }

    public enum ProjectEventAssociationRole {
        Exhibitor, Participant,
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEventEdition() {
        return getEventEdition() != null;
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
