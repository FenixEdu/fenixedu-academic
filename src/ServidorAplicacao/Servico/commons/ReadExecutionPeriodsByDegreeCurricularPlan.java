/*
 * Created on 9/Fev/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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
                start = ((DegreeCurricularPlan) (degreeCurricularPlanDAO.readByOID(
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