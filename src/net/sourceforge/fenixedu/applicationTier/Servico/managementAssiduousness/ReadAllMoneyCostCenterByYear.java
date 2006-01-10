package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoMoneyCostCenter;
import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.managementAssiduousness.MoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllMoneyCostCenterByYear implements IService {

    public List run(Integer year, String usernameWho) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<InfoMoneyCostCenter> infoMoneyCostCenterList = new ArrayList<InfoMoneyCostCenter>();

        // Read employee logged
        Employee employeeWho = null;

        IPessoaPersistente personDAO = sp.getIPessoaPersistente();
        Person personWho = personDAO.lerPessoaPorUsername(usernameWho);
        if (personWho != null) {
            IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
            employeeWho = employeeDAO.readByPerson(personWho.getIdInternal().intValue());
        }

        // Read all cost center and for each verify money at this year
        IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();
        List<CostCenter> costCenterList = (List<CostCenter>) costCenterDAO.readAll(CostCenter.class);
        if (costCenterList != null && costCenterList.size() > 0) {
            IPersistentMoneyCostCenter moneyCostCenterDAO = sp.getIPersistentMoneyCostCenter();

            for (CostCenter costCenter : costCenterList) {
                MoneyCostCenter moneyCostCenter = moneyCostCenterDAO.readByCostCenterAndYear(
                        costCenter, year);
                if (moneyCostCenter == null) {
                    moneyCostCenter = new MoneyCostCenter();
                }
                moneyCostCenter.setCostCenter(costCenter);
                moneyCostCenter.setYear(year);
                Calendar now = Calendar.getInstance();
                moneyCostCenter.setWhen(now.getTime());
                moneyCostCenter.setWhoEmployee(employeeWho);

                InfoMoneyCostCenter infoMoneyCostCenter = InfoMoneyCostCenter
                        .newInfoFromDomain(moneyCostCenter);
                if (infoMoneyCostCenter != null) {
                    InfoCostCenter infoCostCenter = InfoCostCenter.newInfoFromDomain(costCenter);
                    infoMoneyCostCenter.setInfoCostCenter(infoCostCenter);
                }

                infoMoneyCostCenterList.add(infoMoneyCostCenter);
            }
        }

        if (infoMoneyCostCenterList == null || infoMoneyCostCenterList.size() <= 0) {
            throw new FenixServiceException();
        }

        return infoMoneyCostCenterList;
    }

}
