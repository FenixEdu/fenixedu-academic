/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadCurriculum implements IService {

    public InfoCurriculum run(Integer curricularCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {

        ICurricularCourse curricularCourse;
        ICurriculum curriculum;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOID(
                CurricularCourse.class, curricularCourseId);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        curriculum = sp.getIPersistentCurriculum().readCurriculumByCurricularCourse(
                curricularCourse.getIdInternal());

        if (curriculum == null) {
            return null;
        }
        InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
        return infoCurriculum;
    }
}