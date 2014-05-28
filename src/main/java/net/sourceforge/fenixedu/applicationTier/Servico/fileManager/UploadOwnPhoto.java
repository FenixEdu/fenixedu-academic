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
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.fenixedu.dataTransferObject.person.PhotographUploadBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import pt.ist.fenixframework.Atomic;

/**
 * @author Pedro Santos (pmrsa)
 */
public class UploadOwnPhoto {

    @Atomic
    static public void run(byte[] contents, ContentType contentType) {
        check(RolePredicates.PERSON_PREDICATE);
        Person person = AccessControl.getPerson();
        person.setPersonalPhoto(new Photograph(PhotoType.USER, contentType, new ByteArray(contents)));
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
        person.setPersonalPhoto(new Photograph(PhotoType.USER, ContentType.getContentType(photo.getContentType()), new ByteArray(
                photo.getFileInputStream())));
    }
}