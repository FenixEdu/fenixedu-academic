/*
 * Created on 22/Out/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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