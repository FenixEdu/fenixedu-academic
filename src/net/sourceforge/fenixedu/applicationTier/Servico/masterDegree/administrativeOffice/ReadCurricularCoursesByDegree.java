package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeAndScopes;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCoursesByDegree extends Service {

    public List run(String executionYearString, String degreeName) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                executionYearString);

        // Read degree
        ExecutionDegree executionDegree = sp.getIPersistentExecutionDegree()
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
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) sp
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            infoCurricularCourses.add(InfoCurricularCourseWithInfoDegreeAndScopes
                    .newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;
    }

}