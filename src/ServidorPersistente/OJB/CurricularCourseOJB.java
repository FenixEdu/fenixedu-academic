package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularCourse;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.IScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 * 
 * 25/Mar/2003
 */

public class CurricularCourseOJB extends PersistentObjectOJB implements IPersistentCurricularCourse {

    public CurricularCourseOJB() {
    }

    /**
     * @deprecated
     */
    public ICurricularCourse readCurricularCourseByNameAndCode(String name, String code)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        crit.addEqualTo("code", code);
        return (ICurricularCourse) queryObject(CurricularCourse.class, crit);

    }
    
    public List readCurricularCoursesByName(String name)
    		throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        return queryList(CurricularCourse.class, crit);
    }
    
    public ICurricularCourse readCurricularCourseByDegreeCurricularPlanAndNameAndCode(
            Integer degreeCurricularPlanId, String name, String code) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        crit.addEqualTo("code", code);
        crit.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlanId);
        return (ICurricularCourse) queryObject(CurricularCourse.class, crit);

    }

    public void delete(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {

        super.delete(curricularCourse);

    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(CurricularCourse.class, crit);
    }

    public List readCurricularCoursesByCurricularYear(Integer year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("scopes.curricularSemester.curricularYear.year", year);
        return queryList(CurricularCourse.class, crit);

    }

    public List readCurricularCoursesByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.name", degreeCurricularPlan.getName());
        crit.addEqualTo("degreeCurricularPlan.degree.nome", degreeCurricularPlan.getDegree().getNome());
        crit
                .addEqualTo("degreeCurricularPlan.degree.sigla", degreeCurricularPlan.getDegree()
                        .getSigla());
        return queryList(CurricularCourse.class, crit);

    }

    public List readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(
            IDegreeCurricularPlan degreeCurricularPlan, Boolean basic) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlanKey", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("basic", basic);
        return queryList(CurricularCourse.class, criteria);
    }

    public List readAllCurricularCoursesByBranch(IBranch branch) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scopes.branch.name", branch.getName());
        criteria.addEqualTo("scopes.branch.code", branch.getCode());
        return queryList(CurricularCourse.class, criteria);

    }

    public List readAllCurricularCoursesBySemester(Integer semester) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scopes.curricularSemester.semester", semester);
        return queryList(CurricularCourse.class, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourse#readCurricularCoursesBySemesterAndYear(java.lang.Integer,
     *      java.lang.Integer)
     */

    public List readCurricularCoursesBySemesterAndYear(Integer semester, Integer year)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scopes.curricularSemester.semester", semester);
        criteria.addEqualTo("scopes.curricularSemester.curricularYear.year", year);
        return queryList(CurricularCourse.class, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourse#readCurricularCoursesBySemesterAndYearAnBranch(java.lang.Integer,
     *      java.lang.Integer, Dominio.IBranch)
     */

    public List readCurricularCoursesBySemesterAndYearAndBranch(Integer semester, Integer year,
            IBranch branch) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("scopes.curricularSemester.semester", semester);
        criteria.addEqualTo("scopes.curricularSemester.curricularYear.year", year);
        criteria.addEqualTo("scopes.branch.name", branch.getName());
        criteria.addEqualTo("scopes.branch.code", branch.getCode());
        return queryList(CurricularCourse.class, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourse#readCurricularCoursesBySemesterAndYearAndBranchAndNoBranch(java.lang.Integer,
     *      java.lang.Integer, Dominio.IBranch)
     */
    public List readCurricularCoursesBySemesterAndYearAndBranchAndNoBranch(Integer semester,
            Integer year, IBranch branch) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scopes.curricularSemester.semester", semester);
        criteria.addEqualTo("scopes.curricularSemester.curricularYear.year", year);
        Criteria criteria2 = new Criteria();
        criteria2.addEqualTo("scopes.branch.name", branch.getName());
        criteria2.addEqualTo("scopes.branch.code", branch.getCode());

        Criteria criteria3 = new Criteria();
        criteria3.addEqualTo("scopes.branch.name", "");
        criteria3.addEqualTo("scopes.branch.code", "");
        criteria.addOrCriteria(criteria2);
        criteria.addOrCriteria(criteria3);
        return queryList(CurricularCourse.class, criteria);

    }

    public List readbyCourseCodeAndDegreeCurricularPlan(String curricularCourseCode,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("code", curricularCourseCode);
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());

        return queryList(CurricularCourse.class, criteria);
    }

    public List readbyCourseCodeAndDegreeTypeAndDegreeCurricularPlanState(String courseCode,
            TipoCurso degreeType, DegreeCurricularPlanState degreeCurricularPlanState)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("code", courseCode);
        criteria.addEqualTo("degreeCurricularPlan.degree.tipoCurso", degreeType);
        criteria.addEqualTo("degreeCurricularPlan.state", degreeCurricularPlanState);

        return queryList(CurricularCourse.class, criteria);
    }

    public List readbyCourseNameAndDegreeCurricularPlan(String curricularCourseName,
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", curricularCourseName);
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());

        return queryList(CurricularCourse.class, criteria);
    }

    public List readByScientificArea(IScientificArea scientificArea) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("scientificArea.idInternal", scientificArea.getIdInternal());

        return queryList(CurricularCourse.class, criteria);
    }

    public List readAllCurricularCoursesByDegreeCurricularPlanAndBranchAndSemester(
            IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, Integer semester)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("scopes.branch.idInternal", branch.getIdInternal());
        criteria.addEqualTo("scopes.curricularSemester.semester", semester);
        return queryList(CurricularCourse.class, criteria);
    }

    public List readCurricularCoursesByDegreeCurricularPlanAndMandatoryAttribute(
            IDegreeCurricularPlan degreeCurricularPlan, Boolean mandatory) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("mandatory", mandatory);
        return queryList(CurricularCourse.class, criteria);
    }

    public List readAllByDegreeCurricularPlanAndType(IDegreeCurricularPlan degreeCurricularPlan,
            CurricularCourseType curricularCourseType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.idInternal", degreeCurricularPlan.getIdInternal());
        criteria.addEqualTo("type", curricularCourseType);
        return queryList(CurricularCourse.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourse#readExecutedCurricularCoursesByDegreeAndExecutionYear(Dominio.IDegree,
     *      Dominio.IExecutionYear)
     */
    public List readExecutedCurricularCoursesByDegreeAndExecutionYear(IDegree curso,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.degree.idInternal", curso.getIdInternal());
        criteria.addEqualTo("degreeCurricularPlan.state", DegreeCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("associatedExecutionCourses.executionPeriod.executionYear.idInternal",
                executionYear.getIdInternal());
        return queryList(CurricularCourse.class, criteria, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourse#readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(Dominio.IDegree,
     *      java.lang.Integer, Dominio.IExecutionYear)
     */
    public List readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(IDegree curso, Integer year,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCurricularPlan.degree.idInternal", curso.getIdInternal());
        criteria.addEqualTo("degreeCurricularPlan.state", DegreeCurricularPlanState.ACTIVE_OBJ);
        criteria.addEqualTo("associatedExecutionCourses.executionPeriod.executionYear.idInternal",
                executionYear.getIdInternal());
        criteria.addEqualTo("scopes.curricularSemester.curricularYear.year", year);
        return queryList(CurricularCourse.class, criteria, true);
    }

}