package UtilTests.enrollment;

import java.util.List;

import Dominio.CreditsInAnySecundaryArea;
import Dominio.CreditsInScientificArea;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICreditsInAnySecundaryArea;
import Dominio.ICreditsInScientificArea;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LoadDBForCurricularCourseEnrollmentTest {
    
    private static final String FILE = "D:/Fenix/Files/others/dataSet.xml";

    private static final Integer PERSON_ID = new Integer(18000);
    private static final Integer STUDENT_ID = new Integer(13000);
    private static final Integer STUDENT_CURRICULAR_PLAN_ID = new Integer(23000);

    public LoadDBForCurricularCourseEnrollmentTest() {
    }

    public static void main(String[] args) throws Throwable {

        ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
        persistenceDAO.iniciarTransaccao();
        cleanDB(persistenceDAO);
        persistenceDAO.confirmarTransaccao();
        loadDB();
        persistenceDAO.iniciarTransaccao();
        clearCache(persistenceDAO);
        persistenceDAO.confirmarTransaccao();
    }

    private static void cleanDB(ISuportePersistente persistenceDAO) throws ExcepcaoPersistencia {

        // -------------------------------------------------------------------------------------------------------
        IPessoa person = (IPessoa) persistenceDAO.getIPessoaPersistente().readByOID(Pessoa.class, PERSON_ID);
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
                ICreditsInScientificArea creditsInScientificArea = (ICreditsInScientificArea) creditsInScientificAreaList.get(i);
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
    }

    private static void loadDB() throws Exception {
        dbaccess dbAcessPoint = new dbaccess();
        dbAcessPoint.openConnection();
        dbAcessPoint.loadDataBase(FILE);
        dbAcessPoint.closeConnection();
    }

    private static void clearCache(ISuportePersistente persistenceDAO) {
        persistenceDAO.clearCache();
    }

}