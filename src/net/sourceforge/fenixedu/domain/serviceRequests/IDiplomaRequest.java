package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;

import org.joda.time.LocalDate;

public interface IDiplomaRequest extends IDocumentRequest {
    public CycleType getWhatShouldBeRequestedCycle();

    public boolean hasRegistryCode();

    public RegistryCode getRegistryCode();

    public LocalDate getConclusionDate();

    public Integer getFinalAverage();

    public String getFinalAverageQualified();

    public String getDissertationThesisTitle();

    public String getGraduateTitle(final Locale locale);
}
