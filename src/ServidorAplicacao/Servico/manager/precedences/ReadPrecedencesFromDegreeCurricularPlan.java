package ServidorAplicacao.Servico.manager.precedences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.precedences.InfoPrecedence;
import DataBeans.precedences.InfoPrecedenceWithRestrictions;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.precedences.IPrecedence;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadPrecedencesFromDegreeCurricularPlan implements IService {

    public ReadPrecedencesFromDegreeCurricularPlan() {
    }

    public Map run(Integer degreeCurricularPlanID) throws FenixServiceException {

        Map finalListOfInfoPrecedences = new HashMap();

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = persistentSuport.getIPersistentDegreeCurricularPlan();
            IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();
            
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) degreeCurricularPlanDAO.readByOID(
                    DegreeCurricularPlan.class, degreeCurricularPlanID);

            List curricularCourses = degreeCurricularPlan.getCurricularCourses();
            int size = curricularCourses.size();

            for (int i = 0; i < size; i++) {
                ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(i);
                List precedences = precedenceDAO.readByCurricularCourse(curricularCourse);
                putInMap(finalListOfInfoPrecedences, curricularCourse, precedences);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        
        return finalListOfInfoPrecedences;
    }

    private void putInMap(Map finalListOfInfoPrecedences, ICurricularCourse curricularCourse, List precedences) {

        if (!precedences.isEmpty()) {
    		InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);

    		List infoPrecedences = clone(precedences);

    		finalListOfInfoPrecedences.put(infoCurricularCourse, infoPrecedences);
        }
    }

    private List clone(List precedences) {

        List result = new ArrayList();

        int size = precedences.size();

        for (int i = 0; i < size; i++) {
            IPrecedence precedence = (IPrecedence) precedences.get(i);
            InfoPrecedence infoPrecedence = InfoPrecedenceWithRestrictions.newInfoFromDomain(precedence);
            result.add(infoPrecedence);
        }

        return result;
    }
}