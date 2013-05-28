/*
 * Created on 28/Jul/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author asnr and scpo
 * 
 */

public class CreateGrouping {

    protected Boolean run(Integer executionCourseID, InfoGrouping infoGrouping) throws FenixServiceException {

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Grouping grouping =
                Grouping.create(infoGrouping.getName(), infoGrouping.getEnrolmentBeginDay().getTime(), infoGrouping
                        .getEnrolmentEndDay().getTime(), infoGrouping.getEnrolmentPolicy(), infoGrouping.getGroupMaximumNumber(),
                        infoGrouping.getIdealCapacity(), infoGrouping.getMaximumCapacity(), infoGrouping.getMinimumCapacity(),
                        infoGrouping.getProjectDescription(), infoGrouping.getShiftType(), infoGrouping.getAutomaticEnrolment(),
                        infoGrouping.getDifferentiatedCapacity(), executionCourse);

        if (infoGrouping.getDifferentiatedCapacity()) {
            grouping.createOrEditShiftGroupingProperties(infoGrouping.getInfoShifts());
        }
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final CreateGrouping serviceInstance = new CreateGrouping();

    @Service
    public static Boolean runCreateGrouping(Integer executionCourseID, InfoGrouping infoGrouping) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, infoGrouping);
    }

}