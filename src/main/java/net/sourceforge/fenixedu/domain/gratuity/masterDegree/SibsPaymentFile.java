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
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import org.fenixedu.bennu.core.domain.Bennu;

public class SibsPaymentFile extends SibsPaymentFile_Base {

    public SibsPaymentFile() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SibsPaymentFile(String filename) {
        this();
        setFilename(filename);
    }

    public static SibsPaymentFile readByFilename(String filename) {
        for (SibsPaymentFile sibsPaymentFile : Bennu.getInstance().getSibsPaymentFilesSet()) {
            if (sibsPaymentFile.getFilename().equals(filename)) {
                return sibsPaymentFile;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry> getSibsPaymentFileEntries() {
        return getSibsPaymentFileEntriesSet();
    }

    @Deprecated
    public boolean hasAnySibsPaymentFileEntries() {
        return !getSibsPaymentFileEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFilename() {
        return getFilename() != null;
    }

}
