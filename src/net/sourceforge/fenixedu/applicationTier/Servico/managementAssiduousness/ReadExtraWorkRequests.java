/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequestsWithAll;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;


/**
 * @author Tânia Pousão
 * 
 */
public class ReadExtraWorkRequests implements IService {
    public ReadExtraWorkRequests() {
    }

    public List run(String usernameWho, Date beginDate, Date endDate, String costCenterCode,
            String costCenterCodeMoney) throws Exception {
        List infoExtraWorkRequestsListAfter = null;
        List extraWorkRequestsList = null;
        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read employee logged
            IEmployee employeeWho = null;

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();

            IPerson personWho = personDAO.lerPessoaPorUsername(usernameWho);
            if (personWho != null) {
                employeeWho = employeeDAO.readByPerson(personWho.getIdInternal().intValue());
            }

            IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();
            ICostCenter costCenter = costCenterDAO.readCostCenterByCode(costCenterCode);
            if (costCenter == null) {
                // TODO
            }

            ICostCenter costCenterMoney = costCenterDAO.readCostCenterByCode(costCenterCodeMoney);
            if (costCenterMoney == null) {
                // TODO
            }
            IPersistentExtraWorkRequests extraWorkRequestsDAO = sp
                    .getIPersistentExtraWorkRequests();
            extraWorkRequestsList = extraWorkRequestsDAO
                    .readExtraWorkRequestBetweenDaysAndByCC(beginDate, endDate, costCenter
                            .getIdInternal(), costCenterMoney.getIdInternal());
            if (extraWorkRequestsList != null && extraWorkRequestsList.size() > 0) {
                infoExtraWorkRequestsListAfter = (List) CollectionUtils.collect(
                        extraWorkRequestsList, new Transformer() {
                            public Object transform(Object arg0) {
                                IExtraWorkRequests extraWorkRequests = (IExtraWorkRequests) arg0;
                                return InfoExtraWorkRequestsWithAll
                                        .newInfoFromDomain(extraWorkRequests);
                            }

                        });
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw e;
        }

        return infoExtraWorkRequestsListAfter;
    }
}
