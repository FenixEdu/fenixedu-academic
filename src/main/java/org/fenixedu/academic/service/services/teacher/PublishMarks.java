/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.ExcepcaoInexistente;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério
 */
public class PublishMarks {

    private final static int MOBILE_NUMBER_LENGHT = 9;

    private final static String VODAFONE_NETWORK_PREFIX = "91";

    private final static String TMN_NETWORK_PREFIX = "96";

    private final static String OPTIMUS_NETWORK_PREFIX = "93";

    protected Object run(String executionCourseCode, String evaluationCode, String publishmentMessage, Boolean sendSMS,
            String announcementTitle) throws ExcepcaoInexistente, FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationCode);

        if (publishmentMessage == null || publishmentMessage.length() == 0) {
            evaluation.setPublishmentMessage(" ");
        } else {
            evaluation.setPublishmentMessage(publishmentMessage);
        }

        for (Mark mark : evaluation.getMarksSet()) {
            if (!mark.getMark().equals(mark.getPublishedMark())) {
                // update published mark
                mark.setPublishedMark(mark.getMark());
                if (sendSMS != null && sendSMS) {
                    if (mark.getAttend().getRegistration().getPerson().getDefaultMobilePhoneNumber() != null
                            || mark.getAttend().getRegistration().getPerson().getDefaultMobilePhoneNumber().length() == MOBILE_NUMBER_LENGHT) {
                        String StringDestinationNumber =
                                mark.getAttend().getRegistration().getPerson().getDefaultMobilePhoneNumber();

                        if (StringDestinationNumber.startsWith(TMN_NETWORK_PREFIX)
                                || StringDestinationNumber.startsWith(VODAFONE_NETWORK_PREFIX)
                                || StringDestinationNumber.startsWith(OPTIMUS_NETWORK_PREFIX)) {

//                            try {
//                                SmsUtil.getInstance().sendSmsWithoutDeliveryReports(
//                                        Integer.valueOf(StringDestinationNumber),
//                                        evaluation.getPublishmentMessage() + " "
//                                                + mark.getAttend().getExecutionCourse().getSigla() + " - " + mark.getMark());
//                            } catch (FenixUtilException e1) {
//                                throw new SmsNotSentServiceException("error.person.sendSms");
//                            }
                        }
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final PublishMarks serviceInstance = new PublishMarks();

    @Atomic
    public static Object runPublishMarks(String executionCourseCode, String evaluationCode, String publishmentMessage,
            Boolean sendSMS, String announcementTitle) throws ExcepcaoInexistente, FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, evaluationCode, publishmentMessage, sendSMS, announcementTitle);
    }

}