package net.sourceforge.fenixedu.applicationTier.Servicos.coordinator;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 21/Nov/2003
 *  
 */
public class EditCurriculumForCurricularCourseTest extends ServiceTestCase {
    public EditCurriculumForCurricularCourseTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "EditCurriculumForCurricularCourse";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/coordinator/testDataSetCurriculum.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getSecondAuthenticatedAndAuthorizedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    public InfoCurriculum getCurriculumForm(Integer curriculumCode) {
        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curriculumCode);

        infoCurriculum.setGeneralObjectives("objectivos gerais em portugues anterior modificado");
        infoCurriculum.setGeneralObjectivesEn("objectivos gerais em ingles anterior modificado");
        infoCurriculum
                .setOperacionalObjectives("objectivos operacionais em portugues anterior modificado");
        infoCurriculum
                .setOperacionalObjectivesEn("objectivos operacionais em ingles anterior modificado");
        infoCurriculum.setProgram("programa em portugues anterior modificado");
        infoCurriculum.setProgramEn("programa em ingles anterior modificado");

        return infoCurriculum;
    }

    public InfoCurriculum getNewCurriculumForm(Integer curriculumCode) {
        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curriculumCode);

        infoCurriculum.setGeneralObjectives("objectivos gerais em portugues");
        infoCurriculum.setGeneralObjectivesEn("objectivos gerais em ingles");
        infoCurriculum.setOperacionalObjectives("objectivos operacionais em portugues");
        infoCurriculum.setOperacionalObjectivesEn("objectivos operacionais em ingles");
        infoCurriculum.setProgram("programa");
        infoCurriculum.setProgramEn("programa em ingles");

        return infoCurriculum;
    }

    public void testSuccessfull() {
        try {
            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();

            //Service Arguments
            Integer infoExecutionDegreeCode = new Integer(30);
            Integer infoCurriculumCode = new Integer(3);
            Integer infoCurricularCourseCode = new Integer(54);
            InfoCurriculum infoCurriculum = getCurriculumForm(infoCurriculumCode);

            Object[] args = { infoExecutionDegreeCode, infoCurriculumCode, infoCurricularCourseCode,
                    infoCurriculum, argsUser[0] };

            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            //Service
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

            //Read the change in curriculum
            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

            sp.iniciarTransaccao();
            ICurriculum curriculumAck;
            curriculumAck = (ICurriculum) persistentCurriculum.readByOID(Curriculum.class,
                    infoCurriculumCode);
            sp.confirmarTransaccao();

            assertEquals(infoCurriculum.getGeneralObjectives(), curriculumAck.getGeneralObjectives());
            assertEquals(infoCurriculum.getGeneralObjectivesEn(), curriculumAck.getGeneralObjectivesEn());
            assertEquals(infoCurriculum.getOperacionalObjectives(), curriculumAck
                    .getOperacionalObjectives());
            assertEquals(infoCurriculum.getOperacionalObjectivesEn(), curriculumAck
                    .getOperacionalObjectivesEn());
            assertEquals(infoCurriculum.getProgram(), curriculumAck.getProgram());
            assertEquals(infoCurriculum.getProgramEn(), curriculumAck.getProgramEn());
            assertEquals(argsUser[0], curriculumAck.getPersonWhoAltered().getUsername());
            assertNotNull(curriculumAck.getCurricularCourse());

            System.out
                    .println("EditCurriculumForCurricularCourseTest was SUCCESSFULY runned by service: testSuccessfull");

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Reading a curriculum " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Reading a curriculum " + e);
        }
    }

    public void testNewCurriculum() {
        try {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(30);
            Integer infoCurriculumCode = new Integer(0);
            Integer infoCurricularCourseCode = new Integer(54);
            InfoCurriculum infoCurriculum = getNewCurriculumForm(infoCurriculumCode);

            String[] argsUser = getAuthenticatedAndAuthorizedUser();

            Object[] args = { infoExecutionDegreeCode, infoCurriculumCode, infoCurricularCourseCode,
                    infoCurriculum, argsUser[0] };

            //Valid user
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            //Service
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

            //Read the change in curriculum
            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

            sp.iniciarTransaccao();
            ICurricularCourse curricularCourseAck;
            curricularCourseAck = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, infoCurricularCourseCode);
            ICurriculum curriculumAck = persistentCurriculum
                    .readCurriculumByCurricularCourse(curricularCourseAck);
            sp.confirmarTransaccao();

            assertEquals(infoCurriculum.getGeneralObjectives(), curriculumAck.getGeneralObjectives());
            assertEquals(infoCurriculum.getGeneralObjectivesEn(), curriculumAck.getGeneralObjectivesEn());
            assertEquals(infoCurriculum.getOperacionalObjectives(), curriculumAck
                    .getOperacionalObjectives());
            assertEquals(infoCurriculum.getOperacionalObjectivesEn(), curriculumAck
                    .getOperacionalObjectivesEn());
            assertEquals(infoCurriculum.getProgram(), curriculumAck.getProgram());
            assertEquals(infoCurriculum.getProgramEn(), curriculumAck.getProgramEn());
            assertEquals(argsUser[0], curriculumAck.getPersonWhoAltered().getUsername());
            assertNotNull(curriculumAck.getCurricularCourse());

            System.out
                    .println("EditCurriculumForCurricularCourseTest was SUCCESSFULY runned by service: testNewCurriculum");

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Reading a curriculum " + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Reading a curriculum " + e);
        }
    }
}