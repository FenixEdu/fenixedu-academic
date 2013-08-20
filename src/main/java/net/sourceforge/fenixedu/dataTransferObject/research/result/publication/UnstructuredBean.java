package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import bibtex.dom.BibtexEntry;

public class UnstructuredBean extends ResultPublicationBean implements Serializable {

    public UnstructuredBean() {
        this.setPublicationType(null);
        this.setActiveSchema("result.publication.edit.Unstructured");
    }

    public UnstructuredBean(Unstructured unstructured) {
        this();
        this.setExternalId(unstructured.getExternalId());
        this.setNote(unstructured.getTitle());
        this.setYear(unstructured.getYear());
    }

    @Override
    public ResultPublicationBean convertTo(ResultPublicationType type) {
        return ResultPublicationBeanConversions.unstructuredTo(this, type);
    }

    @Override
    protected void fillSpecificFields(ResearchResultPublication publication) {
        // no specific fields
    }

    @Override
    protected void fillBibTeXFields(BibtexEntry bibtexEntry) {
        // no bibtex
    }

}
