/*
 * Created on Apr 12, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.exams.EditExamNew;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.Season;

/**
 * @author Ana e Ricardo
 *  
 */
public class EditExamNewTest extends ServiceTestCase {

    /**
     * @param name
     */
    public EditExamNewTest(java.lang.String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditExamNew";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testEditExamsV4dataset.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamsV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathNoRooms() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamNoRoomsV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathWithRooms() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamWithRoomV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathChangeSeason() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamChangeSeasonV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathAddAndRemoveScopes() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamAddAndRemoveScopesV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathAddAndRemoveExecutionCourses() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamAddAndRemoveExecutionCoursesV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathAddAndRemoveRooms() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamAddAndRemoveRoomsV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathWithNoRoomsAddRooms() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamWithNoRoomsAddRoomsV4dataset.xml";
    }

    protected String getExpectedDataSetFilePathRemoveAllRooms() {
        return "etc/datasets_templates/servicos/sop/testExpectedEditExamRemoveAllRoomsV4dataset.xml";
    }

    protected Calendar originalExamDate() {
        Calendar examDate = Calendar.getInstance();

        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 9);

        return examDate;
    }

    protected Calendar originalExamDate2() {
        Calendar examDate = Calendar.getInstance();

        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 10);

        return examDate;
    }

    protected Calendar originalExamStartTime() {
        Calendar examStartTime = Calendar.getInstance();

        examStartTime.set(Calendar.HOUR_OF_DAY, 10);
        examStartTime.set(Calendar.MINUTE, 0);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        return examStartTime;
    }

    protected Calendar originalExamStartTime2() {
        Calendar examStartTime = Calendar.getInstance();

        examStartTime.set(Calendar.HOUR_OF_DAY, 11);
        examStartTime.set(Calendar.MINUTE, 30);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        return examStartTime;
    }

    protected Calendar originalExamEndTime() {
        Calendar examEndTime = Calendar.getInstance();

        examEndTime.set(Calendar.HOUR_OF_DAY, 13);
        examEndTime.set(Calendar.MINUTE, 0);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        return examEndTime;
    }

    protected Calendar originalExamEndTime2() {
        Calendar examEndTime = Calendar.getInstance();

        examEndTime.set(Calendar.HOUR_OF_DAY, 12);
        examEndTime.set(Calendar.MINUTE, 30);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        return examEndTime;
    }

    protected Calendar editExamDate() {
        Calendar examDate = Calendar.getInstance();

        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 12);

        return examDate;
    }

    protected Calendar editExamStartTime() {
        Calendar examStartTime = Calendar.getInstance();

        examStartTime.set(Calendar.HOUR_OF_DAY, 11);
        examStartTime.set(Calendar.MINUTE, 0);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        return examStartTime;
    }

    protected Calendar editExamEndTime() {
        Calendar examEndTime = Calendar.getInstance();

        examEndTime.set(Calendar.HOUR_OF_DAY, 12);
        examEndTime.set(Calendar.MINUTE, 0);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        return examEndTime;
    }

    // test successfull edit exam all elements
    public void testSuccessfullEditExamNewAddElementsAndChangeDateAndTimeAndSeason() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36723", "36721" // new execution
                                                             // course
        };
        String[] scopeIDArray = { "681", "682", "3004", "17190", "17191", "17192", "17574", "17537",
                "17850", "17851", "17335" //new scope added
        };
        String[] roomIDArray = { "232", "360" //new room added
        };

        Integer examID = new Integer(4526);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddElementsAndChangeDateAndTimeAndSeason - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddElementsAndChangeDateAndTimeAndSeason - Exception " + ex);
        }
    }

    // test successfull edit date and Time in exam with no rooms
    public void testSuccessfullEditExamNewChangeDateAndTimeOnExamWithoutRooms() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36721" };
        String[] scopeIDArray = { "18276" };
        String[] roomIDArray = {};

        Integer examID = new Integer(4527);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathNoRooms());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewChangeDateAndTimeOnExamWithoutRooms - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewChangeDateAndTimeOnExamWithoutRooms - Exception " + ex);
        }
    }

    // test successfull edit exam date and time with rooms
    public void testSuccessfullEditExamNewChangeDateAndTime() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36723" };
        String[] scopeIDArray = { "681", "682", "3004", "17190", "17191", "17192", "17574", "17537",
                "17850", "17851" };
        String[] roomIDArray = { "232" };

        Integer examID = new Integer(4526);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathWithRooms());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewChangeDateAndTime - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewChangeDateAndTime - Exception " + ex);
        }
    }

    // test successfull edit exam season
    public void testSuccessfullEditExamNewChangeSeason() {
        Calendar examDate = originalExamDate();
        Calendar examStartTime = originalExamStartTime();
        Calendar examEndTime = originalExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36723" };
        String[] scopeIDArray = { "681", "682", "3004", "17190", "17191", "17192", "17574", "17537",
                "17850", "17851" };
        String[] roomIDArray = { "232" };

        Integer examID = new Integer(4526);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathChangeSeason());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewChangeSeason - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewChangeSeason - Exception " + ex);
        }
    }

    // test successfull edit exam add and remove scopes
    public void testSuccessfullEditExamNewAddAndRemoveScopes() {
        Calendar examDate = originalExamDate2();
        Calendar examStartTime = originalExamStartTime2();
        Calendar examEndTime = originalExamEndTime2();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36721" };
        String[] scopeIDArray = { "17335" }; //removed 18276 and added 17335
        String[] roomIDArray = {};

        Integer examID = new Integer(4527);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathAddAndRemoveScopes());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddAndRemoveScopes - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddAndRemoveScopes - Exception " + ex);
        }
    }

    // test successfull edit exam add and remove execution courses
    public void testSuccessfullEditExamNewAddAndRemoveExecutionCourses() {
        Calendar examDate = originalExamDate();
        Calendar examStartTime = originalExamStartTime();
        Calendar examEndTime = originalExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36422" }; //removed 36723 and
                                                       // added 36422
        String[] scopeIDArray = { "724", "731" };
        String[] roomIDArray = { "232" };

        Integer examID = new Integer(4526);
        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathAddAndRemoveExecutionCourses());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddAndRemoveExecutionCourses - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddAndRemoveExecutionCourses - Exception " + ex);
        }
    }

    // test successfull edit exam add and remove rooms
    public void testSuccessfullEditExamNewAddAndRemoveRooms() {
        Calendar examDate = originalExamDate();
        Calendar examStartTime = originalExamStartTime();
        Calendar examEndTime = originalExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36723" };
        String[] scopeIDArray = { "681", "682", "3004", "17190", "17191", "17192", "17574", "17537",
                "17850", "17851", };
        String[] roomIDArray = { "360", //removed 232 and added 360
        };

        Integer examID = new Integer(4526);
        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathAddAndRemoveRooms());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddAndRemoveRooms - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewAddAndRemoveRooms - Exception " + ex);
        }
    }

    // test successfull edit exam with no rooms add rooms
    public void testSuccessfullEditExamNewWithNoRoomsAddRooms() {
        Calendar examDate = originalExamDate2();
        Calendar examStartTime = originalExamStartTime2();
        Calendar examEndTime = originalExamEndTime2();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36721" };
        String[] scopeIDArray = { "18276" };
        String[] roomIDArray = { "360", "232" };

        Integer examID = new Integer(4527);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathWithNoRoomsAddRooms());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewWithNoRoomsAddRooms - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewWithNoRoomsAddRooms - Exception " + ex);
        }
    }

    // test successfull edit exam remove all rooms
    public void testSuccessfullEditExamNewRemoveAllRooms() {
        Calendar examDate = originalExamDate();
        Calendar examStartTime = originalExamStartTime();
        Calendar examEndTime = originalExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36723" };
        String[] scopeIDArray = { "681", "682", "3004", "17190", "17191", "17192", "17574", "17537",
                "17850", "17851" };
        String[] roomIDArray = {};

        Integer examID = new Integer(4526);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePathRemoveAllRooms());
        } catch (FenixServiceException ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewRemoveAllRooms - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testSuccessfullEditExamNewRemoveAllRooms - Exception " + ex);
        }
    }

    // test unsuccessfull edit exam date/hour for date/hour
    // were rooms are occupied
    public void testUnsuccessfullEditExamNewChangeDateAndTimeForRoomsOccupied() {
        Calendar examDate = originalExamDate();
        Calendar examStartTime = originalExamStartTime();
        Calendar examEndTime = originalExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36721" };
        String[] scopeIDArray = { "18276" };
        String[] roomIDArray = { "232" };

        Integer examID = new Integer(4527);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewChangeDateAndTimeForRoomsOccupied - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewChangeDateAndTimeForRoomsOccupied - Exception cancelar transacção "
                        + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewChangeDateAndTimeForRoomsOccupied - Exception " + ex);
        }
    }

    // test unsuccessfull edit exam season for already
    //  existing exam for that
    public void testUnsuccessfullEditExamNewChangeSeasonForAlreadyExistingSeason() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36422" };
        String[] scopeIDArray = { "724", "731" };
        String[] roomIDArray = {};

        Integer examID = new Integer(4528);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewChangeSeasonForAlreadyExistingSeason - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewChangeSeasonForAlreadyExistingSeason - Exception cancelar transacção "
                        + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewChangeSeasonForAlreadyExistingSeason - Exception " + ex);
        }
    }

    public void testUnsuccessfullEditExamNewAddExecutionCourseThatAlreadyHasExamForThatSeason() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36422", "36723" //execution course
                                                             // with exam
        };
        String[] scopeIDArray = { "724", "731", "681", "682" };
        String[] roomIDArray = {};

        Integer examID = new Integer(4528);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewAddExecutionCourseThatAlreadyHasExamForThatSeason - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewAddExecutionCourseThatAlreadyHasExamForThatSeason - Exception cancelar transacção "
                        + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewAddExecutionCourseThatAlreadyHasExamForThatSeason - Exception "
                    + ex);
        }
    }

    public void testUnsuccessfullEditExamNewAddScopesThatAlreadyHasExamForThatSeason() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36422" };
        String[] scopeIDArray = { "724", "731" };
        String[] roomIDArray = {};

        Integer examID = new Integer(4528);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewAddScopesThatAlreadyHasExamForThatSeason - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewAddScopesThatAlreadyHasExamForThatSeason - Exception cancelar transacção "
                        + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewAddScopesThatAlreadyHasExamForThatSeason - Exception "
                    + ex);
        }
    }

    public void testUnsuccessfullEditExamNewAddOccupiedRoom() {
        Calendar examDate = originalExamDate();
        Calendar examStartTime = originalExamStartTime2();
        Calendar examEndTime = originalExamEndTime2();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36721" };
        String[] scopeIDArray = { "18276" };
        String[] roomIDArray = { "232" //occupied room
        };

        Integer examID = new Integer(4527);
        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewAddOccupiedRoom - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewAddOccupiedRoom - Exception cancelar transacção " + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewAddOccupiedRoom - Exception " + ex);
        }
    }

    public void testUnsuccessfullEditExamNewWithUnexistentExecutionCourseAndScopeAndRoom() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36000" //unexistent execution
                                                    // course
        };
        String[] scopeIDArray = { "17000" //unexistent scope
        };
        String[] roomIDArray = { "800" //unexistent room
        };

        Integer examID = new Integer(4526);
        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewWithUnexistentExecutionCourseAndScopeAndRoom - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewWithUnexistentExecutionCourseAndScopeAndRoom - Exception cancelar transacção "
                        + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewWithUnexistentExecutionCourseAndScopeAndRoom - Exception "
                    + ex);
        }
    }

    public void testUnsuccessfullEditExamNewRemoveAllExecutionCoursesAndScopes() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = {};
        String[] scopeIDArray = {};
        String[] roomIDArray = { "232" };

        Integer examID = new Integer(4526);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewRemoveAllExecutionCoursesAndScopes - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewRemoveAllExecutionCoursesAndScopes - Exception cancelar transacção "
                        + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewRemoveAllExecutionCoursesAndScopes - Exception " + ex);
        }
    }

    public void testUnsuccessfullEditExamNewThatDoesntExist() {
        Calendar examDate = editExamDate();
        Calendar examStartTime = editExamStartTime();
        Calendar examEndTime = editExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36723" };
        String[] scopeIDArray = { "681" };
        String[] roomIDArray = { "232" };

        Integer examID = new Integer(4500);

        EditExamNew service = new EditExamNew();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examDate, examStartTime, examEndTime, season, executionCourseIDArray,
                    scopeIDArray, roomIDArray, examID);

            sp.confirmarTransaccao();
            fail("testUnsuccessfullEditExamNewThatDoesntExist - Service shouldn't run");
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {
                fail("testUnsuccessfullEditExamNewThatDoesntExist - Exception cancelar transacção " + e);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            fail("testUnsuccessfullEditExamNewThatDoesntExist - Exception " + ex);
        }
    }

}