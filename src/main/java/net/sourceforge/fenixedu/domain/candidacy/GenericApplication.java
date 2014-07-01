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
package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;
import java.util.Random;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

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
        final String subject =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.email.subject",
                        getGenericApplicationPeriod().getTitle().getContent());
        final String body =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.email.body",
                        getApplicationNumber(), generateConfirmationLink(),
                        getGenericApplicationPeriod().getTitle().getContent(), Unit.getInstitutionAcronym());
        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    private String generateConfirmationLink() {
        final String confirmationCode =
                Hashing.sha512()
                        .hashString(
                                getEmail() + System.currentTimeMillis() + hashCode()
                                        + new Random(System.currentTimeMillis()).nextGaussian(), Charsets.UTF_8).toString();
        setConfirmationCode(confirmationCode);
        return FenixConfigurationManager.getConfiguration().getGenericApplicationEmailConfirmationLink() + confirmationCode
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

    @Deprecated
    public boolean hasNationality() {
        return getNationality() != null;
    }

    @Deprecated
    public boolean hasGenericApplicationPeriod() {
        return getGenericApplicationPeriod() != null;
    }

    @Deprecated
    public boolean hasAnyGenericApplicationComment() {
        return !getGenericApplicationCommentSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyGenericApplicationRecomentation() {
        return !getGenericApplicationRecomentationSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyGenericApplicationFileSet() {
        return !getGenericApplicationFileSet().isEmpty();
    }
}
