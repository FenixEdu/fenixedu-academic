/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequests;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequestsWithAll;
import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadAndDeleteExtraWorkRequests implements IService {
    public ReadAndDeleteExtraWorkRequests() {
    }

    public List run(String usernameWho, List infoExtraWorkRequestsList)
            throws Exception {
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
                employeeWho = employeeDAO.readByPerson(personWho
                        .getIdInternal().intValue());
            }

            Iterator iterator = infoExtraWorkRequestsList.listIterator();
            extraWorkRequestsList = new ArrayList();
            IPersistentExtraWorkRequests extraWorkRequestsDAO = sp
                    .getIPersistentExtraWorkRequests();
            IPersistentCostCenter costCenterDAO = sp
                    .getIPersistentCostCenter();
            while (iterator.hasNext()) {
                InfoExtraWorkRequests infoExtraWorkRequests = (InfoExtraWorkRequests) iterator
                        .next();
                if (infoExtraWorkRequests.getForDelete() != null
                        && infoExtraWorkRequests.getForDelete().equals(
                                Boolean.TRUE)) {
                    // delete
                    extraWorkRequestsDAO.deleteByOID(ExtraWorkRequests.class,
                            infoExtraWorkRequests.getIdInternal());
                } else {
                    // read
                    IExtraWorkRequests extraWorkRequests = (IExtraWorkRequests) extraWorkRequestsDAO
                            .readByOID(ExtraWorkRequests.class,
                                    infoExtraWorkRequests.getIdInternal());
                    if(extraWorkRequests == null) {
                        //TODO
                        continue;
                    }
                    
                    IEmployee employee = (IEmployee) employeeDAO.readByOID(
                            Employee.class, extraWorkRequests.getEmployeeKey());
                    if (employee == null) {
                        // TODO
                        continue;
                    }
                    extraWorkRequests.setEmployee(employee);

                    ICostCenter costCenter = (ICostCenter) costCenterDAO
                            .readByOID(CostCenter.class, extraWorkRequests.getCostCenterExtraWorkKey());
                    if (costCenter == null) {
                        //TODO
                        continue;
                    }
                    extraWorkRequests.setCostCenterExtraWork(costCenter);

                    ICostCenter costCenterMoney = (ICostCenter) costCenterDAO.readByOID(CostCenter.class, extraWorkRequests.getCostCenterMoneyKey());
                    if (costCenterMoney == null) {
                        //TODO
                        continue;
                    }
                    extraWorkRequests.setCostCenterMoney(costCenterMoney);
                    
                    extraWorkRequestsList.add(extraWorkRequests);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw e;
        }

        infoExtraWorkRequestsListAfter = (List) CollectionUtils.collect(
                extraWorkRequestsList, new Transformer() {

                    public Object transform(Object arg0) {
                        IExtraWorkRequests extraWorkRequests = (IExtraWorkRequests) arg0;
                        return InfoExtraWorkRequestsWithAll
                                .newInfoFromDomain(extraWorkRequests);
                    }

                });

        return infoExtraWorkRequestsListAfter;
    }
}
