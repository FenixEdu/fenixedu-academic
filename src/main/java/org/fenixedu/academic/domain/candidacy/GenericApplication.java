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
package org.fenixedu.academic.domain.candidacy;

import java.util.Comparator;
import java.util.Random;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.period.GenericApplicationPeriod;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;

import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import pt.ist.fenixframework.Atomic;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

@DeclareMessageTemplate(
        id = "candidacy.generic.application.email",
        bundle = Bundle.CANDIDATE,
        description = "candidacy.generic.application.description",
        subject = "candidacy.generic.application.subject",
        text = "candidacy.generic.application.text",
        parameters = {
                @TemplateParameter(id = "applicationNumber", description = "candidacy.generic.application.param.application.number"),
                @TemplateParameter(id = "title", description = "candidacy.generic.application.param.title"),
                @TemplateParameter(id = "link", description = "candidacy.generic.application.param.link"),
                @TemplateParameter(id = "institution", description = "candidacy.generic.application.param.institution"),
        }
)
public class GenericApplication extends GenericApplication_Base {

    public static final Comparator<GenericApplication> COMPARATOR_BY_APPLICATION_NUMBER = new Comparator<GenericApplication>() {
        @Override
        public int compare(final GenericApplication o1, final GenericApplication o2) {
            final int n = compareAppNumber(o1.getApplicationNumber(), o2.getApplicationNumber());
            return n == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : n;
        }

        private int compareAppNumber(final String an1, final String an2) {
            int i1 = an1.lastIndexOf('/');
            int i2 = an2.lastIndexOf('/');

            final int c = an1.substring(0, i1).compareTo(an2.substring(0, i2));
            return c == 0 ? Integer.valueOf(an1.substring(i1 + 1)).compareTo(Integer.valueOf(an2.substring(i2 + 1))) : c;
        }

    };

    public GenericApplication(final GenericApplicationPeriod period, final String email) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGenericApplicationPeriod(period);
        setApplicationNumber(period.generateApplicationNumber());
        if (email == null || email.isEmpty()) {
            throw new NullPointerException("error.email.cannot.be.null");
        }
        setEmail(email);
        sendEmailForApplication();
        setIdDocumentType(IDDocumentType.IDENTITY_CARD);
        final Unit institutionUnit = Bennu.getInstance().getInstitutionUnit();
        if (institutionUnit != null) {
            setNationality(institutionUnit.getCountry());
        }
    }

    public void sendEmailForApplication() {
        Message.fromSystem()
                .replyToSender()
                .singleBcc(getEmail())
                .template("candidacy.generic.application.email")
                    .parameter("applicationNumber", getApplicationNumber())
                    .parameter("title", getGenericApplicationPeriod().getTitle())
                    .parameter("link", generateConfirmationLink())
                    .parameter("institution", Unit.getInstitutionAcronym())
                .and()
                .send();
    }

    private String generateConfirmationLink() {
        final String confirmationCode =
                Hashing.sha512()
                        .hashString(
                                getEmail() + System.currentTimeMillis() + hashCode()
                                        + new Random(System.currentTimeMillis()).nextGaussian(), Charsets.UTF_8).toString();
        setConfirmationCode(confirmationCode);
        return FenixEduAcademicConfiguration.getConfiguration().getGenericApplicationEmailConfirmationLink() + confirmationCode
                + "&applicationExternalId=" + getExternalId();
    }

    public boolean isAllPersonalInformationFilled() {
        return getGender() != null && getDateOfBirthYearMonthDay() != null && getDocumentIdNumber() != null
                && !getDocumentIdNumber().isEmpty() && getIdDocumentType() != null && getNationality() != null
                /* && getFiscalCode() != null && !getFiscalCode().isEmpty() */&& getAddress() != null && !getAddress().isEmpty()
                && getAreaCode() != null && !getAreaCode().isEmpty() && getArea() != null && !getArea().isEmpty()
                && getTelephoneContact() != null && !getTelephoneContact().isEmpty();
    }

    public int getAvailableGenericApplicationRecomentationCount() {
        int result = 0;
        for (final GenericApplicationRecomentation recomentation : getGenericApplicationRecomentationSet()) {
            if (recomentation.getLetterOfRecomentation() != null) {
                result++;
            }
        }
        return result;
    }

    @Atomic
    public void submitApplication() {
        this.setSubmitted(true);
    }

}
