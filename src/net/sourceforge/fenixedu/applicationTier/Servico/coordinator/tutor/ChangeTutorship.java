package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class ChangeTutorship extends TutorshipManagement {

	public List<TutorshipErrorBean> run(Integer executionDegreeID, List<ChangeTutorshipBean> beans) 
		throws FenixServiceException, ExcepcaoPersistencia {
		
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		
		List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();
		
		for(ChangeTutorshipBean bean : beans) {
			Tutorship tutorship = bean.getTutorship();
			
			Partial newTutorshipEndDate = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
	    			new int[] {bean.getTutorshipEndYear(), bean.getTutorshipEndMonth().getNumberOfMonth()});
			
			if(tutorship.getEndDate() != null && tutorship.getEndDate().isEqual(newTutorshipEndDate))
				continue;
			
			Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
			Integer studentNumber = registration.getNumber();
			
			try{
				validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);
			
				tutorship.setEndDate(newTutorshipEndDate);
				
			} catch (FenixServiceException ex) {
				studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
			}
		}
		
		return studentsWithErrors;
	}
}
