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

import java.util.Random;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class GenericApplicationRecomentation extends GenericApplicationRecomentation_Base {

    public GenericApplicationRecomentation(GenericApplication application, String title, String name, String institution,
            String email) {
        setRootDomainObject(Bennu.getInstance());
        final String confirmationCode =
                Hashing.sha512()
                        .hashString(
                                getEmail() + System.currentTimeMillis() + hashCode()
                                        + new Random(System.currentTimeMillis()).nextGaussian(), Charsets.UTF_8).toString();
        setConfirmationCode(confirmationCode);
        setEmail(email);
        setGenericApplication(application);
        setInstitution(institution);
        setTitle(title);
        setName(name);
        setRequestTime(new DateTime());
        sendEmailForRecommendation();
    }

    @Atomic
    public void sendEmailForRecommendation() {
        final String subject =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.recomentation.email.subject", getGenericApplication()
                        .getName());
        final String body =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.recomentation.email.body", getTitle(), getName(),
                        getGenericApplication().getName(), getGenericApplication().getGenericApplicationPeriod().getTitle()
                                .getContent(), generateConfirmationLink());

        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    @Atomic
    public void sendEmailForRecommendationDelete() {
        final String subject =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.recomentation.email.subject", getGenericApplication()
                        .getName());
        final String body =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.recomentation.email.delete.body", getTitle(),
                        getName(), getGenericApplication().getName(), getGenericApplication().getGenericApplicationPeriod()
                                .getTitle().getContent());

        new Message(getRootDomainObject().getSystemSender(), getEmail(), subject, body);
    }

    private String generateConfirmationLink() {
        return FenixEduAcademicConfiguration.getConfiguration().getGenericApplicationEmailRecommendationLink()
                + getConfirmationCode() + "&recommendationExternalId=" + getExternalId();
    }

}
