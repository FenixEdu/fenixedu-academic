/*
 * Created on Jan 12, 2005
 *
 */
package Dominio.projectsManagement;

import java.util.Calendar;

import Dominio.DomainObject;
import Dominio.IPerson;

/**
 * @author Susana Fernandes
 * 
 */
public class ProjectAccess extends DomainObject implements IProjectAccess {
    private Integer keyPerson;

    private IPerson person;

    private Integer keyProjectCoordinator;

    private Integer keyProject;

    private Calendar beginDate;

    private Calendar endDate;

    public Calendar getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Calendar beginDate) {
        this.beginDate = beginDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Integer getKeyPerson() {
        return keyPerson;
    }

    public void setKeyPerson(Integer keyPerson) {
        this.keyPerson = keyPerson;
    }

    public Integer getKeyProject() {
        return keyProject;
    }

    public void setKeyProject(Integer keyProject) {
        this.keyProject = keyProject;
    }

    public Integer getKeyProjectCoordinator() {
        return keyProjectCoordinator;
    }

    public void setKeyProjectCoordinator(Integer keyProjectCoordinator) {
        this.keyProjectCoordinator = keyProjectCoordinator;
    }

    public IPerson getPerson() {
        return person;
    }

    public void setPerson(IPerson person) {
        this.person = person;
    }
}
