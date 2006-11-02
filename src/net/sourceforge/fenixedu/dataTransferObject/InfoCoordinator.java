/*
 * Created on 27/Out/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DomainReference;

/**
 * fenix-head Dominio
 * 
 * @author João Mota 27/Out/2003
 *  
 */
public class InfoCoordinator extends InfoObject {

    private final DomainReference<Coordinator> coordinatorDomainReference;

    public Coordinator getCoordinator() {
        return coordinatorDomainReference == null ? null : coordinatorDomainReference.getObject();
    }

	public InfoCoordinator(final Coordinator coordinator) {
		coordinatorDomainReference = new DomainReference<Coordinator>(coordinator);
	}

    public InfoExecutionDegree getInfoExecutionDegree() {
    	return InfoExecutionDegree.newInfoFromDomain(getCoordinator().getExecutionDegree());
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(getCoordinator().getPerson().getTeacher());
    }

    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getCoordinator().getPerson());
    }

    public Boolean getResponsible() {
        return getCoordinator().getResponsible();
    }

    public static InfoCoordinator newInfoFromDomain(final Coordinator coordinator) {
    	return coordinator == null ? null : new InfoCoordinator(coordinator);
    }

	@Override
	public Integer getIdInternal() {
		return getCoordinator().getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}