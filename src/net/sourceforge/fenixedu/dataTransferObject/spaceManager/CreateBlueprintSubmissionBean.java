package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;

public class CreateBlueprintSubmissionBean implements Serializable {

    private DomainReference<SpaceInformation> spaceInformationReference;
    
    private String filename;
    
    private transient InputStream inputStream;
    
    public CreateBlueprintSubmissionBean(SpaceInformation spaceInformationReference) {
        super();
        this.spaceInformationReference = new DomainReference<SpaceInformation>(spaceInformationReference);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setSpaceInformation(SpaceInformation spaceInformationReference) {
        this.spaceInformationReference = (spaceInformationReference != null) ? new DomainReference<SpaceInformation>(spaceInformationReference) : null;
    }
    
    public SpaceInformation getSpaceInformation() {
        return (this.spaceInformationReference != null) ? this.spaceInformationReference.getObject() : null;
    }
}
