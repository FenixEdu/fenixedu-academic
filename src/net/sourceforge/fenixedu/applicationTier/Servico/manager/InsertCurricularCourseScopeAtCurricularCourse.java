/*
 * Created on 21/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */
public class InsertCurricularCourseScopeAtCurricularCourse extends Service {

    public void run(InfoCurricularCourseScope infoCurricularCourseScope) throws FenixServiceException, ExcepcaoPersistencia {
        Branch branch = null;
        CurricularSemester curricularSemester = null;
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularSemester persistentCurricularSemester = persistentSuport.getIPersistentCurricularSemester();
            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
            IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();

			
			
            curricularSemester = (CurricularSemester) persistentCurricularSemester.readByOID(
                    CurricularSemester.class, infoCurricularCourseScope.getInfoCurricularSemester().getIdInternal());
            if (curricularSemester == null)
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
         
			
            
			CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(
					CurricularCourse.class, infoCurricularCourseScope.getInfoCurricularCourse().getIdInternal());
            if (curricularCourse == null)
                throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);

			
			
            branch = (Branch) persistentBranch.readByOID(
					Branch.class, infoCurricularCourseScope.getInfoBranch().getIdInternal());
            if (branch == null)
                throw new NonExistingServiceException("message.non.existing.branch", null);
			
			DomainFactory.makeCurricularCourseScope(branch, curricularCourse, curricularSemester, infoCurricularCourseScope.getBeginDate(),
										infoCurricularCourseScope.getEndDate(), infoCurricularCourseScope.getAnotation());
			
        } catch (RuntimeException e) {
            throw new ExistingServiceException("O âmbito pertencente ao ramo " + branch.getCode() + ", no "
                    + curricularSemester.getCurricularYear().getYear() + "º ano,  "
                    + curricularSemester.getSemester() + "º semestre");
        }
    }
}