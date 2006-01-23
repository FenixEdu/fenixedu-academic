package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;


public class EditCurricularCourseScope extends Service {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException, ExcepcaoPersistencia {

        CurricularCourseScope oldCurricularCourseScope = null;
        CurricularSemester newCurricularSemester = null;
        Branch newBranch = null;

        try {
            Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
            newBranch = (Branch) persistentObject.readByOID(Branch.class, branchId);

            if (newBranch == null) {
                throw new NonExistingServiceException("message.non.existing.branch", null);
            }

			
            Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
            newCurricularSemester = (CurricularSemester) persistentObject.readByOID(
                    CurricularSemester.class, curricularSemesterId);

            if (newCurricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }

			
            oldCurricularCourseScope = (CurricularCourseScope) persistentObject.readByOID(
					CurricularCourseScope.class, newInfoCurricularCourseScope.getIdInternal());

            if (oldCurricularCourseScope == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.course.scope",
                        null);
            }

			oldCurricularCourseScope.edit(newBranch, newCurricularSemester, newInfoCurricularCourseScope.getBeginDate(),
					null, newInfoCurricularCourseScope.getAnotation());

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException("O âmbito com ramo " + newBranch.getCode() + ", do "
                    + newCurricularSemester.getCurricularYear().getYear() + "º ano, "
                    + newCurricularSemester.getSemester() + "º semestre");
        }
    }
}