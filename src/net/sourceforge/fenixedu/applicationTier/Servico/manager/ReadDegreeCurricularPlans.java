/*
 * Created on 4/Set/2003, 13:55:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Set/2003, 13:55:41
 *  
 */
public class ReadDegreeCurricularPlans implements IService {

    /**
     * Executes the service. Returns the current InfoDegreeCurricularPlan.
     */
    public List run() throws FenixServiceException {
        List curricularPlans = new LinkedList();
        List infoCurricularPlans = new LinkedList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            curricularPlans = sp.getIPersistentDegreeCurricularPlan().readAll();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
            IDegreeCurricularPlan curricularPlan = (IDegreeCurricularPlan) iter.next();

            infoCurricularPlans
                    .add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(curricularPlan));
        }
        return infoCurricularPlans;
    }
}