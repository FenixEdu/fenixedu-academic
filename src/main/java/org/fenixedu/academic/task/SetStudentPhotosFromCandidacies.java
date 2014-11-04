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
package org.fenixedu.academic.task;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PhotoState;
import org.fenixedu.academic.domain.PhotoType;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.photograph.Picture;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.ContentType;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "SetStudentPhotosFromCandidacies", readOnly = false)
public class SetStudentPhotosFromCandidacies extends CronTask {

    @Override
    public void runTask() throws Exception {
        int missing = 0;
        int withContent = 0;
        int withContentType = 0;
        int unableToProcessImage = 0;
        int fixed = 0;
        for (final IndividualCandidacy candidacy : Bennu.getInstance().getIndividualCandidaciesSet()) {
            for (final IndividualCandidacyDocumentFile file : candidacy.getDocumentsSet()) {
                final IndividualCandidacyDocumentFileType type = file.getCandidacyFileType();
                if (type == IndividualCandidacyDocumentFileType.PHOTO) {
                    final Registration registration = candidacy.getRegistration();
                    if (registration != null) {
                        final Person person = registration.getPerson();
                        final Photograph personalPhotoEvenIfPending = person.getPersonalPhotoEvenIfPending();
                        if (personalPhotoEvenIfPending == null) {
                            missing++;
                            final byte[] content = file.getContent();
                            if (content != null && content.length > 0) {
                                withContent++;
                                final ContentType contentType = ContentType.getContentType(file.getContentType());
                                if (contentType != null) {
                                    withContentType++;
                                    if (chew(content)) {
                                        final Photograph p = new Photograph(PhotoType.INSTITUTIONAL, contentType, content);
                                        p.setState(PhotoState.APPROVED);
                                        person.setPersonalPhoto(p);
                                        fixed++;
                                    } else {
                                        unableToProcessImage++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        taskLog("Missing: %s photos.%n", Integer.toString(missing));
        taskLog("Possible Content Fix: %s.%n", Integer.toString(withContent));
        taskLog("Valid Content Type Fix: %s.%n", Integer.toString(withContentType));
        taskLog("Unable to chew: %s.%n", Integer.toString(unableToProcessImage));
        taskLog("Fixed: %s photos.%n", Integer.toString(fixed));
    }

    private boolean chew(final byte[] content) {
        try {
            Picture.readImage(content);
            return true;
        } catch (final DomainException ex) {
            return false;
        }
    }

}
