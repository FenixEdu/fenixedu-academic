/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Filtro.person.ReadQualificationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Qualification;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteQualification {

    @Service
    public static void run(String qualificationId) {
        Qualification qualification = AbstractDomainObject.fromExternalId(qualificationId);
        qualification.delete();
    }

    // Service Invokers migrated from Berserk

    @Service
    public static void runDeleteQualification(String qualificationId) throws NotAuthorizedException {
        ReadQualificationAuthorizationFilter.instance.execute(qualificationId);
        run(qualificationId);
    }

}