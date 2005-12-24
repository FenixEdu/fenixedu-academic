package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.IContext;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base {

    public DegreeCurricularPlan() {
        super();    
        setOjbConcreteClass(getClass().getName());
    }    

    public DegreeCurricularPlan(IDegree degree, String name, DegreeCurricularPlanState state,
            Date inicialDate, Date endDate, Integer degreeDuration,
            Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
            Integer numerusClausus, String annotation, CurricularStage curricularStage, GradeScale gradeScale) {

        this();

        setDegree(degree);
        setName(name);
        setState(state);
        setInitialDate(inicialDate);
        setEndDate(endDate);
        setDegreeDuration(degreeDuration);
        setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        setNeededCredits(neededCredits);
        setMarkType(markType);
        setNumerusClausus(numerusClausus);
        setAnotation(annotation);
        setGradeScale(gradeScale);
        setCurricularStage(curricularStage);        
        setConcreteClassForStudentCurricularPlans(degree.getConcreteClassForDegreeCurricularPlans());
    }
    
    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getDegree().getGradeScaleChain();    	
    }
    

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "name = " + this.getName() + "; ";
        result += "initialDate = " + this.getInitialDate() + "; ";
        result += "endDate = " + this.getEndDate() + "; ";
        result += "state = " + this.getState() + "; ";
        result += "needed Credits = " + this.getNeededCredits() + "; ";
        result += "Mark Type = " + this.getMarkType() + "; ";
        result += "degree = " + this.getDegree() + "]\n";
        result += "NumerusClausus = " + this.getNumerusClausus() + "]\n";

        return result;
    }

    public String print() {
        if (!this.getCurricularStage().equals(CurricularStage.OLD)) {
            StringBuffer dcp = new StringBuffer();
            
            dcp.append("[DCP ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");
            this.getDegreeModule().print(dcp, "", null);
                        
            return dcp.toString();
        } else {
            return "";    
        }
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

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();

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

        List scopes = area.getScopes();

        int scopesSize = scopes.size();

        for (int i = 0; i < scopesSize; i++) {
            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(i);

            ICurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();

            if (!curricularCourses.contains(curricularCourse)) {
                curricularCourses.add(curricularCourse);
            }
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

    public ICurricularCourse getCurricularCourseByCode(String code) {
        for (ICurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.getCode().equals(code))
                return curricularCourse;
        }
        return null;
    }

    public List getCommonAreas() {
        return (List) CollectionUtils.select(getAreas(), new Predicate() {
            public boolean evaluate(Object obj) {
                IBranch branch = (IBranch) obj;
                if (branch.getBranchType() == null) {
                    return branch.getName().equals("") && branch.getCode().equals("");
                }
                return branch.getBranchType().equals(BranchType.COMNBR);

            }
        });
    }

    public List getTFCs() {

        List curricularCourses = (List) CollectionUtils.select(getCurricularCourses(), new Predicate() {
            public boolean evaluate(Object obj) {
                ICurricularCourse cc = (ICurricularCourse) obj;
                return cc.getType().equals(CurricularCourseType.TFC_COURSE);
            }
        });

        return curricularCourses;
    }

    public List getSpecializationAreas() {

        return (List) CollectionUtils.select(getAreas(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IBranch branch = (IBranch) arg0;
                return branch.getBranchType().equals(BranchType.SPECBR);
            }

        });
    }

    public List getSecundaryAreas() {
        return (List) CollectionUtils.select(getAreas(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IBranch branch = (IBranch) arg0;
                return branch.getBranchType().equals(BranchType.SECNBR);
            }

        });
    }

    public List getActiveCurricularCoursesByYearAndSemester(int year, Integer semester) {
        final List<ICurricularCourse> result = new ArrayList<ICurricularCourse>();
        for (final ICurricularCourse curricularCourse : getCurricularCourses()) {
            for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                final ICurricularSemester curricularSemester = curricularCourseScope
                        .getCurricularSemester();
                if (curricularSemester.getSemester().equals(semester)
                        && curricularSemester.getCurricularYear().getYear().intValue() == year
                        && curricularCourseScope.isActive()) {
                    result.add(curricularCourse);
                    break;
                }
            }
        }
        return result;
    }

    public List getSpecialListOfCurricularCourses() {
        return new ArrayList();
    }

    public List getAllOptionalCurricularCourseGroups() {

        List groups = new ArrayList();

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            IPersistentCurricularCourseGroup curricularCourseGroupDAO = persistentSuport
                    .getIPersistentCurricularCourseGroup();

            groups = curricularCourseGroupDAO
                    .readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(this.getIdInternal());

        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

    public void edit(String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate,
            Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
            MarkType markType, Integer numerusClausus, String annotation, GradeScale gradeScale) {

        setName(name);
        setState(state);
        setInitialDate(inicialDate);
        setEndDate(endDate);
        setDegreeDuration(degreeDuration);
        setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        setNeededCredits(neededCredits);
        setMarkType(markType);
        setNumerusClausus(numerusClausus);
        setAnotation(annotation);
        setGradeScale(gradeScale);
    }

    private Boolean canBeDeleted() {

        return !(hasAnyStudentCurricularPlans() || hasAnyCurricularCourseEquivalences()
                || hasAnyEnrolmentPeriods() || hasAnyCurricularCourses() || hasAnyExecutionDegrees() || hasAnyAreas());
    }

    public void delete() {

        if (canBeDeleted()) {
            removeDegree();
            deleteDomainObject();
        } else
            throw new DomainException("error.degree.curricular.plan.cant.delete");

    }

    public boolean isGradeValid(String grade) {

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(this);

        if (grade == null || grade.length() == 0)
            return false;

        return degreeCurricularPlanStrategy.checkMark(grade.toUpperCase());
    }
    
    public IEnrolmentPeriodInCurricularCoursesSpecialSeason getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(IExecutionPeriod executionPeriod) {
    	for (IEnrolmentPeriod enrolmentPeriod : getEnrolmentPeriods()) {
			if((enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason) && (enrolmentPeriod.getExecutionPeriod().equals(executionPeriod))) {
				return (IEnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod;
			}
		}
    	return null;
    }
    
    public ICurricularCourse createCurricularCourse(String name, String code, String acronym, Boolean enrolmentAllowed, CurricularStage curricularStage) {        
        checkAttributes(name, code, acronym);
        final ICurricularCourse curricularCourse = new CurricularCourse(name, code, acronym, enrolmentAllowed, curricularStage);
        this.addCurricularCourses(curricularCourse);
        return curricularCourse;
    }

    private void checkAttributes(String name, String code, String acronym) {
        for (final ICurricularCourse curricularCourse : this.getCurricularCourses()) {
            if (curricularCourse.getName().equals(name) && curricularCourse.getCode().equals(code)) {
                throw new DomainException("error.curricularCourseWithSameNameAndCode");
            }
            if (curricularCourse.getAcronym().equals(acronym)) {
                throw new DomainException("error.curricularCourseWithSameAcronym");
            }
        }
    }
    
    public List<ICurricularCourse> getDcpCurricularCourses() {
        final Set<ICurricularCourse> result = new HashSet<ICurricularCourse>();
        if (this.getDegreeModule() instanceof ICourseGroup) {
            collectChildCurricularCourses(result, (ICourseGroup) this.getDegreeModule());
        }
        return new ArrayList(result);
    }

    private void collectChildCurricularCourses(final Set<ICurricularCourse> result, ICourseGroup courseGroup) {
        for (final IContext context : courseGroup.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof ICurricularCourse) {
                result.add((ICurricularCourse) context.getDegreeModule());
            } else if (context.getDegreeModule() instanceof ICourseGroup) {
                collectChildCurricularCourses(result, (ICourseGroup) context.getDegreeModule());
            }
        }
    }
}
