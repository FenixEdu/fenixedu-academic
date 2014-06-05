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

import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;

public class GenericApplicationLetterOfRecomentation extends GenericApplicationLetterOfRecomentation_Base {

    public GenericApplicationLetterOfRecomentation(GenericApplicationRecomentation recomentation, String displayName,
            String fileName, byte[] content) {
        super();
        init(fileName, displayName, content, NobodyGroup.get());
        setRecomentation(recomentation);
        sendEmailForRecommendationUploadNotification();
    }

    @Atomic
    public void deleteFromApplication() {
        delete();
    }

    @Override
    protected void disconnect() {
        setRecomentation(null);
        super.disconnect();
    }

    public void sendEmailForRecommendationUploadNotification() {
        final String subject =
                BundleUtil.getString(Bundle.CANDIDATE,
                        "label.application.recomentation.upload.notification.email.subject");
        final String body =
                BundleUtil.getString(Bundle.CANDIDATE,
                        "label.application.recomentation.upload.notification.email.body", getRecomentation().getName(),
                        getRecomentation().getInstitution());

        new Message(Bennu.getInstance().getSystemSender(), getRecomentation().getGenericApplication().getEmail(), subject, body);
    }

    @Deprecated
    public boolean hasRecomentation() {
        return getRecomentation() != null;
    }

}
