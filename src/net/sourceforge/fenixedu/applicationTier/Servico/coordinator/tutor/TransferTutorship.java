package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementByEntryYearBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class TransferTutorship extends TutorshipManagement {

	public List<TutorshipErrorBean> run(Integer executionDegreeID, TutorshipManagementBean bean,
			List<TutorshipManagementByEntryYearBean> tutorshipsToTransfer) throws FenixServiceException{
		
		final Teacher teacher = Teacher.readByNumber(bean.getTeacherNumber());
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		
		validateTeacher(teacher, executionDegree);
		
		List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

		YearMonthDay currentDate = new YearMonthDay();  
		
		Partial tutorshipEndDateDueToTransfer = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()},
    			new int[] {currentDate.year().get(), currentDate.monthOfYear().get()});
		
		for(TutorshipManagementByEntryYearBean tutorshipBean : tutorshipsToTransfer) {
			
			List<Tutorship> tutorships = tutorshipBean.getStudentsList();
			
			for(Tutorship tutorship : tutorships) {
				Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
				Integer studentNumber = registration.getNumber();
				
				try{
					validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);
					
					//1º Update the old Tutorship (endDate)
					
					tutorship.setEndDate(tutorshipEndDateDueToTransfer);
					
					//2º Create new Tutorship
					
					createTutorship(teacher, registration.getActiveStudentCurricularPlan(), bean.getTutorshipEndMonth().getNumberOfMonth(), 
							bean.getTutorshipEndYear());
					
				} catch (FenixServiceException ex) {
					studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
					
				} catch (DomainException ex) {
					studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
				}
			}		
		}	
		
		return studentsWithErrors;
	}
}