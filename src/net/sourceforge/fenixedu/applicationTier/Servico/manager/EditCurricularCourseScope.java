package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class EditCurricularCourseScope implements IService {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException, ExcepcaoPersistencia {

        CurricularCourseScope oldCurricularCourseScope = null;
        CurricularSemester newCurricularSemester = null;
        Branch newBranch = null;

        try {
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentBranch persistentBranch = ps.getIPersistentBranch();
            IPersistentCurricularSemester persistentCurricularSemester = ps.getIPersistentCurricularSemester();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = ps.getIPersistentCurricularCourseScope();

            Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();
            newBranch = (Branch) persistentBranch.readByOID(Branch.class, branchId);

            if (newBranch == null) {
                throw new NonExistingServiceException("message.non.existing.branch", null);
            }

			
            Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester().getIdInternal();
            newCurricularSemester = (CurricularSemester) persistentCurricularSemester.readByOID(
                    CurricularSemester.class, curricularSemesterId);

            if (newCurricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }

			
            oldCurricularCourseScope = (CurricularCourseScope) persistentCurricularCourseScope.readByOID(
					CurricularCourseScope.class, newInfoCurricularCourseScope.getIdInternal(), true);

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