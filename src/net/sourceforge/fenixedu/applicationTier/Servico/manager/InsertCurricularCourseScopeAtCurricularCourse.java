/*
 * Created on 21/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse implements IService {

    public InsertCurricularCourseScopeAtCurricularCourse() {
    }

    public void run(InfoCurricularCourseScope infoCurricularCourseScope) throws FenixServiceException {
        IBranch branch = null;
        ICurricularSemester curricularSemester = null;
        ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSuport
                    .getIPersistentCurricularCourseScope();
            IPersistentCurricularSemester persistentCurricularSemester = persistentSuport
                    .getIPersistentCurricularSemester();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                    .getIPersistentCurricularCourse();
            IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();

            curricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOID(
                    CurricularSemester.class, infoCurricularCourseScope.getInfoCurricularSemester()
                            .getIdInternal());
            if (curricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }
            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, infoCurricularCourseScope
                            .getInfoCurricularCourse().getIdInternal());
            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

            branch = (IBranch) persistentBranch.readByOID(Branch.class, infoCurricularCourseScope
                    .getInfoBranch().getIdInternal());
            if (branch == null)
                throw new NonExistingServiceException("message.non.existing.branch", null);
            // check that there isn't another scope active with the
            // same
            // curricular course, branch and semester
            ICurricularCourseScope curricularCourseScopeFromDB = null;
            curricularCourseScopeFromDB = persistentCurricularCourseScope
                    .readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
                            curricularCourse, curricularSemester, branch, null);
            if (curricularCourseScopeFromDB != null) {
                throw new ExistingPersistentException();
            }
            persistentCurricularCourseScope.simpleLockWrite(curricularCourseScope);
            curricularCourseScope.setBranch(branch);
            curricularCourseScope.setCurricularCourse(curricularCourse);
            curricularCourseScope.setCurricularSemester(curricularSemester);
            curricularCourseScope.setBeginDate(infoCurricularCourseScope.getBeginDate());
            curricularCourseScope.setEndDate(null);
        } catch (ExistingPersistentException e) {
            //FIXME: remove use of portuguese
            throw new ExistingServiceException("O âmbito com ramo " + branch.getCode() + ", do "
                    + curricularSemester.getCurricularYear().getYear() + "º ano, "
                    + curricularSemester.getSemester() + "º semestre");
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}