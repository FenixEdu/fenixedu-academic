/*
 * Created on 4/Mai/2005 - 15:38:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod implements
		IService {

	private static ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod service =
		new ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod();
	
	public ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod() {
	}

	public String getNome() {
		return "student.ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod";
	}

    /**
     * @return Returns the service.
     */
    public static ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod getService() {
        return service;
    }
    
	public List<InfoCurricularCourse> run(Integer studentCurricularPlanId, Integer executionPeriodId)
	throws ExcepcaoPersistencia {
		
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentEnrollment pe = sp.getIPersistentEnrolment();
		
		List<IEnrolment> enrollments =
			pe.readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(studentCurricularPlanId, executionPeriodId);
		
		List<InfoCurricularCourse> enrolledCurricularCourses = new ArrayList<InfoCurricularCourse>();
		
		for(IEnrolment enrollment : enrollments) {
			enrolledCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(enrollment.getCurricularCourse()));
		}
		
		return enrolledCurricularCourses;

		
	}
}
