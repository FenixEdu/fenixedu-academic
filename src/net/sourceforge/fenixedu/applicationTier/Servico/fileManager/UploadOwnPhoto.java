package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Pedro Santos (pmrsa)
 */
public class UploadOwnPhoto extends FenixService {

    @Checked("RolePredicates.PERSON_PREDICATE")
    @Service
    static public void run(byte[] contents, byte[] compressed, ContentType contentType) {
        Person person = AccessControl.getPerson();
        person.setPersonalPhoto(new Photograph(contentType, new ByteArray(contents), new ByteArray(compressed), PhotoType.USER));
    }

    /**
     * 
     * Service used in public candidacies, so person may not have Person role
     * yet
     * 
     * @param photo
     * @param person
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Service
    static public void upload(final PhotographUploadBean photo, final Person person) throws FileNotFoundException, IOException {
        person.setPersonalPhoto(new Photograph(ContentType.getContentType(photo.getContentType()), new ByteArray(photo
                .getFileInputStream()), new ByteArray(photo.getCompressedInputStream()), PhotoType.USER));

    }
}