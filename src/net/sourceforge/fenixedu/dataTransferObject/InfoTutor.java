/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Tutor;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoTutor extends InfoObject {

    private final DomainReference<Tutor> tutorDomainReference;
    
    public InfoTutor(final Tutor tutor) {
	tutorDomainReference = new DomainReference<Tutor>(tutor);
    }
    
    public static InfoTutor newInfoFromDomain(final Tutor tutor) {
	return tutor == null ? null : new InfoTutor(tutor);
    }
    
    private Tutor getTutor() {
	return tutorDomainReference == null ? null : tutorDomainReference.getObject();
    }
    
    @Override
    public Integer getIdInternal() {
	return getTutor().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return InfoStudent.newInfoFromDomain(getTutor().getStudentCurricularPlan().getStudent());
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(getTutor().getTeacher());
    }

}
