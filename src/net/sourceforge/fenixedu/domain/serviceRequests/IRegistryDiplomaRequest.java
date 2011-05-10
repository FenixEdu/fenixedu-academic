package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;

import org.joda.time.LocalDate;

public interface IRegistryDiplomaRequest extends IDocumentRequest {
    public LocalDate getConclusionDate();

    public ExecutionYear getConclusionYear();
    public String getGraduateTitle(Locale locale);
    public CycleType getRequestedCycle();

    public String getFinalAverage(final Locale locale);
    public String getQualifiedAverageGrade(final Locale locale);

    public IDiplomaSupplementRequest getDiplomaSupplement();
}
