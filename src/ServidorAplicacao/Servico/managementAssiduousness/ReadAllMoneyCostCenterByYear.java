/*
 * Created on 15/Dez/2004
 *  
 */
package ServidorAplicacao.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCostCenter;
import DataBeans.managementAssiduousness.InfoMoneyCostCenter;
import Dominio.ICostCenter;
import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.managementAssiduousness.IMoneyCostCenter;
import Dominio.managementAssiduousness.MoneyCostCenter;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.managementAssiduousness.IPersistentCostCenter;
import ServidorPersistente.managementAssiduousness.IPersistentMoneyCostCenter;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadAllMoneyCostCenterByYear implements IService {
    public ReadAllMoneyCostCenterByYear() {

    }

    public List run(Integer year, String usernameWho)
            throws FenixServiceException {
        ISuportePersistente sp;
        List infoMoneyCostCenterList = new ArrayList();
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read employee logged
            IEmployee employeeWho = null;

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPessoa personWho = personDAO.lerPessoaPorUsername(usernameWho);
            if (personWho != null) {
                IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
                employeeWho = employeeDAO.readByPerson(personWho
                        .getIdInternal().intValue());
            }

            // Read all cost center and for each verify money at this year
            IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();
            List costCenterList = costCenterDAO.readAll();
            if (costCenterList != null && costCenterList.size() > 0) {
                IPersistentMoneyCostCenter moneyCostCenterDAO = sp
                        .getIPersistentMoneyCostCenter();

                ListIterator iterator = costCenterList.listIterator();
                while (iterator.hasNext()) {
                    ICostCenter costCenter = (ICostCenter) iterator.next();

                    IMoneyCostCenter moneyCostCenter = moneyCostCenterDAO
                            .readByCostCenterAndYear(costCenter, year);
                    if (moneyCostCenter == null) {
                        moneyCostCenter = new MoneyCostCenter();
                    }
                    moneyCostCenterDAO.simpleLockWrite(moneyCostCenter);
                    moneyCostCenter.setCostCenter(costCenter);
                    moneyCostCenter.setYear(year);
                    Calendar now = Calendar.getInstance();
                    moneyCostCenter.setWhen(now.getTime());
                    moneyCostCenter.setWhoEmployee(employeeWho);

                    InfoMoneyCostCenter infoMoneyCostCenter = InfoMoneyCostCenter
                            .newInfoFromDomain(moneyCostCenter);
                    if(infoMoneyCostCenter != null) {
	                    InfoCostCenter infoCostCenter = InfoCostCenter
	                            .newInfoFromDomain(costCenter);
	                    infoMoneyCostCenter.setInfoCostCenter(infoCostCenter);
                    }
                    
                    infoMoneyCostCenterList.add(infoMoneyCostCenter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException(e);
        }
        if (infoMoneyCostCenterList == null || infoMoneyCostCenterList.size() <= 0) {
            throw new FenixServiceException();
        }

        return infoMoneyCostCenterList;
    }
}