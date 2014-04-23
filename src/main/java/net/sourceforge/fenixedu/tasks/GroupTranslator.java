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
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.Group.InvalidGroupException;
import net.sourceforge.fenixedu.domain.accessControl.NoOneGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.BennuGroupBridge;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert;
import net.sourceforge.fenixedu.domain.resource.Resource;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.util.ConnectionManager;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.emory.mathcs.backport.java.util.Arrays;

@Task(englishTitle = "Group Translator", readOnly = true)
public class GroupTranslator extends CronTask {

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
        int counter = 0;
        for (Department department : Bennu.getInstance().getDepartmentsSet()) {
            try {
                department.setCompetenceCourseMembersGroup(convert(department.getCompetenceCourseMembersGroup()));
                counter++;
            } catch (AlreadyConverted e) {
            }
        }
        taskLog("Converted %d Department.competenceCourseMembersGroup\n", counter);
    }

    private void convertTSDProcess() {
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: automaticValuationGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: omissionConfigurationGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: phasesManagementGroup
//      net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess: tsdCoursesAndTeachersManagementGroup
//      deleted
    }

    private void convertAnnouncementBoard() {
        taskLog("Converting groups of AnnouncementBoard");
        int counter = 0;
        for (List<String> chunk : chunkalhate(50, "CONTENT", ExecutionCourseAnnouncementBoard.class, UnitAnnouncementBoard.class)) {
            counter += convertAnnouncementBoard(chunk);
            if (counter % 10000 == 0) {
                getTaskLogWriter().print("*");
                getTaskLogWriter().flush();
            }
        }
        taskLog("\nConverted %d groups of AnnouncementBoard\n", counter);
    }

    @Atomic(mode = TxMode.WRITE)
    private int convertAnnouncementBoard(List<String> oids) {
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: managers
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: readers
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: writers
//      net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard: approvers
        int counter = 0;
        for (String oid : oids) {
            AnnouncementBoard board = FenixFramework.getDomainObject(oid);
            try {
                board.setManagers(convert(board.getManagers()));
                counter++;
            } catch (AlreadyConverted e) {
            }
            try {
                board.setReaders(convert(board.getReaders()));
                counter++;
            } catch (AlreadyConverted e) {
            }
            try {
                board.setWriters(convert(board.getWriters()));
                counter++;
            } catch (AlreadyConverted e) {
            }
            try {
                board.setApprovers(convert(board.getApprovers()));
                counter++;
            } catch (AlreadyConverted e) {
            }
        }
        return counter;
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertSender() {
//      net.sourceforge.fenixedu.domain.util.email.Sender: members
        int counter = 0;
        Set<String> deleted = new HashSet<>();
        for (Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
            try {
                sender.setMembers(convert(sender.getMembers()));
                counter++;
            } catch (InvalidGroupException e) {
                deleteSender(sender);
                deleted.add(sender.getExternalId());
            } catch (AlreadyConverted e) {
            }
        }
        taskLog("Converted %d Sender.members, deleted %d invalid senders: %s\n", counter, deleted.size(),
                Joiner.on(',').join(deleted));
    }

    private void deleteSender(Sender sender) {
        sender.delete();
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
        int counter = 0;
        for (Resource resource : Bennu.getInstance().getResourcesSet()) {
            if (resource instanceof Space) {
                Space space = (Space) resource;
                try {
                    space.setExtensionOccupationsAccessGroup(convert(space.getExtensionOccupationsAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
                try {
                    space.setGenericEventOccupationsAccessGroup(convert(space.getGenericEventOccupationsAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
                try {
                    space.setLessonOccupationsAccessGroup(convert(space.getLessonOccupationsAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
                try {
                    space.setPersonOccupationsAccessGroup(convert(space.getPersonOccupationsAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
                try {
                    space.setSpaceManagementAccessGroup(convert(space.getSpaceManagementAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
                try {
                    space.setUnitOccupationsAccessGroup(convert(space.getUnitOccupationsAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
                try {
                    space.setWrittenEvaluationOccupationsAccessGroup(convert(space.getWrittenEvaluationOccupationsAccessGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
            }
        }
        taskLog("Converted %d groups of Space\n", counter);
    }

    private void convertInquiryResultComment() {
//      net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment: allowedToView
//      deleted
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertPhdCustomAlert() {
//      net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert: targetGroup
        int counter = 0;
        for (Alert alert : Bennu.getInstance().getAlertsSet()) {
            if (alert instanceof PhdCustomAlert) {
                PhdCustomAlert phdAlert = (PhdCustomAlert) alert;
                try {
                    phdAlert.setTargetGroupWithoutCheckingPermissions(convert(phdAlert.getTargetGroup()));
                    counter++;
                } catch (AlreadyConverted e) {
                }
            }
        }
        taskLog("Converted %d PhdCustomAlert.targetGroup\n", counter);
    }

    private void convertFile() {
        taskLog("Converting File.permittedGroup");
        int counter = 0;
        for (List<String> chunk : chunkalhate(50, "GENERIC_FILE")) {
            counter += convertFile(chunk);
            if (counter % 10000 == 0) {
                getTaskLogWriter().print("*");
                getTaskLogWriter().flush();
            }
        }
        taskLog("\nConverted %d File.permittedGroup\n", counter);
    }

    @Atomic(mode = TxMode.WRITE)
    private int convertFile(List<String> oids) {
//      net.sourceforge.fenixedu.domain.File: permittedGroup
        int counter = 0;
        for (String oid : oids) {
            GenericFile genericFile = FenixFramework.getDomainObject(oid);
            if (genericFile instanceof File) {
                File file = (File) genericFile;
                try {
                    try {
                        file.setPermittedGroup(convert(file.getPermittedGroup()));
                        counter++;
                    } catch (InvalidGroupException e) {
                        file.setPermittedGroup(convert(new NoOneGroup()));
                        counter++;
                    }
                } catch (AlreadyConverted e) {
                }
            }
        }
        return counter;
    }

    @Atomic(mode = TxMode.WRITE)
    private void convertDegreeCurricularPlan() {
//      net.sourceforge.fenixedu.domain.DegreeCurricularPlan: curricularPlanMembersGroup
        int counter = 0;
        for (DegreeCurricularPlan dcp : Bennu.getInstance().getDegreeCurricularPlansSet()) {
            try {
                dcp.setCurricularPlanMembersGroupWithoutCheckingPermissions(convert(dcp.getCurricularPlanMembersGroup()));
                counter++;
            } catch (AlreadyConverted e) {
            }
        }
        taskLog("Converted %d DegreeCurricularPlan.curricularPlanMembersGroup\n", counter);
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
        taskLog("Converting Recipient.members");
        int counter = 0;
        Set<String> deleted = new HashSet<>();
        for (List<String> chunk : chunkalhate(50, "RECIPIENT")) {
            ImmutableList<Object> counters = convertRecipient(chunk);
            counter += (Integer) counters.get(0);
            deleted.addAll((Set<String>) counters.get(1));
            if (counter % 10000 == 0) {
                getTaskLogWriter().print("*");
                getTaskLogWriter().flush();
            }
        }
        taskLog("\nConverted %d Recipient.members, deleted %d invalid recipients: %s\n", counter, deleted.size(), Joiner.on(',')
                .join(deleted));
    }

    @Atomic(mode = TxMode.WRITE)
    private ImmutableList<Object> convertRecipient(List<String> oids) {
//      net.sourceforge.fenixedu.domain.util.email.Recipient: members
        int counter = 0;
        Set<String> deleted = new HashSet<>();
        for (String oid : oids) {
            Recipient recipient = FenixFramework.getDomainObject(oid);
            try {
                recipient.setMembers(convert(recipient.getMembers()));
                counter++;
            } catch (InvalidGroupException e) {
                deleteRecipient(recipient);
                deleted.add(recipient.getExternalId());
            } catch (AlreadyConverted e) {
            }
        }
        return ImmutableList.of(counter, deleted);
    }

    private void deleteRecipient(Recipient recipient) {
        recipient.delete();
    }

    private void convertGroupAvailability() {
//      net.sourceforge.fenixedu.domain.functionalities.GroupAvailability: targetGroup
        taskLog("Converting GroupAvailablity.members");
        int counter = 0;
        for (List<String> chunk : chunkalhate(50, "CONTENT", Section.class, Item.class)) {
            counter += convertContents(chunk);
            if (counter % 5000 == 0) {
                getTaskLogWriter().print("*");
                getTaskLogWriter().flush();
            }
        }
        taskLog("\nConverted %s GroupAvailabilities\n", counter);
    }

    @Atomic(mode = TxMode.WRITE)
    private int convertContents(List<String> oids) {
//      net.sourceforge.fenixedu.domain.functionalities.GroupAvailability: targetGroup
        int counter = 0;
        for (String oid : oids) {
            Content content = FenixFramework.getDomainObject(oid);
            try {
                if (content.getOwnAvailabilityPolicy() != null) {
                    GroupAvailability group = (GroupAvailability) content.getOwnAvailabilityPolicy();
                    try {
                        group.setTargetGroup(convert(group.getTargetGroup()));
                    } catch (InvalidGroupException e) {
                        taskLog("Invalid group for content %s\n", oid);
                        group.setTargetGroup(convert(new NoOneGroup()));
                    }
                }
                counter++;
            } catch (AlreadyConverted e) {
            }
        }
        return counter;
    }

    private void convertManagementGroups() {
//      net.sourceforge.fenixedu.domain.ManagementGroups: assiduousnessManagers
//      net.sourceforge.fenixedu.domain.ManagementGroups: assiduousnessSectionStaff
//      net.sourceforge.fenixedu.domain.ManagementGroups: payrollSectionStaff
//      deleted
    }

    private class AlreadyConverted extends Exception {
        public AlreadyConverted() {
            super();
        }

        public AlreadyConverted(UnsupportedOperationException e) {
            super(e);
        }
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

    private BennuGroupBridge convert(Group group) throws AlreadyConverted {
        if (group != null && !(group instanceof BennuGroupBridge)) {
            return new BennuGroupBridge(group.convert());
        }
        throw new AlreadyConverted();
    }
}
