package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.IEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base {
    private final String DCP_ROOT_NAME = "_ROOT_NAME";

    public DegreeCurricularPlan() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    private DegreeCurricularPlan(Degree degree) {
        this();
        if (degree == null) {
            throw new DomainException("degreeCurricularPlan.degree.not.null");
        }
        super.setDegree(degree);
        super.setOjbConcreteClass(getClass().getName());
    }

    public DegreeCurricularPlan(Degree degree, String name, DegreeCurricularPlanState state,
            Date inicialDate, Date endDate, Integer degreeDuration,
            Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
            Integer numerusClausus, String annotation, GradeScale gradeScale) {

        this(degree);
        super.setCurricularStage(CurricularStage.OLD);
        
        if (name == null) {
            throw new DomainException("degreeCurricularPlan.name.not.null");
        }

        super.setName(name);
        super.setGradeScale(gradeScale);
        
        oldStructureFieldsChange(state, inicialDate, endDate, degreeDuration,
                minimalYearForOptionalCourses, neededCredits, markType, numerusClausus, annotation);
        
        this.setConcreteClassForStudentCurricularPlans(degree.getConcreteClassForDegreeCurricularPlans());
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

    public DegreeCurricularPlan(Degree degree, String name, GradeScale gradeScale, Person creator,
            CurricularPeriod curricularPeriod) {
        this(degree);

        if (name == null) {
            throw new DomainException("degreeCurricularPlan.name.not.null");
        }

        super.setName(name);
        super.setGradeScale(gradeScale);

        newStructureFieldsChange(CurricularStage.DRAFT, null);

        CourseGroup dcpRoot = new CourseGroup(name + this.DCP_ROOT_NAME, name + this.DCP_ROOT_NAME);
        this.setRoot(dcpRoot);

        if (curricularPeriod == null) {
            throw new DomainException("degreeCurricularPlan.curricularPeriod.not.null");
        }
        this.setDegreeStructure(curricularPeriod);

        if (creator == null) {
            throw new DomainException("degreeCurricularPlan.creator.not.null");
        }
        this.setCurricularPlanMembersGroup(new FixedSetGroup(creator));
    }

    private void newStructureFieldsChange(CurricularStage curricularStage, ExecutionYear beginExecutionYear) {
        if (curricularStage == null) {
            throw new DomainException("degreeCurricularPlan.curricularStage.not.null");
        }
        if (curricularStage == CurricularStage.APPROVED) {
            approve(beginExecutionYear);
        } else {
            this.setCurricularStage(curricularStage);            
        }
    }

    public void edit(String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate,
            Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
            MarkType markType, Integer numerusClausus, String annotation, GradeScale gradeScale) {

        commonFieldsChange(name, gradeScale);
        oldStructureFieldsChange(state, inicialDate, endDate, degreeDuration,
                minimalYearForOptionalCourses, neededCredits, markType, numerusClausus, annotation);
    }

    public void edit(String name, CurricularStage curricularStage, GradeScale gradeScale, ExecutionYear beginExecutionYear) {
        commonFieldsChange(name, gradeScale);
        newStructureFieldsChange(curricularStage, beginExecutionYear);

        // assert unique pair name/degree
        for (final DegreeCurricularPlan dcp : this.getDegree().getDegreeCurricularPlans()) {
            if (dcp != this && dcp.getName().equalsIgnoreCase(name)) {
                throw new DomainException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
    }
    
    public void approve(ExecutionYear beginExecutionYear) {
        if (beginExecutionYear == null) {
            throw new DomainException("error.invalid.execution.year");
        } else if (beginExecutionYear.getExecutionPeriodForSemester(Integer.valueOf(1)) == null) {
            throw new DomainException("error.invalid.execution.period");
        }
        checkIfCurricularCoursesBelongToApprovedCompetenceCourses();
        initBeginExecutionPeriodForDegreeCurricularPlan(getRoot(), beginExecutionYear
                .getExecutionPeriodForSemester(Integer.valueOf(1)));
        setCurricularStage(CurricularStage.APPROVED);
    }
    
    private void checkIfCurricularCoursesBelongToApprovedCompetenceCourses() {
        final List<String> approvedCompetenceCourses = new ArrayList<String>();
        for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class)) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
            if (! curricularCourse.isOptional() && ! curricularCourse.getCompetenceCourse().isApproved()) {
                approvedCompetenceCourses.add(
                        curricularCourse.getCompetenceCourse().getDepartmentUnit().getName()
                        + " > " + curricularCourse.getCompetenceCourse().getName());
            }
        }
        if (!approvedCompetenceCourses.isEmpty()) {
            final String[] result = new String[approvedCompetenceCourses.size()];
            throw new DomainException("error.not.all.competence.courses.are.approved", approvedCompetenceCourses.toArray(result));
        }
    }

    private void initBeginExecutionPeriodForDegreeCurricularPlan(final CourseGroup courseGroup,
            final ExecutionPeriod beginExecutionPeriod) {

        for (final CurricularRule curricularRule : courseGroup.getCurricularRules()) {
            curricularRule.setBegin(beginExecutionPeriod);
        }
        for (final Context context : courseGroup.getChildContexts()) {
            context.setBeginExecutionPeriod(beginExecutionPeriod);
            if (! context.getChildDegreeModule().isLeaf()) {
                initBeginExecutionPeriodForDegreeCurricularPlan((CourseGroup) context
                        .getChildDegreeModule(), beginExecutionPeriod);
            }
        }
    }

    private Boolean getCanBeDeleted() {
        return (getRoot().getCanBeDeleted()
                && !(hasAnyStudentCurricularPlans() || hasAnyCurricularCourseEquivalences()
                        || hasAnyEnrolmentPeriods() || hasAnyCurricularCourses()
                        || hasAnyExecutionDegrees() || hasAnyAreas()));
    }

    public void delete() {
        if (getCanBeDeleted()) {
            removeDegree();
            if (hasRoot()) {
                getRoot().delete();
            }
            if (hasDegreeStructure()) {
                getDegreeStructure().delete();
            }
            deleteDomainObject();
        } else
            throw new DomainException("error.degree.curricular.plan.cant.delete");
    }

    public String print() {
        if (!this.getCurricularStage().equals(CurricularStage.OLD)) {
            StringBuilder dcp = new StringBuilder();

            dcp.append("[DCP ").append(this.getIdInternal()).append("] ").append(this.getName()).append(
                    "\n");
            this.getRoot().print(dcp, "", null);

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
            if (executionDegree.getExecutionYear().equals(executionYear)) {
                return executionDegree;
            }
        }
        return null;
    }

    public List<CurricularCourse> getCurricularCoursesWithExecutionIn(ExecutionYear executionYear) {
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
        for (CurricularCourse curricularCourse : getCurricularCourses()) {
            for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
                List<ExecutionCourse> executionCourses = curricularCourse
                        .getExecutionCoursesByExecutionPeriod(executionPeriod);
                if(!executionCourses.isEmpty()){
                    curricularCourses.add(curricularCourse);
                    break;
                }
            }
        }
        return curricularCourses;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List<IEnrollmentRule> result = new ArrayList<IEnrollmentRule>();

        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
                executionPeriod));

        return result;
    }

    public List getCurricularCoursesFromArea(Branch area, AreaType areaType) {

        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();

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
    
    public List<CurricularCourseScope> getActiveCurricularCourseScopes(){
    	final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
    	for (final CurricularCourse course : getCurricularCourses()) {
			result.addAll(course.getActiveScopes());
		}
    	return result;
    }

    public List getSpecialListOfCurricularCourses() {
        return new ArrayList();
    }

    public List getAllOptionalCurricularCourseGroups() {
    	List<CurricularCourseGroup> result = new ArrayList<CurricularCourseGroup>();
    	for (Branch branch : this.getAreas()) {
			for (CurricularCourseGroup curricularCourseGroup : branch.getCurricularCourseGroups()) {
				if(curricularCourseGroup instanceof OptionalCurricularCourseGroup) {
					result.add(curricularCourseGroup);
				}
			}
		}
    	return result;
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
    
    public CourseGroup createCourseGroup(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        parentCourseGroup.checkDuplicateChildNames(name, nameEn);
        final CourseGroup courseGroup = new CourseGroup(name, nameEn);
        new Context(parentCourseGroup, courseGroup, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
        return courseGroup;
    }
    
    public CurricularCourse createCurricularCourse(Double weight, String prerequisites,
            String prerequisitesEn, CurricularStage curricularStage, CompetenceCourse competenceCourse,
            CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod) {

        checkIfPresentInDegreeCurricularPlan(competenceCourse, this);
        checkIfAnualBeginsInFirstPeriod(competenceCourse, curricularPeriod);
        return new CurricularCourse(weight, prerequisites, prerequisitesEn, curricularStage, competenceCourse,
                parentCourseGroup, curricularPeriod, beginExecutionPeriod);
    }

    public CurricularCourse createCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
            CurricularStage curricularStage, CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod) {
        return new CurricularCourse(parentCourseGroup, name, nameEn, curricularStage, curricularPeriod, beginExecutionPeriod);
    }

    private void checkIfPresentInDegreeCurricularPlan(final CompetenceCourse competenceCourse, final DegreeCurricularPlan degreeCurricularPlan) {
        final List<DegreeModule> curricularCoursesFromDegreeCurricularPlan = degreeCurricularPlan.getDcpDegreeModules(CurricularCourse.class);
        for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
            if (curricularCoursesFromDegreeCurricularPlan.contains(curricularCourse)) {
                throw new DomainException("competenceCourse.already.has.a.curricular.course.in.degree.curricular.plan");
            }
        }
    }
    
    private void checkIfAnualBeginsInFirstPeriod(final CompetenceCourse competenceCourse, final CurricularPeriod curricularPeriod) {
        if (competenceCourse.getRegime().equals(RegimeType.ANUAL) && (curricularPeriod.getOrder() == null || curricularPeriod.getOrder() != 1)) {
            throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }
    
    public List<DegreeModule> getDcpDegreeModules(Class<? extends DegreeModule> clazz) {
        final Set<DegreeModule> result = new HashSet<DegreeModule>();
        this.getRoot().collectChildDegreeModules(clazz, result);
        return new ArrayList<DegreeModule>(result);
    }
    
    public List<List<DegreeModule>> getDcpDegreeModulesIncludingFullPath(Class<? extends DegreeModule> clazz) {
        final List<List<DegreeModule>> result = new ArrayList<List<DegreeModule>>(); 
        this.getRoot().collectChildDegreeModulesIncludingFullPath(clazz, result, new ArrayList<DegreeModule>());
        return result;
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
        return this.getCurricularPlanMembersGroup().isMember(person) || person.hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void removeDegree() {
        super.removeDegree();
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setCurricularPlanMembersGroup(Group curricularPlanMembersGroup) {
        super.setCurricularPlanMembersGroup(curricularPlanMembersGroup);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setCurricularStage(CurricularStage curricularStage) {
        super.setCurricularStage(curricularStage);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setDegree(Degree degree) {
        super.setDegree(degree);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setRoot(CourseGroup courseGroup) {
        super.setRoot(courseGroup);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setDegreeStructure(CurricularPeriod degreeStructure) {
        super.setDegreeStructure(degreeStructure);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setGradeScale(GradeScale gradeScale) {
        super.setGradeScale(gradeScale);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setOjbConcreteClass(String ojbConcreteClass) {
        super.setOjbConcreteClass(ojbConcreteClass);
    }

    public String getPresentationName() {
        return getDegree().getPresentationName() + " " + getName();
    }
    
    
    // -------------------------------------------------------------
    // read static methods 
    // -------------------------------------------------------------
    
    public static List<DegreeCurricularPlan> readByCurricularStage(CurricularStage curricularStage){
    	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
    	for (DegreeCurricularPlan degreeCurricularPlan : RootDomainObject.getInstance().getDegreeCurricularPlans()) {
			if(degreeCurricularPlan.getCurricularStage().equals(curricularStage)) {
				result.add(degreeCurricularPlan);
			}
		}
    	return result;
    }
        
    public static List<DegreeCurricularPlan> readByDegreeTypeAndState(DegreeType degreeType, DegreeCurricularPlanState state) {
    	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
    	for (DegreeCurricularPlan degreeCurricularPlan : RootDomainObject.getInstance().getDegreeCurricularPlans()) {
    		if(!degreeCurricularPlan.getDegree().isBolonhaDegree()) {
    			if(degreeCurricularPlan.getDegree().getTipoCurso().equals(degreeType) && degreeCurricularPlan.getState().equals(state) && degreeCurricularPlan.getCurricularStage().equals(CurricularStage.OLD)) {
    				result.add(degreeCurricularPlan);
    			}
    		}
    	}
    	return result;
    }
    
 
}
