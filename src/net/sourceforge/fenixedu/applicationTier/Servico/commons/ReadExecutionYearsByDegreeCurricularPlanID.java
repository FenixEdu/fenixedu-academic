/*
 * Created on 9/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadExecutionYearsByDegreeCurricularPlanID implements IService {

    public List run(Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = sp
                .getIPersistentDegreeCurricularPlan();
        IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) degreeCurricularPlanDAO
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<IExecutionYear> executionYears = (List<IExecutionYear>) CollectionUtils.collect(
                degreeCurricularPlan.getExecutionDegrees(), new Transformer() {

                    public Object transform(Object arg0) {
                        IExecutionDegree executionDegree = (IExecutionDegree) arg0;
                        return InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear());
                    }

                });

        return executionYears;
    }
}