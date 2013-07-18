package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Fernanda Quitério
 */
public class CreateBibliographicReference {

    protected Boolean run(Integer infoExecutionCourseID, String newBibliographyTitle, String newBibliographyAuthors,
            String newBibliographyReference, String newBibliographyYear, Boolean newBibliographyOptional)
            throws FenixServiceException {

        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourseID);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        executionCourse.createBibliographicReference(newBibliographyTitle, newBibliographyAuthors, newBibliographyReference,
                newBibliographyYear, newBibliographyOptional);
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final CreateBibliographicReference serviceInstance = new CreateBibliographicReference();

    @Service
    public static Boolean runCreateBibliographicReference(Integer infoExecutionCourseID, String newBibliographyTitle,
            String newBibliographyAuthors, String newBibliographyReference, String newBibliographyYear,
            Boolean newBibliographyOptional) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(infoExecutionCourseID);
        return serviceInstance.run(infoExecutionCourseID, newBibliographyTitle, newBibliographyAuthors, newBibliographyReference,
                newBibliographyYear, newBibliographyOptional);
    }

}