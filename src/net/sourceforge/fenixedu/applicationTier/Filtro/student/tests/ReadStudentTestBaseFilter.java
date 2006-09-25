/*
 * Created on Dec 14, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student.tests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author jpvl
 */
public abstract class ReadStudentTestBaseFilter extends AuthorizationByRoleFilter {

    final public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        super.execute(request, response);

        Object object = request.getServiceParameters().parametersArray()[1];
        DistributedTest distributedTest = null;
        if (object instanceof Integer) {
            final Integer testId = (Integer) object;
            distributedTest = rootDomainObject.readDistributedTestByOID(testId);
        } else if (object instanceof DistributedTest) {
            distributedTest = (DistributedTest) object;
        }

        if (distributedTest != null) {
            Calendar now = Calendar.getInstance();

            Calendar beginDate = distributedTest.getBeginDate();
            Calendar beginHour = distributedTest.getBeginHour();
            getFullCalendar(beginDate, beginHour);

            Calendar endDate = distributedTest.getEndDate();
            Calendar endHour = distributedTest.getEndHour();
            getFullCalendar(endDate, endHour);

            if (!canReadTest(now, beginDate, endDate)) {
                throw new NotAuthorizedFilterException();
            }

        }

    }

    abstract protected boolean canReadTest(Calendar now, Calendar beginDate, Calendar endDate);

    private void getFullCalendar(Calendar beginDate, Calendar beginHour) {
        beginDate.set(Calendar.HOUR_OF_DAY, beginHour.get(Calendar.HOUR_OF_DAY));
        beginDate.set(Calendar.MINUTE, beginHour.get(Calendar.MINUTE));
        beginDate.set(Calendar.SECOND, 0);
        beginDate.set(Calendar.MILLISECOND, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    final protected RoleType getRoleType() {

        return RoleType.STUDENT;
    }

}