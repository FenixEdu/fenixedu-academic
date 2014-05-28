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
/*
 * Created on Jan 26, 2006
 */
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;

import org.apache.commons.lang.StringUtils;

public class Externalization {

    public static String externalizeBibliographicReferences(BibliographicReferences source) {
        return write(source);
    }

    public static BibliographicReferences internalizeBibliographicReferences(String source) {
        if ((source != null) && (source.length() > 0)) {
            return read(source);
        } else {
            return new BibliographicReferences();
        }
    }

    private static final String NEW_LINE = "\r\n";
    private static final String ELEMENT_SEPARATOR = "||";

    private static String write(final BibliographicReferences bibliographicReferences) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<BibliographicReference> references = bibliographicReferences.getBibliographicReferencesList().iterator();
        while (references.hasNext()) {
            write(stringBuilder, references.next());
            if (references.hasNext()) {
                stringBuilder.append(NEW_LINE);
            }
        }
        return stringBuilder.toString();
    }

    private static void write(final StringBuilder stringBuilder, final BibliographicReference reference) {
        stringBuilder.append(reference.getYear()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getTitle()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getAuthors()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getReference()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(StringUtils.isEmpty(reference.getUrl()) ? "" : reference.getUrl()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getType()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getOrder());
    }

    private static BibliographicReferences read(String source) {
        final BibliographicReferences bibliographicReferences = new BibliographicReferences();
        fillBibliographicReferences(bibliographicReferences, source);
        return bibliographicReferences;
    }

    private static void fillBibliographicReferences(final BibliographicReferences bibliographicReferences, final String source) {
        final int indexOfSep1 = source.indexOf(ELEMENT_SEPARATOR);
        final int indexOfSep2 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep1 + ELEMENT_SEPARATOR.length());
        final int indexOfSep3 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep2 + ELEMENT_SEPARATOR.length());
        final int indexOfSep4 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep3 + ELEMENT_SEPARATOR.length());
        final int indexOfSep5 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep4 + ELEMENT_SEPARATOR.length());
        final int indexOfSep6 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep5 + ELEMENT_SEPARATOR.length());
        final int temp = source.indexOf(NEW_LINE, indexOfSep6 + ELEMENT_SEPARATOR.length());
        final int indexOfSep7 = temp < 0 ? source.length() : temp;

        bibliographicReferences
                .createBibliographicReference(
                        source.substring(0, indexOfSep1),
                        source.substring(indexOfSep1 + ELEMENT_SEPARATOR.length(), indexOfSep2),
                        source.substring(indexOfSep2 + ELEMENT_SEPARATOR.length(), indexOfSep3),
                        source.substring(indexOfSep3 + ELEMENT_SEPARATOR.length(), indexOfSep4),
                        source.substring(indexOfSep4 + ELEMENT_SEPARATOR.length(), indexOfSep5),
                        source.substring(indexOfSep5 + ELEMENT_SEPARATOR.length(), indexOfSep6).equals("null") ? BibliographicReferenceType.MAIN : BibliographicReferenceType
                                .valueOf(source.substring(indexOfSep5 + ELEMENT_SEPARATOR.length(), indexOfSep6)), Integer
                                .valueOf(source.substring(indexOfSep6 + ELEMENT_SEPARATOR.length(), indexOfSep7)));

        if (indexOfSep7 + NEW_LINE.length() < source.length()) {
            fillBibliographicReferences(bibliographicReferences, source.substring(indexOfSep7 + NEW_LINE.length()));
        }
    }

}
