package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

public interface IDiplomaSupplementRequest extends IDocumentRequest {
	public CycleType getRequestedCycle();

	public String getGraduateTitle(final Locale locale);

	public Integer getRegistrationNumber();

	public String getPrevailingScientificArea(final Locale locale);

	public long getEctsCredits();

	public DegreeOfficialPublication getDegreeOfficialPublication();

	public Integer getFinalAverage();

	public String getFinalAverageQualified(final Locale locale);

	public ExecutionYear getConclusionYear();

	public EctsGraduationGradeConversionTable getGraduationConversionTable();

	public Integer getNumberOfCurricularYears();

	public Integer getNumberOfCurricularSemesters();

	public Boolean isExemptedFromStudy();

	public Registration getRegistration();

	public boolean hasRegistration();
}
