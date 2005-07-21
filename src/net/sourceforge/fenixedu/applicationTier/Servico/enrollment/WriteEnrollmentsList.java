/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class WriteEnrollmentsList implements IService {
    public WriteEnrollmentsList() {
    }

    public void run(InfoStudent infoStudent, DegreeType degreeType, Integer executionPeriodId,
            List curricularCoursesList, Map optionalEnrollments, IUserView userView)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        if (infoStudent == null || infoStudent.getNumber() == null || degreeType == null
                || executionPeriodId == null ) {
            throw new FenixServiceException("");
        }

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();
        IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(), degreeType);
        if (studentCurricularPlan == null) {
            throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
        }

        if (curricularCoursesList != null && curricularCoursesList.size() > 0) {
            ListIterator iterator = curricularCoursesList.listIterator();
            while (iterator.hasNext()) {
                String enrollmentInfo = (String) iterator.next();

                Integer curricularCourseID = new Integer(enrollmentInfo.split("-")[0]);

                WriteEnrollment writeEnrollmentService = new WriteEnrollment();
                writeEnrollmentService.run(null, studentCurricularPlan.getIdInternal(),
                        curricularCourseID, executionPeriodId,
                        CurricularCourseEnrollmentType.VALIDATED, new Integer(
                                (String) optionalEnrollments.get(curricularCourseID.toString())),
                        userView);
            }
        }
    }
}