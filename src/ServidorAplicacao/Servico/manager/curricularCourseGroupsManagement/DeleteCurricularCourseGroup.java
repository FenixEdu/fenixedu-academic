/*
 * Created on Jul 28, 2004
 *
 */
package ServidorAplicacao.Servico.manager.curricularCourseGroupsManagement;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.CurricularCourseGroup;
import Dominio.ICurricularCourseGroup;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */
public class DeleteCurricularCourseGroup implements IService {

    /**
     *  
     */
    public DeleteCurricularCourseGroup() {
    }

    public void run(Integer groupId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourseGroup persistentCurricularCourseGroup = persistentSuport
                    .getIPersistentCurricularCourseGroup();
            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) persistentCurricularCourseGroup
                    .readByOID(CurricularCourseGroup.class, groupId);
            if (curricularCourseGroup.getCurricularCourses() == null
                    || curricularCourseGroup.getCurricularCourses().isEmpty()) {
                persistentCurricularCourseGroup.deleteByOID(CurricularCourseGroup.class, groupId);
            } else {
                throw new InvalidArgumentsServiceException();
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}