/*
 * Created on 1/Abr/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.exams.ReadExamByOID;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.Season;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadExamByOIDTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadExamByOIDTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExamByOID";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadExamByOIDdataset.xml";
    }

    // exam does not exists
    public void testUnexistingExam() {
        Integer oid = new Integer(1);

        ReadExamByOID service = ReadExamByOID.getService();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(oid);

            fail("testUnexistingExam");
            sp.cancelarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            fail("testUnexistingExam - persistencia");

        } catch (FenixServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
    }

    // exam with rooms
    public void testSuccessfullReadExamByOIDWithRoomOccupation() {
        Integer examID = new Integer(4526);

        ReadExamByOID service = ReadExamByOID.getService();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExam exam = service.run(examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            Season season = new Season(2);

            assertEquals(exam.getDate(), "9/2/2004");
            assertEquals(exam.getBeginningHour(), "10:00");
            assertEquals(exam.getEndHour(), "13:00");
            assertEquals(exam.getSeason(), season);

            assertEquals(exam.getAssociatedCurricularCourseScope().size(), 2);
            assertEquals(exam.getAssociatedExecutionCourse().size(), 1);
            assertEquals(exam.getAssociatedRoomOccupation().size(), 1);
        } catch (ExcepcaoPersistencia e) {
            fail("testSuccessfullReadExamByOIDWithRoomOccupation - persistencia");
        } catch (Exception e) {
            fail("testSuccessfullReadExamByOIDWithRoomOccupation");
        }
    }

    // exam with rooms
    public void testSuccessfullReadExamByOIDWithoutRoomOccupation() {
        Integer examID = new Integer(4527);

        ReadExamByOID service = ReadExamByOID.getService();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExam exam = service.run(examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            Season season = new Season(2);

            assertEquals(exam.getDate(), "10/2/2004");
            assertEquals(exam.getBeginningHour(), "11:30");
            assertEquals(exam.getEndHour(), "12:30");
            assertEquals(exam.getSeason(), season);

            assertEquals(exam.getAssociatedCurricularCourseScope().size(), 1);
            assertEquals(exam.getAssociatedExecutionCourse().size(), 1);
            assertEquals(exam.getAssociatedRoomOccupation().size(), 0);
        } catch (ExcepcaoPersistencia e) {
            fail("testSuccessfullReadExamByOIDWithoutRoomOccupation - persistencia");
        } catch (Exception e) {
            fail("testSuccessfullReadExamByOIDWithoutRoomOccupation");
        }
    }

}

