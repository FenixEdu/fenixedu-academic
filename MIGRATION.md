# Migration

This file contains the steps required to update from one version to another. The following sections represent the steps required to update from the previous to that version. 


## Migrating from 2.2.x to 3.0

FenixEdu 3.0 brings a series of changes on the underlying infrastructure. Fenix Groups have been replaced by Bennu Groups, and the CMS has been replaced, along with the functionality tree (which is now managed by Bennu Portal).

Note that as there isn't a one-to-one relationship between Bennu Portal's functionalities and the old Fenix functionalities, there is no migration steps, existing Menu data will be erased and must be re-created manually (or using a script specific to your institution).

### Pre-Migration

Note that much of the pre-migration involves exporting data, and as such, data generated after the export will not be migrated. We advise temporarily disabling access to the application while the scripts are run.

Before migrating to version 3.0, you must first ensure that:

1. The `GroupTranslator` task has been run, and replaced every group instance in the system.
2. There are no remaining files on DSpace, as its support has been removed.
3. You have a new theme (see Bennu Portal's documentation) for your institution, or that you are happy using the default theme.
4. Run the following task to export all existing FunctionalityCalls on sites:
    ```java
    package pt.ist.fenix;

    import java.sql.Connection;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.util.ArrayList;
    import java.util.List;

    import net.sourceforge.fenixedu.domain.Site;
    import net.sourceforge.fenixedu.domain.contents.Container;
    import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
    import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
    import net.sourceforge.fenixedu.domain.contents.Node;
    import net.sourceforge.fenixedu.util.ConnectionManager;

    import org.fenixedu.bennu.scheduler.custom.CustomTask;

    import pt.ist.fenixframework.Atomic.TxMode;
    import pt.ist.fenixframework.DomainModelUtil;
    import pt.ist.fenixframework.FenixFramework;

    import com.google.gson.JsonArray;
    import com.google.gson.JsonObject;

    public class ExportFunctionalityCalls extends CustomTask {

        private int calls = 0;

        @Override
        public void runTask() throws Exception {
            int sites = 0;
            JsonArray json = new JsonArray();
            for (String id : getSiteIds()) {
                sites++;
                Container container = FenixFramework.getDomainObject(id);
                add(container, json);
                if (sites % 1000 == 0) {
                    taskLog("Processed %s sites\n", sites);
                }
            }
            taskLog("Done. Exported %s calls\n", calls);
            output("calls.json", json.toString().getBytes());
        }

        private void add(Container container, JsonArray json) {
            for (Node node : container.getChildrenSet()) {
                if (node.getChild() instanceof FunctionalityCall) {
                    calls++;
                    JsonObject obj = new JsonObject();
                    obj.addProperty("parent", node.getParent().getExternalId());
                    obj.addProperty("order", ((ExplicitOrderNode) node).getNodeOrder());
                    obj.addProperty("path", ((FunctionalityCall) node.getChild()).getFunctionality().getPath());
                    json.add(obj);
                } else if (node.getChild() instanceof Container) {
                    add((Container) node.getChild(), json);
                }
            }
        }

        private List<String> getSiteIds() throws SQLException {
            List<String> ids = new ArrayList<>();
            Connection conn = ConnectionManager.getCurrentSQLConnection();
            StringBuilder builder = new StringBuilder();
            for (Class<?> clazz : DomainModelUtil.getDomainClassHierarchy(Site.class, true)) {
                if (builder.length() > 0) {
                    builder.append(',');
                }
                builder.append("'").append(clazz.getName()).append("'");
            }
            String query =
                    "SELECT OID FROM CONTENT "
                            + "WHERE OID >> 32 IN (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME IN ("
                            + builder.toString() + "))";
            taskLog("Running query: '%s'\n", query);
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    ids.add(rs.getString("OID"));
                }
                rs.close();
            }
            taskLog("Returning %s sites\n", ids.size());
            return ids;
        }

        @Override
        public TxMode getTxMode() {
            return TxMode.READ;
        }

    }
    ```
5. Run the following task to remove the RESOURCE_ALLOCATION_MANAGER role to those without permissions.
    ```java
    package net.sourceforge.fenixedu;

    import net.sourceforge.fenixedu.domain.Person;
    import net.sourceforge.fenixedu.domain.ResourceAllocationRole;
    import net.sourceforge.fenixedu.domain.Role;
    import net.sourceforge.fenixedu.domain.person.RoleType;

    import org.fenixedu.bennu.scheduler.custom.CustomTask;

    public class FixResourceAllocationManagerRole extends CustomTask {

        @Override
        public void runTask() throws Exception {
            for (Person person : Role.getRoleByRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER).getAssociatedPersonsSet()) {
                if (!ResourceAllocationRole.personHasPermissionToManageSchedulesAllocation(person)) {
                    person.removeRoleByType(RoleType.RESOURCE_ALLOCATION_MANAGER);
                    taskLog("Removed role for user %s\n", person.getUsername());
                }
            }
        }

    }
    ```

6. Run export tasks for fenixedu-spaces

Please run [SpaceMigrationTask.java](https://raw.githubusercontent.com/sergiofbsilva/fenixedu-spaces-export/master/src/main/java/org/fenixedu/spaces/migration/SpaceMigrationTask.java) and save the generated json files.

### Migration

1. Ensure all the files exported in the Pre-Migration step are accessible, you will need them later.

2. Shut down your application.

3. Run the following SQL which will remove invalid data, and upgrade your database schema:
    ```sql
    -- Run bennu migration to version 3.0.0
    rename table `GROUP` to `PERSISTENT_GROUP`;
    alter table `PERSISTENT_GROUP` add `OID_FIRST` bigint unsigned, add index (OID_FIRST), add `STRATEGY` text;

    create table `INTERSECTION_GROUP_COMPOSITION` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_PERSISTENT_INTERSECTION_GROUP` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_PERSISTENT_INTERSECTION_GROUP), index (OID_PERSISTENT_GROUP), index (OID_PERSISTENT_INTERSECTION_GROUP)) ENGINE=InnoDB, character set utf8;
    insert into INTERSECTION_GROUP_COMPOSITION (OID_PERSISTENT_GROUP, OID_PERSISTENT_INTERSECTION_GROUP)
        select OID_GROUP, OID_COMPOSITION_GROUP
        from GROUP_COMPOSITION
        join FF$DOMAIN_CLASS_INFO on OID_COMPOSITION_GROUP >> 32 = DOMAIN_CLASS_ID
        where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.IntersectionGroup';

    create table `UNION_GROUP_COMPOSITION` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_PERSISTENT_UNION_GROUP` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_PERSISTENT_UNION_GROUP), index (OID_PERSISTENT_GROUP), index (OID_PERSISTENT_UNION_GROUP)) ENGINE=InnoDB, character set utf8;
    insert into UNION_GROUP_COMPOSITION (OID_PERSISTENT_GROUP, OID_PERSISTENT_UNION_GROUP)
        select OID_GROUP, OID_COMPOSITION_GROUP
        from GROUP_COMPOSITION
        join FF$DOMAIN_CLASS_INFO on OID_COMPOSITION_GROUP >> 32 = DOMAIN_CLASS_ID
        where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.UnionGroup';

    create table `DIFFERENCE_GROUP_REST` (`OID_PERSISTENT_GROUP` bigint unsigned, `OID_PERSISTENT_DIFFERENCE_GROUP` bigint unsigned, primary key (OID_PERSISTENT_GROUP, OID_PERSISTENT_DIFFERENCE_GROUP), index (OID_PERSISTENT_GROUP), index (OID_PERSISTENT_DIFFERENCE_GROUP)) ENGINE=InnoDB, character set utf8;
    insert into DIFFERENCE_GROUP_REST (OID_PERSISTENT_GROUP, OID_PERSISTENT_DIFFERENCE_GROUP)
        select OID_GROUP, OID_COMPOSITION_GROUP
        from GROUP_COMPOSITION
        join FF$DOMAIN_CLASS_INFO on OID_COMPOSITION_GROUP >> 32 = DOMAIN_CLASS_ID
        where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.DifferenceGroup';

    alter table `USER_GROUP_MEMBERS` change `OID_USER_GROUP` `OID_PERSISTENT_USER_GROUP` bigint unsigned;

    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.Group';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentUserGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.UserGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentUnionGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.UnionGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentIntersectionGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.IntersectionGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentDifferenceGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.DifferenceGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentDynamicGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.DynamicGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentNegationGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.NegationGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentNobodyGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.NobodyGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentAnyoneGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.AnyoneGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentAnonymousGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.AnonymousGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.PersistentLoggedGroup' where DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.groups.LoggedGroup';

    alter table `FILE_SUPPORT` add `OID_DEFAULT_STORAGE` bigint unsigned;
    alter table `FILE_STORAGE` add `OID_FILE_SUPPORT_AS_DEFAULT` bigint unsigned;
    update FILE_STORAGE set OID_FILE_SUPPORT_AS_DEFAULT = (select OID from FILE_SUPPORT) where PATH is not null and PATH not like '%tmp%' limit 1;
    update FILE_SUPPORT set OID_DEFAULT_STORAGE = (select OID from FILE_STORAGE where OID_FILE_SUPPORT_AS_DEFAULT is not null);

    -- Replace the instance of ResourceAllocation role with a regular Role Instance
    set @xpto = (select OID >> 32 from ROLE where ROLE_TYPE = 'PERSON');
    set @oldOid = (select OID from ROLE where ROLE_TYPE = 'RESOURCE_ALLOCATION_MANAGER');
    update ROLE set OID = ((select @xpto) << 32) + ID_INTERNAL where OID = (select @oldOid);
    set @newOid = (select OID from ROLE where ROLE_TYPE = 'RESOURCE_ALLOCATION_MANAGER');
    update `PERSISTENT_GROUP` set OID_ROLE = (select @newOid) where OID_ROLE = (select @oldOid);
    update PERSON_ROLE set OID_ROLE = (select @newOid) where OID_ROLE = (select @oldOid);
    update ROLE_OPERATION_LOG set OID_ROLE = (select @newOid) where OID_ROLE = (select @oldOid);

    -- Handling the rename of Instalation to Installation
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Installation' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Instalation';
    RENAME TABLE INSTALATION to INSTALLATION;
    alter table BENNU change OID_INSTALATION OID_INSTALLATION bigint unsigned;

    -- Removing DSpace storage
    delete from FILE_STORAGE where OID = (SELECT OID_D_SPACE_FILE_STORAGE from BENNU);

    -- Removing Old Portal Data
    DROP TABLE MENU_ITEM;
    DROP TABLE PORTAL_CONFIGURATION;
    UPDATE BENNU SET OID_CONFIGURATION = NULL;

    -- Removing instances of deleted QueueJob and File Subclasses
    DELETE QUEUE_JOB, GENERIC_FILE from QUEUE_JOB left join GENERIC_FILE on GENERIC_FILE.OID = QUEUE_JOB.OID_FILE join FF$DOMAIN_CLASS_INFO on QUEUE_JOB.OID >> 32 = DOMAIN_CLASS_ID where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.reports.PublicationReportFile'\G
    DELETE FROM GENERIC_FILE where OID >> 32 in (SELECT DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME in ('net.sourceforge.fenixedu.domain.documents.LibraryMissingCardsDocument', 'net.sourceforge.fenixedu.domain.documents.LibraryMissingLettersDocument', 'net.sourceforge.fenixedu.domain.research.result.ResearchResultDocumentFile'));

    -- Creating tables for Content subclasses (Site, Forum, Announcement, AnnouncementBoard, etc)
    alter table `DEPARTMENT` add `OID_FORUM` bigint unsigned;
    create table `SITE_TEMPLATE` (`ID_INTERNAL` int(11) NOT NULL auto_increment, `CONTROLLER` text, `OID_BENNU` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID` bigint unsigned, `OID_FUNCTIONALITY` bigint unsigned, primary key (ID_INTERNAL), index (OID_BENNU), index (OID)) ENGINE=InnoDB, character set utf8;
    create table `CONVERSATION_THREAD` (`ID_INTERNAL` int(11) NOT NULL auto_increment, `OID_DOMAIN_META_OBJECT` bigint unsigned, `CREATION_DATE` timestamp NULL default NULL, `TITLE` text, `OID` bigint unsigned, `OID_FORUM` bigint unsigned, `OID_CREATOR` bigint unsigned, primary key (ID_INTERNAL), index (OID_FORUM), index (OID), index (OID_CREATOR)) ENGINE=InnoDB, character set utf8;
    create table `ANNOUNCEMENT` (`EDITOR_NOTES` text, `PHOTO_URL` text, `PUBLICATION` tinyint(1), `PRIORITY` int(11), `AUTHOR` text, `STICKY` tinyint(1), `VISIBLE` tinyint(1), `OID_ANNOUNCEMENT_BOARD` bigint unsigned, `PUBLICATION_END` timestamp NULL default NULL, `PRESS_RELEASE` tinyint(1), `OID_DOMAIN_META_OBJECT` bigint unsigned, `REFERED_SUBJECT_BEGIN` timestamp NULL default NULL, `ID_INTERNAL` int(11) NOT NULL auto_increment, `AUTHOR_EMAIL` text, `EXCERPT` text, `CREATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `REFERED_SUBJECT_END` timestamp NULL default NULL, `OID_CREATOR` bigint unsigned, `PLACE` text, `PUBLICATION_BEGIN` timestamp NULL default NULL, `OID_CAMPUS` bigint unsigned, `LAST_MODIFICATION` timestamp NULL default NULL, `SUBJECT` text, `APPROVED` tinyint(1), `BODY` longtext, `KEYWORDS` text, primary key (ID_INTERNAL), index (OID_ANNOUNCEMENT_BOARD), index (OID), index (OID_CREATOR), index (OID_CAMPUS)) ENGINE=InnoDB, character set utf8;
    create table `CONVERSATION_MESSAGE` (`ID_INTERNAL` int(11) NOT NULL auto_increment, `OID_DOMAIN_META_OBJECT` bigint unsigned, `CREATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `OID_CREATOR` bigint unsigned, `BODY` longtext, `OID_CONVERSATION_THREAD` bigint unsigned, primary key (ID_INTERNAL), index (OID), index (OID_CREATOR), index (OID_CONVERSATION_THREAD)) ENGINE=InnoDB, character set utf8;
    create table `FORUM` (`OID_EXECUTION_COURSE` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, `OID_DEPARTMENT` bigint unsigned, `DESCRIPTION` text, `OID_DOMAIN_META_OBJECT` bigint unsigned, `CREATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `OID_CREATOR` bigint unsigned, `NAME` text, primary key (ID_INTERNAL), index (OID_EXECUTION_COURSE), index (OID), index (OID_CREATOR)) ENGINE=InnoDB, character set utf8;
    alter table `GENERIC_FILE` add `OID_CMS_CONTENT` bigint unsigned, add `OID_ANNOUNCEMENT_BOARD` bigint unsigned, add index (OID_CMS_CONTENT), add index (OID_ANNOUNCEMENT_BOARD), change PERMITTED_GROUP OID_ACCESS_GROUP bigint unsigned, add `OID_GROUP` bigint unsigned, add `OID_INFORMATION` bigint unsigned, add index (OID_ACCESS_GROUP), add index (OID_GROUP);
    create table `SITE` (`RESEARCH_UNIT_HOMEPAGE` text, `SHOW_CATEGORY` tinyint(1), `OID_SITE_EXECUTION_COURSE` bigint unsigned, `DYNAMIC_MAIL_DISTRIBUTION` tinyint(1), `PERSONALIZED_LOGO` tinyint(1), `SHOW_BANNER` tinyint(1), `OID_ROOT_FROM_TEACHER_EVALUATION_COUNCIL` bigint unsigned, `MAIL` text, `ALTERNATIVE_SITE` text, `SHOW_CURRENT_EXECUTION_COURSES` tinyint(1), `SHOW_INSTITUTION_LOGO` tinyint(1), `ID_INTERNAL` int(11) NOT NULL auto_increment, `STYLE` text, `SIDE_BANNER` text, `SHOW_PATENTS` tinyint(1), `OID` bigint unsigned, `OID_LOGO` bigint unsigned, `SHOW_INTRODUCTION` tinyint(1), `SHOW_PRIZES` tinyint(1), `SHOW_PARTICIPATIONS` tinyint(1), `SHOW_CURRENT_ATTENDING_EXECUTION_COURSES` tinyint(1), `SHOW_INTERESTS` tinyint(1), `SHOW_PHOTO` tinyint(1), `SHOW_FLAGS` tinyint(1), `SHOW_RESEARCH_UNIT_HOMEPAGE` tinyint(1), `LAYOUT` text, `OID_BENNU` bigint unsigned, `SHOW_ANNOUNCEMENTS` tinyint(1), `INITIAL_STATEMENT` text, `GOOGLE_ANALYTICS_CODE` text, `SHOW_ALTERNATIVE_HOMEPAGE` tinyint(1), `DESCRIPTION` text, `SHOW_WORK_TELEPHONE` tinyint(1), `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_THESIS` bigint unsigned, `SHOW_ALUMNI_DEGREES` tinyint(1), `LESSON_PLANNING_AVAILABLE` tinyint(1), `SHOW_MOBILE_TELEPHONE` tinyint(1), `RESEARCH_UNIT` text, `OID_DEGREE` bigint unsigned, `SHOW_EVENTS` tinyint(1), `SHOW_EMAIL` tinyint(1), `SHOW_PUBLICATIONS` tinyint(1), `OID_PERSON` bigint unsigned, `SHOW_UNIT` tinyint(1), `OID_UNIT` bigint unsigned, `SHOW_TELEPHONE` tinyint(1), `ACTIVATED` tinyint(1), `INTRODUCTION` text, `SHOW_ACTIVE_STUDENT_CURRICULAR_PLANS` tinyint(1), `OID_MANAGERS_OF_UNIT_SITE_GROUP` bigint unsigned, primary key (ID_INTERNAL), index (OID_BENNU), index (OID)) ENGINE=InnoDB, character set utf8;
    create table `CMS_CONTENT` (`ID_INTERNAL` int(11) NOT NULL auto_increment, `OID_SECTION_TEMPLATE` bigint unsigned, `NORMALIZED_NAME` text, `CREATION_DATE` timestamp NULL default NULL, `SHOW_NAME` tinyint(1), `OID_GROUP` bigint unsigned, `OID` bigint unsigned, `NAME` text, `VISIBLE` tinyint(1), `ORDER` int(11), `OID_SITE` bigint unsigned, `CUSTOM_PATH` text, `OID_PARENT` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `MODIFICATION_DATE` timestamp NULL default NULL, `ENABLED` tinyint(1), `BODY` longtext, `SHOW_SUB_SECTIONS` tinyint(1), `OID_TEMPLATE` bigint unsigned, primary key (ID_INTERNAL), index (OID_SITE), index (OID_SECTION_TEMPLATE), index (OID_PARENT), index (OID_GROUP), index (OID), index (OID_TEMPLATE)) ENGINE=InnoDB, character set utf8;
    create table `ANNOUNCEMENT_BOARD` (`OID_EXECUTION_COURSE` bigint unsigned, `OID_READERS_GROUP` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, `OID_BENNU` bigint unsigned, `CREATION_DATE` timestamp NULL default NULL, `OID` bigint unsigned, `UNIT_PERMITTED_MANAGEMENT_GROUP_TYPE` text, `NAME` text, `UNIT_PERMITTED_WRITE_GROUP_TYPE` text, `OID_APPROVERS_GROUP` bigint unsigned, `EXECUTION_COURSE_PERMITTED_MANAGEMENT_GROUP_TYPE` text, `MANDATORY` tinyint(1), `OID_UNIT` bigint unsigned, `EXECUTION_COURSE_PERMITTED_READ_GROUP_TYPE` text, `OID_WRITERS_GROUP` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_MANAGERS_GROUP` bigint unsigned, `EXECUTION_COURSE_PERMITTED_WRITE_GROUP_TYPE` text, `UNIT_PERMITTED_READ_GROUP_TYPE` text, primary key (ID_INTERNAL), index (OID_UNIT), index (OID_BENNU), index (OID)) ENGINE=InnoDB, character set utf8;

    -- Migrating Announcements
    insert into ANNOUNCEMENT_BOARD (ID_INTERNAL, OID_EXECUTION_COURSE, OID_READERS_GROUP, OID_BENNU, CREATION_DATE, OID, UNIT_PERMITTED_MANAGEMENT_GROUP_TYPE, NAME, UNIT_PERMITTED_WRITE_GROUP_TYPE, OID_APPROVERS_GROUP, EXECUTION_COURSE_PERMITTED_MANAGEMENT_GROUP_TYPE, MANDATORY, OID_UNIT, EXECUTION_COURSE_PERMITTED_READ_GROUP_TYPE, OID_WRITERS_GROUP, OID_MANAGERS_GROUP, EXECUTION_COURSE_PERMITTED_WRITE_GROUP_TYPE, UNIT_PERMITTED_READ_GROUP_TYPE) select ID_INTERNAL, OID_EXECUTION_COURSE, READERS, OID_ROOT_DOMAIN_OBJECT, CREATION_DATE, OID, UNIT_PERMITTED_MANAGEMENT_GROUP_TYPE, NAME, UNIT_PERMITTED_WRITE_GROUP_TYPE, APPROVERS, EXECUTION_COURSE_PERMITTED_MANAGEMENT_GROUP_TYPE, MANDATORY, OID_PARTY, EXECUTION_COURSE_PERMITTED_READ_GROUP_TYPE, WRITERS, MANAGERS, EXECUTION_COURSE_PERMITTED_WRITE_GROUP_TYPE, UNIT_PERMITTED_READ_GROUP_TYPE from CONTENT where OID >> 32 in (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like '%AnnouncementBoard');

    insert into ANNOUNCEMENT (ID_INTERNAL, EDITOR_NOTES, PHOTO_URL, PUBLICATION, PRIORITY, AUTHOR, STICKY, VISIBLE, PUBLICATION_END, PRESS_RELEASE, REFERED_SUBJECT_BEGIN, AUTHOR_EMAIL, EXCERPT, CREATION_DATE, OID,REFERED_SUBJECT_END, OID_CREATOR, PLACE, PUBLICATION_BEGIN, OID_CAMPUS, LAST_MODIFICATION, SUBJECT, APPROVED, BODY, KEYWORDS) select ID_INTERNAL, EDITOR_NOTES, PHOTO_URL, PUBLICATION, PRIORITY, AUTHOR, STICKY, VISIBLE, PUBLICATION_END, PRESS_RELEASE, REFERED_SUBJECT_BEGIN, AUTHOR_EMAIL, EXCERPT, CREATION_DATE, OID,REFERED_SUBJECT_END, OID_CREATOR, PLACE, PUBLICATION_BEGIN, OID_CAMPUS, LAST_MODIFICATION, SUBJECT, APPROVED, BODY, KEYWORDS from CONTENT where OID >> 32 = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like '%Announcement');
    update ANNOUNCEMENT join NODE on ANNOUNCEMENT.OID = NODE.OID_CHILD set OID_ANNOUNCEMENT_BOARD = OID_PARENT;
    update GENERIC_FILE join CONTENT on GENERIC_FILE.OID = CONTENT.OID_FILE join NODE on CONTENT.OID = NODE.OID_CHILD set GENERIC_FILE.OID_ANNOUNCEMENT_BOARD = NODE.OID_PARENT where NODE.OID_PARENT >> 32 in (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like '%AnnouncementBoard');

    -- Migrating Forums
    insert into FORUM (ID_INTERNAL, OID_EXECUTION_COURSE, DESCRIPTION, CREATION_DATE, OID, OID_CREATOR, NAME) select ID_INTERNAL, OID_EXECUTION_COURSE, DESCRIPTION, CREATION_DATE, OID, OID_CREATOR, NAME from CONTENT where OID >> 32 in (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like '%Forum') ;
    update FORUM join NODE on FORUM.OID = NODE.OID_CHILD join CONTENT on NODE.OID_PARENT = CONTENT.OID set FORUM.OID_EXECUTION_COURSE = CONTENT.OID_SITE_EXECUTION_COURSE;
    update FORUM join NODE on FORUM.OID = NODE.OID_CHILD join CONTENT on NODE.OID_PARENT = CONTENT.OID join PARTY on PARTY.OID = CONTENT.OID_UNIT set FORUM.OID_DEPARTMENT = PARTY.OID_DEPARTMENT;
     insert into CONVERSATION_THREAD (ID_INTERNAL, CREATION_DATE, TITLE, OID, OID_CREATOR) select ID_INTERNAL, CREATION_DATE, TITLE, OID, OID_CREATOR from CONTENT where OID >> 32 = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like '%ConversationThread');
    update CONVERSATION_THREAD join NODE on NODE.OID_CHILD = CONVERSATION_THREAD.OID set OID_FORUM = NODE.OID_PARENT;
    insert into CONVERSATION_MESSAGE (ID_INTERNAL, CREATION_DATE, OID, OID_CREATOR, BODY) select ID_INTERNAL, CREATION_DATE, OID, OID_CREATOR, BODY from CONTENT where OID >> 32 in (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like '%ConversationMessage');
    update CONVERSATION_MESSAGE join NODE on NODE.OID_CHILD = CONVERSATION_MESSAGE.OID set OID_CONVERSATION_THREAD = NODE.OID_PARENT;

    -- Migrating sites (this is the fun part)
    insert into SITE (ID_INTERNAL, RESEARCH_UNIT_HOMEPAGE,SHOW_CATEGORY,OID_SITE_EXECUTION_COURSE,DYNAMIC_MAIL_DISTRIBUTION,PERSONALIZED_LOGO,SHOW_BANNER,OID_ROOT_FROM_TEACHER_EVALUATION_COUNCIL,MAIL,ALTERNATIVE_SITE,SHOW_CURRENT_EXECUTION_COURSES,SHOW_INSTITUTION_LOGO,STYLE,SIDE_BANNER,SHOW_PATENTS,OID,OID_LOGO,SHOW_INTRODUCTION,SHOW_PRIZES,SHOW_PARTICIPATIONS,SHOW_CURRENT_ATTENDING_EXECUTION_COURSES,SHOW_INTERESTS,SHOW_PHOTO,SHOW_FLAGS,SHOW_RESEARCH_UNIT_HOMEPAGE,LAYOUT,OID_BENNU,SHOW_ANNOUNCEMENTS,INITIAL_STATEMENT,GOOGLE_ANALYTICS_CODE,SHOW_ALTERNATIVE_HOMEPAGE,DESCRIPTION,SHOW_WORK_TELEPHONE,OID_THESIS,SHOW_ALUMNI_DEGREES,LESSON_PLANNING_AVAILABLE,SHOW_MOBILE_TELEPHONE,RESEARCH_UNIT,OID_DEGREE,SHOW_EVENTS,SHOW_EMAIL,SHOW_PUBLICATIONS,OID_PERSON,SHOW_UNIT,OID_UNIT,SHOW_TELEPHONE,ACTIVATED,INTRODUCTION,SHOW_ACTIVE_STUDENT_CURRICULAR_PLANS, OID_MANAGERS_OF_UNIT_SITE_GROUP) select ID_INTERNAL,RESEARCH_UNIT_HOMEPAGE,SHOW_CATEGORY,OID_SITE_EXECUTION_COURSE,DYNAMIC_MAIL_DISTRIBUTION,PERSONALIZED_LOGO,SHOW_BANNER,OID_ROOT_FROM_TEACHER_EVALUATION_COUNCIL,MAIL,ALTERNATIVE_SITE,SHOW_CURRENT_EXECUTION_COURSES,SHOW_INSTITUTION_LOGO,STYLE,SIDE_BANNER,SHOW_PATENTS,OID,OID_LOGO,SHOW_INTRODUCTION,SHOW_PRIZES,SHOW_PARTICIPATIONS,SHOW_CURRENT_ATTENDING_EXECUTION_COURSES,SHOW_INTERESTS,SHOW_PHOTO,SHOW_FLAGS,SHOW_RESEARCH_UNIT_HOMEPAGE,LAYOUT,OID_BENNU,SHOW_ANNOUNCEMENTS,INITIAL_STATEMENT,GOOGLE_ANALYTICS_CODE,SHOW_ALTERNATIVE_HOMEPAGE,DESCRIPTION,SHOW_WORK_TELEPHONE,OID_THESIS,SHOW_ALUMNI_DEGREES,LESSON_PLANNING_AVAILABLE,SHOW_MOBILE_TELEPHONE,RESEARCH_UNIT,OID_DEGREE,SHOW_EVENTS,SHOW_EMAIL,SHOW_PUBLICATIONS,OID_PERSON,SHOW_UNIT,OID_UNIT,SHOW_TELEPHONE,ACTIVATED,INTRODUCTION,SHOW_ACTIVE_STUDENT_CURRICULAR_PLANS, OID_MANAGERS_OF_UNIT_SITE_GROUP from CONTENT where OID >> 32 in (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME IN ('net.sourceforge.fenixedu.domain.thesis.ThesisSite','net.sourceforge.fenixedu.domain.ScientificCouncilSite','net.sourceforge.fenixedu.domain.ResearchUnitSite','net.sourceforge.fenixedu.domain.SpecificUnitSite','net.sourceforge.fenixedu.domain.UnitSite','net.sourceforge.fenixedu.domain.ExecutionCourseSite','net.sourceforge.fenixedu.domain.ScientificAreaSite','net.sourceforge.fenixedu.domain.AssemblySite','net.sourceforge.fenixedu.domain.TutorSite','net.sourceforge.fenixedu.domain.InstitutionSite','net.sourceforge.fenixedu.domain.DegreeSite','net.sourceforge.fenixedu.domain.homepage.Homepage','net.sourceforge.fenixedu.domain.ManagementCouncilSite','net.sourceforge.fenixedu.domain.Site','net.sourceforge.fenixedu.domain.EdamSite','net.sourceforge.fenixedu.domain.StudentsSite','net.sourceforge.fenixedu.domain.DepartmentSite','net.sourceforge.fenixedu.domain.PedagogicalCouncilSite'));
    insert into CMS_CONTENT (ID_INTERNAL, OID,NAME,NORMALIZED_NAME,ENABLED,CREATION_DATE,MODIFICATION_DATE,SHOW_SUB_SECTIONS) select ID_INTERNAL,OID,NAME,NORMALIZED_NAME,ENABLED,CREATION_DATE,MODIFICATION_DATE,SHOW_SUB_SECTIONS  from CONTENT where OID >> 32 = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Section');
    insert into CMS_CONTENT (ID_INTERNAL,BODY,SHOW_NAME,OID,NAME,NORMALIZED_NAME,ENABLED,CREATION_DATE,MODIFICATION_DATE,SHOW_SUB_SECTIONS) select ID_INTERNAL,BODY,SHOW_NAME,OID,NAME,NORMALIZED_NAME,ENABLED,CREATION_DATE,MODIFICATION_DATE,SHOW_SUB_SECTIONS  from CONTENT where OID >> 32 = (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Item');
    update CMS_CONTENT join NODE on OID_CHILD = CMS_CONTENT.OID set CMS_CONTENT.OID_PARENT = NODE.OID_PARENT, CMS_CONTENT.ORDER = NODE.NODE_ORDER, CMS_CONTENT.VISIBLE = NODE.VISIBLE where NODE.OID_PARENT >> 32 =  (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.Section');
    update GENERIC_FILE join CONTENT on GENERIC_FILE.OID = CONTENT.OID_FILE join NODE on CONTENT.OID = NODE.OID_CHILD set GENERIC_FILE.OID_CMS_CONTENT = NODE.OID_PARENT where NODE.OID_PARENT >> 32 IN (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME in ('net.sourceforge.fenixedu.domain.Section', 'net.sourceforge.fenixedu.domain.Item'));
    update CMS_CONTENT join NODE on OID_CHILD = CMS_CONTENT.OID set CMS_CONTENT.OID_SITE = NODE.OID_PARENT, CMS_CONTENT.ORDER = NODE.NODE_ORDER, CMS_CONTENT.VISIBLE = NODE.VISIBLE where NODE.OID_PARENT >> 32 in  (select DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME IN ('net.sourceforge.fenixedu.domain.thesis.ThesisSite','net.sourceforge.fenixedu.domain.ScientificCouncilSite','net.sourceforge.fenixedu.domain.ResearchUnitSite','net.sourceforge.fenixedu.domain.SpecificUnitSite','net.sourceforge.fenixedu.domain.UnitSite','net.sourceforge.fenixedu.domain.ExecutionCourseSite','net.sourceforge.fenixedu.domain.ScientificAreaSite','net.sourceforge.fenixedu.domain.AssemblySite','net.sourceforge.fenixedu.domain.TutorSite','net.sourceforge.fenixedu.domain.InstitutionSite','net.sourceforge.fenixedu.domain.DegreeSite','net.sourceforge.fenixedu.domain.homepage.Homepage','net.sourceforge.fenixedu.domain.ManagementCouncilSite','net.sourceforge.fenixedu.domain.Site','net.sourceforge.fenixedu.domain.EdamSite','net.sourceforge.fenixedu.domain.StudentsSite','net.sourceforge.fenixedu.domain.DepartmentSite','net.sourceforge.fenixedu.domain.PedagogicalCouncilSite'));
    update CMS_CONTENT join AVAILABILITY_POLICY on CMS_CONTENT.OID = AVAILABILITY_POLICY.OID_CONTENT set CMS_CONTENT.OID_GROUP = AVAILABILITY_POLICY.TARGET_GROUP;

    -- Migrate Groups
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.accessControl.PersistentRoleGroup' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.accessControl.RoleCustomGroup';
    alter table ROLE change OID_ROLE_CUSTOM_GROUP OID_ROLE_GROUP bigint unsigned;

    alter table DEGREE_CURRICULAR_PLAN change CURRICULAR_PLAN_MEMBERS_GROUP OID_MEMBERS_GROUP bigint unsigned;
    alter table DEPARTMENT change COMPETENCE_COURSE_MEMBERS_GROUP OID_MEMBERS_GROUP bigint unsigned;
    alter table RECIPIENT change MEMBERS OID_MEMBERS_GROUP bigint unsigned;
    alter table SENDER change MEMBERS OID_MEMBERS_GROUP bigint unsigned;
    alter table ALERT change TARGET_GROUP OID_TARGET_GROUP bigint unsigned;

    ALTER TABLE RESOURCE ADD `OID_LOCALITY` bigint unsigned;
    UPDATE RESOURCE R SET R.OID_LOCALITY = (SELECT SI.OID_LOCALITY FROM SPACE_INFORMATION SI WHERE SI.OID_SPACE = R.OID AND SI.OID_LOCALITY IS NOT NULL);

    DELETE FROM RESOURCE where OID >> 32 in (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME in ('net.sourceforge.fenixedu.domain.resource.Vehicle', 'net.sourceforge.fenixedu.domain.material.Extension', 'net.sourceforge.fenixedu.domain.material.FireExtinguisher'));
    DELETE FROM RESOURCE_ALLOCATION where OID >> 32 IN (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME IN ('net.sourceforge.fenixedu.domain.resource.VehicleAllocation', 'net.sourceforge.fenixedu.domain.space.ExtensionSpaceOccupation', 'net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation', 'net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation','net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation' ));
    DELETE FROM GENERIC_FILE where OID >> 32 in (SELECT DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME in ('net.sourceforge.fenixedu.domain.space.BlueprintFile'));

    DELETE FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.Space';

    ALTER TABLE RESOURCE ADD COLUMN `CREATED` timestamp;
    UPDATE RESOURCE SET CREATED = STR_TO_DATE(CONCAT(CREATED_ON, " 00:00"), "%Y-%m-%D %H:%i");

    ALTER TABLE RESOURCE CHANGE OID_ROOT_DOMAIN_OBJECT OID_BENNU bigint unsigned;

    UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.Space' WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.space.Room';
    set @spaceId = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.Space');
    UPDATE RESOURCE SET OID = ((SELECT @spaceId) << 32) + ID_INTERNAL WHERE OID >> 32 <> (SELECT @spaceId);

    CREATE TABLE `SPACE_OCCUPATION` (
      `OID_OCCUPATION` bigint(20) unsigned NOT NULL DEFAULT '0',
      `OID_SPACE` bigint(20) unsigned NOT NULL DEFAULT '0',
      PRIMARY KEY (`OID_OCCUPATION`,`OID_SPACE`),
      KEY `OID_OCCUPATION` (`OID_OCCUPATION`),
      KEY `OID_SPACE` (`OID_SPACE`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    -- CHANGE SPACE OIDS

    UPDATE MEASUREMENT_TEST SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;
    UPDATE BENNU SET OID_DEFAULT_CAMPUS = ((SELECT @spaceId << 32) + (OID_DEFAULT_CAMPUS & 0x0000FFFF)) WHERE OID_DEFAULT_CAMPUS IS NOT NULL;
    UPDATE INQUIRIES_ROOM SET OID_ROOM = ((SELECT @spaceId << 32) + (OID_ROOM & 0x0000FFFF)) WHERE OID_ROOM IS NOT NULL;
    UPDATE SUMMARY SET OID_ROOM = ((SELECT @spaceId << 32) + (OID_ROOM & 0x0000FFFF)) WHERE OID_ROOM IS NOT NULL;
    UPDATE PARTY SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;
    UPDATE WRITTEN_EVALUATION_ENROLMENT SET OID_ROOM = ((SELECT @spaceId << 32) + (OID_ROOM & 0x0000FFFF)) WHERE OID_ROOM IS NOT NULL;
    UPDATE GIAF_PROFESSIONAL_DATA SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;
    UPDATE ANNOUNCEMENT SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;
    UPDATE QUEUE_JOB SET OID_DGES_STUDENT_IMPORTATION_FOR_CAMPUS = ((SELECT @spaceId << 32) + (OID_DGES_STUDENT_IMPORTATION_FOR_CAMPUS & 0x0000FFFF)) WHERE OID_DGES_STUDENT_IMPORTATION_FOR_CAMPUS IS NOT NULL;
    UPDATE SPACE_ATTENDANCES SET OID_OCCUPIED_LIBRARY_PLACE = ((SELECT @spaceId << 32) + (OID_OCCUPIED_LIBRARY_PLACE & 0x0000FFFF)) WHERE OID_OCCUPIED_LIBRARY_PLACE IS NOT NULL;
    UPDATE SPACE_ATTENDANCES SET OID_VISITED_LIBRARY_PLACE = ((SELECT @spaceId << 32) + (OID_VISITED_LIBRARY_PLACE & 0x0000FFFF)) WHERE OID_VISITED_LIBRARY_PLACE IS NOT NULL;
    UPDATE PERSISTENT_GROUP SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;
    UPDATE RESOURCE_ALLOCATION SET OID_RESOURCE = ((SELECT @spaceId << 32) + (OID_RESOURCE & 0x0000FFFF)) WHERE OID_RESOURCE IS NOT NULL;
    UPDATE EXECUTION_DEGREE SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;

    INSERT INTO SPACE_OCCUPATION(OID_OCCUPATION, OID_SPACE) SELECT OID, OID_RESOURCE FROM RESOURCE_ALLOCATION;

    RENAME TABLE `RESOURCE` TO `SPACE`;
    RENAME TABLE `RESOURCE_ALLOCATION` TO `OCCUPATION`;
    
    ALTER TABLE SPACE CHANGE OID_SUROUNDING_SPACE OID_PARENT bigint unsigned;
    UPDATE SPACE SET OID_PARENT = ((SELECT @spaceId << 32) + (OID_PARENT & 0x0000FFFF)) WHERE OID_PARENT IS NOT NULL;

    ALTER TABLE OCCUPATION CHANGE OID_ROOT_DOMAIN_OBJECT OID_BENNU bigint unsigned;
    UPDATE FILE_STORAGE_CONFIGURATION SET FILE_TYPE = 'org.fenixedu.spaces.domain.BlueprintFile' where FILE_TYPE = 'net.sourceforge.fenixedu.domain.space.BlueprintFile';

    -- occupation requests
    -- update class names
    UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.occupation.requests.OccupationRequest' WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest';
    UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.occupation.requests.OccupationStateInstant' WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.PunctualRoomsOccupationStateInstant';
    UPDATE FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.occupation.requests.OccupationComment' WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment';

    -- rename tables
    RENAME TABLE `PUNCTUAL_ROOMS_OCCUPATION_COMMENT` TO `OCCUPATION_COMMENT`;
    RENAME TABLE `PUNCTUAL_ROOMS_OCCUPATION_REQUEST` TO `OCCUPATION_REQUEST`;
    RENAME TABLE `PUNCTUAL_ROOMS_OCCUPATION_STATE_INSTANT` TO `OCCUPATION_STATE_INSTANT`;

    -- update schema so if update is made, instant will not be changed
    ALTER TABLE OCCUPATION_COMMENT CHANGE INSTANT INSTANT TIMESTAMP NULL DEFAULT NULL;
    ALTER TABLE OCCUPATION_REQUEST CHANGE INSTANT INSTANT TIMESTAMP NULL DEFAULT NULL;

    -- update owner and requestor from person to user
    UPDATE OCCUPATION_COMMENT OC SET OID_OWNER = (SELECT OID FROM USER U WHERE U.OID_PERSON = OC.OID_OWNER) WHERE OID_OWNER IS NOT NULL;
    UPDATE OCCUPATION_REQUEST OC SET OID_OWNER = (SELECT OID FROM USER U WHERE U.OID_PERSON = OC.OID_OWNER) WHERE OID_OWNER IS NOT NULL;
    UPDATE OCCUPATION_REQUEST OC SET OID_REQUESTOR = (SELECT OID FROM USER U WHERE U.OID_PERSON = OC.OID_REQUESTOR) WHERE OID_REQUESTOR IS NOT NULL;

    -- update campus oid to the new space oid
    set @spaceId = (SELECT DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME = 'org.fenixedu.spaces.domain.Space');
    UPDATE OCCUPATION_REQUEST SET OID_CAMPUS = ((SELECT @spaceId << 32) + (OID_CAMPUS & 0x0000FFFF)) WHERE OID_CAMPUS IS NOT NULL;

    -- convert MLS to normal string
    UPDATE OCCUPATION_COMMENT SET DESCRIPTION = SUBSTR(DESCRIPTION, LOCATE(':', DESCRIPTION) + 1);
    UPDATE OCCUPATION_COMMENT SET SUBJECT = SUBSTR(SUBJECT, LOCATE(':', SUBJECT) + 1);
    ```

4. Support for ID Cards and Parking has been removed from the core of FenixEdu. If you wish to install the new modules, you will need to run the following SQL:
    ```sql
    -- Migrate ID Cards
     update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = replace(DOMAIN_CLASS_NAME, 'net.sourceforge.fenixedu.domain.cardGeneration', 'org.fenixedu.idcards.domain');
     
    -- Migrate Parking
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.NewParkingDocument' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.NewParkingDocument';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingDocument' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingDocument';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingFile' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingFile';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingGroup' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingGroup';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingParty' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingParty';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingPartyHistory' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingPartyHistory';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingRequest' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingRequest';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.ParkingRequestPeriod' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.Vehicle' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.parking.Vehicle';

    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.parking.domain.reports.ParkingDataReportFile' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.reports.ParkingDataReportFile';
    update FILE_STORAGE_CONFIGURATION set FILE_TYPE = 'org.fenixedu.parking.domain.ParkingFile' where FILE_TYPE = 'net.sourceforge.fenixedu.domain.parking.ParkingFile';
    ```
5. Start your application and login with a manager user. Once you login, you will be redirected to the application's start page. If you want to manually manage the new menu structure, go to `http://your-app/bennu-admin` and click on 'Manage Portal'. If you wish to import the menu structure using a script, you must go to `http://your-app/bennu-scheduler-ui/#custom` and run the task. A sample task (containing IST's structure) can be found at: https://gist.github.com/jcarvalho/cc89b00606135cf6ce97.

6. You now need to configure the revamped CMS. You need to instantiate SiteTemplates for every different site you want to support, as well as connecting them to Portal Functionalities. An example script can be found at https://gist.github.com/jcarvalho/0c16a878255a7afe6971. It contains paths and names that are specific to IST's installation, but you can use it as the starting point to configure your own sites.

7. After the sites have been created, run the following task to import the previously existing FunctionalityCalls. Replace the /path/to/json with a server accessible location of the calls.json file exported previously and /path/to/transformations/json to a server accessible location of a translations file with all the transformations you need to do. An example of this last file can be found here https://gist.github.com/pedrosan7os/0bc8d3e7368c5a88acbc.
8. To import the spaces information, please copy the following custom task [ImportSpacesTask.java](https://raw.githubusercontent.com/sergiofbsilva/fenixedu-spaces-import/master/src/main/java/org/fenixedu/spaces/migration/ImportSpacesTask.java), update the `IMPORT_URL` path and run it.

    ```java
    package pt.ist.fenix;

    import java.io.FileReader;
    import java.util.HashMap;
    import java.util.Map;

    import net.sourceforge.fenixedu.domain.Section;
    import net.sourceforge.fenixedu.domain.Site;
    import net.sourceforge.fenixedu.domain.cms.SiteTemplate;
    import net.sourceforge.fenixedu.domain.cms.TemplatedSection;
    import net.sourceforge.fenixedu.domain.cms.TemplatedSectionInstance;

    import org.fenixedu.bennu.core.domain.Bennu;
    import org.fenixedu.bennu.scheduler.custom.CustomTask;

    import pt.ist.fenixframework.DomainObject;
    import pt.ist.fenixframework.FenixFramework;

    import com.google.gson.JsonArray;
    import com.google.gson.JsonElement;
    import com.google.gson.JsonObject;
    import com.google.gson.JsonParser;

    public class ImportFunctionalityCalls extends CustomTask {

        @Override
        public void runTask() throws Exception {
            JsonArray transformationsArray = new JsonParser().parse(new FileReader("/path/to/transformations/json")).getAsJsonArray();
            Map<String, String> transformations = new HashMap<>();
            for (JsonElement element : transformationsArray) {
                JsonObject json = element.getAsJsonObject();
                String path = json.get("path").getAsString();
                String transformed = json.get("transformed").getAsString();
                transformations.put(path, transformed);
            }

            JsonArray array = new JsonParser().parse(new FileReader("/path/to/json")).getAsJsonArray();

            int count = 0;

            for (JsonElement element : array) {
                JsonObject json = element.getAsJsonObject();
                String path = json.get("path").getAsString();
                TemplatedSection template = findTemplate(path, transformations);
                if (template == null) {
                    taskLog("Could not find template for path %s\n", path);
                    continue;
                }

                int order = json.get("order").getAsInt();

                DomainObject parent = FenixFramework.getDomainObject(json.get("parent").getAsString());
                if (parent instanceof Site) {
                    Site site = (Site) parent;
                    new TemplatedSectionInstance(template, site).setOrder(0);
                } else {
                    Section section = (Section) parent;
                    new TemplatedSectionInstance(template, section).setOrder(order);
                }
                count++;

                if (count % 100 == 0) {
                    taskLog("Imported %s calls\n", count);
                }
            }
            taskLog("Done\nImported %s calls\n", count);
        }

        private TemplatedSection findTemplate(String path, Map<String, String> transformations) {
            String actualPath;
            if (transformations.containsKey(path)) {
                actualPath = transformations.get(path);
            } else {
                actualPath = path;
            }
            for (SiteTemplate template : Bennu.getInstance().getSiteTemplateSet()) {
                for (TemplatedSection section : template.getTemplatedSectionSet()) {
                    if (section.getCustomPath().equals(actualPath)) {
                        return section;
                    }
                }
            }
            return null;
        }
    }
    ```
8. Your application should now be fully functional.

## Migrating from 1.x to 2.0

Version 2.0 introduces major architectural changes. FenixEdu is now built on top of the [Bennu](https://github.com/FenixEdu/bennu) Framework.

Migrating to version 2.0 is a multi-step process that should take about 30 minutes in which the application will be unavailable.

#### Pre-Migration

Before migrating to Fenix 2.x, you must first ensure that:

##### Import Metadata From DSpace 
1. This is a migration guide of EducationalResourceType metadata from DSpace to Fenix. 
This metadata is used in resource search by execution course site interfaces.

  1. Import all files to dspace including metadata

    run script `pt.utl.ist.scripts.process.cron.file.UploadPendingFiles`

  2. Grab FileContent instances OID and EXTERNAL_STORAGE_IDENTIFICATION from fenix DB
    
    ```
    mysql fenix_production_db -A --skip-column-names -e "select OID, EXTERNAL_STORAGE_IDENTIFICATION from FILE where OID >> 32 = (SELECT DOMAIN_CLASS_ID FROM FF\$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME like 'net.sourceforge.fenixedu.domain.FileContent');" > filecontents.txt
   ```

  3. Dump metadata for all files in dspace
    ```
    psql -t -ddspace -Udspace -c "select handle, text_value from handle inner join dcvalue on handle.resource_id = dcvalue.item_id where dc_type_id = 66;" > categories.txt
    ```

  4. Run the following python script to generate sql queries for file content resourcetype update

	```python
	#this script reads from categories.txt and filecontents.txt files and generates migrate-categories.sql with the necessary SQL statements to update the file category directly in the fenix database.
	import sys
	def load_categories():
	    catsMap ={}
	    cats = open("categories.txt").readlines()
	    for cat in cats:
	        scat = cat.strip()
	        if len(scat) == 0:
	            continue;
	        parts = scat.split("|");
	        esi = parts[0].strip()
	        category = parts[1].strip()
	        catsMap[esi] = category
	    return catsMap
	
	cats = load_categories()
	def dump_categories_sql():
	    dump_file = open("migrate-categories.sql", "w")
	    lines = open('filecontents.txt').readlines()
	    for line in lines:
	        parts = line.strip().split("\t")
	        oid = parts[0].strip()
	        esi = parts[1].strip()
	        if esi == "NULL":
	            sys.stderr.write("%s doesn't have esi.\n" % oid)
	        if esi.endswith("/1"):
	            esi = esi[:-2]
	        if esi in cats:
	            sql = "update GENERIC_FILE set RESOURCE_TYPE = '%s' where OID = %s;" % (cats[esi], oid)
	            dump_file.write("%s\n" % sql)
	    dump_file.close()
	
	dump_categories_sql()
	```

  5. keep file `migrate-categories.sql` to run before migration step 7 described below.
    
2. Username generation is now handled by the [bennu-user-management](https://github.com/FenixEdu/bennu-user-management) module. Ensure that your installation registers a custom `UsernameGenerator`.
3. Ensure that your installation contains an implementation of `net.sourceforge.fenixedu.util.ConnectionManager`, otherwise SQL-based lookups will not work.
4. If your institution has external applications using Fenix's Jersey Services, the URL must be updated. Instead of `https://fenix.xpto/jersey/services`, it is now at `https://fenix.xpto/api/fenix/jersey/services`. If you are using Bennu's `web-service-utils`, simply update your Host URL to contain the suffix `/api/fenix`.

#### Migration

1. Shut down the application before upgrading

2. If your institutions used the 2nd cycle thesis features, then run the script pt.utl.ist.scripts.runOnce.thesis.CorrectThesisJuryWithOrientation (from the scripts project at https://fenix-ashes.ist.utl.pt/open/trunk/scripts/ ). Be sure to change the username in the script for whatever makes sense at your institution.

3. In this step, some information must be exported to a Json file, so it can be later imported into the new data structures of the application. Run the following script (from the scripts project at https://fenix-ashes.ist.utl.pt/open/trunk/scripts/ ):

    `pt.utl.ist.scripts.runOnce.ExportUserPrivateKeys`

    This script will generate a file `keys.json`. Keep it around for later.

4. Next, we will rename several of the existing infrastructural classes to their Bennu counterparts. Run the following SQL:

    ```sql
    -- RootDomainObject has been removed, Bennu is now used
    RENAME TABLE `ROOT_DOMAIN_OBJECT` TO `BENNU`;
    ALTER TABLE DOMAIN_ROOT change OID_ROOT_DOMAIN_OBJECT OID_BENNU bigint unsigned;
    alter table `BENNU` add `OID_ROOT` bigint unsigned;
    update BENNU set OID_ROOT = 1;
    
    -- Users are now managed by Bennu
    alter table `USER` add `USERNAME` text, add `PASSWORD` text, add `OID_BENNU` bigint unsigned, add index (OID_BENNU);
    update USER set OID_BENNU = OID_ROOT_DOMAIN_OBJECT;
    -- Users with a defined UserUId have their username attribute populated
    update USER set USERNAME = USER_U_ID;
    
    -- Login Periods are now managed in Bennu User Management
    RENAME TABLE `LOGIN_PERIOD` TO `USER_LOGIN_PERIOD`;
    ALTER TABLE `USER_LOGIN_PERIOD` ADD `OID_USER` bigint unsigned, add index (OID_USER);
    update USER_LOGIN_PERIOD set OID_USER = (SELECT OID_USER from IDENTIFICATION where IDENTIFICATION.OID = OID_LOGIN);
    
    -- Remove users with no username
    DELETE FROM `USER_LOGIN_PERIOD` WHERE OID_USER IN (SELECT OID FROM USER WHERE USERNAME IS NULL);
    UPDATE `PARTY` SET OID_USER = NULL WHERE OID_USER IN (SELECT OID FROM USER WHERE USERNAME IS NULL);
    DELETE FROM `USER` WHERE USERNAME IS NULL;
    
    -- Update DomainClassInfo entries, so that no OIDs need changing
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.Bennu' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.RootDomainObject';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.core.domain.User' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.User';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'org.fenixedu.bennu.user.management.UserLoginPeriod' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.LoginPeriod';
    
    -- Update MetaDomainObject
    alter table `CONTENT` add `TYPE` text, add `OID_BENNU` bigint unsigned;
    update CONTENT join META_DOMAIN_OBJECT on CONTENT.OID_META_DOMAIN_OBJECT = META_DOMAIN_OBJECT.OID set CONTENT.TYPE = META_DOMAIN_OBJECT.TYPE;
    update CONTENT set OID_BENNU = (SELECT OID FROM BENNU) where TYPE is not null;
    ```

    Another SQL needs to be run, to remove the roles related to the Project Management module. Run the following:

    ```sql
    -- Removes the relation between depracated roles and persons
    DELETE FROM PERSON_ROLE WHERE OID_ROLE IN ( SELECT OID FROM ROLE WHERE ROLE_TYPE IN ('PROJECTS_MANAGER', 'INSTITUCIONAL_PROJECTS_MANAGER', 'IT_PROJECTS_MANAGER', 'ISTID_PROJECTS_MANAGER', 'ISTID_INSTITUCIONAL_PROJECTS_MANAGER', 'ADIST_PROJECTS_MANAGER', 'ADIST_INSTITUCIONAL_PROJECTS_MANAGER') );
    
    -- Removes from the role_operation_log all the logs that are related with depracated roles
    DELETE FROM ROLE_OPERATION_LOG WHERE OID_ROLE IN ( SELECT OID FROM ROLE WHERE ROLE_TYPE IN ('PROJECTS_MANAGER', 'INSTITUCIONAL_PROJECTS_MANAGER', 'IT_PROJECTS_MANAGER', 'ISTID_PROJECTS_MANAGER', 'ISTID_INSTITUCIONAL_PROJECTS_MANAGER', 'ADIST_PROJECTS_MANAGER', 'ADIST_INSTITUCIONAL_PROJECTS_MANAGER') );
    
    -- Removes all the roles that are related with depracated role types
    DELETE FROM ROLE WHERE ROLE_TYPE IN ('PROJECTS_MANAGER', 'INSTITUCIONAL_PROJECTS_MANAGER', 'IT_PROJECTS_MANAGER', 'ISTID_PROJECTS_MANAGER', 'ISTID_INSTITUCIONAL_PROJECTS_MANAGER', 'ADIST_PROJECTS_MANAGER', 'ADIST_INSTITUCIONAL_PROJECTS_MANAGER');
    ```

5. Start your application, only one instance if possible, and shut it down once the start up finishes. The goal of this procedure is to allow the automatic bootstrap mechanisms to initialize the new data structures that will be populated in later steps. Note that it is crutial that the application be shut down ASAP to prevent inconsistent data.

6. With the application down, it is time to migrate the existing `File` instances to the new `GenericFile` hierarchy. Run the following SQL: (This step WILL take some time)

    ```sql
    -- Rename FILE, as File now extends GenericFile
    DROP TABLE `GENERIC_FILE`;
    RENAME TABLE `FILE` TO `GENERIC_FILE`;
    ALTER TABLE `GENERIC_FILE` CHANGE `CONTENT` `CONTENT_FILE` longblob, CHANGE `SIZE` `SIZE` bigint(20) default NULL, CHANGE `UPLOAD_TIME` `CREATION_DATE` timestamp NULL default NULL,CHANGE `MIME_TYPE` `CONTENT_TYPE` text,CHANGE `DISPLAY_NAME` `DISPLAY_NAME` text,CHANGE `FILENAME` `FILENAME` text, ADD `CONTENT_KEY` text, ADD `OID_STORAGE` bigint unsigned, add index (OID_STORAGE), ADD `OID_FILE_SUPPORT` bigint unsigned, add index (OID_FILE_SUPPORT);
    
    -- All existing files should be connected to the DSpace file storage
    UPDATE GENERIC_FILE SET OID_STORAGE = (SELECT OID_D_SPACE_FILE_STORAGE FROM BENNU), CONTENT_KEY = OID;
    -- And to the FileSupport root
    UPDATE GENERIC_FILE SET OID_FILE_SUPPORT = (SELECT OID FROM FILE_SUPPORT);
    ```
    
    If you want to migrate categories from DSpace (described in [import-metadata-from-dspace](#import-metadata-from-dspace))
    
      1. Create column in fenix db for resourcetype in file content
    
      ```
      ALTER TABLE `GENERIC_FILE` ADD `RESOURCE_TYPE` text;
      ```

      2. Import resource type data to file contents in fenix (takes 2.5 minutes)

        `mysql fenix_production_db -A < migrate-categories.sql`

7. You can now safely start you application. It is now time to import some data and perform initial configurations.

8. Log in with a manager user. (The following steps assume that your installation has the core bennu UI modules).

9. If you have functionalities with path: "/publico/searchScormContent.do" change them to "/publico/searchFileContent.do"

10. Go to `https://<url>/bennu-scheduler-ui/index.html#custom`, and run the following script to import User's private keys. You should change the path to the `keys.json` file being read, so that the path points to the file created in step 2), and is acessible by the Application Server.

    ```java
    package pt.ist.fenix;
    
    import java.io.FileNotFoundException;
    import java.io.FileReader;
    
    import net.sourceforge.fenixedu.domain.UserPrivateKey;
    import net.sourceforge.fenixedu.util.ByteArray;
    
    import org.joda.time.DateTime;
    
    import org.fenixedu.bennu.core.domain.User;
    import org.fenixedu.bennu.scheduler.custom.CustomTask;
    import pt.ist.fenixframework.FenixFramework;
    
    import com.google.common.io.BaseEncoding;
    import com.google.gson.JsonArray;
    import com.google.gson.JsonElement;
    import com.google.gson.JsonIOException;
    import com.google.gson.JsonObject;
    import com.google.gson.JsonParser;
    import com.google.gson.JsonSyntaxException;
    
    public class ImportUserPrivateKeys extends CustomTask {
    
        @Override
        public void runTask() {
            try {
                JsonArray keys = new JsonParser().parse(new FileReader("keys.json")).getAsJsonArray();
                for (JsonElement element : keys) {
                    JsonObject key = element.getAsJsonObject();
                    User user = FenixFramework.getDomainObject(key.get("user").getAsString());
                    UserPrivateKey pk = new UserPrivateKey();
                    user.setPrivateKey(pk);
                    pk.setPrivateKey(new ByteArray(BaseEncoding.base64().decode(key.get("key").getAsString())));
                    pk.setPrivateKeyCreation(new DateTime(key.get("created").getAsLong()));
                    pk.setPrivateKeyValidity(new DateTime(key.get("validity").getAsLong()));
    
                    taskLog("Imported key for user " + user.getUsername());
                }
            } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    ```

11. Still in the Custom Task view, run the following script, to fill out the user's expiration dates

    ```java
    package pt.ist.fenix;
    
    import java.util.Objects;
    
    import org.fenixedu.bennu.core.domain.Bennu;
    import org.fenixedu.bennu.core.domain.User;
    import org.fenixedu.bennu.scheduler.custom.CustomTask;
    import org.fenixedu.bennu.user.management.UserLoginPeriod;
    import pt.ist.fenixframework.Atomic.TxMode;
    
    public class FillUserExpirations extends CustomTask {
    
        @Override
        public void runTask() throws Exception {
            for (User user : Bennu.getInstance().getUserSet()) {
                Object original = user.getExpiration();
                UserLoginPeriod.computeUserExpiration(user);
                if (!Objects.equals(original, user.getExpiration())) {
                    taskLog("Set expiration for user %s (%s) to %s\n", user.getUsername(), user.getExternalId(), user.getExpiration());
                }
            }
        }
    }
    ```

11. Also, run the following script to initilize the Instalation object, adapting the default values to the instalation specific values.

	```java
	package pt.ist.fenix;

	import org.fenixedu.bennu.core.domain.Bennu;
	import org.fenixedu.bennu.scheduler.custom.CustomTask;

	public class InstalationInitialization extends CustomTask {
		private static String DEFAULT_INSTALATION_DOMAIN = "ees.example.edu";
		private static String DEFAULT_INSTALATION_EMAIL_DOMAIN = "ees.example.edu";
		private static String DEFAULT_INSTALATION_NAME = "EES FenixEdu";
		private static String DEFAULT_INSTALATION_URL = "http://fenixedu.ees.example.edu/";

		@Override
		public void runTask() throws Exception {
			Instalation instalation = Bennu.getInstance().getInstalation();
			instalation.setInstalationDomain(DEFAULT_INSTALATION_DOMAIN);
			instalation.setInstalationName(DEFAULT_INSTALATION_NAME);
			instalation.setInstituitionEmailDomain(DEFAULT_INSTALATION_EMAIL_DOMAIN);
			instalation.setInstituitionURL(DEFAULT_INSTALATION_URL);
			taskLog("The Instalation has been initialized with your institution values");
		}
	}
	```

12. Go to `https://<url>/bennu-io-ui/index.html` and configure the file storage that will be used for new files, and then, associate the file types with the desired storage.

    Note that the provided `DSpaceFileStorage` is provided only as a legacy compatibility layer, and as such, it only allows reading from DSpace, and should NOT be used for new files.

13. Go to `https://<url>/bennu-scheduler-ui/index.html` and configure the Schedules for the scripts that were previously run in Cron.

14. Your application should now be fully functional :)

## 1.1.0
 * Run etc/database_operations/run
  
## 1.2.0
 * Run etc/database_operations/run
 * Run script : ChangeEveryoneGroupFilesToPrivateScript


