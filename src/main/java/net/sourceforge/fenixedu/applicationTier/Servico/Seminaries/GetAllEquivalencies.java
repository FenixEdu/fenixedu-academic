package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalencyWithCurricularCourse;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class GetAllEquivalencies {

    public List<InfoEquivalency> run() {
        List<InfoEquivalency> result = new LinkedList<InfoEquivalency>();

        Collection<CourseEquivalency> courseEquivalencies = Bennu.getInstance().getCourseEquivalencysSet();
        for (CourseEquivalency courseEquivalency : courseEquivalencies) {
            result.add(InfoEquivalencyWithCurricularCourse.newInfoFromDomain(courseEquivalency));
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final GetAllEquivalencies serviceInstance = new GetAllEquivalencies();

    @Atomic
    public static List<InfoEquivalency> runGetAllEquivalencies() throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run();
    }

}