/*
 * Created on 20/Dez/2004
 */
package ServidorAplicacao.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCostCenter;
import DataBeans.managementAssiduousness.InfoMoneyCostCenter;
import Dominio.IEmployee;
import Dominio.IPerson;
import Dominio.managementAssiduousness.IMoneyCostCenter;
import Dominio.managementAssiduousness.MoneyCostCenter;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.managementAssiduousness.IPersistentMoneyCostCenter;

/**
 * @author Tânia Pousão
 * 
 */
public class UpdateAllMoneyCostCenterByYear implements IService {

    public UpdateAllMoneyCostCenterByYear() {
    }

    public List run(String usernameWho, List infoMoneyCostCenterList)
            throws FenixServiceException {
        ISuportePersistente sp;
        List infoMoneyCostCenterUpdatedList = new ArrayList();
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read employee logged
            IEmployee employeeWho = null;

            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPerson personWho = personDAO.lerPessoaPorUsername(usernameWho);
            if (personWho != null) {
                IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();
                employeeWho = employeeDAO.readByPerson(personWho
                        .getIdInternal().intValue());
            }

            if (infoMoneyCostCenterList != null
                    && infoMoneyCostCenterList.size() > 0) {

                IPersistentMoneyCostCenter moneyCostCenterDAO = sp
                .getIPersistentMoneyCostCenter();
                ListIterator iterator = infoMoneyCostCenterList.listIterator();
                while (iterator.hasNext()) {
                    InfoMoneyCostCenter infoMoneyCostCenter = (InfoMoneyCostCenter) iterator.next();

                    IMoneyCostCenter moneyCostCenter = (IMoneyCostCenter) moneyCostCenterDAO.readByOID(MoneyCostCenter.class, infoMoneyCostCenter.getIdInternal(), true);
                    Calendar now = Calendar.getInstance();
                    moneyCostCenter.setWhen(now.getTime());
                    moneyCostCenter.setWhoEmployee(employeeWho);

                    moneyCostCenter.setInitialValue(infoMoneyCostCenter.getInitialValue());
                    moneyCostCenter.setTotalValue(infoMoneyCostCenter.getTotalValue());  
                    
                    InfoMoneyCostCenter infoMoneyCostCenterUpdated = InfoMoneyCostCenter
                            .newInfoFromDomain(moneyCostCenter);
                    if(infoMoneyCostCenterUpdated != null) {
	                    InfoCostCenter infoCostCenter = InfoCostCenter
	                            .newInfoFromDomain(moneyCostCenter.getCostCenter());
	                    infoMoneyCostCenterUpdated.setInfoCostCenter(infoCostCenter);
                    }
                    
                    infoMoneyCostCenterUpdatedList.add(infoMoneyCostCenterUpdated);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new FenixServiceException(e);
        }
        if (infoMoneyCostCenterUpdatedList == null
                || infoMoneyCostCenterUpdatedList.size() <= 0) {
            throw new FenixServiceException();
        }

        return infoMoneyCostCenterUpdatedList;
    }
}
