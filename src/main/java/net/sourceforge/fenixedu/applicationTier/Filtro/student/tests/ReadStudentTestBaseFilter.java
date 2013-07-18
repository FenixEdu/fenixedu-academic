/*
 * Created on Dec 14, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student.tests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author jpvl
 */
public abstract class ReadStudentTestBaseFilter extends AuthorizationByRoleFilter {

    final public void execute(Integer testId) throws NotAuthorizedException {
        super.execute();

        DistributedTest distributedTest = RootDomainObject.getInstance().readDistributedTestByOID(testId);

        if (distributedTest != null) {
            Calendar now = Calendar.getInstance();

            Calendar beginDate = distributedTest.getBeginDate();
            Calendar beginHour = distributedTest.getBeginHour();
            getFullCalendar(beginDate, beginHour);

            Calendar endDate = distributedTest.getEndDate();
            Calendar endHour = distributedTest.getEndHour();
            getFullCalendar(endDate, endHour);

            if (!canReadTest(now, beginDate, endDate)) {
                throw new NotAuthorizedException();
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
    @Override
    final protected RoleType getRoleType() {

        return RoleType.STUDENT;
    }

}