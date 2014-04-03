package net.sourceforge.fenixedu.tasks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Alert;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group.InvalidGroupException;
import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;
import net.sourceforge.fenixedu.domain.accessControl.GroupIntersection;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.ConnectionManager;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.AnyoneGroup;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import edu.emory.mathcs.backport.java.util.Arrays;

@Task(englishTitle = "Group Tester", readOnly = true)
public class GroupTester extends CronTask {

    private Set<String> cache = new HashSet<>();

//    private static Map<Class<?>, String> tables = new HashMap<>();

    @Override
    public void runTask() throws Exception {
        convertLibraryCardSystem();
        convertDepartment();
        convertTSDProcess();
        convertAnnouncementBoard();
//        net.sourceforge.fenixedu.domain.PersonalGroup: concreteGroup (no instances found, and its a mess)
        convertInquiryResultComment();
        convertDegreeCurricularPlan();
        convertResourceAllocationRole();
        convertTeacherServiceDistribution();
        convertManagementGroups();
        convertFile();
        convertPhdCustomAlert();
        convertSpace();
        convertSender();
        convertRecipient();
        convertGroupAvailability();
//        
//        DomainModel model = FenixFramework.getDomainModel();
//        for (DomainClass type : model.getDomainClasses()) {
//            for (Slot slot : type.getSlotsList()) {
//                if (!slot.getSlotType().isBuiltin() && !slot.getSlotType().isEnum()) {
//                    Class<?> slotType = Class.forName(slot.getSlotType().getBaseType().getFullname());
//                    if (Group.class.isAssignableFrom(slotType)) {
//                        taskLog("%s: %s\n", type.getFullName(), slot.getName());
//                    }
//                }
//            }
//        }
    }

    private void convertLibraryCardSystem() {
//      net.sourceforge.fenixedu.domain.LibraryCardSystem: higherClearenceGroup
//      deleted
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertDepartment() {
//        net.sourceforge.fenixedu.domain.Department: competenceCourseMembersGroup
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            test(department.getCompetenceCourseMembersGroup());
        }
    }

    private void convertTSDProcess() {
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: automaticValuationGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: omissionConfigurationGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: phasesManagementGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: tsdCoursesAndTeachersManagementGroup
//      deleted
    }

    private void convertAnnouncementBoard() {
        for (List<String> chunk : chunkalhate(50, "CONTENT", ExecutionCourseAnnouncementBoard.class, UnitAnnouncementBoard.class)) {
            convertAnnouncementBoard(chunk);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertAnnouncementBoard(List<String> oids) {
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: managers
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: readers
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: writers
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: approvers
        for (String oid : oids) {
            AnnouncementBoard board = FenixFramework.getDomainObject(oid);
            test(board.getManagers());
            test(board.getReaders());
            test(board.getWriters());
            test(board.getApprovers());
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertSender() {
//      net.sourceforge.fenixedu.domain.util.email.Sender: members
        for (Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
            try {
                test(sender.getMembers());
            } catch (InvalidGroupException e) {
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertSpace() {
//      net.sourceforge.fenixedu.domain.space.Space: extensionOccupationsAccessGroup
//      net.sourceforge.fenixedu.domain.space.Space: genericEventOccupationsAccessGroup
//      net.sourceforge.fenixedu.domain.space.Space: lessonOccupationsAccessGroup
//      net.sourceforge.fenixedu.domain.space.Space: personOccupationsAccessGroup
//      net.sourceforge.fenixedu.domain.space.Space: spaceManagementAccessGroup
//      net.sourceforge.fenixedu.domain.space.Space: unitOccupationsAccessGroup
//      net.sourceforge.fenixedu.domain.space.Space: writtenEvaluationOccupationsAccessGroup
        for (Resource resource : Bennu.getInstance().getResourcesSet()) {
            if (resource instanceof Space) {
                Space space = (Space) resource;
                test(space.getExtensionOccupationsAccessGroup());
                test(space.getGenericEventOccupationsAccessGroup());
                test(space.getLessonOccupationsAccessGroup());
                test(space.getPersonOccupationsAccessGroup());
                test(space.getSpaceManagementAccessGroup());
                test(space.getUnitOccupationsAccessGroup());
                test(space.getWrittenEvaluationOccupationsAccessGroup());
            }
        }
    }

    private void convertInquiryResultComment() {
//      net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment: allowedToView
//      deleted
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertPhdCustomAlert() {
//      net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert: targetGroup
        for (Alert alert : Bennu.getInstance().getAlertsSet()) {
            if (alert instanceof PhdCustomAlert) {
                PhdCustomAlert phdAlert = (PhdCustomAlert) alert;
                test(phdAlert.getTargetGroup());
            }
        }
    }

    private void convertFile() {
        for (List<String> chunk : chunkalhate(50, "GENERIC_FILE")) {
            convertFile(chunk);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertFile(List<String> oids) {
//      net.sourceforge.fenixedu.domain.File: permittedGroup
        for (String oid : oids) {
            GenericFile genericFile = FenixFramework.getDomainObject(oid);
            if (genericFile instanceof File) {
                File file = (File) genericFile;
                try {
                    test(file.getPermittedGroup());
                } catch (InvalidGroupException e) {
                }
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertDegreeCurricularPlan() {
//      net.sourceforge.fenixedu.domain.DegreeCurricularPlan: curricularPlanMembersGroup
        for (DegreeCurricularPlan dcp : Bennu.getInstance().getDegreeCurricularPlansSet()) {
            test(dcp.getCurricularPlanMembersGroup());
        }
    }

    private void convertResourceAllocationRole() {
//      net.sourceforge.fenixedu.domain.ResourceAllocationRole: schedulesAccessGroup
//      net.sourceforge.fenixedu.domain.ResourceAllocationRole: spacesAccessGroup
//      net.sourceforge.fenixedu.domain.ResourceAllocationRole: materialsAccessGroup
//      net.sourceforge.fenixedu.domain.ResourceAllocationRole: vehiclesAccessGroup
//      deleted
    }

    private void convertTeacherServiceDistribution() {
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution: coursesManagementGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution: teachersManagementGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution: coursesValuationManagers
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution: teachersValuationManagers
//      deleted
    }

    private void convertRecipient() {
        for (List<String> chunk : chunkalhate(50, "RECIPIENT")) {
            convertRecipient(chunk);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertRecipient(List<String> oids) {
//      net.sourceforge.fenixedu.domain.util.email.Recipient: members
        for (String oid : oids) {
            Recipient recipient = FenixFramework.getDomainObject(oid);
            try {
                test(recipient.getMembers());
            } catch (InvalidGroupException e) {
            }
        }
    }

    private void convertGroupAvailability() {
//      net.sourceforge.fenixedu.domain.functionalities.GroupAvailability: targetGroup
//      deleted
    }

    private void convertManagementGroups() {
//      net.sourceforge.fenixedu.domain.ManagementGroups: assiduousnessManagers
//      net.sourceforge.fenixedu.domain.ManagementGroups: assiduousnessSectionStaff
//      net.sourceforge.fenixedu.domain.ManagementGroups: payrollSectionStaff
//      deleted
    }

    private List<List<String>> chunkalhate(int chunkSize, String table, Class<?>... types) {
        Connection connection = ConnectionManager.getCurrentSQLConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String query = "select OID from " + table;
            if (types.length != 0) {
                Function<Class<?>, String> function = new Function<Class<?>, String>() {
                    @Override
                    public String apply(Class<?> type) {
                        return "DOMAIN_CLASS_NAME = '" + type.getName() + "'";
                    }
                };
                query +=
                        " join FF$DOMAIN_CLASS_INFO on OID >> 32 = DOMAIN_CLASS_ID where "
                                + Joiner.on(" OR ").join(FluentIterable.from(Arrays.asList(types)).transform(function));
            }

            ResultSet rs = statement.executeQuery(query);
            List<String> oids = new ArrayList<>();
            while (rs.next()) {
                oids.add(rs.getString("OID"));
            }
            return Lists.partition(oids, chunkSize);
        } catch (SQLException e) {
            throw new Error(e);
        } finally {
            try {
                connection.close();
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new Error(e);
            }
        }
    }

    private void test(IGroup group) {
        if (group instanceof GroupUnion) {
            for (IGroup child : ((GroupUnion) group).getChildren()) {
                test(child);
            }
        } else if (group instanceof GroupIntersection) {
            for (IGroup child : ((GroupIntersection) group).getChildren()) {
                test(child);
            }
        } else if (group instanceof GroupDifference) {
            for (IGroup child : ((GroupDifference) group).getChildren()) {
                test(child);
            }
        } else {
            testPart(group);
        }
    }

    private void testPart(IGroup group) {
        if (group != null) {
            String currentExpression = group.getExpression();
            if (!cache.contains(currentExpression)) {
                cache.add(currentExpression);
                org.fenixedu.bennu.core.domain.groups.Group converted = group.convert();
                if (converted instanceof AnyoneGroup) {
                    return;
                }
                Set<User> current = peopleToUsers(group.getElements());
                Set<User> now = converted.getMembers();
                if (!current.equals(now)) {
                    taskLog("%s not equal to %s\n", currentExpression, converted.expression());
                }
            }
        }
    }

    private Set<User> peopleToUsers(Set<Person> people) {
        return FluentIterable.from(people).filter(new Predicate<Person>() {
            @Override
            public boolean apply(Person person) {
                return person.getUser() != null;
            }
        }).transform(new Function<Person, User>() {
            @Override
            public User apply(Person person) {
                return person.getUser();
            }
        }).toSet();
    }
}
