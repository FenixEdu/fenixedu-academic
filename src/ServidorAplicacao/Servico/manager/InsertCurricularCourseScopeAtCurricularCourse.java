/*
 * Created on 21/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseScope;
import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.CurricularSemester;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse implements IService
    {

        public InsertCurricularCourseScopeAtCurricularCourse()
            {
            }

        public void run(InfoCurricularCourseScope infoCurricularCourseScope)
                throws FenixServiceException
            {
                IBranch branch = null;
                ICurricularSemester curricularSemester = null;
                ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
                try
                    {
                        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
                        IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSuport
                                .getIPersistentCurricularCourseScope();
                        IPersistentCurricularSemester persistentCurricularSemester = persistentSuport
                                .getIPersistentCurricularSemester();
                        IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
                                .getIPersistentCurricularCourse();
                        IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
                        ICurricularSemester semester = new CurricularSemester();
                        semester.setIdInternal(infoCurricularCourseScope.getInfoCurricularSemester()
                                .getIdInternal());
                        curricularSemester = (ICurricularSemester) persistentCurricularSemester
                                .readByOId(semester, false);
                        if (curricularSemester == null) { throw new NonExistingServiceException(
                                "message.non.existing.curricular.semester", null); }
                        ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                                .readByOId(new CurricularCourse(infoCurricularCourseScope
                                        .getInfoCurricularCourse().getIdInternal()), false);
                        if (curricularCourse == null)
                                throw new NonExistingServiceException(
                                        "message.nonExistingCurricularCourse", null);
                        IBranch temporaryBranch = new Branch();
                        temporaryBranch.setIdInternal(infoCurricularCourseScope.getInfoBranch()
                                .getIdInternal());
                        branch = (IBranch) persistentBranch.readByOId(temporaryBranch, false);
                        if (branch == null)
                                throw new NonExistingServiceException("message.non.existing.branch",
                                        null);
                        // check that there isn't another scope active with the
                        // same
                        // curricular course, branch and semester
                        ICurricularCourseScope curricularCourseScopeFromDB = null;
                        curricularCourseScopeFromDB = persistentCurricularCourseScope
                                .readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
                                        curricularCourse, curricularSemester, branch, null);
                        if (curricularCourseScopeFromDB != null) { throw new ExistingPersistentException(); }
                        persistentCurricularCourseScope.simpleLockWrite(curricularCourseScope);
                        curricularCourseScope.setBranch(branch);
                        curricularCourseScope.setCurricularCourse(curricularCourse);
                        curricularCourseScope.setCurricularSemester(curricularSemester);
                        curricularCourseScope.setBeginDate(infoCurricularCourseScope.getBeginDate());
                        curricularCourseScope.setEndDate(null);
                    }
                catch (ExistingPersistentException e)
                    {
                        //FIXME: remove use of portuguese
                        throw new ExistingServiceException("O âmbito com ramo " + branch.getCode()
                                + ", do " + curricularSemester.getCurricularYear().getYear() + "º ano, "
                                + curricularSemester.getSemester() + "º semestre");
                    }
                catch (ExcepcaoPersistencia e)
                    {
                        throw new FenixServiceException(e);
                    }
            }
    }