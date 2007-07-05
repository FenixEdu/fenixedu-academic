/*
 * Created on Jan 26, 2006
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaBibliographicReferences2SqlBibliographicReferencesFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        return source instanceof BibliographicReferences ?
            write((BibliographicReferences) source) : source;
    }
    
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            final String string = (String) source;
            return string.length() > 0 ? read((String) source) : new BibliographicReferences();
        }
        return new BibliographicReferences();
    }
    
    private final String NEW_LINE = "\r\n";
    private final String ELEMENT_SEPARATOR = "||";    
    
    private String write(final BibliographicReferences bibliographicReferences) {
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

    private void write(final StringBuilder stringBuilder, final BibliographicReference reference) {
        stringBuilder.append(reference.getYear()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getTitle()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getAuthors()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getReference()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(StringUtils.isEmpty(reference.getUrl()) ? "" : reference.getUrl()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getType()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getOrder());
    }

    private BibliographicReferences read(String source) {
        final BibliographicReferences bibliographicReferences = new BibliographicReferences();
        fillBibliographicReferences(bibliographicReferences, source);
        return bibliographicReferences;
    }

    private void fillBibliographicReferences(final BibliographicReferences bibliographicReferences, final String source) {
        final int indexOfSep1 = source.indexOf(ELEMENT_SEPARATOR);
        final int indexOfSep2 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep1 + ELEMENT_SEPARATOR.length());
        final int indexOfSep3 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep2 + ELEMENT_SEPARATOR.length());
        final int indexOfSep4 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep3 + ELEMENT_SEPARATOR.length());
        final int indexOfSep5 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep4 + ELEMENT_SEPARATOR.length());
        final int indexOfSep6 = source.indexOf(ELEMENT_SEPARATOR, indexOfSep5 + ELEMENT_SEPARATOR.length());
        final int temp = source.indexOf(NEW_LINE, indexOfSep6 + ELEMENT_SEPARATOR.length());
        final int indexOfSep7 = temp < 0 ? source.length() : temp;

        bibliographicReferences.createBibliographicReference(
                source.substring(0, indexOfSep1),
                source.substring(indexOfSep1 + ELEMENT_SEPARATOR.length(), indexOfSep2),
                source.substring(indexOfSep2 + ELEMENT_SEPARATOR.length(), indexOfSep3),
                source.substring(indexOfSep3 + ELEMENT_SEPARATOR.length(), indexOfSep4),
                source.substring(indexOfSep4 + ELEMENT_SEPARATOR.length(), indexOfSep5),
                BibliographicReferenceType.valueOf(source.substring(indexOfSep5 + ELEMENT_SEPARATOR.length(), indexOfSep6)),
                Integer.valueOf(source.substring(indexOfSep6 + ELEMENT_SEPARATOR.length(), indexOfSep7)));

        if (indexOfSep7 + NEW_LINE.length() < source.length()) {
            fillBibliographicReferences(bibliographicReferences, source.substring(indexOfSep7 + NEW_LINE.length()));
        }
    }

}
