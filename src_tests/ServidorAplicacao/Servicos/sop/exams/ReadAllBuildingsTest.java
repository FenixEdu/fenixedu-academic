/*
 * Created on Abr 1, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.exams.ReadAllBuildings;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadAllBuildingsTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadAllBuildingsTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAllBuildings";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadAllBuildingsExamsV4.xml";
    }

    // test successfull create exam
    public void testSuccessfullReadAllBuildings() {
        ReadAllBuildings service = ReadAllBuildings.getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            List buildingNames = service.run();

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(buildingNames.size(), 19);

        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullReadAllBuildings - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullReadAllBuildings - Exception " + ex);
        }
    }

}