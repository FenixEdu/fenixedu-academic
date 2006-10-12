package pt.utl.ist.codeGenerator.database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.joda.time.DateTime;

/** 
 * This ant task enables the administrator
 * to install or upgrade the fenix database...
 * 
 * On install, it will create the fenix and slide databases
 * For that it will need some information, such as the main database jdbc url schema,
 * the administration user and password, the fenix and slide username and password
 * for the access user, the privileges granting queries ddl's and the create database query ddl
 * Also, it creates a VERSION table with the date the install was made, to enable the upgrade process
 * Also it may create a Fenix management user as defined by the tasks properties
 * 
 * On upgrade, it will run all directories under the upgrade database operations dir
 * and choose the directories whose name is a date (formated like Ryyyy-mm-dd) which are
 * newer than the VERSION date in the main fenix database and that have "run" scripts...
 * Then for each run file, it tries to parse lines in the format...
 * execute ["comment"] file.sql 
 * or
 * executeWithTempFile ["comment"] file.sql
 * and will fork system processes to run the database client on the command line, executing
 * these queries against the fenix databases
 * In case of executing with a temporary file, it may keep the generated ddl sql files 
 * so that the result may be inspected if something goes wrong.
 * 
 * This install process is provided as is, with no guarantees whatsoever that it will work
 * in all conditions.
 * 
 * The developers that use this class and the build tasks associated with it should report
 * any problems they find or even maybe, in the spirit of community programmng solve the problem
 * and commit it to CVS.
 * 
 * 
 * @author jpereira - Linkare TI
 *
 */
public class Installer extends Task implements TaskContainer {

	private String installOrUpgrade = null;
	
	private String driverClassName = "com.mysql.jdbc.Driver";

	private String connectionUrlSchema = "jdbc;mysql://${host}:${port}/${dbname}?allowMultiQueries=true&jdbcCompliantTruncation=false";

	private String fenixDBName = "fenix";

	private String slideDBName = "fenix";

	private String baseDBName = "mysql";

	private String createDatabaseDDLSchema = "create database if not exists ${dbname}";

	private String createUserDDLSchema = "grant select, update, delete, insert, create on ${dbname}.* to '${username}'@'%' identified by '${password}'; flush privileges;";

	private String grantFullPrivsUserDDLSchema = "grant ALL on ${dbname}.* to '${username}'@'%' identified by '${password}'; flush privileges;";

	private String dbHost = "localhost";

	private String dbPort = "3306";

	private String dbAdminUser = "root";

	private String dbAdminPassword = "";

	private String fenixDBUser = "fenix";

	private String fenixDBPassword = "fenix";

	private String slideDBUser = "fenix_slide";

	private String slideDBPassword = "fenix_slide";

	private String dbNameFenix = "fenix";

	private String dbNameSlide = "fenix_slide";

	private String dbNameBase = "mysql";

	private boolean createFenixManagerUser = false;

	private String fenixManagerUserPassword = "admin";

	private String fenixManagerUser = "admin";

	private String fenixManagerUserName = "Administrator";

	private String fenixManagerUserGender = Gender.MALE.name();

	private File slideDDLFile = new File("etc/slide.sql");

	private File ojbDDLFile = new File("etc/ojb.sql");

	private File dmlDDLFile = new File("etc/dml.sql");

	private File fenixBaseDDLFile = new File("etc/fenix-base.sql");

	private File fenixDDLFile = new File("etc/fenix.sql");

	private File fenixVersionDDLFile = new File("etc/fenix-date-version-create.sql");

	private File fenixVersionRetrieveSQLFile = new File("etc/fenix-date-version-retrieve.sql");

	private String dbDDLClientSchemaExecute = "mysql -u${username} -f -p${password} ${dbname} < ${file}";

	private String dbDDLClientSchemaExecuteToTempFile = "mysql -u${username} -f -p${password} ${dbname} < ${file} > ${tempfile};";

	private boolean keepDDLTempFile = false;
	
	private String upgradesDirectory="etc/database_operations";
	
	
	
	private static final String CRLF=System.getProperty("line.separator");
	
	public void installAll() throws Exception {

		try {
			Class.forName(getDriverClassName());
		}
		catch (Exception e) {
			throw new Exception(
					"Can't load driver for Database access. Please make sure the driver jar is in your classpath!");
		}

		if(installOrUpgrade==null)
			return;
		
		if (installOrUpgrade.equals("install")) {
			installDatabaseFromScratch();
		}
		else if (installOrUpgrade.equals("upgrade")) {
			upgradeExistingDatabase();
		}
		else
			System.out.println("Nothing to be done on database installer task - installOption not recognized: "+installOrUpgrade);
	}

	

	
	
	private class UpgradeDirFilter implements java.io.FileFilter
	{
		private long millisLastVersion=Long.MAX_VALUE;
		
		public UpgradeDirFilter()
		{
			this.millisLastVersion=lastVersionDate.getTime();
		}
		
		public boolean  accept(File f)
		{
			if(f.isDirectory() && f.canRead())
			{
				//check if it has a "run" file inside
				File[] runFiles=f.listFiles(runFileFilter);
				if(runFiles==null || runFiles.length==0)
					return false;
				
				String dirName=f.getName();
				//name should be in format R2006-06-12 - Ryyyy-mm-dd
				if(dirName.startsWith("R") && dirName.contains("-") && dirName.length()>="Ryyyy-mm-dd".length())
				{
					String yearStr=dirName.substring(1,5);
					String monthStr=dirName.substring(6,8);
					String dayStr=dirName.substring(9,11);
					try
					{
						int day=Integer.parseInt(dayStr);
						int month=Integer.parseInt(monthStr);
						int year=Integer.parseInt(yearStr);
						Calendar c=new GregorianCalendar(year,month,day);
						if(millisLastVersion<=c.getTimeInMillis())
							return true;
					}
					catch(Exception e)
					{
						
					}
					
				}
			}
			
			return false;
		}
		
	}
	
	private static class RunFileFilter implements java.io.FileFilter
	{
		public boolean accept(File file) {
			return file.isFile() && file.canRead() && file.getName().equals("run");
		}
	}

	private Date lastVersionDate=null;
	private void upgradeExistingDatabase() {
		
		String dbConnectionFenix = getConnectionUrlSchema().replaceAll("${dbname}", dbNameFenix).replaceAll("${host}",
				dbHost).replaceAll("${port}", dbPort);
		
		
		Connection conFenix = null;
		try {
			conFenix = DriverManager.getConnection(dbConnectionFenix, dbAdminUser, dbAdminPassword);
						
		}
		catch (Exception e) {
			System.out.println("Couldn't open connection to Fenix Database");
			return;
		}

		// now we have the database connections... Read in the SQL and execute it against the database

		try {
			String ddlFenixRetrieveDate= DDLReadFromFile(getFenixVersionRetrieveSQLFile());
			conFenix.setAutoCommit(false);
			System.out.println("Retrieving VERSION date from " + dbNameFenix + " database!");
			ResultSet result=conFenix.createStatement().executeQuery(ddlFenixRetrieveDate);
			result.next();
			lastVersionDate=result.getDate(1);
			conFenix.commit();
			conFenix.close();
		}
		catch (Exception e) {
			System.out.println("Couldn't get the VERSION date from "+dbNameFenix+" database!");
			return;
		}

		
		File dirUpgrade=new File(upgradesDirectory);
		File[] subDirsUpgrade=dirUpgrade.listFiles(new UpgradeDirFilter());
		System.out.println("Executing all available upgrades from date "+lastVersionDate);
		for(File subDirUpgrade:subDirsUpgrade)
			try {
				executeUpgradesOnDir(subDirUpgrade);
			}
			catch (IOException e) {
				System.out.println("IO problem executing upgrades on dir "+subDirUpgrade.getAbsolutePath()+": "+e.getMessage());
			}
			catch (InterruptedException e) {
				System.out.println("Processing problem executing upgrades on dir "+subDirUpgrade.getAbsolutePath()+": "+e.getMessage());
			}
		
	}
	
	private static final RunFileFilter runFileFilter=new RunFileFilter();
	private void executeUpgradesOnDir(File dirUpgrade) throws IOException, InterruptedException
	{
		File[] runFiles=dirUpgrade.listFiles(runFileFilter);
		System.out.println("--> Executing upgrades available in directory "+dirUpgrade.getName());
		for(File runFile:runFiles)
		{
			System.out.print("----> Executing upgrades available in run file "+runFile.getName());
			BufferedReader fileReader=new BufferedReader(new FileReader(runFile));
			String lineInRunFile=null;
			while((lineInRunFile=fileReader.readLine())!=null)
			{
				Upgrade toExec=parseUpgradeFromExecLine(dirUpgrade, lineInRunFile);
				if(toExec!=null)
				{
					toExec.execUpgrade();
				}
			}
			System.out.print("----> Done Executing upgrades available in run file "+runFile.getName());
		}
		System.out.println("--> Done Executing upgrades available in directory "+dirUpgrade.getName());
	}
	
	
	private enum UpgradeType
	{
		UPGRADE_DIRECT_SIMPLE("execute"),UPGRADE_DIRECT_WITH_TEMP_FILE("executeWithTempFile");
		private String functionName=null;
		private UpgradeType(String functionName)
		{
			this.functionName=functionName;
		}
		
		public static UpgradeType findUpgradeTypeFromExecLine(String execLine)
		{
			for(UpgradeType type:UpgradeType.values())
				if(execLine.startsWith(type.functionName+" "))
					return type;
			
			return null;
		}

		
	}

	
	public Upgrade parseUpgradeFromExecLine(File baseDir,String execLine)
	{
		UpgradeType type=UpgradeType.findUpgradeTypeFromExecLine(execLine);
		String fileName="";
		if(type!=null)
		{
			String[] commandLine=execLine.split(" ");
			if(commandLine.length>=2)
			{
				//The type is the first String...
				//The file is the last
				fileName=commandLine[commandLine.length-1];
				File sqlFile=new File(baseDir,fileName);
				if(!sqlFile.exists() || !sqlFile.canRead() || !sqlFile.isFile())
					return null;
				
				
				//The rest of the command line... join in again - it is a comment...
				String comment="";
				for(int i=1;i<commandLine.length-1;i++)
				{
					comment+=" "+commandLine[i];
				}
				comment=comment.trim();
				
				
				return new Upgrade(type,sqlFile,comment);
			
			}
		}
		return null;
	}
	
	private class Upgrade
	{
		
		private UpgradeType upgradeType=UpgradeType.UPGRADE_DIRECT_SIMPLE;
		private File sqlFile=null;
		private String upgradeComment=null;
		
		public Upgrade(UpgradeType upgradeType,File sqlFile,String upgradeComment)
		{
			this.sqlFile=sqlFile;
			this.upgradeComment=upgradeComment;
		}
		
		
		
		public void execUpgrade() throws IOException, InterruptedException
		{
			if(this.upgradeComment!=null && upgradeComment.length()>0)
				System.out.print("       Executing upgrade "+upgradeComment+" Result: ");
			else
				System.out.print("       Executing sql upgrade "+sqlFile.getName()+" Result: ");

			if(upgradeType==UpgradeType.UPGRADE_DIRECT_SIMPLE)
				System.out.println(executeDDLDirect(fenixDBName, dbAdminUser, dbAdminPassword, sqlFile)?"OK":"Failed");
			else if(upgradeType==UpgradeType.UPGRADE_DIRECT_WITH_TEMP_FILE)
				System.out.println(executeDDLDirectToTempFile(fenixDBName, dbAdminUser, dbAdminPassword, sqlFile)?"OK":"Failed");
			else
			{
				System.out.println("Failed!");
			    System.out.println("       Don't know how to execute upgrade of type "+upgradeType);
			}
		}
		
		private boolean executeDDLDirect(String dbname, String username, String password, File ddlFile) throws IOException, InterruptedException {
			String ddlExecuteLine = dbDDLClientSchemaExecute.replaceAll("${username}", username).replaceAll("${password}",
					password).replaceAll("${dbname}", dbname).replaceAll("${file}", ddlFile.getAbsolutePath());

			String[] command=ddlExecuteLine.split(" ");
			ProcessBuilder pBuild=new ProcessBuilder(command);
			pBuild.directory(new File(System.getProperty("temp.dir")));
			ProcessRunner pRunner=new ProcessRunner(pBuild);
			if(!pRunner.exitedOK())
					pRunner.writeOutProcess();
			
			return pRunner.exitedOK();
		}
		
		private boolean executeDDLDirectToTempFile(String dbname, String username, String password, File ddlFile) throws IOException, InterruptedException {
			File generateDir=ddlFile.getParentFile();
			File tempFile=File.createTempFile("", "_generated_from_"+ddlFile.getName(),generateDir);
			
			String ddlExecuteLine = dbDDLClientSchemaExecuteToTempFile.replaceAll("${username}", username).replaceAll("${password}",
					password).replaceAll("${dbname}", dbname).replaceAll("${file}", ddlFile.getAbsolutePath()).replaceAll("${tempfile}", tempFile.getAbsolutePath());

			String[] command=ddlExecuteLine.split(" ");
			ProcessBuilder pBuild=new ProcessBuilder(command);
			pBuild.directory(new File(System.getProperty("temp.dir")));
			ProcessRunner pRunner=new ProcessRunner(pBuild);
			if(!pRunner.exitedOK())
					pRunner.writeOutProcess();
			
			boolean execResult=pRunner.exitedOK();
			if(execResult)
			{
				execResult=executeDDLDirect(dbname, username, password, tempFile);
			}
			
			if(!keepDDLTempFile && tempFile.exists() && tempFile.canWrite())
				tempFile.delete();
		
			return execResult;
		}
		
		
		private class ProcessRunner 
		{
			private Process p=null;
			private BufferedReader errorReader=null;
			private BufferedReader inReader=null;
			private int exitValue=0;
			private StringBuffer errorDesc=new StringBuffer("");
			private StringBuffer output=new StringBuffer("");
			
			public ProcessRunner(ProcessBuilder pBuild) throws IOException, InterruptedException
			{
				pBuild.redirectErrorStream(false);
				p=pBuild.start();
				errorReader=new BufferedReader(new InputStreamReader(p.getErrorStream()));
				inReader=new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				exitValue=p.waitFor();
				
				String lineTemp=null;
				while((lineTemp=errorReader.readLine())!=null)
					errorDesc.append(lineTemp).append(CRLF);
			
				
				while((lineTemp=inReader.readLine())!=null)
					output.append(lineTemp).append(CRLF);
			}
			
			public boolean exitedOK()
			{
				return exitValue==0;
			}
		
			public String getError()
			{
				return errorDesc.toString();
			}
			
			public String getOuput()
			{
				return output.toString();
			}
			
			public boolean isErrorAvailable()
			{
				return errorDesc.length()!=0;
			}
			
			public boolean isOutputAvailable()
			{
				return output.length()!=0;
			}
			
			public void writeOutProcess()
			{
				System.out.println("The process "+(exitedOK()?" executed correctly":" had a problem executing")+"!");
				if(!exitedOK())
				{
					System.out.println("The exit value from the process was "+exitValue);
				
					if(isErrorAvailable())
					{
						System.out.println("Error description returned by the process was: ");
						System.out.println(getError());
					}
					else
						System.out.println("No further error descriptions from the process");
				}
				if(isOutputAvailable())
				{
					System.out.println("The process also returned this information:");
					System.out.println(getOuput());
				}
			}
		}
		
	}

	private void installDatabaseFromScratch() throws Exception {
		String dbConnectionBase = getConnectionUrlSchema().replaceAll("${dbname}", dbNameBase).replaceAll("${host}",
				dbHost).replaceAll("${port}", dbPort);
		String dbConnectionFenix = getConnectionUrlSchema().replaceAll("${dbname}", dbNameFenix).replaceAll("${host}",
				dbHost).replaceAll("${port}", dbPort);
		String dbConnectionSlide = getConnectionUrlSchema().replaceAll("${dbname}", dbNameSlide).replaceAll("${host}",
				dbHost).replaceAll("${port}", dbPort);

		Connection conBase = null;
		try {
			conBase = DriverManager.getConnection(dbConnectionBase, dbAdminUser, dbAdminPassword);
		}
		catch (Exception e) {
			throw new Exception("Couldn't open connection to Main Database", e);
		}

		try {
			String createDBFenixDDL = getCreateDatabaseDDLSchema().replaceAll("${dbname}", dbNameFenix);
			String createDBSlideDDL = getCreateDatabaseDDLSchema().replaceAll("${dbname}", dbNameSlide);
			String createFenixUserDDL = getCreateUserDDLSchema().replaceAll("${dbname}", dbNameFenix).replaceAll(
					"${username}", fenixDBUser).replaceAll("${password}", fenixDBPassword);
			String createSlideUserDDL = getCreateUserDDLSchema().replaceAll("${dbname}", dbNameSlide).replaceAll(
					"${username}", slideDBUser).replaceAll("${password}", slideDBPassword);
			String grantAllPrivsFenixAdminDDL = getGrantFullPrivsUserDDLSchema().replaceAll("${dbname}", dbNameSlide)
					.replaceAll("${username}", dbAdminUser).replaceAll("${password}", dbAdminPassword);
			String grantAllPrivsSlideAdminDDL = getGrantFullPrivsUserDDLSchema().replaceAll("${dbname}", dbNameSlide)
					.replaceAll("${username}", dbAdminUser).replaceAll("${password}", dbAdminPassword);

			conBase.createStatement().executeUpdate(createDBFenixDDL);
			conBase.createStatement().executeUpdate(createDBSlideDDL);
			conBase.createStatement().executeUpdate(createFenixUserDDL);
			conBase.createStatement().executeUpdate(createSlideUserDDL);
			conBase.createStatement().executeUpdate(grantAllPrivsFenixAdminDDL);
			conBase.createStatement().executeUpdate(grantAllPrivsSlideAdminDDL);

		}
		catch (Exception e) {
			throw new Exception("Problem creating databases and granting privileges", e);
		}
		finally {
			if (conBase != null) {
				try {
					conBase.close();
				}
				catch (SQLException e) {
					// ignore for now
				}
			}
		}

		Connection conFenix = null;
		Connection conSlide = null;
		try {
			conFenix = DriverManager.getConnection(dbConnectionFenix, dbAdminUser, dbAdminPassword);

		}
		catch (Exception e) {
			throw new Exception("Couldn't open connection to Fenix Database", e);
		}
		try {
			conSlide = DriverManager.getConnection(dbConnectionSlide, dbAdminUser, dbAdminPassword);

		}
		catch (Exception e) {
			throw new Exception("Couldn't open connection to Slide Database", e);
		}

		// now we have the database connections... Read in the SQL and execute it against the database

		// first lets do the slide thing... Easier

		try {
			String ddlSlide = DDLReadFromFile(getSlideDDLFile());
			conSlide.setAutoCommit(false);
			System.out.println("Creating " + dbNameSlide + " database!");
			conSlide.prepareStatement(ddlSlide).execute();

			String ddlOJB = DDLReadFromFile(getOjbDDLFile());
			String ddlDML = DDLReadFromFile(getDmlDDLFile());
			String ddlFenixBase = DDLReadFromFile(getFenixBaseDDLFile());
			String ddlFenix = DDLReadFromFile(getFenixDDLFile());
			String ddlFenixVersionDate = DDLReadFromFile(getFenixVersionDDLFile());
			Date dateCreatedDB = getFileDate(getFenixDDLFile());
			conFenix.setAutoCommit(false);
			System.out.println("Creating " + dbNameFenix + " OJB tables!");
			conFenix.createStatement().execute(ddlOJB);
			System.out.println("Creating " + dbNameFenix + " DML tables!");
			conFenix.createStatement().execute(ddlDML);
			System.out.println("Creating " + dbNameFenix + " Fenix base tables!");
			conFenix.createStatement().execute(ddlFenixBase);
			System.out.println("Creating " + dbNameFenix + " Fenix Domain model tables!");
			conFenix.createStatement().execute(ddlFenix);
			System.out.println("Creating and Initializing " + dbNameFenix + " Fenix Date Version table!");
			PreparedStatement sqlVersionDate = conFenix.prepareStatement(ddlFenixVersionDate);
			sqlVersionDate.setDate(1, dateCreatedDB);
			sqlVersionDate.execute();

			conSlide.commit();
			conFenix.commit();

		}
		catch (IOException e) {
			try {
				conSlide.rollback();
				conFenix.rollback();
			}
			catch (SQLException e1) {
				// ignore
			}
			throw new Exception("Input/Output error reading SQL files!", e);
		}
		catch (SQLException e) {
			try {
				conSlide.rollback();
				conFenix.rollback();
			}
			catch (SQLException e1) {
				// ignore
			}
			throw new Exception("Input/Output error reading SQL files!", e);
		}
	}

	private Date getFileDate(File f) {
		return new Date(f.lastModified());
	}

	private String DDLReadFromFile(File f) throws IOException {
		StringBuffer ddl = new StringBuffer();
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));

		int byteCount = 0;
		byte[] data = new byte[2048];
		while ((byteCount = bis.read(data)) != -1)
			ddl.append(new String(data, 0, byteCount));

		bis.close();

		return ddl.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		try {
			this.installAll();
			for (Task t : subTasks) {
				t.perform();
			}
			this.installAdministrationUser();
		}
		catch (Exception e) {
			log(getTaskName() + " Problem occurred - " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void installAdministrationUser() {
		if (isCreateFenixManagerUser()) {

			String name = getFenixManagerUserName();
			String username = getFenixManagerUser();
			String password = getFenixManagerUserPassword();
			Gender gender=Gender.parseGender(getFenixManagerUserGender());
			MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
			SuportePersistenteOJB.fixDescriptors();
			RootDomainObject.init();

			ISuportePersistente persistentSupport = null;
			try {
				persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
				persistentSupport.iniciarTransaccao();

				Person pUserAdmin = new Person(name, gender, null, null, null, null, null, null, null);
				Login login = Login.readLoginByUsername(username);
				// crypt password? PasswordEncryptor.encryptPassword(newPassword)
				login.setPassword(PasswordEncryptor.encryptPassword(password));
				login.setActive(true);
				login.setBeginDateDateTime(new DateTime());
				pUserAdmin.addPersonRoleByRoleType(RoleType.MANAGER);
				pUserAdmin.addPersonRoleByRoleType(RoleType.PERSON);
				//The strategy of adding person to PERSON role changes the username...
				//So change it back to what it was
				//pUserAdmin.setUsername(username);
				LoginAlias.createNewCustomLoginAlias(login, username);
				//If it changes the username, maybe it may also change the password,
				//so redefine it!!!
				login.setPassword(PasswordEncryptor.encryptPassword(password));

				persistentSupport.confirmarTransaccao();
			}
			catch (Exception ex) {
				ex.printStackTrace();

				try {
					if (persistentSupport != null) {
						persistentSupport.cancelarTransaccao();
					}
				}
				catch (ExcepcaoPersistencia e) {
					throw new Error(e);
				}
			}

			System.out.println("Initialization complete.");

		}

	}

	private ArrayList<Task> subTasks = new ArrayList<Task>();

	public void addTask(Task subTask) {
		subTasks.add(subTask);
	}

	

	/**
	 * @return Returns the dbDDLClientSchemaExecute.
	 */
	public String getDbDDLClientSchemaExecute() {
		return dbDDLClientSchemaExecute;
	}

	/**
	 * @param dbDDLClientSchemaExecute The dbDDLClientSchemaExecute to set.
	 */
	public void setDbDDLClientSchemaExecute(String dbDDLClientSchemaExecute) {
		this.dbDDLClientSchemaExecute = dbDDLClientSchemaExecute;
	}

	/**
	 * @return Returns the dbDDLClientSchemaExecuteToTempFile.
	 */
	public String getDbDDLClientSchemaExecuteToTempFile() {
		return dbDDLClientSchemaExecuteToTempFile;
	}

	/**
	 * @param dbDDLClientSchemaExecuteToTempFile The dbDDLClientSchemaExecuteToTempFile to set.
	 */
	public void setDbDDLClientSchemaExecuteToTempFile(String dbDDLClientSchemaExecuteToTempFile) {
		this.dbDDLClientSchemaExecuteToTempFile = dbDDLClientSchemaExecuteToTempFile;
	}

	/**
	 * @return Returns the dbHost.
	 */
	public String getDbHost() {
		return dbHost;
	}

	/**
	 * @param dbHost The dbHost to set.
	 */
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	/**
	 * @return Returns the dbNameBase.
	 */
	public String getDbNameBase() {
		return dbNameBase;
	}

	/**
	 * @param dbNameBase The dbNameBase to set.
	 */
	public void setDbNameBase(String dbNameBase) {
		this.dbNameBase = dbNameBase;
	}

	/**
	 * @return Returns the dbNameFenix.
	 */
	public String getDbNameFenix() {
		return dbNameFenix;
	}

	/**
	 * @param dbNameFenix The dbNameFenix to set.
	 */
	public void setDbNameFenix(String dbNameFenix) {
		this.dbNameFenix = dbNameFenix;
	}

	/**
	 * @return Returns the dbNameSlide.
	 */
	public String getDbNameSlide() {
		return dbNameSlide;
	}

	/**
	 * @param dbNameSlide The dbNameSlide to set.
	 */
	public void setDbNameSlide(String dbNameSlide) {
		this.dbNameSlide = dbNameSlide;
	}

	/**
	 * @return Returns the dbPort.
	 */
	public String getDbPort() {
		return dbPort;
	}

	/**
	 * @param dbPort The dbPort to set.
	 */
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	/**
	 * @return Returns the dmlDDLFile.
	 */
	public File getDmlDDLFile() {
		return dmlDDLFile;
	}

	/**
	 * @param dmlDDLFile The dmlDDLFile to set.
	 */
	public void setDmlDDLFile(File dmlDDLFile) {
		this.dmlDDLFile = dmlDDLFile;
	}

	/**
	 * @return Returns the driverClassName.
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * @param driverClassName The driverClassName to set.
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * @return Returns the fenixBaseDDLFile.
	 */
	public File getFenixBaseDDLFile() {
		return fenixBaseDDLFile;
	}

	/**
	 * @param fenixBaseDDLFile The fenixBaseDDLFile to set.
	 */
	public void setFenixBaseDDLFile(File fenixBaseDDLFile) {
		this.fenixBaseDDLFile = fenixBaseDDLFile;
	}

	/**
	 * @return Returns the fenixDBName.
	 */
	public String getFenixDBName() {
		return fenixDBName;
	}

	/**
	 * @param fenixDBName The fenixDBName to set.
	 */
	public void setFenixDBName(String fenixDBName) {
		this.fenixDBName = fenixDBName;
	}

	/**
	 * @return Returns the fenixDBPassword.
	 */
	public String getFenixDBPassword() {
		return fenixDBPassword;
	}

	/**
	 * @param fenixDBPassword The fenixDBPassword to set.
	 */
	public void setFenixDBPassword(String fenixDBPassword) {
		this.fenixDBPassword = fenixDBPassword;
	}

	/**
	 * @return Returns the fenixDBUser.
	 */
	public String getFenixDBUser() {
		return fenixDBUser;
	}

	/**
	 * @param fenixDBUser The fenixDBUser to set.
	 */
	public void setFenixDBUser(String fenixDBUser) {
		this.fenixDBUser = fenixDBUser;
	}

	/**
	 * @return Returns the fenixDDLFile.
	 */
	public File getFenixDDLFile() {
		return fenixDDLFile;
	}

	/**
	 * @param fenixDDLFile The fenixDDLFile to set.
	 */
	public void setFenixDDLFile(File fenixDDLFile) {
		this.fenixDDLFile = fenixDDLFile;
	}

	/**
	 * @return Returns the fenixManagerUser.
	 */
	public String getFenixManagerUser() {
		return fenixManagerUser;
	}

	/**
	 * @param fenixManagerUser The fenixManagerUser to set.
	 */
	public void setFenixManagerUser(String fenixManagerUser) {
		this.fenixManagerUser = fenixManagerUser;
	}

	/**
	 * @return Returns the fenixManagerUserGender.
	 */
	public String getFenixManagerUserGender() {
		return fenixManagerUserGender;
	}

	/**
	 * @param fenixManagerUserGender The fenixManagerUserGender to set.
	 */
	public void setFenixManagerUserGender(String fenixManagerUserGender) {
		this.fenixManagerUserGender = fenixManagerUserGender;
	}

	/**
	 * @return Returns the fenixManagerUserName.
	 */
	public String getFenixManagerUserName() {
		return fenixManagerUserName;
	}

	/**
	 * @param fenixManagerUserName The fenixManagerUserName to set.
	 */
	public void setFenixManagerUserName(String fenixManagerUserName) {
		this.fenixManagerUserName = fenixManagerUserName;
	}

	/**
	 * @return Returns the fenixManagerUserPassword.
	 */
	public String getFenixManagerUserPassword() {
		return fenixManagerUserPassword;
	}

	/**
	 * @param fenixManagerUserPassword The fenixManagerUserPassword to set.
	 */
	public void setFenixManagerUserPassword(String fenixManagerUserPassword) {
		this.fenixManagerUserPassword = fenixManagerUserPassword;
	}

	/**
	 * @return Returns the fenixVersionDDLFile.
	 */
	public File getFenixVersionDDLFile() {
		return fenixVersionDDLFile;
	}

	/**
	 * @param fenixVersionDDLFile The fenixVersionDDLFile to set.
	 */
	public void setFenixVersionDDLFile(File fenixVersionDDLFile) {
		this.fenixVersionDDLFile = fenixVersionDDLFile;
	}

	/**
	 * @return Returns the fenixVersionRetrieveSQLFile.
	 */
	public File getFenixVersionRetrieveSQLFile() {
		return fenixVersionRetrieveSQLFile;
	}

	/**
	 * @param fenixVersionRetrieveSQLFile The fenixVersionRetrieveSQLFile to set.
	 */
	public void setFenixVersionRetrieveSQLFile(File fenixVersionRetrieveSQLFile) {
		this.fenixVersionRetrieveSQLFile = fenixVersionRetrieveSQLFile;
	}

	/**
	 * @return Returns the grantFullPrivsUserDDLSchema.
	 */
	public String getGrantFullPrivsUserDDLSchema() {
		return grantFullPrivsUserDDLSchema;
	}

	/**
	 * @param grantFullPrivsUserDDLSchema The grantFullPrivsUserDDLSchema to set.
	 */
	public void setGrantFullPrivsUserDDLSchema(String grantFullPrivsUserDDLSchema) {
		this.grantFullPrivsUserDDLSchema = grantFullPrivsUserDDLSchema;
	}

	/**
	 * @return Returns the keepDDLTempFile.
	 */
	public boolean isKeepDDLTempFile() {
		return keepDDLTempFile;
	}

	/**
	 * @param keepDDLTempFile The keepDDLTempFile to set.
	 */
	public void setKeepDDLTempFile(boolean keepDDLTempFile) {
		this.keepDDLTempFile = keepDDLTempFile;
	}

	/**
	 * @return Returns the ojbDDLFile.
	 */
	public File getOjbDDLFile() {
		return ojbDDLFile;
	}

	/**
	 * @param ojbDDLFile The ojbDDLFile to set.
	 */
	public void setOjbDDLFile(File ojbDDLFile) {
		this.ojbDDLFile = ojbDDLFile;
	}

	/**
	 * @return Returns the slideDBName.
	 */
	public String getSlideDBName() {
		return slideDBName;
	}

	/**
	 * @param slideDBName The slideDBName to set.
	 */
	public void setSlideDBName(String slideDBName) {
		this.slideDBName = slideDBName;
	}

	/**
	 * @return Returns the slideDBPassword.
	 */
	public String getSlideDBPassword() {
		return slideDBPassword;
	}

	/**
	 * @param slideDBPassword The slideDBPassword to set.
	 */
	public void setSlideDBPassword(String slideDBPassword) {
		this.slideDBPassword = slideDBPassword;
	}

	/**
	 * @return Returns the slideDBUser.
	 */
	public String getSlideDBUser() {
		return slideDBUser;
	}

	/**
	 * @param slideDBUser The slideDBUser to set.
	 */
	public void setSlideDBUser(String slideDBUser) {
		this.slideDBUser = slideDBUser;
	}

	/**
	 * @return Returns the slideDDLFile.
	 */
	public File getSlideDDLFile() {
		return slideDDLFile;
	}

	/**
	 * @param slideDDLFile The slideDDLFile to set.
	 */
	public void setSlideDDLFile(File slideDDLFile) {
		this.slideDDLFile = slideDDLFile;
	}

	/**
	 * @return Returns the upgradesDirectory.
	 */
	public String getUpgradesDirectory() {
		return upgradesDirectory;
	}

	/**
	 * @param upgradesDirectory The upgradesDirectory to set.
	 */
	public void setUpgradesDirectory(String upgradesDirectory) {
		this.upgradesDirectory = upgradesDirectory;
	}

	/**
	 * @return Returns the baseDBName.
	 */
	public String getBaseDBName() {
		return baseDBName;
	}

	/**
	 * @param baseDBName The baseDBName to set.
	 */
	public void setBaseDBName(String baseDBName) {
		this.baseDBName = baseDBName;
	}

	/**
	 * @return Returns the connectionUrlSchema.
	 */
	public String getConnectionUrlSchema() {
		return connectionUrlSchema;
	}

	/**
	 * @param connectionUrlSchema The connectionUrlSchema to set.
	 */
	public void setConnectionUrlSchema(String connectionUrlSchema) {
		this.connectionUrlSchema = connectionUrlSchema;
	}

	/**
	 * @return Returns the createDatabaseDDLSchema.
	 */
	public String getCreateDatabaseDDLSchema() {
		return createDatabaseDDLSchema;
	}

	/**
	 * @param createDatabaseDDLSchema The createDatabaseDDLSchema to set.
	 */
	public void setCreateDatabaseDDLSchema(String createDatabaseDDLSchema) {
		this.createDatabaseDDLSchema = createDatabaseDDLSchema;
	}

	/**
	 * @return Returns the createFenixManagerUser.
	 */
	public boolean isCreateFenixManagerUser() {
		return createFenixManagerUser;
	}

	/**
	 * @param createFenixManagerUser The createFenixManagerUser to set.
	 */
	public void setCreateFenixManagerUser(boolean createFenixManagerUser) {
		this.createFenixManagerUser = createFenixManagerUser;
	}

	/**
	 * @return Returns the createUserDDLSchema.
	 */
	public String getCreateUserDDLSchema() {
		return createUserDDLSchema;
	}

	/**
	 * @param createUserDDLSchema The createUserDDLSchema to set.
	 */
	public void setCreateUserDDLSchema(String createUserDDLSchema) {
		this.createUserDDLSchema = createUserDDLSchema;
	}

	/**
	 * @return Returns the dbAdminPassword.
	 */
	public String getDbAdminPassword() {
		return dbAdminPassword;
	}

	/**
	 * @param dbAdminPassword The dbAdminPassword to set.
	 */
	public void setDbAdminPassword(String dbAdminPassword) {
		this.dbAdminPassword = dbAdminPassword;
	}

	/**
	 * @return Returns the dbAdminUser.
	 */
	public String getDbAdminUser() {
		return dbAdminUser;
	}

	/**
	 * @param dbAdminUser The dbAdminUser to set.
	 */
	public void setDbAdminUser(String dbAdminUser) {
		this.dbAdminUser = dbAdminUser;
	}
}