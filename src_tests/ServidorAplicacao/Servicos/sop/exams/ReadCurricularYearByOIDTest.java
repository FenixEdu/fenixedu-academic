/*
 * Created on 22/Out/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurricularYearByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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