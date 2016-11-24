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
package org.fenixedu.academic.service.services.fileManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PhotoType;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.dto.person.PhotographUploadBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.service.services.ExcepcaoInexistente;
import org.fenixedu.academic.util.ContentType;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StorePersonalPhoto {

    @Atomic
    public static void run(byte[] contents, ContentType contentType, String personUsername) throws ExcepcaoInexistente {
        Person person = Person.readPersonByUsername(personUsername);

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }

        storePersonalPhoto(contents, contentType, person);
    }

    private static void storePersonalPhoto(byte[] contents, ContentType contentType, Person person) {
        person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, contentType, contents));
    }

    @Atomic
    static public void uploadPhoto(final PhotographUploadBean photoBean, final Person person) throws FileNotFoundException,
            IOException {
        check(AcademicPredicates.MANAGE_PHD_PROCESSES);
        try (InputStream stream = photoBean.getFileInputStream()) {
            person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL,
                    ContentType.getContentType(photoBean.getContentType()), ByteStreams.toByteArray(stream)));
        }

    }
}