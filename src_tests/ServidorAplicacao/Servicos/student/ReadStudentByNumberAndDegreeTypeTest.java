package ServidorAplicacao.Servicos.student;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servicos.ServiceTestCase;
import Util.TipoCurso;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ReadStudentByNumberAndDegreeTypeTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public ReadStudentByNumberAndDegreeTypeTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/student/testReadStudentByNumberAndDegreeTypeDataSet.xml";
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentByNumberAndDegreeType";
    }

    public void testReadExistingStudent() {
        try {
            Integer studentNumber = new Integer(142);
            Object[] argsReadStudent = { studentNumber, TipoCurso.MESTRADO_OBJ };

            InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), argsReadStudent);
            assertNotNull(infoStudent);
            assertEquals(infoStudent.getNumber(), studentNumber);
            assertEquals(infoStudent.getDegreeType(), TipoCurso.MESTRADO_OBJ);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingActiveStudentCurricularPlan " + ex.getMessage());
        }

    }

    public void testReadNonExistingStudent() {
        try {
            Integer studentNumber = new Integer(2345);
            Object[] argsReadStudent = { studentNumber, TipoCurso.MESTRADO_OBJ };

            InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), argsReadStudent);
            assertNull(infoStudent);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadNonExistingActiveStudentCurricularPlan " + ex.getMessage());
        }
    }

}