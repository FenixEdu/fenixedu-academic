/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package ServidorAplicacao.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadCoordinatedDegrees implements IService {

    public ReadCoordinatedDegrees() {
    }

    public List run(UserView userView) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        List degrees = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the Teacher

            ITeacher teacher = sp.getIPersistentTeacher()
                    .readTeacherByUsername(userView.getUtilizador());
            if (teacher == null) {
                throw new ExcepcaoInexistente("No Teachers Found !!");
            }
            degrees = sp.getIPersistentCoordinator().readExecutionDegreesByTeacher(teacher);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }
        if (degrees == null) {
            throw new ExcepcaoInexistente("No Degrees Found !!");
        }
        Iterator iterator = degrees.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
            result.add(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus
                    .newInfoFromDomain(executionDegree));
        }

        return result;
    }
}