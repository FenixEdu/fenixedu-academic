/*
 * Created on 22/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScope implements IService {

    /**
     * Executes the service. Returns the current InfoCurricularCourseScope.
     */
    public InfoCurricularCourseScope run(Integer idInternal) throws FenixServiceException {
        ICurricularCourseScope curricularCourseScope;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            curricularCourseScope = (ICurricularCourseScope) sp.getIPersistentCurricularCourseScope()
                    .readByOID(CurricularCourseScope.class, idInternal);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (curricularCourseScope == null) {
            throw new NonExistingServiceException();
        }

        Integer curricularSemesterId = curricularCourseScope.getCurricularSemester().getIdInternal();
        InfoCurricularCourseScope infoCurricularCourseScope = Cloner
                .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);

        infoCurricularCourseScope.getInfoCurricularSemester().setIdInternal(curricularSemesterId);

        return infoCurricularCourseScope;
    }
}