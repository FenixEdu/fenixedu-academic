package UtilTests.enrollment;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import Dominio.CreditsInAnySecundaryArea;
import Dominio.CreditsInScientificArea;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICreditsInAnySecundaryArea;
import Dominio.ICreditsInScientificArea;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.PersonRole;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

public class LoadDBForCurricularCourseEnrollmentTest {

    private static final String FILE = "D:/Fenix/Files/others/dataSet.xml";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_ALIAS = "jdbc:mysql://localhost/";
    private static final String DEFAULT_DB_NAME = "ciapl";
    private static final String DEFAULT_DB_PASSWORD = "";
    private static final String DEFAULT_DB_USERNAME = "root";

    private static final boolean CALL_CLEAN_DB = false;

    private static final Integer PERSON_ID = new Integer(18000);
    private static final Integer STUDENT_ID = new Integer(13000);
    private static final Integer STUDENT_CURRICULAR_PLAN_ID = new Integer(23000);

    private IDatabaseConnection connection;
    private String dbName;
    private String username;
    private String password;
    private DatabaseOperation insertDatabaseOperation;

    public LoadDBForCurricularCourseEnrollmentTest() {
        this.connection = null;
        this.dbName = null;
        this.username = null;
        this.password = null;
        this.insertDatabaseOperation = DatabaseOperation.CLEAN_INSERT;
    }

    public static void main(String[] args) throws Throwable {

        cleanDB();
        loadDB();
    }

    private static void cleanDB() throws ExcepcaoPersistencia {

        if (CALL_CLEAN_DB) {
            ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
            persistenceDAO.iniciarTransaccao();

            // -------------------------------------------------------------------------------------------------------
            IPessoa person = (IPessoa) persistenceDAO.getIPessoaPersistente().readByOID(Pessoa.class, PERSON_ID);

            IRole role = new Role();

            role.setRoleType(RoleType.PERSON);
            IPersonRole personRole1 = persistenceDAO.getIPersistentPersonRole().readByPersonAndRole(person, role);

            role.setRoleType(RoleType.STUDENT);
            IPersonRole personRole2 = persistenceDAO.getIPersistentPersonRole().readByPersonAndRole(person, role);

            IStudent student = (IStudent) persistenceDAO.getIPersistentStudent().readByOID(Student.class, STUDENT_ID);

            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) persistenceDAO
                    .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class, STUDENT_CURRICULAR_PLAN_ID);

            if (studentCurricularPlan != null) {

                // ---------------------------------------------------------------------------------------------------
                int size = studentCurricularPlan.getEnrolments().size();
                for (int i = 0; i < size; i++) {
                    IEnrollment enrollment = (IEnrollment) studentCurricularPlan.getEnrolments().get(i);

                    int size2 = enrollment.getEvaluations().size();
                    for (int j = 0; j < size2; j++) {
                        IEnrolmentEvaluation enrollmentEvaluation = (IEnrolmentEvaluation) enrollment.getEvaluations().get(j);
                        persistenceDAO.getIPersistentEnrolmentEvaluation().deleteByOID(EnrolmentEvaluation.class,
                                enrollmentEvaluation.getIdInternal());
                    }

                    persistenceDAO.getIPersistentEnrolment().deleteByOID(Enrolment.class, enrollment.getIdInternal());
                }

                // ---------------------------------------------------------------------------------------------------
                List creditsInAnySecundaryAreaList = persistenceDAO.getIPersistentCreditsInAnySecundaryArea()
                        .readAllByStudentCurricularPlan(studentCurricularPlan);

                size = creditsInAnySecundaryAreaList.size();
                for (int i = 0; i < size; i++) {
                    ICreditsInAnySecundaryArea creditsInAnySecundaryArea = (ICreditsInAnySecundaryArea) creditsInAnySecundaryAreaList
                            .get(i);
                    persistenceDAO.getIPersistentCreditsInAnySecundaryArea().deleteByOID(CreditsInAnySecundaryArea.class,
                            creditsInAnySecundaryArea.getIdInternal());
                }

                // ---------------------------------------------------------------------------------------------------
                List creditsInScientificAreaList = persistenceDAO.getIPersistentCreditsInSpecificScientificArea()
                        .readAllByStudentCurricularPlan(studentCurricularPlan);

                size = creditsInScientificAreaList.size();
                for (int i = 0; i < size; i++) {
                    ICreditsInScientificArea creditsInScientificArea = (ICreditsInScientificArea) creditsInScientificAreaList
                            .get(i);
                    persistenceDAO.getIPersistentCreditsInSpecificScientificArea().deleteByOID(CreditsInScientificArea.class,
                            creditsInScientificArea.getIdInternal());
                }

                // ---------------------------------------------------------------------------------------------------
                persistenceDAO.getIStudentCurricularPlanPersistente().deleteByOID(StudentCurricularPlan.class,
                        studentCurricularPlan.getIdInternal());
            }

            // -------------------------------------------------------------------------------------------------------
            if (person != null) {
                persistenceDAO.getIPessoaPersistente().deleteByOID(Pessoa.class, person.getIdInternal());
            }

            if (student != null) {
                persistenceDAO.getIPersistentStudent().deleteByOID(Student.class, student.getIdInternal());
            }

            if (personRole1 != null) {
                persistenceDAO.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole1.getIdInternal());
            }

            if (personRole2 != null) {
                persistenceDAO.getIPersistentPersonRole().deleteByOID(PersonRole.class, personRole2.getIdInternal());
            }

            // -------------------------------------------------------------------------------------------------------
            persistenceDAO.confirmarTransaccao();
        }
    }

    private static void loadDB() throws Exception {
        LoadDBForCurricularCourseEnrollmentTest obj = new LoadDBForCurricularCourseEnrollmentTest();
        obj.openConnection();
        obj.loadDataBase(FILE);
        obj.closeConnection();
    }

//    private static void clearCache() throws ExcepcaoPersistencia {
//        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
//        persistenceDAO.iniciarTransaccao();
//        persistenceDAO.clearCache();
//        persistenceDAO.confirmarTransaccao();
//    }

    public void openConnection() throws Exception {
        if (this.connection == null) {
            Class.forName(DB_DRIVER);
            Connection jdbcConnection = DriverManager.getConnection(this.getDbName(), this.getUsername(), this.getPassword());
            this.connection = new DatabaseConnection(jdbcConnection);
        }
    }

    public void closeConnection() throws Exception {
        this.connection.close();
        this.connection = null;
    }

    public void loadDataBase(String filename) throws Exception {
        FileReader fileReader = new FileReader(new File(filename));
        IDataSet dataSet = new FlatXmlDataSet(fileReader);
        this.insertDatabaseOperation.execute(this.connection, dataSet);
    }

    public IDatabaseConnection getConnection() {
        return this.connection;
    }

    public String getDbName() {
        if (this.dbName == null) {
            return DB_ALIAS + DEFAULT_DB_NAME;
        } else {
            return DB_ALIAS + this.dbName;
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPassword() {
        if (this.password == null) {
            return DEFAULT_DB_PASSWORD;
        } else {
            return this.password;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        if (this.username == null) {
            return DEFAULT_DB_USERNAME;
        } else {
            return this.username;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

}