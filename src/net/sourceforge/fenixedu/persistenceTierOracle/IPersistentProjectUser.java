/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentProjectUser {
    public IRubric readProjectCoordinator(Integer userCode) throws ExcepcaoPersistencia;

    public List<IRubric> getInstitucionalProjectCoordId(Integer userCode) throws ExcepcaoPersistencia;

    public List<IRubric> getInstitucionalProjectByCCIDs(List<Integer> ccCodes) throws ExcepcaoPersistencia;

    public String getCCNameByCoordinatorAndCC(Integer userNumber, Integer costCenter) throws ExcepcaoPersistencia;

    public IRubric getCostCenterByID(Integer costCenter) throws ExcepcaoPersistencia;
}