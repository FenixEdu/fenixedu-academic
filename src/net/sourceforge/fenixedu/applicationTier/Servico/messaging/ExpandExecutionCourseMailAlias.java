/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.ExpandExecutionCourseMailAlias.ForwardMailsReport.AliasExpandingStatus;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 18, 2006, 12:07:00 PM
 * 
 */
public class ExpandExecutionCourseMailAlias extends Service {

    public static class ForwardMailsReport {

        public static enum AliasExpandingStatus {
            OK, INVALID_ADDRESS, UNKNOWN_EXECUTION_COURSE, DYNAMIC_MAIL_DELIVERY_DISABLE, INVALID_HOST, EC_SITE_NOT_AVAILABLE;
        }

        private AliasExpandingStatus status;

        private Collection<String> expandedAddresses = new ArrayList<String>();

        public Collection<String> getExpandedAddresses() {
            return this.expandedAddresses;
        }

        public AliasExpandingStatus getStatus() {
            return this.status;
        }
    }

    public ForwardMailsReport run(String address, String prefix, String host)
            throws FenixServiceException {
        ForwardMailsReport report = new ForwardMailsReport();

        Integer id = this.extractExecutionCourseId(address, prefix, host, report);
        if (id != null) {
            ExecutionCourse course = RootDomainObject.getInstance().readExecutionCourseByOID(id);
            if (course != null) {
                List<String> addresses = new ArrayList<String>();
                if (course.getSite() != null) {
                    if (course.getSite().getDynamicMailDistribution()) {
                        for (Professorship professorship : course.getProfessorships()) {
                            addresses.add(professorship.getTeacher().getPerson().getEmail());
                        }
                        report.status = AliasExpandingStatus.OK;
                        report.expandedAddresses = addresses;
                    } else {
                        report.status = AliasExpandingStatus.DYNAMIC_MAIL_DELIVERY_DISABLE;
                    }
                } else {

                    report.status = AliasExpandingStatus.EC_SITE_NOT_AVAILABLE;
                }
            } else {
                report.status = AliasExpandingStatus.UNKNOWN_EXECUTION_COURSE;
            }
        }
        return report;

    }

    private Integer extractExecutionCourseId(String address, String emailAddressPrefix, String host,
            ForwardMailsReport report) {
        Integer result = null;
        if (address != null) {
            String[] splittedAddress = address.split("@");
            if (splittedAddress.length == 2) {
                if (splittedAddress[1].equals(host)) {
                    if (splittedAddress[0].startsWith(emailAddressPrefix)) {
                        String stringId = splittedAddress[0].substring(emailAddressPrefix.length());
                        try {
                            result = Integer.valueOf(stringId);
                        } catch (NumberFormatException e) {
                            report.status = AliasExpandingStatus.INVALID_ADDRESS;
                        }
                    } else {
                        report.status = AliasExpandingStatus.INVALID_ADDRESS;
                    }
                } else {
                    report.status = AliasExpandingStatus.INVALID_HOST;
                }
            } else {
                report.status = AliasExpandingStatus.INVALID_ADDRESS;
            }
        } else {
            report.status = AliasExpandingStatus.INVALID_ADDRESS;
        }
        return result;
    }
}
