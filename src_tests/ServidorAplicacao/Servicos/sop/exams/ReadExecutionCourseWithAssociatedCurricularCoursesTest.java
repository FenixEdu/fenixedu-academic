/*
 * Created on 20/Abr/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.exams.ReadExecutionCoursewithAssociatedCurricularCourses;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadExecutionCourseWithAssociatedCurricularCoursesTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadExecutionCourseWithAssociatedCurricularCoursesTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionCoursewithAssociatedCurricularCourses";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadExecutionCourseWithAssociatedCurricularCourseDataset.xml";
    }

    public void testSuccessfullReadExecutionCourseWithAllAssociatedCurricularCourses() {
        Integer executionCourseID = new Integer(36422);

        ReadExecutionCoursewithAssociatedCurricularCourses service = ReadExecutionCoursewithAssociatedCurricularCourses
                .getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            InfoExecutionCourse executionCourse = service.run(executionCourseID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(executionCourse.getNome(), "Aprendizagem");
            assertEquals(executionCourse.getAssociatedInfoCurricularCourses().size(), 2);
        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadExecutionCourseWithAllAssociatedCurricularCourses - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadExecutionCourseWithAllAssociatedCurricularCourses " + ex);
        }
    }

    public void testSuccessfullReadExecutionCourseWithSomeAssociatedCurricularCourses() {
        Integer executionCourseID = new Integer(36721);

        ReadExecutionCoursewithAssociatedCurricularCourses service = ReadExecutionCoursewithAssociatedCurricularCourses
                .getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            InfoExecutionCourse executionCourse = service.run(executionCourseID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(executionCourse.getNome(), "Robótica");

            assertEquals(executionCourse.getAssociatedInfoCurricularCourses().size(), 1);
        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadExecutionCourseWithSomeAssociatedCurricularCourses - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadExecutionCourseWithSomeAssociatedCurricularCourses " + ex);
        }
    }

    public void testSuccessfullReadExecutionCourseWithNoAssociatedCurricularCourses() {
        Integer executionCourseID = new Integer(36723);

        ReadExecutionCoursewithAssociatedCurricularCourses service = ReadExecutionCoursewithAssociatedCurricularCourses
                .getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            InfoExecutionCourse executionCourse = service.run(executionCourseID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(executionCourse.getNome(), "Representação do Conhecimento");

            assertNull(executionCourse.getAssociatedInfoCurricularCourses());
        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadExecutionCourseWithNoAssociatedCurricularCourses - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadExecutionCourseWithNoAssociatedCurricularCourses " + ex);
        }
    }

    public void testUnexistingReadExecutionCourse() {
        Integer executionCourseID = new Integer(1);

        ReadExecutionCoursewithAssociatedCurricularCourses service = ReadExecutionCoursewithAssociatedCurricularCourses
                .getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(executionCourseID);

            fail("testUnexistingReadExecutionCourse");
        } catch (FenixServiceException ex) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnexistingReadExecutionCourse " + ex);
        }
    }

}

