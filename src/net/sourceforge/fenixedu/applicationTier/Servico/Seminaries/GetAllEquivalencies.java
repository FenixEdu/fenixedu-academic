package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalencyWithCurricularCourse;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class GetAllEquivalencies extends Service {

	public List<InfoEquivalency> run() { 
		List<InfoEquivalency> result = new LinkedList<InfoEquivalency>();

		List<CourseEquivalency> courseEquivalencies = rootDomainObject.getCourseEquivalencys();
		for (CourseEquivalency courseEquivalency : courseEquivalencies) {
            result.add(InfoEquivalencyWithCurricularCourse.newInfoFromDomain(courseEquivalency));
		}

		return result;
	}
    
}
