/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Filtro.person.ReadQualificationAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Qualification;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteQualification {

    @Atomic
    public static void run(String qualificationId) {
        Qualification qualification = FenixFramework.getDomainObject(qualificationId);
        qualification.delete();
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static void runDeleteQualification(String qualificationId) throws NotAuthorizedException {
        ReadQualificationAuthorizationFilter.instance.execute(qualificationId);
        run(qualificationId);
    }

}