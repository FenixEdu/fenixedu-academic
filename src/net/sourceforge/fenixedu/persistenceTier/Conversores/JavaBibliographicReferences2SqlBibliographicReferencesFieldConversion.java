/*
 * Created on Jan 26, 2006
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.util.Iterator;
import java.util.StringTokenizer;

import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaBibliographicReferences2SqlBibliographicReferencesFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
        if (source instanceof BibliographicReferences) {
            return write((BibliographicReferences) source);
        }
        return source;
    }
    
    public Object sqlToJava(Object source) {
        if (source instanceof String) {
            return read((String) source);
        }
        return new BibliographicReferences();
    }
    
    private final String NEW_LINE = "\r\n";
    private final String ELEMENT_SEPARATOR = "||";
    private final int MAX_NUMBER_OF_ELEMENTS = 7;    
    
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
        stringBuilder.append(reference.getUrl()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getType()).append(ELEMENT_SEPARATOR);
        stringBuilder.append(reference.getOrder());
    }
    
    private BibliographicReferences read(String source) {
        final BibliographicReferences bibliographicReferences = new BibliographicReferences();
        final StringTokenizer line = new StringTokenizer(source, NEW_LINE);
        while (line.hasMoreTokens()) {
            final StringTokenizer element = new StringTokenizer(line.nextToken(), ELEMENT_SEPARATOR);
            if (element.countTokens() != MAX_NUMBER_OF_ELEMENTS) {
                return null; // invalid data (must correct first)
            }
            bibliographicReferences.createBibliographicReference(element.nextToken(), element
                    .nextToken(), element.nextToken(), element.nextToken(), element.nextToken(),
                    BibliographicReferenceType.valueOf(element.nextToken()), Integer.valueOf(element
                            .nextToken()));
        }
        return bibliographicReferences;
    }
}
