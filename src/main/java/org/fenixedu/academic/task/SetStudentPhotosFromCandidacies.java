package org.fenixedu.academic.task;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.photograph.Picture;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.ContentType;

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
