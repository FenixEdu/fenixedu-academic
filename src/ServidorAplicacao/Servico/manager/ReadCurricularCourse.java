/*
 * Created on 16/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadCurricularCourse implements IService {

    /**
     * Executes the service. Returns the current InfoCurricularCourse.
     */
    public InfoCurricularCourse run(Integer idInternal) throws FenixServiceException {
        ICurricularCourse curricularCourse;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse().readByOID(
                    CurricularCourse.class, idInternal);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        InfoCurricularCourse infoCurricularCourse = Cloner
                .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
        return infoCurricularCourse;
    }
}