# Migration

This file contains the steps required to update from one version to another. The following sections represent the steps required to update from the previous to that version. 

## Migrating from 1.x to 2.0

Version 2.0 introduces major architectural changes. FenixEdu is now built on top of the [Bennu](https://github.com/FenixEdu/bennu) Framework.

Migrating to version 2.0 is a multi-step process that should take about 30 minutes in which the application will be unavailable.

Note that in order to properly migrate the application, there must be NO `FileLocalContent` instances, they all MUST be stored in DSpace.

1. Shut down the application before upgrading

2. In this step, some information must be exported to a Json file, so it can be later imported into the new data structures of the application. Run the following script (from the scripts project at https://fenix-ashes.ist.utl.pt/open/trunk/scripts/ ):

    `pt.utl.ist.scripts.runOnce.ExportUserPrivateKeys`

    This script will generate a file `keys.json`. Keep it around for later.

3. Next, we will rename several of the existing infrastructural classes to their Bennu counterparts. Run the following SQL:

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
    -- For others, we don't quite know what to do yet
    update USER set USERNAME = '<unknown>' where USERNAME is null;
    
    -- Login Periods are now managed in Bennu User Management
    RENAME TABLE `LOGIN_PERIOD` TO `USER_LOGIN_PERIOD`;
    ALTER TABLE `USER_LOGIN_PERIOD` ADD `OID_USER` bigint unsigned, add index (OID_USER);
    update USER_LOGIN_PERIOD set OID_USER = (SELECT OID_USER from IDENTIFICATION where IDENTIFICATION.OID = OID_LOGIN);
    
    -- Update DomainClassInfo entries, so that no OIDs need changing
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'pt.ist.bennu.core.domain.Bennu' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.RootDomainObject';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'pt.ist.bennu.core.domain.User' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.User';
    update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'pt.ist.bennu.user.management.UserLoginPeriod' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.LoginPeriod';
    
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

4. Start your application, only one instance if possible, and shut it down once the start up finishes. The goal of this procedure is to allow the automatic bootstrap mechanisms to initialize the new data structures that will be populated in later steps. Note that it is crutial that the application be shut down ASAP to prevent inconsistent data.

5. With the application down, it is time to migrate the existing `File` instances to the new `GenericFile` hierarchy. Run the following SQL: (This step WILL take some time)

    ```sql
    -- Rename FILE, as File now extends GenericFile
    DROP TABLE `GENERIC_FILE`;
    RENAME TABLE `FILE` TO `GENERIC_FILE`;
    ALTER TABLE `GENERIC_FILE` CHANGE `CONTENT` `CONTENT_FILE` longblob; /* blueprint files */
    ALTER TABLE `GENERIC_FILE` CHANGE `SIZE` `SIZE` bigint(20) default NULL;
    ALTER TABLE `GENERIC_FILE` CHANGE `UPLOAD_TIME` `CREATION_DATE` timestamp NULL default NULL;
    ALTER TABLE `GENERIC_FILE` CHANGE `MIME_TYPE` `CONTENT_TYPE` text;
    ALTER TABLE `GENERIC_FILE` CHANGE `DISPLAY_NAME` `DISPLAY_NAME` text;
    ALTER TABLE `GENERIC_FILE` CHANGE `FILENAME` `FILENAME` text;
    ALTER TABLE `GENERIC_FILE` ADD `CONTENT_KEY` text;
    ALTER TABLE `GENERIC_FILE` ADD `OID_STORAGE` bigint unsigned, add index (OID_STORAGE);
    
    -- All existing files should be connected to the DSpace file storage
    UPDATE GENERIC_FILE SET OID_STORAGE = (SELECT OID_D_SPACE_FILE_STORAGE FROM BENNU);
    UPDATE GENERIC_FILE SET CONTENT_KEY = EXTERNAL_STORAGE_IDENTIFICATION;
    ```

6. You can now safely start you application. It is now time to import some data and perform initial configurations.

7. Log in with a manager user. (The following steps assume that your installation has the core bennu UI modules).

8. Go to `https://<url>/bennu-scheduler-ui/index.html#custom`, and run the following script to import User's private keys. You should change the path to the `keys.json` file being read, so that the path points to the file created in step 2), and is acessible by the Application Server.

    ```java
    package pt.ist.fenix;
    
    import java.io.FileNotFoundException;
    import java.io.FileReader;
    
    import net.sourceforge.fenixedu.domain.UserPrivateKey;
    import net.sourceforge.fenixedu.util.ByteArray;
    
    import org.joda.time.DateTime;
    
    import pt.ist.bennu.core.domain.User;
    import pt.ist.bennu.scheduler.custom.CustomTask;
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

9. Go to `https://<url>/bennu-io-ui/index.html` and configure the file storage that will be used for new files, and then, associate the file types with the desired storage.

    Note that the provided `DSpaceFileStorage` is provided only as a legacy compatibility layer, and as such, it only allows reading from DSpace, and should NOT be used for new files.

10. Go to `https://<url>/bennu-scheduler-ui/index.html` and configure the Schedules for the scripts that were previously run in Cron.

11. Your application should now be fully functional :)

## 1.1.0
 * Run etc/database_operations/run
  
## 1.2.0
 * Run etc/database_operations/run
 * Run script : ChangeEveryoneGroupFilesToPrivateScript
