/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import DataBeans.InfoCurricularYear;
import ServidorAplicacao.Servico.commons.ReadCurricularYearByOID;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadCurricularYearByOIDTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadCurricularYearByOIDTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCurricularYearByOID";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testExamsV4dataset.xml";
    }

    public void testSuccessfullReadCurricularYearByOID() {
        Integer id = new Integer(3);

        ReadCurricularYearByOID service = new ReadCurricularYearByOID();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoCurricularYear year = service.run(id);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(year.getYear(), new Integer(3));
        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadCurricularYearByOID - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadCurricularYearByOID - Exception " + ex);
        }

    }

    public void testUnsuccessfullReadCurricularYearByOID() {
        Integer id = new Integer(6);
        ReadCurricularYearByOID service = new ReadCurricularYearByOID();

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(id);
            fail("testUnsuccessfullReadCurricularYearByOID");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {

                fail("testUnsuccessfullReadCurricularYearByOID - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullReadCurricularYearByOID - Exception (not fenix service) " + ex);
        }

    }

}