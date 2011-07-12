package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.ApprovementInfoForEquivalenceProcess;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.YearMonthDay;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Response;
import org.restlet.representation.InputRepresentation;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

public class SendAcademicServiceRequestToExternalEntity extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final AcademicServiceRequest academicServiceRequest, final YearMonthDay sendDate,
	    final String justification) {

	academicServiceRequest.sendToExternalEntity(sendDate, justification);

	if (academicServiceRequest instanceof EquivalencePlanRequest) {
	    //sendRequestDataToExternal(academicServiceRequest);
	}

    }

    private static void sendRequestDataToExternal(final AcademicServiceRequest academicServiceRequest) {
	final Registration registration = ((RegistrationAcademicServiceRequest) academicServiceRequest).getRegistration();
	final ExecutionYear executionYear = ((RegistrationAcademicServiceRequest) academicServiceRequest).getExecutionYear();

	if (registration.hasIndividualCandidacy()) {
	    final IndividualCandidacy individualCandidacy = registration.getIndividualCandidacy();
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");

	    final StringBuilder studentData = new StringBuilder();
	    registration.exportValues(studentData);
	    individualCandidacy.exportValues(studentData);

	    final StringBuilder studentGrades = new StringBuilder();
	    for (final Registration otherRegistration : registration.getStudent().getRegistrations()) {
		if (otherRegistration.getDegreeType().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE) {
		    String approvementsInfo = ApprovementInfoForEquivalenceProcess.getApprovementsInfo(otherRegistration);
		    if (!StringUtils.isEmpty(approvementsInfo.trim())) {
			studentGrades.append("\n\n\n");
			studentGrades.append(bundle.getString("label.degree"));
			studentGrades.append(" : ");
			studentGrades.append(otherRegistration.getDegree().getPresentationName());
			studentGrades.append("\n\n");
			studentGrades.append(approvementsInfo);
		    }
		}
	    }

	    System.out.println(studentData.toString());
	    System.out.println();
	    System.out.println();
	    System.out.println(studentGrades.toString());

	    final ByteArrayOutputStream resultStream = buildDocumentsStream(individualCandidacy.getDocuments(), studentData
		    .toString(), studentGrades.toString(), registration, executionYear);
	    // throw new DomainException("");

	    final InputRepresentation ir = new InputRepresentation(new ByteArrayInputStream(resultStream.toByteArray()));

	    final Reference reference = new Reference(PropertiesManager
		    .getProperty("external.application.workflow.equivalences.uri")
		    + academicServiceRequest.getServiceRequestNumber()).addQueryParameter("creator",
		    UserView.getUser().getUsername()).addQueryParameter("requestor", registration.getPerson().getUsername());
	    final Response response = new Client(Protocol.HTTP).post(reference, ir);

	    if (response.getStatus().getCode() != 200) {
		throw new DomainException(response.getStatus().getThrowable() != null ? response.getStatus().getThrowable()
			.getMessage() : "error.externalEntity");
	    }
	}

    }

    private static ByteArrayOutputStream buildDocumentsStream(final Collection<? extends File> files,
	    final String candidacyResume, final String studentGrades, Registration registration, ExecutionYear executionYear) {
	ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

	Set<String> fileNames = new HashSet<String>();

	try {
	    ZipOutputStream out = new ZipOutputStream(resultStream);

	    out.putNextEntry(new ZipEntry("candidacyResume.txt"));
	    out.write(candidacyResume.getBytes());
	    out.closeEntry();

	    out.putNextEntry(new ZipEntry("studentApprovements.txt"));
	    out.write(studentGrades.getBytes());
	    out.closeEntry();

	    for (final File file : files) {
		String filename = file.getFilename();
		while (fileNames.contains(filename)) {
		    filename = filename + "_";
		}
		fileNames.add(filename);
		out.putNextEntry(new ZipEntry(filename + ".pdf"));
		if (file.hasLocalContent()) {
		    out.write(file.getContents());
		} else {
		    final byte[] content = FileUtils.readFileInBytes("/home/rcro/Documents/resultados-quc.html");
		    out.write(content);
		}
		out.closeEntry();
	    }

	    if (registration.getActiveDegreeCurricularPlan().hasRoot()) {
		StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Disciplinas");
		RootCourseGroup rootGroup = registration.getActiveDegreeCurricularPlan().getRoot();
		spreadsheet.newRow();
		spreadsheet.addCell(rootGroup.getName(), spreadsheet.getExcelStyle().getTitleStyle());
		buildCurricularCoursesGroups(executionYear, rootGroup
			.getSortedChildContextsWithCourseGroupsByExecutionYear(executionYear), spreadsheet);

		out.putNextEntry(new ZipEntry("plano_de_estudos.xls"));
		out.write(spreadsheet.getWorkbook().getBytes());
		out.closeEntry();

	    }

	    out.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}

	return resultStream;
    }

    private static void buildCurricularCoursesGroups(ExecutionYear executionYear, List<Context> sortedContexts,
	    StyledExcelSpreadsheet spreadsheet) {
	for (Context context : sortedContexts) {
	    CourseGroup childDegreeModule = (CourseGroup) context.getChildDegreeModule();
	    spreadsheet.newRow();
	    spreadsheet.addCell(childDegreeModule.getName(), spreadsheet.getExcelStyle().getTitleStyle());
	    List<Context> sortedCurricularContexts = childDegreeModule
		    .getSortedChildContextsWithCurricularCoursesByExecutionYear(executionYear);
	    if (sortedContexts.size() > 1) {
		for (Context courseContext : sortedCurricularContexts) {
		    DegreeModule courseModule = courseContext.getChildDegreeModule();
		    spreadsheet.newRow();
		    spreadsheet.addCell(courseModule.getName());
		    spreadsheet.addCell(((CurricularCourse) courseModule).getEctsCredits());
		}
	    }
	    List<Context> sortedGroupContexts = childDegreeModule
		    .getSortedChildContextsWithCourseGroupsByExecutionYear(executionYear);
	    if (sortedGroupContexts.size() > 0) {
		buildCurricularCoursesGroups(executionYear, sortedGroupContexts, spreadsheet);
	    }
	}
    }
}