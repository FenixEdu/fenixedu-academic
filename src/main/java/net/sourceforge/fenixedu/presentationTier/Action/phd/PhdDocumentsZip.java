package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import pt.ist.fenixframework.FenixFramework;

public class PhdDocumentsZip implements Serializable {

    static private final long serialVersionUID = 1L;

    static public String ZIP_MIME_TYPE = "application/zip";

    static private Comparator<PhdProgramProcessDocument> COMPARE_BY_DOCUMENT_TYPE_AND_NAME =
            new Comparator<PhdProgramProcessDocument>() {
                @Override
                public int compare(PhdProgramProcessDocument o1, PhdProgramProcessDocument o2) {
                    int result = o1.getDocumentType().compareTo(o2.getDocumentType());

                    if (result == 0) {
                        result = o1.getFilename().compareTo(o2.getFilename());
                    }

                    return (result != 0) ? result : AbstractDomainObject.COMPARATOR_BY_ID.compare(o1, o2);
                }
            };

    private Collection<PhdProgramProcessDocument> documents;

    public PhdDocumentsZip() {
        this.documents = new TreeSet<PhdProgramProcessDocument>(COMPARE_BY_DOCUMENT_TYPE_AND_NAME);
    }

    public PhdDocumentsZip add(final PhdProgramProcessDocument document) {
        this.documents.add(document);
        return this;
    }

    public PhdDocumentsZip addAll(final Collection<PhdProgramProcessDocument> documents) {
        this.documents.addAll(documents);
        return this;
    }

    public byte[] create() throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final ZipOutputStream zipFile = new ZipOutputStream(result);

        int prefixName = 0;

        for (final PhdProgramProcessDocument document : this.documents) {
            zipEntry(++prefixName, zipFile, document);
        }

        zipFile.close();
        return result.toByteArray();
    }

    private void zipEntry(final int prefixName, final ZipOutputStream zipFile, final PhdProgramProcessDocument document)
            throws IOException {
        zipFile.putNextEntry(new ZipEntry(String.format("%s-%s", prefixName, document.getFilename())));
        zipFile.write(document.getContents());
        zipFile.closeEntry();
    }

    static public byte[] zip(final Collection<PhdProgramProcessDocument> documents) throws IOException {
        return new PhdDocumentsZip().addAll(documents).create();
    }
}
