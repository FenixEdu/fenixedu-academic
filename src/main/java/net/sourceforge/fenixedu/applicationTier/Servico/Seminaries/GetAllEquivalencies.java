package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalencyWithCurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import pt.ist.fenixWebFramework.services.Service;

public class GetAllEquivalencies {

    public List<InfoEquivalency> run() {
        List<InfoEquivalency> result = new LinkedList<InfoEquivalency>();

        List<CourseEquivalency> courseEquivalencies = RootDomainObject.getInstance().getCourseEquivalencys();
        for (CourseEquivalency courseEquivalency : courseEquivalencies) {
            result.add(InfoEquivalencyWithCurricularCourse.newInfoFromDomain(courseEquivalency));
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final GetAllEquivalencies serviceInstance = new GetAllEquivalencies();

    @Service
    public static List<InfoEquivalency> runGetAllEquivalencies() throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run();
    }

}