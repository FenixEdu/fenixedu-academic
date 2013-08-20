/*
 * Created on 2/Abr/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Tânia Pousão
 * 
 */
public class DeleteGrouping {

    protected Boolean run(String executionCourseId, String groupPropertiesId) throws FenixServiceException {

        if (groupPropertiesId == null) {
            return Boolean.FALSE;
        }

        Grouping groupProperties = AbstractDomainObject.fromExternalId(groupPropertiesId);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        groupProperties.delete();

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteGrouping serviceInstance = new DeleteGrouping();

    @Service
    public static Boolean runDeleteGrouping(String executionCourseId, String groupPropertiesId) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, groupPropertiesId);
    }
}