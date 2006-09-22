package net.sourceforge.fenixedu.dataTransferObject.research.result.publication.bibtex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean;

public class ImportBibtexBean implements Serializable {

    private List<BibtexPublicationBean> bibtexPublications = new ArrayList<BibtexPublicationBean>();

    private int totalPublicationsReaded = 0;

    private int currentPublicationPosition = 1;

    public List<BibtexPublicationBean> getBibtexPublications() {
        return bibtexPublications;
    }

    public void setBibtexPublications(List<BibtexPublicationBean> bibtexPublications) {
        this.bibtexPublications = bibtexPublications;
    }

    public ResultPublicationBean getCurrentPublicationBean() {
        if (bibtexPublications.get(0) != null)
            return bibtexPublications.get(0).getPublicationBean();
        return null;
    }

    public BibtexPublicationBean getCurrentBibtexPublication() {
        if (bibtexPublications.get(0) != null)
            return bibtexPublications.get(0);
        return null;
    }

    public String getCurrentBibtex() {
        if (bibtexPublications.get(0) != null)
            return bibtexPublications.get(0).getBibtex();
        return null;
    }

    public List<BibtexParticipatorBean> getCurrentAuthors() {
        if (bibtexPublications.get(0) != null)
            return bibtexPublications.get(0).getAuthors();
        return null;
    }

    public List<BibtexParticipatorBean> getCurrentEditors() {
        if (bibtexPublications.get(0) != null)
            return bibtexPublications.get(0).getEditors();
        return null;
    }

    public List<BibtexParticipatorBean> getCurrentParticipators() {
        if (bibtexPublications.get(0) != null)
            return bibtexPublications.get(0).getParticipators();
        return null;
    }

    public boolean isCurrentProcessedParticipators() {
        return bibtexPublications.get(0).isProcessedParticipators();
    }

    public void setCurrentProcessedParticipators(boolean value) {
        bibtexPublications.get(0).setProcessedParticipators(value);
    }

    public boolean hasMorePublications() {
        if (bibtexPublications.size() > 0)
            return true;
        return false;
    }

    public int getCurrentPublicationPosition() {
        return currentPublicationPosition;
    }

    public void setCurrentPublicationPosition(int currentPublicationPosition) {
        this.currentPublicationPosition = currentPublicationPosition;
    }

    public int getTotalPublicationsReaded() {
        return totalPublicationsReaded;
    }

    public void setTotalPublicationsReaded(int totalPublicationsReaded) {
        this.totalPublicationsReaded = totalPublicationsReaded;
    }

    public boolean moveToNextPublicaton() {
        if (hasMorePublications()) {
            bibtexPublications.remove(0);
            currentPublicationPosition++;
            if (hasMorePublications())
                return true;
        }
        return false;
    }
}
