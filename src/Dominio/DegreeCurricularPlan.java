package Dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import Dominio.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import Dominio.degree.enrollment.rules.PrecedencesEnrollmentRule;
import Dominio.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.BranchType;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlan extends DomainObject implements IDegreeCurricularPlan {

    protected Integer degreeKey;

    protected String ojbConcreteClass;

    protected String concreteClassForStudentCurricularPlans;

    protected IDegree degree;

    protected String name;

    protected DegreeCurricularPlanState state;

    protected Date initialDate;

    protected Date endDate;

    protected Integer degreeDuration;

    protected Double neededCredits;

    protected MarkType markType;

    protected Integer numerusClausus;

    protected String description;

    protected String descriptionEn;

    protected List curricularCourses;

    protected List areas;

    // For enrollment purposes
    protected Integer minimalYearForOptionalCourses;

    protected List curricularCourseEquivalences;

    public DegreeCurricularPlan() {
        ojbConcreteClass = getClass().getName();
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
        result += "name = " + this.name + "; ";
        result += "initialDate = " + this.initialDate + "; ";
        result += "endDate = " + this.endDate + "; ";
        result += "state = " + this.state + "; ";
        result += "needed Credits = " + this.neededCredits + "; ";
        result += "Mark Type = " + this.markType + "; ";
        result += "degree = " + this.degree + "]\n";
        result += "NumerusClausus = " + this.numerusClausus + "]\n";

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

    public Integer getDegreeDuration() {
        return degreeDuration;
    }

    public Integer getDegreeKey() {
        return degreeKey;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public Integer getMinimalYearForOptionalCourses() {
        return minimalYearForOptionalCourses;
    }

    public String getName() {
        return name;
    }

    public Double getNeededCredits() {
        return neededCredits;
    }

    public Integer getNumerusClausus() {
        return numerusClausus;
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

    public void setDegreeDuration(Integer degreeDuration) {
        this.degreeDuration = degreeDuration;
    }

    public void setDegreeKey(Integer degreeKey) {
        this.degreeKey = degreeKey;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public void setMarkType(MarkType markType) {
        this.markType = markType;
    }

    public void setMinimalYearForOptionalCourses(Integer minimalYearForOptionalCourses) {
        this.minimalYearForOptionalCourses = minimalYearForOptionalCourses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNeededCredits(Double neededCredits) {
        this.neededCredits = neededCredits;
    }

    public void setNumerusClausus(Integer numerusClausus) {
        this.numerusClausus = numerusClausus;
    }

    public void setState(DegreeCurricularPlanState state) {
        this.state = state;
    }

    public String getConcreteClassForStudentCurricularPlans() {
        return concreteClassForStudentCurricularPlans;
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setConcreteClassForStudentCurricularPlans(String concreteClassForStudentCurricularPlans) {
        this.concreteClassForStudentCurricularPlans = concreteClassForStudentCurricularPlans;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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