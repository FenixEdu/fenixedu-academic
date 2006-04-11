/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadInfoEnrolmentEvaluationByEvaluationOID extends Service {

    public InfoEnrolmentEvaluation run(IUserView userView, Integer enrolmentOID)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {
        Student student = userView.getPerson().getStudentByType(DegreeType.DEGREE);
        if (student == null) {
            student = userView.getPerson().getStudentByType(DegreeType.MASTER_DEGREE);
        }
        final Enrolment enrolment = student.findEnrolmentByEnrolmentID(enrolmentOID);
        return (new GetEnrolmentGrade()).run(enrolment);
    }

}