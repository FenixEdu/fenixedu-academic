/*
 * Created on 30/Jul/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertSimplePrecedence {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(String className, String curricularCourseToAddPrecedenceID, String precedentCurricularCourseID,
            Integer number) throws FenixServiceException {
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