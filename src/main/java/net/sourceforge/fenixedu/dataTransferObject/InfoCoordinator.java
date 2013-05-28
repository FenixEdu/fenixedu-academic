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

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public InfoCoordinator(final Coordinator coordinator) {
        this.coordinator = coordinator;
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
    public Integer getExternalId() {
        return getCoordinator().getExternalId();
    }

    @Override
    public void setExternalId(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
