package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCoursesByDegree {

    @Service
    public static List run(String executionYearString, String degreeName) throws FenixServiceException {
        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearString);

        // Read degree
        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanNameAndExecutionYear(degreeName, executionYear);

        if (executionDegree == null || executionDegree.getDegreeCurricularPlan() == null
                || executionDegree.getDegreeCurricularPlan().getCurricularCourses() == null
                || executionDegree.getDegreeCurricularPlan().getCurricularCourses().isEmpty()) {
            throw new NonExistingServiceException();
        }

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCourses()) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;

    }

    @Service
    public static List run(Integer degreeCurricularPlanID) {
        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;
    }

}