/*
 * Created on 5/Fev/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.managementAssiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequests;
import net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness.InfoExtraWorkRequestsWithAll;
import net.sourceforge.fenixedu.domain.CostCenter;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadAndDeleteExtraWorkRequests implements IService {

	public List run(String usernameWho, List infoExtraWorkRequestsList) throws Exception {
		List infoExtraWorkRequestsListAfter = null;
		List extraWorkRequestsList = null;
		ISuportePersistente sp;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentEmployee employeeDAO = sp.getIPersistentEmployee();

		Iterator iterator = infoExtraWorkRequestsList.listIterator();
		extraWorkRequestsList = new ArrayList();
		IPersistentExtraWorkRequests extraWorkRequestsDAO = sp.getIPersistentExtraWorkRequests();
		IPersistentCostCenter costCenterDAO = sp.getIPersistentCostCenter();
		while (iterator.hasNext()) {
			InfoExtraWorkRequests infoExtraWorkRequests = (InfoExtraWorkRequests) iterator.next();
			if (infoExtraWorkRequests.getForDelete() != null
					&& infoExtraWorkRequests.getForDelete().equals(Boolean.TRUE)) {
				// delete
				extraWorkRequestsDAO.deleteByOID(ExtraWorkRequests.class, infoExtraWorkRequests
						.getIdInternal());
			} else {
				// read
				ExtraWorkRequests extraWorkRequests = (ExtraWorkRequests) extraWorkRequestsDAO
						.readByOID(ExtraWorkRequests.class, infoExtraWorkRequests.getIdInternal());
				if (extraWorkRequests == null) {
					// TODO
					continue;
				}

				Employee employee = (Employee) employeeDAO.readByOID(Employee.class, extraWorkRequests
						.getEmployeeKey());
				if (employee == null) {
					// TODO
					continue;
				}
				extraWorkRequests.setEmployee(employee);

				CostCenter costCenter = (CostCenter) costCenterDAO.readByOID(CostCenter.class,
						extraWorkRequests.getCostCenterExtraWorkKey());
				if (costCenter == null) {
					// TODO
					continue;
				}
				extraWorkRequests.setCostCenterExtraWork(costCenter);

				CostCenter costCenterMoney = (CostCenter) costCenterDAO.readByOID(CostCenter.class,
						extraWorkRequests.getCostCenterMoneyKey());
				if (costCenterMoney == null) {
					// TODO
					continue;
				}
				extraWorkRequests.setCostCenterMoney(costCenterMoney);

				extraWorkRequestsList.add(extraWorkRequests);
			}
		}

		infoExtraWorkRequestsListAfter = (List) CollectionUtils.collect(extraWorkRequestsList,
				new Transformer() {

					public Object transform(Object arg0) {
						ExtraWorkRequests extraWorkRequests = (ExtraWorkRequests) arg0;
						return InfoExtraWorkRequestsWithAll.newInfoFromDomain(extraWorkRequests);
					}

				});

		return infoExtraWorkRequestsListAfter;
	}
}
