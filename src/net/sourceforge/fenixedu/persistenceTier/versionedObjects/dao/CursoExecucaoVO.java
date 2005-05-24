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

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PeriodState;

public class CursoExecucaoVO extends VersionedObjectsBase implements IPersistentExecutionDegree {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(ExecutionDegree.class);
    }

    public List readByExecutionYear(String executionYear) throws ExcepcaoPersistencia {
        List<IExecutionYear> executionYears = (List<IExecutionYear>) readAll(ExecutionYear.class);

        for (IExecutionYear executionYearElement : executionYears) {
            if (executionYearElement.getYear().equals(executionYear)) {
                return executionYearElement.getExecutionDegrees();
            }
        }
        return new ArrayList();
    }

    public IExecutionDegree readByDegreeCurricularPlanAndExecutionYear(String degreeCurricularPlanName,
            String degreeCurricularPlanAcronym, String year) throws ExcepcaoPersistencia {

        List<IExecutionDegree> executionDegrees = (List<IExecutionDegree>) readAll(ExecutionDegree.class);
        for (IExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getName().equals(degreeCurricularPlanName)
                    && executionDegree.getDegreeCurricularPlan().getDegree().getSigla().equals(
                            degreeCurricularPlanAcronym)
                    && executionDegree.getExecutionYear().getYear().equals(year)) {
                return executionDegree;

            }

        }
        return null;
    }

    public IExecutionDegree readByDegreeCurricularPlanIDAndExecutionYear(Integer degreeCurricularPlanID,
            String executionYear) throws ExcepcaoPersistencia {

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<IExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        for (IExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getExecutionYear().getYear().equals(executionYear)) {
                return executionDegree;
            }

        }

        return null;
    }

    public List readMasterDegrees(String year) throws ExcepcaoPersistencia {

        List<IExecutionYear> executionYears = (List<IExecutionYear>) readAll(ExecutionYear.class);
        List result = new ArrayList();

        for (IExecutionYear executionYear : executionYears) {
            if (executionYear.getYear().equals(year)) {
                List<IExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
                for (IExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            DegreeType.MASTER_DEGREE)) {
                        result.add(executionDegree);
                    }
                }
            }
        }

        return result;
    }

    public List readByDegreeAndExecutionYear(Integer degreeOID, String year) throws ExcepcaoPersistencia {

        IDegree degree = (IDegree) readByOID(Degree.class, degreeOID);
        List<IExecutionDegree> result = new ArrayList();

        List<IDegreeCurricularPlan> degreeCurricularPlans = degree.getDegreeCurricularPlans();
        for (IDegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (IExecutionDegree executionDegree : (List<IExecutionDegree>) degreeCurricularPlan
                    .getExecutionDegrees()) {
                if (executionDegree.getExecutionYear().getYear().equals(year)) {
                    result.add(executionDegree);
                }
            }
        }
        return result;
    }

    public List readByTeacher(Integer teacherOID) throws ExcepcaoPersistencia {
        ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherOID);
        List<IExecutionDegree> result = new ArrayList();
        List<ICoordinator> coordinators = teacher.getCoordinators();
        for (ICoordinator coordinator : coordinators) {
            result.add(coordinator.getExecutionDegree());
        }
        return result;

    }

    public List readByExecutionYearAndDegreeType(Integer executionYearOID, DegreeType degreeType)
            throws ExcepcaoPersistencia {

        IExecutionYear executionYear = (IExecutionYear) readByOID(ExecutionYear.class, executionYearOID);

        List<IExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        List<IExecutionDegree> result = new ArrayList();

        for (IExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(degreeType)) {
                result.add(executionDegree);
            }
        }
        return result;
    }

    public List readByDegreeCurricularPlan(Integer degreeCurricularPlanOID) throws ExcepcaoPersistencia {
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanOID);

        return degreeCurricularPlan.getExecutionDegrees();

    }

    public List readExecutionsDegreesByDegree(Integer degreeOID) throws ExcepcaoPersistencia {
        IDegree degree = (IDegree) readByOID(Degree.class, degreeOID);
        List<IExecutionDegree> result = new ArrayList();
        List<IDegreeCurricularPlan> degreeCurricularPlans = (List<IDegreeCurricularPlan>) degree
                .getDegreeCurricularPlans();
        for (IDegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (IExecutionDegree executionDegree : (List<IExecutionDegree>) degreeCurricularPlan
                    .getExecutionDegrees()) {
                result.add(executionDegree);
            }
        }

        return result;
    }

    public List readByExecutionCourseAndByTeacher(Integer executionCourseOID, Integer teacherOID)
            throws ExcepcaoPersistencia {

        IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);
        ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherOID);
        List<IExecutionDegree> result = new ArrayList();

        List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        for (ICurricularCourse curricularCourse : curricularCourses) {
            IDegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
            List<IExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();
            for (IExecutionDegree executionDegree : executionDegrees) {
                if (executionDegree.getCoordinatorsList().contains(teacher)) {
                    result.add(executionDegree);
                }
            }
        }
        return result;

    }

    public IExecutionDegree readByDegreeCurricularPlanNameAndExecutionYear(String name,
            Integer executionYearOID) throws ExcepcaoPersistencia {

        IExecutionYear executionYear = (IExecutionYear) readByOID(ExecutionYear.class, executionYearOID);
        List<IExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        for (IExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getName().equals(name)) {
                return executionDegree;
            }
        }
        return null;

    }

    public List readExecutionDegreesOfTypeDegree() throws ExcepcaoPersistencia {
        List<IExecutionYear> executionYears = (List<IExecutionYear>) readAll(ExecutionYear.class);
        List<IExecutionDegree> result = new ArrayList();
        for (IExecutionYear executionYear : executionYears) {
            if (executionYear.getState().equals(PeriodState.CURRENT)) {
                List<IExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
                for (IExecutionDegree executionDegree : executionDegrees) {
                    if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            DegreeType.DEGREE)) {
                        result.add(executionDegree);
                    }
                }
            }
        }
        return result;
    }

    public IExecutionDegree readExecutionDegreesbyDegreeCurricularPlanIDAndExecutionYearID(
            Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia {
        IExecutionYear executionYear = (IExecutionYear) readByOID(ExecutionYear.class, executionYearID);
        List<IExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        for (IExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanID)) {
                return executionDegree;
            }
        }
        return null;

    }

    public List readByExecutionYearOID(Integer executionYearOID) throws ExcepcaoPersistencia {
        IExecutionYear executionYear = (IExecutionYear) readByOID(ExecutionYear.class, executionYearOID);
        return executionYear.getExecutionDegrees();

    }

    public List readListByDegreeNameAndExecutionYearAndDegreeType(String name, Integer executionYearOID,
            DegreeType degreeType) throws ExcepcaoPersistencia {

        IExecutionYear executionYear = (IExecutionYear) readByOID(ExecutionYear.class, executionYearOID);
        List<IExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        List<IExecutionDegree> result = new ArrayList();
        for (IExecutionDegree executionDegree : executionDegrees) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getNome().equals(name)
                    && executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            degreeType)) {
                result.add(executionDegree);

            }
        }
        return result;
    }
}
