package net.sourceforge.fenixedu.domain.candidacyProcess;

import pt.ist.fenixframework.Atomic;

public class IndividualCandidacyDocumentFile extends IndividualCandidacyDocumentFile_Base {

    protected IndividualCandidacyDocumentFile() {
        super();
        this.setCandidacyFileActive(Boolean.TRUE);
    }

    protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, IndividualCandidacy candidacy,
            byte[] contents, String filename) {
        this();
        this.setCandidacyFileActive(Boolean.TRUE);
        addIndividualCandidacy(candidacy);
        setCandidacyFileType(type);
        init(filename, filename, contents, null);

        if (type.equals(IndividualCandidacyDocumentFileType.PHOTO)) {
            // storeToContentManager();
        }
    }

    protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, byte[] contents, String filename) {
        this();
        this.setCandidacyFileActive(Boolean.TRUE);
        setCandidacyFileType(type);
        init(filename, filename, contents, null);

        if (type.equals(IndividualCandidacyDocumentFileType.PHOTO)) {
            // storeToContentManager();
        }
    }

    @Atomic
    public static IndividualCandidacyDocumentFile createCandidacyDocument(byte[] contents, String filename,
            IndividualCandidacyDocumentFileType type, String processName, String documentIdNumber) {
        return new IndividualCandidacyDocumentFile(type, contents, filename);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getIndividualCandidacy() {
        return getIndividualCandidacySet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacy() {
        return !getIndividualCandidacySet().isEmpty();
    }

    @Deprecated
    public boolean hasCandidacyFileType() {
        return getCandidacyFileType() != null;
    }

    @Deprecated
    public boolean hasCandidacyFileActive() {
        return getCandidacyFileActive() != null;
    }

}
