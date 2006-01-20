package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadCurricularCourseScopes extends Service {

    public List<InfoCurricularCourseScope> run(Integer curricularCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(
                CurricularCourse.class, curricularCourseId);

        List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
        if (curricularCourseScopes == null || curricularCourseScopes.isEmpty()) {
            return new ArrayList<InfoCurricularCourseScope>();
        }

        List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>(
                curricularCourseScopes.size());
        for (CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
            result.add(InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                    .newInfoFromDomain(curricularCourseScope));
        }

        return result;
    }

}
