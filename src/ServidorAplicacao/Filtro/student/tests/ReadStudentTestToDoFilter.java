/*
 * Created on Dec 14, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.student.tests;

import java.util.Calendar;

/**
 * @author jpvl
 */
public class ReadStudentTestToDoFilter extends ReadStudentTestBaseFilter
{
    public ReadStudentTestToDoFilter()
    {

    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.student.tests.ReadStudentTestBaseFilter#canReadTest(java.util.Calendar,
	 *          java.util.Calendar, java.util.Calendar)
	 */
    protected boolean canReadTest(Calendar now, Calendar beginDate, Calendar endDate)
    {
        // TODO Auto-generated method stub
        Calendar endDate2Compare = Calendar.getInstance();
        endDate2Compare.setTimeInMillis(endDate.getTimeInMillis());
        endDate2Compare.set(Calendar.MINUTE, endDate2Compare.get(Calendar.MINUTE)+1);
        return ((now.after(beginDate)) && (now.before(endDate2Compare)));
    }

}