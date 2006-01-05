package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequests;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequestsWithAll;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class WriteExtraWorkRequests implements IService {

    public List run(String usernameWho, List<InfoExtraWorkRequests> infoExtraWorkRequestsList,
            String costCenterCode, String costCenterMoneyCode) throws Exception {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IPessoaPersistente personDAO = sp.getIPessoaPersistente();
        final IPerson personWho = personDAO.lerPessoaPorUsername(usernameWho);

        // Read employee logged
        IEmployee employeeWho = null;
        IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
        if (personWho != null) {
            employeeWho = employeeDAO.readByPerson(personWho.getIdInternal().intValue());
        }

        IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();
        ICostCenter costCenter = costCenterDAO.readCostCenterByCode(costCenterCode);
        if (costCenter == null) {
            // TODO
        }

        ICostCenter costCenterMoney = costCenterDAO.readCostCenterByCode(costCenterMoneyCode);
        if (costCenterMoney == null) {
            // TODO
        }

        List<IExtraWorkRequests> extraWorkRequestsList = new ArrayList<IExtraWorkRequests>();
        for (InfoExtraWorkRequests infoExtraWorkRequests : infoExtraWorkRequestsList) {
            IExtraWorkRequests extraWorkRequests = null;
            if (infoExtraWorkRequests.getIdInternal() != null
                    && infoExtraWorkRequests.getIdInternal().intValue() > 0) {
                extraWorkRequests = (IExtraWorkRequests) sp.getIPersistentObject().readByOID(
                        ExtraWorkRequests.class, infoExtraWorkRequests.getIdInternal());
            }
            if (extraWorkRequests == null) {
                extraWorkRequests = new ExtraWorkRequests();
            }

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

            IEmployee employee = employeeDAO.readByNumber(infoExtraWorkRequests.getInfoEmployee()
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

        return (List) CollectionUtils.collect(extraWorkRequestsList, new Transformer() {
            public Object transform(Object arg0) {
                IExtraWorkRequests extraWorkRequests = (IExtraWorkRequests) arg0;
                return InfoExtraWorkRequestsWithAll.newInfoFromDomain(extraWorkRequests);
            }
        });

    }

}
