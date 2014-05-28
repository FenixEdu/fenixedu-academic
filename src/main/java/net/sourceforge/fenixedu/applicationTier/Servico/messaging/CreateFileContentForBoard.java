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
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.io.File;
import java.io.IOException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.Files;

public class CreateFileContentForBoard {

    protected void run(AnnouncementBoard board, File file, String originalFilename, String displayName, Group permittedGroup,
            Person person) throws FenixServiceException, DomainException, IOException {

        if (!board.hasWriter(person)) {
            throw new FenixServiceException("error.person.not.board.writer");
        }

        if (StringUtils.isEmpty(displayName)) {
            displayName = file.getName();
        }

        final byte[] bs = Files.toByteArray(file);
        FileContent fileContent = new FileContent(originalFilename, displayName, bs, permittedGroup, null);

        board.addFileContent(fileContent);
    }

    // Service Invokers migrated from Berserk

    private static final CreateFileContentForBoard serviceInstance = new CreateFileContentForBoard();

    @Atomic
    public static void runCreateFileContentForBoard(AnnouncementBoard board, File file, String originalFilename,
            String displayName, Group permittedGroup, Person person) throws FenixServiceException, DomainException, IOException {
        serviceInstance.run(board, file, originalFilename, displayName, permittedGroup, person);
    }

}