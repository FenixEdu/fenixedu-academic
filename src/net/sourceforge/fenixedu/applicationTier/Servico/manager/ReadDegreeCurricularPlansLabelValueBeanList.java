package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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