/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Coordinator;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCoordinatorWithInfoPerson extends InfoCoordinator {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator#copyFromDomain(Dominio.Coordinator)
     */
    public void copyFromDomain(Coordinator coordinator) {
        super.copyFromDomain(coordinator);
        if (coordinator != null) {
            setInfoTeacher(InfoTeacher.newInfoFromDomain(coordinator.getTeacher()));
        }
    }

    public static InfoCoordinator newInfoFromDomain(Coordinator coordinator) {
        InfoCoordinatorWithInfoPerson infoCoordinator = null;
        if (coordinator != null) {
            infoCoordinator = new InfoCoordinatorWithInfoPerson();
            infoCoordinator.copyFromDomain(coordinator);
        }
        return infoCoordinator;
    }
}