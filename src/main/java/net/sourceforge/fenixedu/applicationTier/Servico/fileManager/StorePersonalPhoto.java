package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StorePersonalPhoto {

    @Atomic
    public static void run(byte[] contents, ContentType contentType, String personUsername) throws ExcepcaoInexistente {
        check(RolePredicates.OPERATOR_PREDICATE);
        Person person = Person.readPersonByUsername(personUsername);

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        storePersonalPhoto(contents, contentType, person);
    }

    private static void storePersonalPhoto(byte[] contents, ContentType contentType, Person person) {
        person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, contentType, new ByteArray(contents)));
    }

    @Atomic
    static public void uploadPhoto(final PhotographUploadBean photoBean, final Person person) throws FileNotFoundException,
            IOException {
        check(AcademicPredicates.MANAGE_PHD_PROCESSES);
        person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, ContentType.getContentType(photoBean.getContentType()),
                new ByteArray(photoBean.getFileInputStream())));

    }
}