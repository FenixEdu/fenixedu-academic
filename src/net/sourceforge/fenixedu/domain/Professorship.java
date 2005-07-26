package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerAware;
import org.apache.ojb.broker.PersistenceBrokerException;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base implements ICreditsEventOriginator,
        PersistenceBrokerAware {

    public String toString() {
        String result = "Professorship :\n";
        result += "\n  - ExecutionCourse : " + getExecutionCourse();
        result += "\n  - Teacher : " + getTeacher();

        return result;
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    private void notifyTeacher() {
        if (this.getExecutionCourse() != null && this.getExecutionCourse().isMasterDegreeOnly()) {
            ITeacher teacher = this.getTeacher();
            teacher.notifyCreditsChange(CreditsEvent.MASTER_DEGREE_LESSONS, this);
        }
    }

    public void afterUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterInsert(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void afterDelete(PersistenceBroker broker) throws PersistenceBrokerException {
        notifyTeacher();
    }

    public void beforeUpdate(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeInsert(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void beforeDelete(PersistenceBroker broker) throws PersistenceBrokerException {
    }

    public void afterLookup(PersistenceBroker broker) throws PersistenceBrokerException {
    }
    
    public static IProfessorship create(Boolean responsibleFor, IExecutionCourse executionCourse, ITeacher teacher, Double hours) throws MaxResponsibleForExceed, InvalidCategory{
       
        if(responsibleFor == null || executionCourse == null || teacher == null)
            throw new NullPointerException();
        
        IProfessorship professorShip = new Professorship();
               
        if(hours == null)
            professorShip.setHours(new Double(0.0));
        else
            professorShip.setHours(hours);

        if (responsibleFor.booleanValue()) {            
            ResponsibleForValidator.getInstance().validateResponsibleForList(teacher, executionCourse, professorShip);
            professorShip.setResponsibleFor(true);
        }
        else
            professorShip.setResponsibleFor(false);
        
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setTeacher(teacher);
        
        return professorShip;
    }

    public void delete() {

        if (this.getAssociatedSummariesCount() > 0 || this.getAssociatedShiftProfessorshipCount() > 0
                || this.getSupportLessonsCount() > 0)
            throw new DomainException(this.getClass().getName(), "");

        this.setExecutionCourse(null);
        this.setTeacher(null);
    }
}