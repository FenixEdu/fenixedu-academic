/*
 * Created on May 2, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadInstitutions extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static List run() {

		List<Unit> institutions = UnitUtils.readAllExternalInstitutionUnits();

		List<InfoInstitution> infoInstitutions = new ArrayList<InfoInstitution>();
		for (Unit institution : institutions) {
			InfoInstitution infoInstitution = new InfoInstitution();
			infoInstitution.copyFromDomain(institution);
			infoInstitutions.add(infoInstitution);
		}

		return infoInstitutions;
	}
}