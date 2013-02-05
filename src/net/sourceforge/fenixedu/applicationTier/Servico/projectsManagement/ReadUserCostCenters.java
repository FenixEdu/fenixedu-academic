/*
 * Created on Jan 11, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;

/**
 * @author Susana Fernandes
 */
public class ReadUserCostCenters extends FenixService {

    public List run(Person person, String costCenter, BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
        List<InfoRubric> infoCostCenterList = new ArrayList<InfoRubric>();

        PersistentProjectUser persistentProjectUser = new PersistentProjectUser();
        List<IRubric> costCenterList = persistentProjectUser.getInstitucionalProjectCoordId(new Integer(userNumber), instance);

        List<Integer> projectCodes = new ArrayList<Integer>();
        List<ProjectAccess> accesses = ProjectAccess.getAllByPerson(person, instance);
        for (ProjectAccess access : accesses) {
            Integer keyCostCenter = access.getKeyProjectCoordinator();

            if (!projectCodes.contains(keyCostCenter)) {
                projectCodes.add(keyCostCenter);
            }
        }

        costCenterList.addAll(persistentProjectUser.getInstitucionalProjectByCCIDs(projectCodes, instance));

        for (IRubric cc : costCenterList) {
            infoCostCenterList.add(InfoRubric.newInfoFromDomain(cc));
        }

        return infoCostCenterList;
    }
}