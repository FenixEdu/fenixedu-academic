package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCurricularCourseByID implements IService {

    /**
     *  
     */
    public ReadCurricularCourseByID() {

    }

    public InfoCurricularCourse run(Integer curricularCourseID) throws FenixServiceException {

        ICurricularCourse curricularCourse = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            curricularCourse = (ICurricularCourse) persistentObject.readByOID(CurricularCourse.class,
                    curricularCourseID);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        return Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
    }
}