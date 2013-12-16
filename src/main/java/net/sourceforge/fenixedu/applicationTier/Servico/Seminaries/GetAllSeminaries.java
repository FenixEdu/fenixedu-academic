package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class GetAllSeminaries {

    protected List run(Boolean inEnrollmentPeriod) throws BDException {
        List<InfoSeminary> result = new LinkedList<InfoSeminary>();

        Collection<Seminary> seminaries = Bennu.getInstance().getSeminarysSet();
        for (Seminary seminary : seminaries) {

            if (!inEnrollmentPeriod) {
                result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
            } else {
                final DateTime now = new DateTime();
                if (!now.isBefore(seminary.getEnrollmentBeginYearMonthDay().toDateMidnight())
                        && !now.isAfter(seminary.getEnrollmentEndYearMonthDay().toDateMidnight())) {
                    result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
                }
            }
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final GetAllSeminaries serviceInstance = new GetAllSeminaries();

    @Atomic
    public static List runGetAllSeminaries(Boolean inEnrollmentPeriod) throws BDException, NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(inEnrollmentPeriod);
    }

}