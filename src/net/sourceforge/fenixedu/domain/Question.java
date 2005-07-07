/*
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Susana Fernandes
 */
public class Question extends DomainObject implements IQuestion {
    private String xmlFile;

    private String xmlFileName;

    private IMetadata metadata;

    private Integer keyMetadata;

    private Boolean visibility;

    public Integer getKeyMetadata() {
        return keyMetadata;
    }

    public IMetadata getMetadata() {
        return metadata;
    }

    public String getXmlFile() {
        return xmlFile;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setKeyMetadata(Integer integer) {
        keyMetadata = integer;
    }

    public void setMetadata(IMetadata metadata) {
        this.metadata = metadata;
    }

    public void setXmlFile(String string) {
        xmlFile = string;
    }

    public void setXmlFileName(String string) {
        xmlFileName = string;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean boolean1) {
        visibility = boolean1;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IQuestion) {
            final IQuestion question = (IQuestion) obj;
            return this.getIdInternal().equals(question.getIdInternal());
        }
        return false;
    }

}
