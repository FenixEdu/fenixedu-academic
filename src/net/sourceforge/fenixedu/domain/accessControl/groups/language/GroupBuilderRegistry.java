package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.accessControl.AllDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllMasterDegreesStudents;
import net.sourceforge.fenixedu.domain.accessControl.CurrentDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.CurricularCourseStudentsByExecutionPeriodGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreeTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentStudentsByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.DepartmentTeachersByExecutionYearGroup;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsiblesGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.IfTrueGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupBuilderNameTakenException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.NoSuchGroupBuilderException;

import org.apache.log4j.Logger;

import pt.ist.utl.fenix.utils.Pair;

/**
 * Keeps all the registered {@link GroupBuilder} by name.
 * 
 * @author cfgi
 */
public class GroupBuilderRegistry {

    private static final Logger logger = Logger.getLogger(GroupBuilderRegistry.class);

    private static final GroupBuilderRegistry instance = new GroupBuilderRegistry();

    private Map<String, Pair<Class, GroupBuilder>> builders;

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
        register("executionCourseTeachersAndStudents", ExecutionCourseTeachersAndStudentsGroup.class, new ExecutionCourseTeachersAndStudentsGroup.Builder());
        register("degreeStudents", DegreeStudentsGroup.class, new DegreeStudentsGroup.Builder());
        register("degreeTeachers", DegreeTeachersGroup.class, new DegreeTeachersGroup.Builder());
        register("departmentEmployeesByYear", DepartmentEmployeesByExecutionYearGroup.class, new DepartmentEmployeesByExecutionYearGroup.Builder());
        register("departmentStudentsByYear", DepartmentStudentsByExecutionYearGroup.class, new DepartmentStudentsByExecutionYearGroup.Builder());
        register("departmentTeachersByYear", DepartmentTeachersByExecutionYearGroup.class, new DepartmentTeachersByExecutionYearGroup.Builder());
        register("curricularCourseStudentsByPeriod", CurricularCourseStudentsByExecutionPeriodGroup.class, new CurricularCourseStudentsByExecutionPeriodGroup.Builder());
        register("ifTrue", IfTrueGroup.class, new IfTrueGroup.Builder());
        register("currentDegreeCoordinators", CurrentDegreeCoordinatorsGroup.class, new CurrentDegreeCoordinatorsGroup.Builder());
        register("departmentEmployees", DepartmentEmployeesGroup.class, new DepartmentEmployeesGroup.Builder());
        
        registerGroupsWithNoArguments();
    }

    private static void registerGroupsWithNoArguments() {
        Class[] groups = new Class[] {
                EveryoneGroup.class,
                NoOneGroup.class,
                MasterDegreeCoordinatorsGroup.class,
                DegreeCoordinatorsGroup.class,
                AllMasterDegreesStudents.class,
                AllDegreesStudentsGroup.class,
                InternalPersonGroup.class,
                ExecutionCourseResponsiblesGroup.class
        };
        
        for (Class groupClass : groups) {
            String className = groupClass.getSimpleName();
            
            String groupName;
            if (className.endsWith("Group")) { 
                groupName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length() - "Group".length());
            }
            else {
                groupName = className.substring(0, 1).toLowerCase() + className.substring(1);
            }
            
            register(groupName, groupClass, new NoArgumentsGroupBuilder(groupClass));
        }
    }

}
