/*
 * Created on 21/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBranch;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class EditCurricularCourseScope implements IService {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws FenixServiceException {

        ICurricularCourseScope oldCurricularCourseScope = null;
        ICurricularSemester newCurricularSemester = null;
        IBranch newBranch = null;

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentBranch persistentBranch = ps.getIPersistentBranch();
            IPersistentCurricularSemester persistentCurricularSemester = ps
                    .getIPersistentCurricularSemester();
            IPersistentCurricularCourseScope persistentCurricularCourseScope = ps
                    .getIPersistentCurricularCourseScope();

            Integer branchId = newInfoCurricularCourseScope.getInfoBranch().getIdInternal();

            newBranch = (IBranch) persistentBranch.readByOID(Branch.class, branchId);

            if (newBranch == null) {
                throw new NonExistingServiceException("message.non.existing.branch", null);
            }

            Integer curricularSemesterId = newInfoCurricularCourseScope.getInfoCurricularSemester()
                    .getIdInternal();

            newCurricularSemester = (ICurricularSemester) persistentCurricularSemester.readByOID(
                    CurricularSemester.class, curricularSemesterId);

            if (newCurricularSemester == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.semester", null);
            }

            oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
                    .readByOID(CurricularCourseScope.class,
                            newInfoCurricularCourseScope.getIdInternal(), true);

            if (oldCurricularCourseScope == null) {
                throw new NonExistingServiceException("message.non.existing.curricular.course.scope",
                        null);
            }

            oldCurricularCourseScope.setBranch(newBranch);
            //it already includes the curricular year
            oldCurricularCourseScope.setCurricularSemester(newCurricularSemester);
            oldCurricularCourseScope.setBeginDate(newInfoCurricularCourseScope.getBeginDate());
            oldCurricularCourseScope.setEndDate(null);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException("O âmbito com ramo " + newBranch.getCode() + ", do "
                    + newCurricularSemester.getCurricularYear().getYear() + "º ano, "
                    + newCurricularSemester.getSemester() + "º semestre");
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}