/*
 * Created on 27/Out/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Coordinator;

/**
 * fenix-head Dominio
 * 
 * @author João Mota 27/Out/2003
 *  
 */
public class InfoCoordinator extends InfoObject {

	private final Coordinator coordinator;

	public InfoCoordinator(final Coordinator coordinator) {
		this.coordinator = coordinator;
	}

    public InfoExecutionDegree getInfoExecutionDegree() {
    	return InfoExecutionDegree.newInfoFromDomain(coordinator.getExecutionDegree());
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(coordinator.getTeacher());
    }

    public Boolean getResponsible() {
        return coordinator.getResponsible();
    }

    public static InfoCoordinator newInfoFromDomain(final Coordinator coordinator) {
    	return coordinator == null ? null : new InfoCoordinator(coordinator);
    }

}