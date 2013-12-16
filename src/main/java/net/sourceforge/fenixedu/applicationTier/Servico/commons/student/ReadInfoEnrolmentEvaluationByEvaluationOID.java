/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadInfoEnrolmentEvaluationByEvaluationOID {

    @Atomic
    public static InfoEnrolmentEvaluation run(User userView, Integer studentNumber, DegreeType degreeType,
            String enrolmentOID) throws ExcepcaoInexistente, FenixServiceException {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);
        return (new GetEnrolmentGrade()).run((Enrolment) FenixFramework.getDomainObject(enrolmentOID));
    }

}