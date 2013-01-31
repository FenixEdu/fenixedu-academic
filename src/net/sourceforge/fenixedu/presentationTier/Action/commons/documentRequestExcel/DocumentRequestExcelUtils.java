package net.sourceforge.fenixedu.presentationTier.Action.commons.documentRequestExcel;

import java.io.IOException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IDiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DocumentRequestExcelUtils {

	HttpServletRequest request;
	HttpServletResponse response;

	public DocumentRequestExcelUtils(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public void generateSortedExcel(Set<AcademicServiceRequest> documents, String prefix) {

		SortedSet<AcademicServiceRequest> sorted =
				new TreeSet<AcademicServiceRequest>(DocumentRequest.COMPARATOR_BY_REGISTRY_NUMBER);
		sorted.addAll(documents);
		generate(sorted, getCodes(sorted), prefix);

	}

	public void generateExcel(Set<AcademicServiceRequest> documents, String prefix) {

		generate(documents, getCodes(documents), prefix);

	}

	private Set<RegistryCode> getCodes(Set<AcademicServiceRequest> documents) {
		Set<RegistryCode> codes = new HashSet<RegistryCode>();
		for (AcademicServiceRequest document : documents) {
			codes.add(document.getRegistryCode());
		}
		return codes;
	}

	private Integer calculateMin(Set<RegistryCode> codes) {
		Integer min = null;
		for (RegistryCode code : codes) {
			Integer codeNumber = code.getCodeNumber();
			if (min == null || codeNumber.intValue() < min.intValue()) {
				min = codeNumber;
			}
		}
		return min;
	}

	private Integer calculateMax(Set<RegistryCode> codes) {
		Integer min = null;
		Integer max = null;
		for (RegistryCode code : codes) {
			Integer codeNumber = code.getCodeNumber();
			if (max == null || codeNumber.intValue() > max.intValue()) {
				max = codeNumber;
			}
		}
		return max;
	}

	private void generate(Set<AcademicServiceRequest> documents, Set<RegistryCode> codes, String prefix) {

		SheetData<AcademicServiceRequest> data = new SheetData<AcademicServiceRequest>(documents) {
			@Override
			protected void makeLine(AcademicServiceRequest request) {
				IDocumentRequest document = (IDocumentRequest) request;
				ResourceBundle enumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
				addCell("Código", document.getRegistryCode().getCode());
				addCell("Tipo de Documento", enumeration.getString(document.getDocumentRequestType().name()));
				CycleType cycle = null;
				switch (document.getDocumentRequestType()) {
				case REGISTRY_DIPLOMA_REQUEST:
					cycle = ((IRegistryDiplomaRequest) document).getRequestedCycle();
					break;
				case DIPLOMA_REQUEST:
					cycle = ((IDiplomaRequest) document).getWhatShouldBeRequestedCycle();
					break;
				case DIPLOMA_SUPPLEMENT_REQUEST:
					cycle = ((IDiplomaSupplementRequest) document).getRequestedCycle();
					break;
				default:
					addCell("Ciclo", null);
				}
				addCell("Ciclo", cycle != null ? enumeration.getString(cycle.name()) : null);

				if (document.isRequestForRegistration()) {
					addCell("Tipo de Curso",
							enumeration.getString(((RegistrationAcademicServiceRequest) document).getDegreeType().name()));
				} else if (document.isRequestForPhd()) {
					addCell("Tipo de Estudos", "Programa doutoral");
				}
				addCell("Nº de Aluno", document.getStudent().getNumber());
				addCell("Nome", document.getPerson().getName());
				if (!(document.isDiploma())) {
					addCell("Ficheiro", request.getLastGeneratedDocument().getFilename());
				}
			}
		};

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=" + prefix + calculateMin(codes) + "-"
					+ calculateMax(codes) + "(" + codes.size() + ")" + ".xls");
			final ServletOutputStream writer = response.getOutputStream();
			new SpreadsheetBuilder().addSheet("lote", data).build(WorkbookExportFormat.EXCEL, writer);
			writer.flush();
			response.flushBuffer();
		} catch (IOException e) {
			throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata", e);
		}
	}

}
