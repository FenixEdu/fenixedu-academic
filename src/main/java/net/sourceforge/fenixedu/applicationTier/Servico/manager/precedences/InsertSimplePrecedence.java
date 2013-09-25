/*
 * Created on 30/Jul/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertSimplePrecedence {

    @Atomic
    public static void run(String className, String curricularCourseToAddPrecedenceID, String precedentCurricularCourseID,
            Integer number) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        CurricularCourse curricularCourseToAddPrecedence =
                (CurricularCourse) FenixFramework.getDomainObject(curricularCourseToAddPrecedenceID);
        if (curricularCourseToAddPrecedence == null) {
            throw new FenixServiceException("curricularCourseToAddPrecedence.NULL");
        }

        CurricularCourse precedentCurricularCourse = null;
        if (precedentCurricularCourseID != null) {
            precedentCurricularCourse = (CurricularCourse) FenixFramework.getDomainObject(precedentCurricularCourseID);
            if (precedentCurricularCourse == null) {
                throw new FenixServiceException("precedentCurricularCourse.NULL");
            }
        }

        new Precedence(curricularCourseToAddPrecedence, className, precedentCurricularCourse, number);
    }

}