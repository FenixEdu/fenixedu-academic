package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.AreaType;
import net.sourceforge.fenixedu.util.BranchType;
import net.sourceforge.fenixedu.util.CurricularCourseType;
import net.sourceforge.fenixedu.util.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base implements IDegreeCurricularPlan {

    protected IDegree degree;

    protected DegreeCurricularPlanState state;

    protected MarkType markType;

    protected List curricularCourses;

    protected List areas;

    protected List curricularCourseEquivalences;

    public DegreeCurricularPlan() {
        setOjbConcreteClass(getClass().getName());
    }

    public IStudentCurricularPlan getNewStudentCurricularPlan() {
        IStudentCurricularPlan studentCurricularPlan = null;

        try {
            Class classDefinition = Class.forName(getConcreteClassForStudentCurricularPlans());
            studentCurricularPlan = (IStudentCurricularPlan) classDefinition.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (ClassNotFoundException e) {
        }

        return studentCurricularPlan;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "name = " + this.getName() + "; ";
        result += "initialDate = " + this.getInitialDate() + "; ";
        result += "endDate = " + this.getEndDate() + "; ";
        result += "state = " + this.state + "; ";
        result += "needed Credits = " + this.getNeededCredits() + "; ";
        result += "Mark Type = " + this.markType + "; ";
        result += "degree = " + this.degree + "]\n";
        result += "NumerusClausus = " + this.getNumerusClausus() + "]\n";

        return result;
    }

    public List getCurricularCourseEquivalences() {
        return curricularCourseEquivalences;
    }

    public void setCurricularCourseEquivalences(List curricularCourseEquivalences) {
        this.curricularCourseEquivalences = curricularCourseEquivalences;
    }

    public List getAreas() {
        return areas;
    }

    public List getCurricularCourses() {
        return curricularCourses;
    }

    public IDegree getDegree() {
        return degree;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public DegreeCurricularPlanState getState() {
        return state;
    }

    public void setAreas(List areas) {
        this.areas = areas;
    }

    public void setCurricularCourses(List curricularCourses) {
        this.curricularCourses = curricularCourses;
    }

    public void setDegree(IDegree degree) {
        this.degree = degree;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }

    public void setState(DegreeCurricularPlanState state) {
        this.state = state;
    }
    
    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        //result.add(new SecretaryEnrollmentRule(studentCurricularPlan));
        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
                executionPeriod));

        return result;
    }

    public List getCurricularCoursesFromArea(IBranch area, AreaType areaType) {

        List curricularCourses = new ArrayList();

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourseScope curricularCourseScopeDAO = persistentSuport
                    .getIPersistentCurricularCourseScope();

            List scopes = curricularCourseScopeDAO.readByBranch(area);

            int scopesSize = scopes.size();

            for (int i = 0; i < scopesSize; i++) {
                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(i);

                ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();

                if (!curricularCourses.contains(curricularCourse)) {
                    curricularCourses.add(curricularCourse);
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }

        return curricularCourses;
    }

    public List getCurricularCoursesFromAnyArea() {
        List curricularCourses = new ArrayList();
        for (Iterator iter = getAreas().iterator(); iter.hasNext();) {
            IBranch branch = (IBranch) iter.next();
            getCurricularCoursesFromArea(branch, null);
        }
        return curricularCourses;
    }

    public List getCommonAreas() {
        return (List) CollectionUtils.select(this.areas, new Predicate() {
            public boolean evaluate(Object obj) {
                IBranch branch = (IBranch) obj;
                if (branch.getBranchType() == null) {
                    return branch.getName().equals("") && branch.getCode().equals("");
                }
                return branch.getBranchType().equals(BranchType.COMMON_BRANCH);

            }
        });
    }

    public List getTFCs() {

        List curricularCourses = (List) CollectionUtils.select(getCurricularCourses(), new Predicate() {
            public boolean evaluate(Object obj) {
                ICurricularCourse cc = (ICurricularCourse) obj;
                return cc.getType().equals(CurricularCourseType.TFC_COURSE_OBJ);
            }
        });

        return curricularCourses;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IDegreeCurricularPlan#getSpecializationAreas()
     */
    public List getSpecializationAreas() {

        return (List) CollectionUtils.select(getAreas(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IBranch branch = (IBranch) arg0;
                return branch.getBranchType().equals(BranchType.SPECIALIZATION_BRANCH);
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IDegreeCurricularPlan#getSecundaryAreas()
     */
    public List getSecundaryAreas() {
        return (List) CollectionUtils.select(getAreas(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IBranch branch = (IBranch) arg0;
                return branch.getBranchType().equals(BranchType.SECUNDARY_BRANCH);
            }

        });
    }

    public List getCurricularCoursesByYearAndSemesterAndBranch(int year, Integer semester, IBranch area) {

        List finalCurricularCourses = new ArrayList();
        List curricularCourses = getCurricularCourses();
        final Integer wantedSemester = semester;
        final IBranch branch = area;

        Collections.sort(curricularCourses, new Comparator() {

            public int compare(Object obj1, Object obj2) {

                ICurricularCourse curricularCourse1 = (ICurricularCourse) obj1;
                ICurricularCourse curricularCourse2 = (ICurricularCourse) obj2;
                ICurricularYear curricularYear1 = curricularCourse1
                        .getCurricularYearByBranchAndSemester(branch, wantedSemester);
                ICurricularYear curricularYear2 = curricularCourse2
                        .getCurricularYearByBranchAndSemester(branch, wantedSemester);

                return curricularYear1.getYear().intValue() - curricularYear2.getYear().intValue();
            }
        });

        ICurricularCourse cc = null;
        ICurricularYear curricularYear = null;
        for (int iter = 0; iter < curricularCourses.size(); iter++) {
            cc = (ICurricularCourse) curricularCourses.get(iter);
            curricularYear = cc.getCurricularYearByBranchAndSemester(branch, wantedSemester);

            if ((curricularYear.getYear().intValue() == year) && belongsToSemester(cc, semester))
                finalCurricularCourses.add(cc);
            else if (curricularYear.getYear().intValue() > year)
                break;
        }

        return finalCurricularCourses;
    }

    public List getSpecialListOfCurricularCourses() {
        return new ArrayList();
    }

    private boolean belongsToSemester(ICurricularCourse curricularCourse, Integer semester) {
        List scopes = curricularCourse.getScopes();
        ICurricularCourseScope ccs = null;

        for (int iter = 0; iter < scopes.size(); iter++) {
            ccs = (ICurricularCourseScope) scopes.get(iter);
            if (ccs.getCurricularSemester().getSemester().intValue() == semester.intValue())
                return true;
        }
        return false;
    }

    public List getAllOptionalCurricularCourseGroups() {

        List groups = new ArrayList();

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                    .getIPersistentCurricularCourseGroup();

            groups = curricularCourseGroupDAO
                    .readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(this);

        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------

    
    
    
}