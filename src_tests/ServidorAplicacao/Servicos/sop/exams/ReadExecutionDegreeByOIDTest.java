/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.Servico.commons.ReadExecutionDegreeByOID;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadExecutionDegreeByOIDTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadExecutionDegreeByOIDTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionDegreeByOID";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadFilteredExamsMapDataset.xml";
    }

    public void testSuccessfullReadExecutionDegreeByOID() {
        Integer id = new Integer(10);

        ReadExecutionDegreeByOID service = new ReadExecutionDegreeByOID();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExecutionDegree infoExecutionDegree = service.run(id);
            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(infoExecutionDegree.getIdInternal(), new Integer(10));
            assertEquals(infoExecutionDegree.getInfoCampus().getName(), "Alameda");
            assertEquals(infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                    "LEIC - Currículo Antigo");
        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadExecutionDegreeByOID" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadExecutionDegreeByOID error on compareDataSet" + ex);
        }
    }

    public void testUnsuccessfullReadExecutionDegreeByOID() {
        Integer id = new Integer(1);

        ReadExecutionDegreeByOID service = new ReadExecutionDegreeByOID();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExecutionDegree infoExecutionDegree = service.run(id);
            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            if (infoExecutionDegree != null) {
                fail("testUnsuccessfullReadExecutionDegreeByOID reading of an unexisting Execution Degree");
            }

        } catch (FenixServiceException ex) {
            fail("testUnsuccessfullReadCurricularYearByOID error on compareDataSet" + ex);

        } catch (ExcepcaoPersistencia ex) {
            fail("testUnsuccessfullReadCurricularYearByOID error on persistencia" + ex);

        }
    }

}