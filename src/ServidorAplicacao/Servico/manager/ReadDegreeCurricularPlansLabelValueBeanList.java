package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class ReadDegreeCurricularPlansLabelValueBeanList implements IService {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        List curricularPlans = sp.getIPersistentDegreeCurricularPlan().readAll();

        List result = new ArrayList();
        Iterator iterator = curricularPlans.iterator();
        while (iterator.hasNext()) {
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
            String name = degreeCurricularPlan.getName();
            Integer id = degreeCurricularPlan.getIdInternal();
            result.add(new LabelValueBean(name, id.toString()));
        }

        return result;
    }
}