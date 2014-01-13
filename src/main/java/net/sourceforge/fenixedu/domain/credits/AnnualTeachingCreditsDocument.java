package net.sourceforge.fenixedu.domain.credits;

public class AnnualTeachingCreditsDocument extends AnnualTeachingCreditsDocument_Base {

    public AnnualTeachingCreditsDocument(AnnualTeachingCredits annualTeachingCredits, byte[] content,
            boolean hasConfidencialInformation) {
        super();
        String filename = getFilename(annualTeachingCredits);
        init(filename, filename, content, null);
        setAnnualTeachingCredits(annualTeachingCredits);
        setHasConfidencialInformation(hasConfidencialInformation);
    }

    private String getFilename(AnnualTeachingCredits annualTeachingCredits) {
        return (annualTeachingCredits.getTeacher().getPerson().getIstUsername() + "_"
                + annualTeachingCredits.getAnnualCreditsState().getExecutionYear().getYear() + ".pdf").replaceAll(" ", "_")
                .replaceAll("/", "_");
    }

    @Deprecated
    public boolean hasHasConfidencialInformation() {
        return getHasConfidencialInformation() != null;
    }

    @Deprecated
    public boolean hasAnnualTeachingCredits() {
        return getAnnualTeachingCredits() != null;
    }

}
