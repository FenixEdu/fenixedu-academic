package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.LibraryCardSystem.HigherClearenceGroupBuilder;
import net.sourceforge.fenixedu.domain.ManagementGroups;
import net.sourceforge.fenixedu.domain.accessControl.ActiveAssistantCoordinatorCycle1Group;
import net.sourceforge.fenixedu.domain.accessControl.ActiveAssistantCoordinatorCycle2Group;
import net.sourceforge.fenixedu.domain.accessControl.ActiveAssistantCoordinatorCycle3Group;
import net.sourceforge.fenixedu.domain.accessControl.ActiveAssistantCoordinatorDFAGroup;
import net.sourceforge.fenixedu.domain.accessControl.ActiveAssistantCoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.ActiveAssistantCoordinatorIntegradedMasterDegreeGroup;
import net.sourceforge.fenixedu.domain.accessControl.ActiveCoordinatorCycle1Group;
import net.sourceforge.fenixedu.domain.accessControl.ActiveCoordinatorCycle2Group;
import net.sourceforge.fenixedu.domain.accessControl.ActiveCoordinatorCycle3Group;
import net.sourceforge.fenixedu.domain.accessControl.ActiveCoordinatorDFAGroup;
import net.sourceforge.fenixedu.domain.accessControl.ActiveCoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.ActiveCoordinatorIntegradedMasterDegreeGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllEmployeesByCampus;
import net.sourceforge.fenixedu.domain.accessControl.AllEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllFirstCycleStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllMasterDegreesStudents;
import net.sourceforge.fenixedu.domain.accessControl.AllResearchersGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllSecondCycleStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsByCampus;
import net.sourceforge.fenixedu.domain.accessControl.AllStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersByCampus;
import net.sourceforge.fenixedu.domain.accessControl.AllTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.AlumniDegreeGroup;
import net.sourceforge.fenixedu.domain.accessControl.CompetenceCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.ConclusionYearDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeScientificCommissionMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.CurricularCourseStudentsByExecutionPeriodGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeAllCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsCycleGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreesOfExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.DelegateCurricularCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DelegateStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DelegatesGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentStudentsByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentTeachersByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsiblesGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExternalTeachersForCurrentSemester;
import net.sourceforge.fenixedu.domain.accessControl.ExternalTeachersForCurrentYear;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupingGroup;
import net.sourceforge.fenixedu.domain.accessControl.IfTrueGroup;
import net.sourceforge.fenixedu.domain.accessControl.InstitutionSiteManagers;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.accessControl.NotUpdatedAlumniInfoForSpecificTimeGroup;
import net.sourceforge.fenixedu.domain.accessControl.PedagogicalCouncilMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonsInFunctionGroup;
import net.sourceforge.fenixedu.domain.accessControl.PhdProcessGuidingsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ProfessorshipsForCurrentSemester;
import net.sourceforge.fenixedu.domain.accessControl.ProfessorshipsForCurrentYear;
import net.sourceforge.fenixedu.domain.accessControl.ProjectDepartmentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.RegisteredAlumniGroup;
import net.sourceforge.fenixedu.domain.accessControl.ResearchUnitElementGroup;
import net.sourceforge.fenixedu.domain.accessControl.ResearchUnitMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ResearchersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ResponsibleProfessorshipsForCurrentSemester;
import net.sourceforge.fenixedu.domain.accessControl.ResponsibleProfessorshipsForCurrentYear;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.ScientificAreaMemberGroup;
import net.sourceforge.fenixedu.domain.accessControl.ScientificCouncilMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.SearchDegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeachersAndInstitutionSiteManagersGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeachersWithGradesToSubmit;
import net.sourceforge.fenixedu.domain.accessControl.TeachersWithMarkSheetsToConfirm;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitMembersGroup;
import net.sourceforge.fenixedu.domain.accessControl.VigilancyGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup.AcademicAuthorizationGroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.ActiveStudentsFromDegreeTypeGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.DepartmentAdministrativeOfficeGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.DepartmentPresidentGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.InternalOrExternalTeacherGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.StudentsByDegreeAndCurricularYear;
import net.sourceforge.fenixedu.domain.accessControl.groups.StudentsFromDegreeTypeGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupBuilderNameTakenException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.NoSuchGroupBuilderException;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryGroup;
import net.sourceforge.fenixedu.domain.research.AuthorGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.util.Pair;

/**
 * Keeps all the registered {@link GroupBuilder} by name.
 * 
 * @author cfgi
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public class GroupBuilderRegistry {

    private static final Logger logger = LoggerFactory.getLogger(GroupBuilderRegistry.class);

    private static final GroupBuilderRegistry instance = new GroupBuilderRegistry();

    private final Map<String, Pair<Class, GroupBuilder>> builders;

    private GroupBuilderRegistry() {
        this.builders = new Hashtable<String, Pair<Class, GroupBuilder>>();
    }

    /**
     * Registers a new group builder by name.
     * 
     * @param name
     *            the name of the builder
     * @param builder
     *            the builder implementation
     * 
     * @exception GroupBuilderNameTakenException
     *                if a group builder with the same name was already
     *                registered
     */
    public static void register(String name, Class groupClass, GroupBuilder builder) {
        if (instance.builders.containsKey(name)) {
            throw new GroupBuilderNameTakenException(name);
        }

        logger.debug("registering builder: " + name + " = " + builder);
        instance.builders.put(name, new Pair<Class, GroupBuilder>(groupClass, builder));
    }

    /**
     * Removes the group builder with the given name.
     * 
     * @param name
     *            the name of the group builder to remove
     */
    public static void unregister(String name) {
        instance.builders.remove(name);
    }

    /**
     * Obtains a group builder by name.
     * 
     * @param name
     *            the name of the group builder
     * @return the group builder registered under the given name
     * 
     * @exception NoSuchGroupBuilderException
     *                when there is no group builder registered with the given
     *                name
     */
    public static GroupBuilder getGroupBuilder(String name) {
        if (!instance.builders.containsKey(name)) {
            throw new NoSuchGroupBuilderException(name);
        }

        return instance.builders.get(name).getValue();
    }

    /**
     * Obtains the map of currently registered builders.
     * 
     * @return an unmodifiable map of builders
     */
    public static Map<String, Pair<Class, GroupBuilder>> getRegisteredBuilders() {
        return Collections.unmodifiableMap(instance.builders);
    }

    /**
     * Obtains the name of the first builder found that is associated with the
     * given group class. If there is more than one builder for each group class
     * then the behaviour of the method is not well defined.
     * 
     * @param groupClass
     *            the class of the group
     * @return the name through wich the group builder is recognized in the
     *         language
     */
    public static String getNameOfBuilder(Class groupClass) {
        for (Map.Entry<String, Pair<Class, GroupBuilder>> entry : instance.builders.entrySet()) {
            if (entry.getValue().getKey().equals(groupClass)) {
                return entry.getKey();
            }
        }

        return null;
    }

    //
    // register builders
    //

    static {
        register("role", RoleGroup.class, new RoleGroup.Builder());
        register("person", PersonGroup.class, new PersonGroup.Builder());
        register("custom", Group.class, new CustomGroupBuilder());
        register("fixed", FixedSetGroup.class, new FixedSetGroup.Builder());
        register("unitEmployees", UnitEmployeesGroup.class, new UnitEmployeesGroup.Builder());
        register("studentGroupStudents", StudentGroupStudentsGroup.class, new StudentGroupStudentsGroup.Builder());
        register("executionCourseStudents", ExecutionCourseStudentsGroup.class, new ExecutionCourseStudentsGroup.Builder());
        register("executionCourseTeachers", ExecutionCourseTeachersGroup.class, new ExecutionCourseTeachersGroup.Builder());
        register("executionCourseResponsibleTeachers", ExecutionCourseResponsibleTeachersGroup.class,
                new ExecutionCourseResponsibleTeachersGroup.Builder());
        register("executionCourseTeachersAndStudents", ExecutionCourseTeachersAndStudentsGroup.class,
                new ExecutionCourseTeachersAndStudentsGroup.Builder());
        register("degreesOfExecutionCourse", DegreesOfExecutionCourseGroup.class, new DegreesOfExecutionCourseGroup.Builder());
        register("competenceCourse", CompetenceCourseGroup.class, new CompetenceCourseGroup.Builder());
        register("degreeStudentsCycle", DegreeStudentsCycleGroup.class, new DegreeStudentsCycleGroup.Builder());
        register("degreeStudents", DegreeStudentsGroup.class, new DegreeStudentsGroup.Builder());
        register("degreeTeachers", DegreeTeachersGroup.class, new DegreeTeachersGroup.Builder());
        register("departmentEmployeesByYear", DepartmentEmployeesByExecutionYearGroup.class,
                new DepartmentEmployeesByExecutionYearGroup.Builder());
        register("departmentStudentsByYear", DepartmentStudentsByExecutionYearGroup.class,
                new DepartmentStudentsByExecutionYearGroup.Builder());
        register("departmentTeachersByYear", DepartmentTeachersByExecutionYearGroup.class,
                new DepartmentTeachersByExecutionYearGroup.Builder());
        register("curricularCourseStudentsByPeriod", CurricularCourseStudentsByExecutionPeriodGroup.class,
                new CurricularCourseStudentsByExecutionPeriodGroup.Builder());
        register("ifTrue", IfTrueGroup.class, new IfTrueGroup.Builder());
        register("currentDegreeCoordinators", CurrentDegreeCoordinatorsGroup.class, new CurrentDegreeCoordinatorsGroup.Builder());
        register("departmentEmployees", DepartmentEmployeesGroup.class, new DepartmentEmployeesGroup.Builder());
        register("thesisFileReaders", ThesisFileReadersGroup.class, new ThesisFileReadersGroup.Builder());
        register("researchers", ResearchersGroup.class, new ResearchersGroup.Builder());
        register("unitSiteManagers", WebSiteManagersGroup.class, new WebSiteManagersGroup.Builder());
        register("researchUnitMembers", ResearchUnitMembersGroup.class, new ResearchUnitMembersGroup.Builder());
        register("persistentGroup", PersistentGroup.class, new PersistentGroup.Builder());
        register("researchUnitElement", ResearchUnitElementGroup.class, new ResearchUnitElementGroup.Builder());
        register("personInFunction", PersonsInFunctionGroup.class, new PersonsInFunctionGroup.Builder());
        register("currentDegreeScientificComissionMembers", CurrentDegreeScientificCommissionMembersGroup.class,
                new CurrentDegreeScientificCommissionMembersGroup.Builder());
        register("assiduousnessManagers", ManagementGroups.class, new ManagementGroups.AssiduousnessManagerGroupBuilder());
        register("assiduousnessSectionStaff", ManagementGroups.class,
                new ManagementGroups.AssiduousnessSectionStaffGroupBuilder());
        register("scientificAreaMembers", ScientificAreaMemberGroup.class, new ScientificAreaMemberGroup.Builder());
        register("payrollSectionStaff", ManagementGroups.class, new ManagementGroups.PayrollSectionStaffGroupBuilder());
        register("allStudentsByCampus", AllStudentsByCampus.class, new AllStudentsByCampus.Builder());
        register("allTeachersByCampus", AllTeachersByCampus.class, new AllTeachersByCampus.Builder());
        register("allEmployeesByCampus", AllEmployeesByCampus.class, new AllEmployeesByCampus.Builder());
        register("studentsFromDegreeType", StudentsFromDegreeTypeGroup.class, new StudentsFromDegreeTypeGroup.Builder());
        register("delegates", DelegatesGroup.class, new DelegatesGroup.Builder());
        register("studentsByDegreeAndCurricularYear", StudentsByDegreeAndCurricularYear.class,
                new StudentsByDegreeAndCurricularYear.Builder());
        register("teachersWithMarkSheetsToConfirm", TeachersWithMarkSheetsToConfirm.class,
                new TeachersWithMarkSheetsToConfirm.Builder());
        register("teachersWithGradesToSubmit", TeachersWithGradesToSubmit.class, new TeachersWithGradesToSubmit.Builder());
        register("groupingGroup", GroupingGroup.class, new GroupingGroup.Builder());
        register("degreeAllCoordinatorsGroup", DegreeAllCoordinatorsGroup.class, new DegreeAllCoordinatorsGroup.Builder());
        register("delegateCurricularCourseStudentsGroup", DelegateCurricularCourseStudentsGroup.class,
                new DelegateCurricularCourseStudentsGroup.Builder());
        register("delegateStudentsGroup", DelegateStudentsGroup.class, new DelegateStudentsGroup.Builder());
        register("unitMembersGroup", UnitMembersGroup.class, new UnitMembersGroup.Builder());
        register("departmentPresidentGroup", DepartmentPresidentGroup.class, new DepartmentPresidentGroup.Builder());
        register("departmentAdministrativeOfficeGroup", DepartmentAdministrativeOfficeGroup.class,
                new DepartmentAdministrativeOfficeGroup.Builder());
        register("vigilancyGroup", VigilancyGroup.class, new VigilancyGroup.Builder());
        register("activeStudentsFromDegreeTypeGroup", ActiveStudentsFromDegreeTypeGroup.class,
                new ActiveStudentsFromDegreeTypeGroup.Builder());
        register("cerimonyInquiryGroup", CerimonyInquiryGroup.class, new CerimonyInquiryGroup.Builder());
        register("conclusionYearDegreesStudentsGroup", ConclusionYearDegreesStudentsGroup.class,
                new ConclusionYearDegreesStudentsGroup.Builder());
        register("notUpdatedAlumniInfoForSpecificTimeGroup", NotUpdatedAlumniInfoForSpecificTimeGroup.class,
                new NotUpdatedAlumniInfoForSpecificTimeGroup.Builder());
        register("alumniDegreeGroup", AlumniDegreeGroup.class, new AlumniDegreeGroup.Builder());
        register(HigherClearenceGroupBuilder.GROUP_EXPRESSION_NAME, FixedSetGroup.class, new HigherClearenceGroupBuilder());
        register("author", AuthorGroup.class, new AuthorGroup.AuthorGroupBuilder());
        register("projectDepartmentAccessGroup", ProjectDepartmentAccessGroup.class, new ProjectDepartmentAccessGroup.Builder());

        register("searchDegreeStudentsGroup", SearchDegreeStudentsGroup.class, new SearchDegreeStudentsGroup.Builder());
        register("academic", AcademicAuthorizationGroup.class, new AcademicAuthorizationGroupBuilder());
        register("phdProcessGuidingsGroup", PhdProcessGuidingsGroup.class, new PhdProcessGuidingsGroup.Builder());

        registerGroupsWithNoArguments();
    }

    private static void registerGroupsWithNoArguments() {
        Class[] groups =
                new Class[] { EveryoneGroup.class, NoOneGroup.class, MasterDegreeCoordinatorsGroup.class,
                        DegreeCoordinatorsGroup.class, AllMasterDegreesStudents.class, AllDegreesStudentsGroup.class,
                        InternalPersonGroup.class, ExecutionCourseResponsiblesGroup.class, InstitutionSiteManagers.class,
                        TeachersAndInstitutionSiteManagersGroup.class, PedagogicalCouncilMembersGroup.class,
                        ScientificCouncilMembersGroup.class, AllTeachersGroup.class, AllEmployeesGroup.class,
                        AllStudentsGroup.class, AllResearchersGroup.class, InternalOrExternalTeacherGroup.class,
                        AllSecondCycleStudentsGroup.class, AllFirstCycleStudentsGroup.class, ActiveCoordinatorGroup.class,
                        ActiveCoordinatorCycle1Group.class, ActiveCoordinatorCycle2Group.class,
                        ActiveCoordinatorCycle3Group.class, ActiveCoordinatorDFAGroup.class,
                        ActiveCoordinatorIntegradedMasterDegreeGroup.class, ActiveAssistantCoordinatorGroup.class,
                        ActiveAssistantCoordinatorCycle1Group.class, ActiveAssistantCoordinatorCycle2Group.class,
                        ActiveAssistantCoordinatorCycle3Group.class, ActiveAssistantCoordinatorDFAGroup.class,
                        ActiveAssistantCoordinatorIntegradedMasterDegreeGroup.class, RegisteredAlumniGroup.class,
                        ExternalTeachersForCurrentSemester.class, ExternalTeachersForCurrentYear.class,
                        ProfessorshipsForCurrentSemester.class, ProfessorshipsForCurrentYear.class,
                        ResponsibleProfessorshipsForCurrentSemester.class, ResponsibleProfessorshipsForCurrentYear.class };
        for (Class groupClass : groups) {
            String className = groupClass.getSimpleName();

            String groupName;
            if (className.endsWith("Group")) {
                groupName =
                        className.substring(0, 1).toLowerCase() + className.substring(1, className.length() - "Group".length());
            } else {
                groupName = className.substring(0, 1).toLowerCase() + className.substring(1);
            }

            register(groupName, groupClass, new NoArgumentsGroupBuilder(groupClass));
        }
    }

}
