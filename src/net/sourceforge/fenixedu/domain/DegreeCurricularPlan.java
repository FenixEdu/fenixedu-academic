package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.predicates.DegreeCurricularPlanPredicates;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base {
    private final String DCP_ROOT_NAME = "_ROOT_NAME";

    public DegreeCurricularPlan() {
        super();
    }

    private DegreeCurricularPlan(Degree degree) {
        this();
        if (degree == null) {
            throw new DomainException("degreeCurricularPlan.degree.not.null");
        }
        this.setDegree(degree);
        this.setOjbConcreteClass(getClass().getName());
    }

    public DegreeCurricularPlan(Degree degree, String name, DegreeCurricularPlanState state,
            Date inicialDate, Date endDate, Integer degreeDuration,
            Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
            Integer numerusClausus, String annotation, GradeScale gradeScale) {

        this(degree);
        commonFieldsChange(name, gradeScale);
        oldStructureFieldsChange(state, inicialDate, endDate, degreeDuration,
                minimalYearForOptionalCourses, neededCredits, markType, numerusClausus, annotation);

        this.setCurricularStage(CurricularStage.OLD);
        this
                .setConcreteClassForStudentCurricularPlans(degree
                        .getConcreteClassForDegreeCurricularPlans());
    }

    private void commonFieldsChange(String name, GradeScale gradeScale) {
        if (name == null) {
            throw new DomainException("degreeCurricularPlan.name.not.null");
        }

        this.setName(name);
        this.setGradeScale(gradeScale);
    }

    private void oldStructureFieldsChange(DegreeCurricularPlanState state, Date inicialDate,
            Date endDate, Integer degreeDuration, Integer minimalYearForOptionalCourses,
            Double neededCredits, MarkType markType, Integer numerusClausus, String annotation) {

        if (inicialDate == null) {
            throw new DomainException("degreeCurricularPlan.inicialDate.not.null");
        } else if (degreeDuration == null) {
            throw new DomainException("degreeCurricularPlan.degreeDuration.not.null");
        } else if (minimalYearForOptionalCourses == null) {
            throw new DomainException("degreeCurricularPlan.minimalYearForOptionalCourses.not.null");
        }

        this.setState(state);
        this.setInitialDate(inicialDate);
        this.setEndDate(endDate);
        this.setDegreeDuration(degreeDuration);
        this.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
        this.setNeededCredits(neededCredits);
        this.setMarkType(markType);
        this.setNumerusClausus(numerusClausus);
        this.setAnotation(annotation);
    }

    public DegreeCurricularPlan(Degree degree, String name, GradeScale gradeScale, Person creator, CurricularPeriod curricularPeriod) {
        this(degree);
        commonFieldsChange(name, gradeScale);
        newStructureFieldsChange(CurricularStage.DRAFT);

        CourseGroup dcpRoot = new CourseGroup(name + this.DCP_ROOT_NAME);
        this.setDegreeModule(dcpRoot);
        
        if (curricularPeriod == null) {
            throw new DomainException("degreeCurricularPlan.curricularPeriod.not.null");
        }
        this.setDegreeStructure(curricularPeriod);

        if (creator == null) {
            throw new DomainException("degreeCurricularPlan.creator.not.null");
        }
        this.setCurricularPlanMembersGroup(new FixedSetGroup(creator));
    }

    private void newStructureFieldsChange(CurricularStage curricularStage) {
        if (curricularStage == null) {
            throw new DomainException("degreeCurricularPlan.curricularStage.not.null");
        }

        this.setCurricularStage(curricularStage);
    }

    public void edit(String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate,
            Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
            MarkType markType, Integer numerusClausus, String annotation, GradeScale gradeScale) {

        commonFieldsChange(name, gradeScale);
        oldStructureFieldsChange(state, inicialDate, endDate, degreeDuration,
                minimalYearForOptionalCourses, neededCredits, markType, numerusClausus, annotation);
    }

    public void edit(String name, CurricularStage curricularStage, GradeScale gradeScale) {
        commonFieldsChange(name, gradeScale);
        newStructureFieldsChange(curricularStage);
        
        // assert unique pair name/degree
        for (final DegreeCurricularPlan dcp : this.getDegree().getDegreeCurricularPlans()) {
            if (dcp != this && dcp.getName().equalsIgnoreCase(name)) {
                throw new DomainException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
    }

    private Boolean getCanBeDeleted() {
        return ((CourseGroup) getDegreeModule()).getCanBeDeleted()
                && !(hasAnyStudentCurricularPlans() || hasAnyCurricularCourseEquivalences()
                        || hasAnyEnrolmentPeriods() || hasAnyCurricularCourses()
                        || hasAnyExecutionDegrees() || hasAnyAreas());
    }

    public void delete() {
        if (getCanBeDeleted()) {
            removeDegree();
            if (hasDegreeModule()) {
                getDegreeModule().delete();
            }
            if (hasDegreeStructure()) {
                getDegreeStructure().delete();
            }
            deleteDomainObject();
        } else
            throw new DomainException("error.degree.curricular.plan.cant.delete");
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
            StringBuilder dcp = new StringBuilder();

            dcp.append("[DCP ").append(this.getIdInternal()).append("] ").append(this.getName()).append(
                    "\n");
            this.getDegreeModule().print(dcp, "", null);

            return dcp.toString();
        } else {
            return "";
        }
    }

    public GradeScale getGradeScaleChain() {
        return super.getGradeScale() != null ? super.getGradeScale() : getDegree().getGradeScaleChain();
    }

    public StudentCurricularPlan getNewStudentCurricularPlan() {
        StudentCurricularPlan studentCurricularPlan = null;

        try {
            Class classDefinition = Class.forName(getConcreteClassForStudentCurricularPlans());
            studentCurricularPlan = (StudentCurricularPlan) classDefinition.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return studentCurricularPlan;
    }

    public ExecutionDegree getExecutionDegreeByYear(ExecutionYear executionYear) {
    	for (ExecutionDegree executionDegree : getExecutionDegrees()) {
			if(executionDegree.getExecutionYear().equals(executionYear)) {
				return executionDegree;
			}
		}
    	return null;
    }
    
    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
                executionPeriod));

        return result;
    }

    public List getCurricularCoursesFromArea(Branch area, AreaType areaType) {

        List curricularCourses = new ArrayList();

        List scopes = area.getScopes();

        int scopesSize = scopes.size();

        for (int i = 0; i < scopesSize; i++) {
            CurricularCourseScope curricularCourseScope = (CurricularCourseScope) scopes.get(i);

            CurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();

            if (!curricularCourses.contains(curricularCourse)) {
                curricularCourses.add(curricularCourse);
            }
        }

        return curricularCourses;
    }

    public List getCurricularCoursesFromAnyArea() {
        List curricularCourses = new ArrayList();
        for (Iterator iter = getAreas().iterator(); iter.hasNext();) {
            Branch branch = (Branch) iter.next();
            getCurricularCoursesFromArea(branch, null);
        }
        return curricularCourses;
    }

    public CurricularCourse getCurricularCourseByCode(String code) {
        for (CurricularCourse curricularCourse : getCurricularCourses()) {
            if (curricularCourse.getCode().equals(code))
                return curricularCourse;
        }
        return null;
    }

    public List getCommonAreas() {
        return (List) CollectionUtils.select(getAreas(), new Predicate() {
            public boolean evaluate(Object obj) {
                Branch branch = (Branch) obj;
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
                CurricularCourse cc = (CurricularCourse) obj;
                return cc.getType().equals(CurricularCourseType.TFC_COURSE);
            }
        });

        return curricularCourses;
    }

    public List getSpecializationAreas() {

        return (List) CollectionUtils.select(getAreas(), new Predicate() {

            public boolean evaluate(Object arg0) {
                Branch branch = (Branch) arg0;
                return branch.getBranchType().equals(BranchType.SPECBR);
            }

        });
    }

    public List getSecundaryAreas() {
        return (List) CollectionUtils.select(getAreas(), new Predicate() {

            public boolean evaluate(Object arg0) {
                Branch branch = (Branch) arg0;
                return branch.getBranchType().equals(BranchType.SECNBR);
            }

        });
    }

    public List getActiveCurricularCoursesByYearAndSemester(int year, Integer semester) {
        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (final CurricularCourse curricularCourse : getCurricularCourses()) {
            for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                final CurricularSemester curricularSemester = curricularCourseScope
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

    public boolean isGradeValid(String grade) {

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(this);

        if (grade == null || grade.length() == 0)
            return false;

        return degreeCurricularPlanStrategy.checkMark(grade.toUpperCase());
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(
            ExecutionPeriod executionPeriod) {
        for (EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriods()) {
            if ((enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason)
                    && (enrolmentPeriod.getExecutionPeriod().equals(executionPeriod))) {
                return (EnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod;
            }
        }
        return null;
    }

    public CurricularCourse createCurricularCourse(String name, String code, String acronym,
            Boolean enrolmentAllowed, CurricularStage curricularStage) {
        checkAttributes(name, code, acronym);
        final CurricularCourse curricularCourse = new CurricularCourse(name, code, acronym,
                enrolmentAllowed, curricularStage);
        this.addCurricularCourses(curricularCourse);
        return curricularCourse;
    }

    private void checkAttributes(String name, String code, String acronym) {
        for (final CurricularCourse curricularCourse : this.getCurricularCourses()) {
            if (curricularCourse.getName().equals(name) && curricularCourse.getCode().equals(code)) {
                throw new DomainException("error.curricularCourseWithSameNameAndCode");
            }
            if (curricularCourse.getAcronym().equals(acronym)) {
                throw new DomainException("error.curricularCourseWithSameAcronym");
            }
        }
    }

    public List<CurricularCourse> getDcpCurricularCourses() {
        final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
        if (this.getDegreeModule() instanceof CourseGroup) {
            collectChildCurricularCourses(result, (CourseGroup) this.getDegreeModule());
        }
        return new ArrayList(result);
    }

    private void collectChildCurricularCourses(final Set<CurricularCourse> result,
            CourseGroup courseGroup) {
        for (final Context context : courseGroup.getCourseGroupContexts()) {
            if (context.getDegreeModule() instanceof CurricularCourse) {
                result.add((CurricularCourse) context.getDegreeModule());
            } else if (context.getDegreeModule() instanceof CourseGroup) {
                collectChildCurricularCourses(result, (CourseGroup) context.getDegreeModule());
            }
        }
    }
    
    public Branch getBranchByName(final String branchName) {
        for (final Branch branch : getAreas()) {
            if (branchName.equals(branch.getName())) {
                return branch;
            }
        }
        return null;
    }
    

    public Boolean getUserCanBuild() {
        Person person = AccessControl.getUserView().getPerson();
        return this.getCurricularPlanMembersGroup().isMember(person);
    }
    
}
