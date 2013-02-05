/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNonAffiliatedTeacher extends FenixService {

    @Checked("RolePredicates.GEP_PREDICATE")
    @Service
    public static NonAffiliatedTeacher run(String nonAffiliatedTeacherName, Integer institutionID)
            throws NotExistingServiceException {
        final Unit institution = (Unit) rootDomainObject.readPartyByOID(institutionID);
        if (institution == null) {
            throw new NotExistingServiceException("no.institution");
        }

        if (institution.findNonAffiliatedTeacherByName(nonAffiliatedTeacherName) != null) {
            throw new NotExistingServiceException("teacher.already.exists");
        }

        final NonAffiliatedTeacher nonAffiliatedTeacher = new NonAffiliatedTeacher();
        nonAffiliatedTeacher.setName(nonAffiliatedTeacherName);
        nonAffiliatedTeacher.setInstitutionUnit(institution);

        return nonAffiliatedTeacher;
    }

}
