package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ReadActiveStudentCurricularPlanByNumberAndDegreeTypeTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public ReadActiveStudentCurricularPlanByNumberAndDegreeTypeTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/student/testReadActiveStudentCurricularPlanByNumberAndDegreeTypeDataSet.xml";
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType";
    }

    public void testReadExistingActiveStudentCurricularPlan() {
        try {
            Integer studentNumber = new Integer(142);
            Object[] argsReadActiveStudentCurricularPlan = { studentNumber, TipoCurso.MESTRADO_OBJ };

            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(null, getNameOfServiceToBeTested(),
                            argsReadActiveStudentCurricularPlan);
            assertNotNull(infoStudentCurricularPlan);
            assertEquals(infoStudentCurricularPlan.getInfoStudent().getNumber(), studentNumber);
            assertEquals(infoStudentCurricularPlan.getInfoStudent().getDegreeType(),
                    TipoCurso.MESTRADO_OBJ);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingActiveStudentCurricularPlan " + ex.getMessage());
        }

    }

    public void testReadNonExistingActiveStudentCurricularPlan() {
        try {
            Integer studentNumber = new Integer(3561);
            Object[] argsReadActiveStudentCurricularPlan = { studentNumber, TipoCurso.MESTRADO_OBJ };

            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(null, getNameOfServiceToBeTested(),
                            argsReadActiveStudentCurricularPlan);
            assertNull(infoStudentCurricularPlan);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadNonExistingActiveStudentCurricularPlan " + ex.getMessage());
        }
    }

}