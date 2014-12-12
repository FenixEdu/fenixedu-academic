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
package org.fenixedu.academic.service.services.thesis;

import java.io.IOException;
import java.util.Locale;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisFile;
import org.fenixedu.academic.service.filter.student.thesis.ScientificCouncilOrStudentThesisAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class CreateThesisDissertationFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile dissertation = thesis.getDissertation();
        if (dissertation != null) {
            if (RoleType.SCIENTIFIC_COUNCIL.isMember(Authenticate.getUser().getPerson().getUser())) {
                dissertation.deleteWithoutStateCheck();
            } else {
                dissertation.delete();
            }
        }
    }

    @Override
    protected void updateThesis(Thesis thesis, ThesisFile file, String title, String subTitle, Locale language, String fileName,
            byte[] bytes) throws FenixServiceException, IOException {
        if (title == null) {
            throw new DomainException("thesis.files.dissertation.title.required");
        }

        file.setTitle(title);
        file.setSubTitle(subTitle == null ? "" : subTitle);
        file.setLanguage(language);

        thesis.setDissertation(file);
    }

    // Service Invokers migrated from Berserk

    private static final CreateThesisDissertationFile serviceInstance = new CreateThesisDissertationFile();

    @Atomic
    public static ThesisFile runCreateThesisDissertationFile(Thesis thesis, byte[] bytes, String fileName, String title,
            String subTitle, Locale language) throws FenixServiceException, IOException {
        ScientificCouncilOrStudentThesisAuthorizationFilter.instance.execute(thesis);
        return serviceInstance.run(thesis, bytes, fileName, title, subTitle, language);
    }

}
