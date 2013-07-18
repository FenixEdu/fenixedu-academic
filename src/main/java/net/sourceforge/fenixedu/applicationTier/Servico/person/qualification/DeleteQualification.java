/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;


import net.sourceforge.fenixedu.applicationTier.Filtro.person.ReadQualificationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteQualification {

    @Service
    public static void run(Integer qualificationId) {
        Qualification qualification = RootDomainObject.getInstance().readQualificationByOID(qualificationId);
        qualification.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteQualification serviceInstance = new DeleteQualification();

    @Service
    public static void runDeleteQualification(Integer qualificationId) throws NotAuthorizedException {
        ReadQualificationAuthorizationFilter.instance.execute(qualificationId);
        serviceInstance.run(qualificationId);
    }

}