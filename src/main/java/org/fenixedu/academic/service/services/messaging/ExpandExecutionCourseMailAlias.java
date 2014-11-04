/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package org.fenixedu.academic.service.services.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.messaging.ExpandExecutionCourseMailAlias.ForwardMailsReport.AliasExpandingStatus;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on May 18, 2006, 12:07:00 PM
 * 
 */
public class ExpandExecutionCourseMailAlias {

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

    @Atomic
    public static ForwardMailsReport run(String address, String prefix, String host) throws FenixServiceException {
        ForwardMailsReport report = new ForwardMailsReport();

        String id = extractExecutionCourseId(address, prefix, host, report);
        if (id != null) {
            ExecutionCourse course = FenixFramework.getDomainObject(id);
            if (course != null) {
                List<String> addresses = new ArrayList<String>();
                if (course.getDynamicMailDistribution()) {
                    for (Professorship professorship : course.getProfessorshipsSet()) {
                        addresses.add(professorship.getPerson().getEmail());
                    }
                    report.status = AliasExpandingStatus.OK;
                    report.expandedAddresses = addresses;
                } else {
                    report.status = AliasExpandingStatus.DYNAMIC_MAIL_DELIVERY_DISABLE;
                }
            } else {
                report.status = AliasExpandingStatus.UNKNOWN_EXECUTION_COURSE;
            }
        }
        return report;

    }

    private static String extractExecutionCourseId(String address, String emailAddressPrefix, String host,
            ForwardMailsReport report) {
        String result = null;
        if (address != null) {
            String[] splittedAddress = address.split("@");
            if (splittedAddress.length == 2) {
                if (splittedAddress[1].equals(host)) {
                    if (splittedAddress[0].startsWith(emailAddressPrefix)) {
                        String stringId = splittedAddress[0].substring(emailAddressPrefix.length());
                        try {
                            result = stringId;
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