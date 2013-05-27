/*
 * Created on 11/Ago/2005 - 16:12:00
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.teacher.EditExternalActivityTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class EditExternalActivity {

    protected void run(Integer externalActivityId, InfoExternalActivity infoExternalActivity) throws FenixServiceException {
        ExternalActivity externalActivity = RootDomainObject.getInstance().readExternalActivityByOID(externalActivityId);
        // If it doesn't exist in the database, a new one has to be created
        if (externalActivity == null) {
            Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(infoExternalActivity.getInfoTeacher().getIdInternal());
            externalActivity = new ExternalActivity(teacher, infoExternalActivity);

        } else {
            externalActivity.edit(infoExternalActivity);
        }
    }
    // Service Invokers migrated from Berserk

    private static final EditExternalActivity serviceInstance = new EditExternalActivity();

    @Service
    public static void runEditExternalActivity(Integer externalActivityId, InfoExternalActivity infoExternalActivity) throws FenixServiceException  , NotAuthorizedException {
        EditExternalActivityTeacherAuthorizationFilter.instance.execute(externalActivityId, infoExternalActivity);
        serviceInstance.run(externalActivityId, infoExternalActivity);
    }

}