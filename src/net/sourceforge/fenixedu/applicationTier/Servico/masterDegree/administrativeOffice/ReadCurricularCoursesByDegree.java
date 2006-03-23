package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeAndScopes;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCoursesByDegree extends Service {

    public List run(String executionYearString, String degreeName) throws FenixServiceException,
            ExcepcaoPersistencia {
        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearString);

        // Read degree
        ExecutionDegree executionDegree = persistentSupport.getIPersistentExecutionDegree()
                .readByDegreeCurricularPlanNameAndExecutionYear(degreeName,
                        executionYear.getIdInternal());

        if (executionDegree == null || executionDegree.getDegreeCurricularPlan() == null
                || executionDegree.getDegreeCurricularPlan().getCurricularCourses() == null
                || executionDegree.getDegreeCurricularPlan().getCurricularCourses().isEmpty()) {
            throw new NonExistingServiceException();
        }

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan()
                .getCurricularCourses()) {
            infoCurricularCourses.add(InfoCurricularCourseWithInfoDegreeAndScopes
                    .newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;

    }

    public List run(Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            infoCurricularCourses.add(InfoCurricularCourseWithInfoDegreeAndScopes
                    .newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;
    }

}