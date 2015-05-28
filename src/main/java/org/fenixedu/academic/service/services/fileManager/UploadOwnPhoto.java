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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PhotoType;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.dto.person.PhotographUploadBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.ContentType;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

/**
 * @author Pedro Santos (pmrsa)
 */
public class UploadOwnPhoto {

    @Atomic
    static public void run(byte[] contents, ContentType contentType) {
        Person person = AccessControl.getPerson();
        person.setPersonalPhoto(new Photograph(PhotoType.USER, contentType, contents));
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
    @Atomic
    static public void upload(final PhotographUploadBean photo, final Person person) throws FileNotFoundException, IOException {
        try (InputStream stream = photo.getFileInputStream()) {
            person.setPersonalPhoto(new Photograph(PhotoType.USER, ContentType.getContentType(photo.getContentType()),
                    ByteStreams.toByteArray(stream)));
        }
    }
}