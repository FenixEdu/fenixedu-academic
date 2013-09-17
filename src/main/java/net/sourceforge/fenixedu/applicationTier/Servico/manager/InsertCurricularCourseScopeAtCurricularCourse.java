/*
 * Created on 21/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeEditor;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse {

    @Atomic
    public static void run(InfoCurricularCourseScopeEditor infoCurricularCourseScope) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Branch branch = null;
        CurricularSemester curricularSemester = null;
        try {
            curricularSemester =
                    FenixFramework.getDomainObject(infoCurricularCourseScope.getInfoCurricularSemester()
                            .getExternalId());
            if (curricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }

            CurricularCourse curricularCourse =
                    (CurricularCourse) FenixFramework.getDomainObject(infoCurricularCourseScope.getInfoCurricularCourse()
                            .getExternalId());
            if (curricularCourse == null) {
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
            }

            branch = FenixFramework.getDomainObject(infoCurricularCourseScope.getInfoBranch().getExternalId());
            if (branch == null) {
                throw new NonExistingServiceException("message.non.existing.branch", null);
            }

            new CurricularCourseScope(branch, curricularCourse, curricularSemester, infoCurricularCourseScope.getBeginDate(),
                    infoCurricularCourseScope.getEndDate(), infoCurricularCourseScope.getAnotation());
        } catch (WriteOnReadError iwe) {
            throw iwe;
        } catch (RuntimeException e) {
            throw new ExistingServiceException("O âmbito pertencente ao ramo " + branch.getCode() + ", no "
                    + curricularSemester.getCurricularYear().getYear() + "º ano,  " + curricularSemester.getSemester()
                    + "º semestre");
        }
    }

}