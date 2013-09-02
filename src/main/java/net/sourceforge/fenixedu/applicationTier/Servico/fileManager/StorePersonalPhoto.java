package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StorePersonalPhoto {
    @Checked("RolePredicates.OPERATOR_PREDICATE")
    @Service
    public static void run(byte[] contents, ContentType contentType, String personUsername) throws ExcepcaoInexistente {
        Person person = Person.readPersonByUsername(personUsername);

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        storePersonalPhoto(contents, contentType, person);
    }

    private static void storePersonalPhoto(byte[] contents, ContentType contentType, Person person) {
        person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, contentType, new ByteArray(contents)));
    }

    @Checked("AcademicPredicates.MANAGE_PHD_PROCESSES")
    @Service
    static public void uploadPhoto(final PhotographUploadBean photoBean, final Person person) throws FileNotFoundException,
            IOException {
        person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, ContentType.getContentType(photoBean.getContentType()),
                new ByteArray(photoBean.getFileInputStream())));

    }
}