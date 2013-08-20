/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.gep.GEPAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNonAffiliatedTeacher {

    @Checked("RolePredicates.GEP_PREDICATE")
    @Service
    public static NonAffiliatedTeacher run(String nonAffiliatedTeacherName, String institutionID)
            throws NotExistingServiceException {
        final Unit institution = (Unit) AbstractDomainObject.fromExternalId(institutionID);
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

    // Service Invokers migrated from Berserk

    @Service
    public static NonAffiliatedTeacher runInsertNonAffiliatedTeacher(String nonAffiliatedTeacherName, String institutionID)
            throws NotExistingServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return run(nonAffiliatedTeacherName, institutionID);
        } catch (NotAuthorizedException ex1) {
            try {
                GEPAuthorizationFilter.instance.execute();
                return run(nonAffiliatedTeacherName, institutionID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}