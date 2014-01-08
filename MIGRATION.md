# Migration

This file contains the steps required to update from one version to another. The following sections represent the steps required to update from the previous to that version. 

## Migrating from 1.x to 2.0

Version 2.0 introduces major architectural changes. FenixEdu is now built on top of the [Bennu](https://github.com/FenixEdu/bennu) Framework.

Migrating to version 2.0 is a multi-step process that should take about 30 minutes in which the application will be unavailable.

#### Pre-Migration

Before migrating to Fenix 2.x, you must first ensure that:

1. There are no instances of `FileLocalContent`.
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
    -- For others, we don't quite know what to do yet
    update USER set USERNAME = '<unknown>' where USERNAME is null;
    
    -- Login Periods are now managed in Bennu User Management
    RENAME TABLE `LOGIN_PERIOD` TO `USER_LOGIN_PERIOD`;
    ALTER TABLE `USER_LOGIN_PERIOD` ADD `OID_USER` bigint unsigned, add index (OID_USER);
    update USER_LOGIN_PERIOD set OID_USER = (SELECT OID_USER from IDENTIFICATION where IDENTIFICATION.OID = OID_LOGIN);
    
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
    UPDATE GENERIC_FILE SET OID_STORAGE = (SELECT OID_D_SPACE_FILE_STORAGE FROM BENNU), CONTENT_KEY = EXTERNAL_STORAGE_IDENTIFICATION;
    -- And to the FileSupport root
    UPDATE GENERIC_FILE SET OID_FILE_SUPPORT = (SELECT OID FROM FILE_SUPPORT);
    ```

7. You can now safely start you application. It is now time to import some data and perform initial configurations.

8. Log in with a manager user. (The following steps assume that your installation has the core bennu UI modules).

9. Go to `https://<url>/bennu-scheduler-ui/index.html#custom`, and run the following script to import User's private keys. You should change the path to the `keys.json` file being read, so that the path points to the file created in step 2), and is acessible by the Application Server.

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

10. Still in the Custom Task view, run the following script, to fill out the user's expiration dates

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
		private static String DEFAULT_INSTALATION_DOMAIN = "example.com";
		private static String DEFAULT_INSTALATION_NAME = "InstalationExample";
		private static String DEFAULT_INSTALATION_URL = "http://www.example.com/";

		@Override
		public void runTask() throws Exception {
			Instalation instalation = Bennu.getInstance().getInstalation();
			instalation.setInstalationDomain(DEFAULT_INSTALATION_DOMAIN);
			instalation.setInstalationName(DEFAULT_INSTALATION_NAME);
			instalation.setInstituitionEmailDomain(DEFAULT_INSTALATION_DOMAIN);
			instalation.setInstituitionURL(DEFAULT_INSTALATION_URL);
			taskLog("The Instalation has been initialized with specific IST values");
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


