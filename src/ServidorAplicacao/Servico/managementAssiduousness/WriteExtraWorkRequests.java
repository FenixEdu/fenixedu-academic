/*
 * Created on 29/Jan/2005
 */
package ServidorAplicacao.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.managementAssiduousness.InfoExtraWorkRequests;
import DataBeans.managementAssiduousness.InfoExtraWorkRequestsWithAll;
import Dominio.ICostCenter;
import Dominio.IEmployee;
import Dominio.IPerson;
import Dominio.managementAssiduousness.ExtraWorkRequests;
import Dominio.managementAssiduousness.IExtraWorkRequests;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.managementAssiduousness.IPersistentCostCenter;
import ServidorPersistente.managementAssiduousness.IPersistentExtraWorkRequests;

/**
 * @author Tânia Pousão
 * 
 */
public class WriteExtraWorkRequests implements IService {
    public WriteExtraWorkRequests() {
    }

    public List run(String usernameWho, List infoExtraWorkRequestsList,
            String costCenterCode, String costCenterMoneyCode) throws Exception {
        List infoExtraWorkRequestsListAfterWrite = null;
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
                employeeWho = employeeDAO.readByPerson(personWho
                        .getIdInternal().intValue());
            }

            IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();
            ICostCenter costCenter = costCenterDAO
                    .readCostCenterByCode(costCenterCode);
            if (costCenter == null) {
                // TODO
            }

            ICostCenter costCenterMoney = costCenterDAO
                    .readCostCenterByCode(costCenterMoneyCode);
            if (costCenterMoney == null) {
                // TODO
            }

            Iterator iterator = infoExtraWorkRequestsList.listIterator();
            extraWorkRequestsList = new ArrayList();
            IPersistentExtraWorkRequests extraWorkRequestsDAO = sp
                    .getIPersistentExtraWorkRequests();
            while (iterator.hasNext()) {
                InfoExtraWorkRequests infoExtraWorkRequests = (InfoExtraWorkRequests) iterator
                        .next();

                IExtraWorkRequests extraWorkRequests = null;
                if (infoExtraWorkRequests.getIdInternal() != null
                        && infoExtraWorkRequests.getIdInternal().intValue() > 0) {
                    extraWorkRequests = (IExtraWorkRequests) extraWorkRequestsDAO
                            .readByOID(ExtraWorkRequests.class,
                                    infoExtraWorkRequests.getIdInternal());
                }
                if (extraWorkRequests == null) {
                    extraWorkRequests = new ExtraWorkRequests();
                } 
                extraWorkRequestsDAO.simpleLockWrite(extraWorkRequests);

                extraWorkRequests.setBeginDate(infoExtraWorkRequests.getBeginDate());
                extraWorkRequests.setEndDate(infoExtraWorkRequests.getEndDate());
                extraWorkRequests.setOption1(infoExtraWorkRequests.getOption1());
                extraWorkRequests.setOption2(infoExtraWorkRequests.getOption2());
                extraWorkRequests.setOption3(infoExtraWorkRequests.getOption3());
                extraWorkRequests.setOption4(infoExtraWorkRequests.getOption4());
                extraWorkRequests.setOption5(infoExtraWorkRequests.getOption5());
                extraWorkRequests.setOption6(infoExtraWorkRequests.getOption6());
                extraWorkRequests.setOption7(infoExtraWorkRequests.getOption7());
                extraWorkRequests.setOption8(infoExtraWorkRequests.getOption8());
                extraWorkRequests.setOption9(infoExtraWorkRequests.getOption9());
                extraWorkRequests.setOption10(infoExtraWorkRequests.getOption10());
                extraWorkRequests.setOption11(infoExtraWorkRequests.getOption11());
                extraWorkRequests.setOption12(infoExtraWorkRequests.getOption12());
                
                extraWorkRequests.setCostCenterExtraWork(costCenter);
                extraWorkRequests.setCostCenterMoney(costCenterMoney);

                IEmployee employee = employeeDAO
                        .readByNumber(infoExtraWorkRequests.getInfoEmployee()
                                .getEmployeeNumber());
                if (employee == null) {
                    // TODO
                }
                extraWorkRequests.setEmployee(employee);
                
                extraWorkRequests.setWho(employeeWho.getIdInternal().intValue());
                extraWorkRequests.setWhoEmployee(employeeWho);
                extraWorkRequests.setWhen(Calendar.getInstance().getTime());
                
                extraWorkRequestsList.add(extraWorkRequests);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw e;
        }

        infoExtraWorkRequestsListAfterWrite = (List) CollectionUtils.collect(
                extraWorkRequestsList, new Transformer() {

                    public Object transform(Object arg0) {
                        ExtraWorkRequests extraWorkRequests = (ExtraWorkRequests) arg0;
                        return InfoExtraWorkRequestsWithAll
                                .newInfoFromDomain(extraWorkRequests);
                    }

                });

        return infoExtraWorkRequestsListAfterWrite;
    }
}
