/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNonAffiliatedTeacher extends Service {

    public NonAffiliatedTeacher run(String nonAffiliatedTeacherName, Integer institutionID) throws ExcepcaoPersistencia,
            NotExistingServiceException {
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
