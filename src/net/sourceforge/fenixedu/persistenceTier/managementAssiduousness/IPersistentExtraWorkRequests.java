/*
 * Created on 29/Jan/2005
 */
package net.sourceforge.fenixedu.persistenceTier.managementAssiduousness;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentExtraWorkRequests extends IPersistentObject {
    public IExtraWorkRequests readExtraWorkRequestByDay(Date day, Integer employeeID) throws Exception;
    public List readExtraWorkRequestBetweenDays(Date beginDay, Date lastDay) throws Exception;
    public List readExtraWorkRequestBetweenDaysAndByCC(Date beginDay, Date lastDay, Integer costCenterId, Integer costCenterMoneyId) throws Exception;
}
