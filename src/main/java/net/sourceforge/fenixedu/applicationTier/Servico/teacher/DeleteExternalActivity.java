/*
 * Created on 11/Ago/2005 - 19:12:31
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.ExternalActivityTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class DeleteExternalActivity {

    protected void run(Integer externalActivityId) {
        ExternalActivity externalActivity = RootDomainObject.getInstance().readExternalActivityByOID(externalActivityId);
        externalActivity.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteExternalActivity serviceInstance = new DeleteExternalActivity();

    @Service
    public static void runDeleteExternalActivity(Integer externalActivityId) throws NotAuthorizedException {
        ExternalActivityTeacherAuthorizationFilter.instance.execute(externalActivityId);
        serviceInstance.run(externalActivityId);
    }

}