/*
 * Created on Oct 10, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio;

import java.sql.Timestamp;
import java.util.List;

import Util.State;

/**
 * Authors : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 * (naat@mega.ist.utl.pt)
 */
public interface IMasterDegreeThesisDataVersion extends IDomainObject {

    public abstract void setAssistentGuiders(List assistentGuiders);

    public abstract List getAssistentGuiders();

    public abstract void setDissertationTitle(String dissertationTitle);

    public abstract String getDissertationTitle();

    public abstract void setExternalAssistentGuiders(List externalAssistentGuiders);

    public abstract List getExternalAssistentGuiders();

    public void setResponsibleEmployee(IEmployee responsibleEmployee);

    public IEmployee getResponsibleEmployee();

    public abstract void setGuiders(List guiders);

    public abstract List getGuiders();

    public List getExternalGuiders();

    public void setExternalGuiders(List externalGuiders);

    public abstract void setLastModification(Timestamp lastModification);

    public abstract Timestamp getLastModification();

    public abstract void setMasterDegreeThesis(IMasterDegreeThesis masterDegreeThesis);

    public abstract IMasterDegreeThesis getMasterDegreeThesis();

    public void setCurrentState(State currentState);

    public State getCurrentState();
}