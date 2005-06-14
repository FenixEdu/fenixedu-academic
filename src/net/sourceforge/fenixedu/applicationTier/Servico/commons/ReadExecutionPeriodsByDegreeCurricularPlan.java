/*
 * Created on 9/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadExecutionPeriodsByDegreeCurricularPlan implements IService {

    public List run(Integer degreeCurricularPlanID) throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentDegreeCurricularPlan degreeCurricularPlanDAO = sp
                    .getIPersistentDegreeCurricularPlan();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
            Date end = null;
            Date start = null;

            //End date of the current year
            try {
                end = executionYearDAO.readCurrentExecutionYear().getEndDate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Start date of the degree curricular plan
            try {
                start = ((IDegreeCurricularPlan) (degreeCurricularPlanDAO.readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanID))).getInitialDate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            List executionPeriods = executionPeriodDAO.readExecutionPeriodsInTimePeriod(start, end);
            result = (List) CollectionUtils.collect(executionPeriods, new Transformer() {

                public Object transform(Object input) {
                    IExecutionPeriod executionPeriod = (IExecutionPeriod) input;
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                            .get(executionPeriod);
                    return infoExecutionPeriod;
                }
            });
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return result;
    }

}