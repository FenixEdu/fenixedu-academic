/*
 * Created on 18/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.CantDeleteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteEnrollmentsList extends Service {

    public void run(InfoStudent infoStudent, DegreeType degreeType, List enrolmentIDList)
            throws FenixServiceException, ExcepcaoPersistencia {
        final Student student = Student.readStudentByNumberAndDegreeType(infoStudent.getNumber(), degreeType);
        if (student != null && enrolmentIDList != null) {
            for (final Integer enrolmentID : ((List<Integer>) enrolmentIDList)) {
                final Enrolment enrolment = student.findEnrolmentByEnrolmentID(enrolmentID);
                if (enrolment != null) {
                    try {
                        enrolment.unEnroll();
                    } catch (DomainException e) {
                        throw new CantDeleteServiceException();
                    }
                }                
            }
        }
    }

}
