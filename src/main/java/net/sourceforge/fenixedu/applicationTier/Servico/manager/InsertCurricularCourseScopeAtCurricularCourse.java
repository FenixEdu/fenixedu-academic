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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(InfoCurricularCourseScopeEditor infoCurricularCourseScope) throws FenixServiceException {
        Branch branch = null;
        CurricularSemester curricularSemester = null;
        try {
            curricularSemester =
                    RootDomainObject.getInstance().readCurricularSemesterByOID(infoCurricularCourseScope.getInfoCurricularSemester()
                            .getIdInternal());
            if (curricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }

            CurricularCourse curricularCourse =
                    (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(infoCurricularCourseScope.getInfoCurricularCourse()
                            .getIdInternal());
            if (curricularCourse == null) {
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
            }

            branch = RootDomainObject.getInstance().readBranchByOID(infoCurricularCourseScope.getInfoBranch().getIdInternal());
            if (branch == null) {
                throw new NonExistingServiceException("message.non.existing.branch", null);
            }

            new CurricularCourseScope(branch, curricularCourse, curricularSemester, infoCurricularCourseScope.getBeginDate(),
                    infoCurricularCourseScope.getEndDate(), infoCurricularCourseScope.getAnotation());
        } catch (IllegalWriteException iwe) {
            throw iwe;
        } catch (RuntimeException e) {
            throw new ExistingServiceException("O âmbito pertencente ao ramo " + branch.getCode() + ", no "
                    + curricularSemester.getCurricularYear().getYear() + "º ano,  " + curricularSemester.getSemester()
                    + "º semestre");
        }
    }

}