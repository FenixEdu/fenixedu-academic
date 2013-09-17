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
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNonAffiliatedTeacher {

    @Atomic
    public static NonAffiliatedTeacher run(String nonAffiliatedTeacherName, String institutionID)
            throws NotExistingServiceException {
        check(RolePredicates.GEP_PREDICATE);
        final Unit institution = (Unit) FenixFramework.getDomainObject(institutionID);
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

    @Atomic
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