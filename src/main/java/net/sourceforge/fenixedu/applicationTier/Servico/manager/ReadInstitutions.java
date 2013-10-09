/*
 * Created on May 2, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadInstitutions {

    @Atomic
    public static List run() {
        check(RolePredicates.MANAGER_PREDICATE);

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