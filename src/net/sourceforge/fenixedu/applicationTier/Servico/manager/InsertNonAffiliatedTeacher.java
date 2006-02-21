/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InsertNonAffiliatedTeacher extends Service {

    public NonAffiliatedTeacher run(String nonAffiliatedTeacherName, Integer institutionID) throws ExcepcaoPersistencia,
            NotExistingServiceException {
               
        IPersistentNonAffiliatedTeacher persistentNonAffiliatedTeacher = persistentSupport
                .getIPersistentNonAffiliatedTeacher();

        Unit institution = (Unit) persistentObject.readByOID(Unit.class,
                institutionID);

        if (institution == null) {
            throw new NotExistingServiceException("Non Existing Institution");
        }

        NonAffiliatedTeacher nonAffiliatedTeacher = persistentNonAffiliatedTeacher
                .readByNameAndInstitution(nonAffiliatedTeacherName, institution);

        if (nonAffiliatedTeacher != null) {
            throw new NotExistingServiceException("teacher Already Exists");
        }

        nonAffiliatedTeacher = DomainFactory.makeNonAffiliatedTeacher();
        nonAffiliatedTeacher.setName(nonAffiliatedTeacherName);
        nonAffiliatedTeacher.setInstitutionUnit(institution);
        
        return nonAffiliatedTeacher;
    }

}
