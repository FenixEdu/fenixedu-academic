/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;

/**
 * @author Susana Fernandes
 * 
 */
public interface IProjectAccess extends IDomainObject {
    public abstract Calendar getBeginDate();

    public abstract void setBeginDate(Calendar beginDate);

    public abstract Calendar getEndDate();

    public abstract void setEndDate(Calendar endDate);

    public abstract Integer getKeyPerson();

    public abstract void setKeyPerson(Integer keyPerson);

    public abstract Integer getKeyProject();

    public abstract void setKeyProject(Integer keyProject);

    public abstract Integer getKeyProjectCoordinator();

    public abstract void setKeyProjectCoordinator(Integer keyProjectCoordinator);

    public abstract IPerson getPerson();

    public abstract void setPerson(IPerson person);

}