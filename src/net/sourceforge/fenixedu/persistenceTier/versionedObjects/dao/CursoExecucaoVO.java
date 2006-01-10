/*
 * CursoExecucaoOJB.java
 * 
 * Created on 2 de Novembro de 2002, 21:17
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author rpfi
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PeriodState;

public class CursoExecucaoVO extends VersionedObjectsBase implements IPersistentExecutionDegree {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(ExecutionDegree.class);
    }

    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia {
        List<ExecutionYear> executionYears = (List<ExecutionYear>) readAll(ExecutionYear.class);

        for (ExecutionYear executionYearElement : executionYears) {
            if (executionYearElement.getYear().equals(executionYear)) {
                return executionYearElement.getExecutionDegrees();
            }
        }
        return new ArrayList();
    }

    public ExecutionDegree readByDegreeCurricularPlanAndExecutionYear(String degreeCurricularPlanName,
            String degreeCurricularPlanAcronym, String year) throws ExcepcaoPersistencia {

        List<ExecutionDegree> executionDegrees = (List<ExecutionDegree>) readAll(ExecutionDegree.class);
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getName().equals(degreeCurricularPlanName)
                    && executionDegree.getDegreeCurricularPlan().getDegree().getSigla().equals(
                            degreeCurricularPlanAcronym)
                    && executionDegree.getExecutionYear().getYear().equals(year)) {
                return executionDegree;

            }

        }
        return null;
    }

    public ExecutionDegree readByDegreeCurricularPlanIDAndExecutionYear(Integer degreeCurricularPlanID,
            String executionYear) throws ExcepcaoPersistencia {

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        for (ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getExecutionYear().getYear().equals(executionYear)) {
                return executionDegree;
            }

        }

        return null;
    }

    public List readMasterDegrees(String year) throws ExcepcaoPersistencia {

        List<ExecutionYear> executionYears = (List<ExecutionYear>) readAll(ExecutionYear.class);
        List result = new ArrayList();

        for (ExecutionYear executionYear : executionYears) {
            if (executionYear.getYear().equals(year)) {
                List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
                for (ExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            DegreeType.MASTER_DEGREE)) {
                        result.add(executionDegree);
                    }
                }
            }
        }
        return result;
    }

    public List readByDegreeAndExecutionYear(Integer degreeOID, String year, CurricularStage curricularStage) throws ExcepcaoPersistencia {

        Degree degree = (Degree) readByOID(Degree.class, degreeOID);

        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getCurricularStage().equals(curricularStage)) {
                for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
                    if (executionDegree.getExecutionYear().getYear().equals(year)) {
                        result.add(executionDegree);
                    }
                }
            }
        }
        return result;
    }

    public List readByTeacher(Integer teacherOID) throws ExcepcaoPersistencia {
        Teacher teacher = (Teacher) readByOID(Teacher.class, teacherOID);
        List<ExecutionDegree> result = new ArrayList();
        if(teacher != null){
            List<Coordinator> coordinators = teacher.getCoordinators();
            for (Coordinator coordinator : coordinators) {
                result.add(coordinator.getExecutionDegree());
            }
        }
        return result;

    }

    public List readByExecutionYearAndDegreeType(String year, DegreeType degreeType)
            throws ExcepcaoPersistencia {

        List<ExecutionYear> executionYears = (List<ExecutionYear>) readAll(ExecutionYear.class);
        List<ExecutionDegree> result = new ArrayList();

        for (ExecutionYear executionYear : executionYears) {
            if (executionYear.getYear().equals(year)) {

                List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();

                for (ExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            degreeType)) {
                        result.add(executionDegree);
                    }
                }

            }
        }
        return result;
    }

    public List readByDegreeCurricularPlan(Integer degreeCurricularPlanOID) throws ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanOID);

        return degreeCurricularPlan.getExecutionDegrees();

    }

    public List readExecutionsDegreesByDegree(Integer degreeOID, CurricularStage curricularStage) throws ExcepcaoPersistencia {
        Degree degree = (Degree) readByOID(Degree.class, degreeOID);
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.getCurricularStage().equals(curricularStage)) {
                    result.addAll(degreeCurricularPlan.getExecutionDegrees());
            }
        }

        return result;
    }

    public List readByExecutionCourseAndByTeacher(Integer executionCourseOID, Integer teacherOID)
            throws ExcepcaoPersistencia {

        ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        Teacher teacher = (Teacher) readByOID(Teacher.class, teacherOID);
        List<ExecutionDegree> result = new ArrayList();

        List<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        for (CurricularCourse curricularCourse : curricularCourses) {
            DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            List<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();
            for (ExecutionDegree executionDegree : executionDegrees) {
                if (executionDegree.getCoordinatorsList().contains(teacher)) {
                    result.add(executionDegree);
                }
            }
        }
        return result;

    }

    public ExecutionDegree readByDegreeCurricularPlanNameAndExecutionYear(String name,
            Integer executionYearOID) throws ExcepcaoPersistencia {

        ExecutionYear executionYear = (ExecutionYear) readByOID(ExecutionYear.class, executionYearOID);
        List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getName().equals(name)) {
                return executionDegree;
            }
        }
        return null;

    }

    public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia {
        List<ExecutionYear> executionYears = (List<ExecutionYear>) readAll(ExecutionYear.class);
        List<ExecutionDegree> result = new ArrayList();
        for (ExecutionYear executionYear : executionYears) {
            if (executionYear.getState().equals(PeriodState.CURRENT)) {
                List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
                for (ExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            DegreeType.DEGREE)) {
                        result.add(executionDegree);
                    }
                }
            }
        }
        return result;
    }

    public ExecutionDegree readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
            Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia {
        ExecutionYear executionYear = (ExecutionYear) readByOID(ExecutionYear.class, executionYearID);
        List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanID)) {
                return executionDegree;
            }
        }
        return null;

    }

    public List readByExecutionYearOID(Integer executionYearOID) throws ExcepcaoPersistencia {
        ExecutionYear executionYear = (ExecutionYear) readByOID(ExecutionYear.class, executionYearOID);
        return executionYear.getExecutionDegrees();

    }

    public List readListByDegreeNameAndExecutionYearAndDegreeType(String name, Integer executionYearOID,
            DegreeType degreeType) throws ExcepcaoPersistencia {

        ExecutionYear executionYear = (ExecutionYear) readByOID(ExecutionYear.class, executionYearOID);
        List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        List<ExecutionDegree> result = new ArrayList();
        for (ExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getNome().equals(name)
                    && executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            degreeType)) {
                result.add(executionDegree);

            }
        }
        return result;
    }
}
