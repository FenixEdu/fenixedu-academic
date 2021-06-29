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
package org.fenixedu.academic.ui.spring.controller.teacher;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.MarkSheet;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class MarkSheetController {

    @SkipCSRF
    @RequestMapping(value = "/teacher/{executionCourse}/uploadSignedMarkSheet/{markSheet}", method = RequestMethod.POST)
    public String uploadSignedMarkSheet(final Model model, @PathVariable ExecutionCourse executionCourse,
                                        @PathVariable MarkSheet markSheet, @RequestParam MultipartFile signedMarkSheet,
                                        final HttpServletRequest request) {
        if (markSheet != null && markSheet.getResponsibleTeacher() != null
                && markSheet.getResponsibleTeacher().getPerson().getUser() == Authenticate.getUser()) {
            try {
                markSheet.saveSignedMarkSheet(signedMarkSheet.getBytes());
            } catch (final IOException e) {
                throw new Error(e);
            }
        }

        final String path = request.getContextPath() + "/teacher/markSheetManagement.do?method=viewMarkSheet&msID="
                + markSheet.getExternalId() + "&executionCourseID=" + executionCourse.getExternalId();
        return "redirect:" + path + "&_request_checksum_="
                + GenericChecksumRewriter.calculateChecksum(request.getContextPath() + path, request.getSession(false));
    }

}
