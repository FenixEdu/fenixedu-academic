/*
 * Created on Dec 14, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.student.tests;

import java.util.Calendar;

/**
 * @author jpvl
 */
public class ReadStudentTestForCorrectionFilter extends ReadStudentTestBaseFilter
{
    public ReadStudentTestForCorrectionFilter()
    {
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.student.tests.StudentReadTestToDoFilter#testIfCanReadTest(java.util.Calendar,
	 *          java.util.Calendar, java.util.Calendar)
	 */
    protected boolean canReadTest(Calendar now, Calendar beginDate, Calendar endDate)
    {
        return endDate.before(now);
    }
}