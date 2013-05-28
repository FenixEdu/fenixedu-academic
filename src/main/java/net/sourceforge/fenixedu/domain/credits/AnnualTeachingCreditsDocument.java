package net.sourceforge.fenixedu.domain.credits;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class AnnualTeachingCreditsDocument extends AnnualTeachingCreditsDocument_Base {

    public AnnualTeachingCreditsDocument(AnnualTeachingCredits annualTeachingCredits, byte[] content,
            boolean hasConfidencialInformation) {
        super();
        String filename = getFilename(annualTeachingCredits);
        init(getfilePath(filename), filename, filename, null, content, null);
        setAnnualTeachingCredits(annualTeachingCredits);
        setHasConfidencialInformation(hasConfidencialInformation);
    }

    private String getFilename(AnnualTeachingCredits annualTeachingCredits) {
        return (annualTeachingCredits.getTeacher().getPerson().getIstUsername() + "_"
                + annualTeachingCredits.getAnnualCreditsState().getExecutionYear().getYear() + ".pdf").replaceAll(" ", "_")
                .replaceAll("/", "_");
    }

    private VirtualPath getfilePath(String filename) {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("AnnualTeachingCreditsDocuments", "Teaching Credits Documents"));
        filePath.addNode(new VirtualPathNode("AnnualTeachingCreditsDocument" + getExternalId(), filename));
        return filePath;
    }

}
