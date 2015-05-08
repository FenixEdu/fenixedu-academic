package org.fenixedu.academic.domain.serviceRequests;

import java.util.Locale;

import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;

public interface IProgramConclusionRequest extends IDocumentRequest {

    public CycleType getRequestedCycle();

    public ProgramConclusion getProgramConclusion();

    public String getGraduateTitle(final Locale locale);

}
