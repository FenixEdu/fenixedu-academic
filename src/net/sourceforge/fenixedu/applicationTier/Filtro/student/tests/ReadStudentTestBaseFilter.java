/*
 * Created on Dec 14, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student.tests;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author jpvl
 */
public abstract class ReadStudentTestBaseFilter extends AuthorizationByRoleFilter {

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    final public void execute(ServiceRequest request, ServiceResponse response) throws FilterException,
            Exception {
        super.execute(request, response);

        Integer testId = (Integer) request.getServiceParameters().parametersArray()[1];
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentDistributedTest distributedTestDAO = sp.getIPersistentDistributedTest();

        IDistributedTest distributedTest = (IDistributedTest) distributedTestDAO.readByOID(
                DistributedTest.class, testId);

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