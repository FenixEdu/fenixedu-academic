package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.util.TipoCurso;

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