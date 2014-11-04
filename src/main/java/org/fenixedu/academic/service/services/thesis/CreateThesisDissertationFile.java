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
package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import java.io.IOException;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis.ScientificCouncilOrStudentThesisAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class CreateThesisDissertationFile extends CreateThesisFile {

    @Override
    protected void removePreviousFile(Thesis thesis) {
        ThesisFile dissertation = thesis.getDissertation();
        if (dissertation != null) {
            if (Authenticate.getUser().getPerson().hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
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
