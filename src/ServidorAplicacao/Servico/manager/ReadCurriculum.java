/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurriculum;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadCurriculum implements IService {

    /**
     * Executes the service. Returns the current InfoCurriculum.
     */
    public InfoCurriculum run(Integer curricularCourseId) throws FenixServiceException {

        ICurricularCourse curricularCourse;
        ICurriculum curriculum;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOID(
                    CurricularCourse.class, curricularCourseId);
            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }
            curriculum = sp.getIPersistentCurriculum()
                    .readCurriculumByCurricularCourse(curricularCourse);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        if (curriculum == null) {
            return null;
        }
        InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
        return infoCurriculum;
    }
}