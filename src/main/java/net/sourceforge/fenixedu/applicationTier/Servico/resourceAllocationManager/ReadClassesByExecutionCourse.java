package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResourceAllocationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixframework.Atomic;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse {

    public List<InfoClass> run(ExecutionCourse executionCourse) {

        final Set<SchoolClass> classes = executionCourse.findSchoolClasses();
        final List<InfoClass> infoClasses = new ArrayList<InfoClass>(classes.size());

        for (final SchoolClass schoolClass : classes) {
            final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClasses.add(infoClass);
        }

        return infoClasses;
    }

    // Service Invokers migrated from Berserk

    private static final ReadClassesByExecutionCourse serviceInstance = new ReadClassesByExecutionCourse();

    @Atomic
    public static List<InfoClass> runReadClassesByExecutionCourse(ExecutionCourse executionCourse) throws NotAuthorizedException {
        try {
            ResourceAllocationManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionCourse);
        } catch (NotAuthorizedException ex1) {
            try {
                StudentAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionCourse);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}