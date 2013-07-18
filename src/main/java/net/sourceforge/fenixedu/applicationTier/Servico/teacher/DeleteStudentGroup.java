/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author asnr and scpo
 * 
 */
public class DeleteStudentGroup {

    protected Boolean run(Integer executionCourseCode, Integer studentGroupCode) throws FenixServiceException {
        StudentGroup deletedStudentGroup = RootDomainObject.getInstance().readStudentGroupByOID(studentGroupCode);

        if (deletedStudentGroup == null) {
            throw new ExistingServiceException();
        }

        deletedStudentGroup.delete();

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteStudentGroup serviceInstance = new DeleteStudentGroup();

    @Service
    public static Boolean runDeleteStudentGroup(Integer executionCourseCode, Integer studentGroupCode)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode);
    }
}