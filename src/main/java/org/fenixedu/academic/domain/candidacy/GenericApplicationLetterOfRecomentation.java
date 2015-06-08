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

import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class GenericApplicationLetterOfRecomentation extends GenericApplicationLetterOfRecomentation_Base {

    public GenericApplicationLetterOfRecomentation(GenericApplicationRecomentation recomentation, String displayName,
            String fileName, byte[] content) {
        super();
        init(displayName, fileName, content);
        setRecomentation(recomentation);
        sendEmailForRecommendationUploadNotification();
    }

    @Override
    public void setFilename(String filename) {
        super.setFilename(FileUtils.cleanupUserInputFilename(filename));
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(FileUtils.cleanupUserInputFileDisplayName(displayName));
    }

    @Override
    public boolean isAccessible(User user) {
        return false;
    }

    @Override
    public void delete() {
        setRecomentation(null);
        super.delete();
    }

    public void sendEmailForRecommendationUploadNotification() {
        final String subject =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.recomentation.upload.notification.email.subject");
        final String body =
                BundleUtil.getString(Bundle.CANDIDATE, "label.application.recomentation.upload.notification.email.body",
                        getRecomentation().getName(), getRecomentation().getInstitution());

        new Message(Bennu.getInstance().getSystemSender(), getRecomentation().getGenericApplication().getEmail(), subject, body);
    }

}
