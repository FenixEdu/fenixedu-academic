/*
 * Created on Dec 14, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student.tests;

import java.util.Calendar;

/**
 * @author jpvl
 */
public class ReadStudentTestForCorrectionFilter extends ReadStudentTestBaseFilter {

    public static final ReadStudentTestForCorrectionFilter instance = new ReadStudentTestForCorrectionFilter();

    public ReadStudentTestForCorrectionFilter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @seeServidorAplicacao.Filtro.student.tests.StudentReadTestToDoFilter#
     * testIfCanReadTest(java.util.Calendar, java.util.Calendar,
     * java.util.Calendar)
     */
    @Override
    protected boolean canReadTest(Calendar now, Calendar beginDate, Calendar endDate) {
        return endDate.before(now);
    }
}