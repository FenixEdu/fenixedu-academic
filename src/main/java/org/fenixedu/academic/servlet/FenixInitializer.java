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
package org.fenixedu.academic.servlet;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.organizationalStructure.UnitNamePart;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.service.StudentWarningsDefaultCheckers;
import org.fenixedu.academic.service.StudentWarningsService;
import org.fenixedu.academic.ui.struts.action.externalServices.PhoneValidationUtils;
import org.fenixedu.bennu.core.api.SystemResource;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.rest.Healthcheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.smtp.SMTPTransport;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestChecksumFilter.ChecksumPredicate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@WebListener
public class FenixInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(FenixInitializer.class);

    @Override
    @Atomic(mode = TxMode.READ)
    public void contextInitialized(ServletContextEvent event) {

        setEmptyAcademicPeriodsCompetenceCourses();
        setEmptyAcademicPeriodsCalendarEntries();

        Installation.ensureInstallation();
        loadUnitNames();
        startContactValidationServices();

        registerChecksumFilterRules();

        registerHealthchecks();
        registerDefaultStudentWarningCheckers();

    }

    @Atomic
    private void setEmptyAcademicPeriodsCompetenceCourses() {
        final Set<CompetenceCourseInformation> courseInformations = Bennu.getInstance().getCompetenceCourseInformationsSet();
        Set<CompetenceCourseInformation> courseInformationswithoutPeriods =
                courseInformations.stream().filter(cci -> cci.getPersistentAcademicPeriod() == null).collect(Collectors.toSet());
        int withoutPeriodBefore = courseInformationswithoutPeriods.size();
        if (withoutPeriodBefore > 0) {
            final AtomicInteger counter = new AtomicInteger();
            courseInformationswithoutPeriods.stream().forEach(cci -> {
                boolean success = cci.populateAcademicPeriod();
                if (success) {
                    counter.incrementAndGet();
                }
            });
            long withoutPeriodAfter =
                    courseInformations.stream().filter(cci -> cci.getPersistentAcademicPeriod() == null).count();

            logger.info("START setting Academic Periods @ CCI");
            logger.info(withoutPeriodBefore + " CCI without AcademicPeriod (before)");
            logger.info(counter.intValue() + " CCI changed");
            logger.info(withoutPeriodAfter + " CCI without AcademicPeriod (after)");
            logger.info("END setting Academic Periods @ CCI");
        }
    }

    @Atomic
    private void setEmptyAcademicPeriodsCalendarEntries() {
        final Set<AcademicCalendarEntry> calendarEntries = Bennu.getInstance().getAcademicCalendarEntriesSet();
        Set<AcademicCalendarEntry> calendarEntriesWithoutPeriods =
                calendarEntries.stream().filter(ace -> ace.getPersistentAcademicPeriod() == null).collect(Collectors.toSet());
        int withoutPeriodBefore = calendarEntriesWithoutPeriods.size();
        if (withoutPeriodBefore > 0) {
            final AtomicInteger counter = new AtomicInteger();
            calendarEntriesWithoutPeriods.stream().forEach(ace -> {
                boolean success = ace.populateAcademicPeriod();
                if (success) {
                    counter.incrementAndGet();
                }
            });
            long withoutPeriodAfter = calendarEntries.stream().filter(ace -> ace.getPersistentAcademicPeriod() == null).count();

            logger.info("START setting Academic Periods @ Calendar Entries");
            logger.info(withoutPeriodBefore + " Calendar Entries without AcademicPeriod (before)");
            logger.info(counter.intValue() + " Calendar Entries changed");
            logger.info(withoutPeriodAfter + " Calendar Entries without AcademicPeriod (after)");
            logger.info("END setting Academic Periods @ Calendar Entries");
        }
    }

    private void registerDefaultStudentWarningCheckers() {
        StudentWarningsService.register(StudentWarningsDefaultCheckers.WARNING_VALID_ID_DOCUMENT);
    }

    private void registerHealthchecks() {
        SystemResource.registerHealthcheck(new Healthcheck() {
            @Override
            public String getName() {
                return "SMTP";
            }

            @Override
            protected Result check() throws Exception {
                final Properties properties = new Properties();
                properties.put("mail.transport.protocol", "smtp");
                Transport transport = Session.getInstance(properties).getTransport();
                transport.connect(FenixEduAcademicConfiguration.getConfiguration().getMailSmtpHost(), null, null);
                String response = ((SMTPTransport) transport).getLastServerResponse();
                transport.close();
                return Result.healthy("SMTP server returned response: " + response);
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    private void startContactValidationServices() {
        PhoneValidationUtils.getInstance();
    }

    private void loadUnitNames() {
        long start = System.currentTimeMillis();
        UnitNamePart.find("...PlaceANonExistingUnitNameHere...");
        long end = System.currentTimeMillis();
        logger.debug("Load of all unit names took: " + (end - start) + "ms.");
    }

    private void registerChecksumFilterRules() {
        RequestChecksumFilter.registerFilterRule(new ChecksumPredicate() {
            @Override
            public boolean shouldFilter(HttpServletRequest request) {
                final String uri = request.getRequestURI().substring(request.getContextPath().length());
                if (uri.indexOf("home.do") >= 0) {
                    return false;
                }
                if (uri.indexOf("/student/fillInquiries.do") >= 0) {
                    return false;
                }
                if ((uri.indexOf("/teacher/executionCourseForumManagement.do") >= 0
                        || uri.indexOf("/student/viewExecutionCourseForuns.do") >= 0)
                        && request.getQueryString().indexOf("method=viewThread") >= 0) {
                    return false;
                }
                if (uri.indexOf("notAuthorized.do") >= 0) {
                    return false;
                }
                return (uri.indexOf("external/") == -1) && (uri.indexOf("login.do") == -1) && (uri.indexOf("loginCAS.do") == -1)
                        && (uri.indexOf("logoff.do") == -1) && (uri.indexOf("publico/") == -1)
                        && (uri.indexOf("siteMap.do") == -1);
            }

        });
    }

}
