package net.sourceforge.fenixedu.presentationTier.Action.commons.excel;

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
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ExcelUtils {

    HttpServletRequest request;
    HttpServletResponse response;

    public ExcelUtils(HttpServletRequest request, HttpServletResponse response) {
	this.request = request;
	this.response = response;
    }

    public void generateSortedExcel(Set<DocumentRequest> documents, String prefix) {

	SortedSet<DocumentRequest> sorted = new TreeSet<DocumentRequest>(DocumentRequest.COMPARATOR_BY_REGISTRY_NUMBER);
	sorted.addAll(documents);
	generate(sorted, getCodes(sorted), prefix);

    }

    public void generateExcel(Set<DocumentRequest> documents, String prefix) {

	generate(documents, getCodes(documents), prefix);

    }

    private Set<RegistryCode> getCodes(Set<DocumentRequest> documents) {
	Set<RegistryCode> codes = new HashSet<RegistryCode>();
	for (DocumentRequest document : documents) {
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

    private void generate(Set<DocumentRequest> documents, Set<RegistryCode> codes, String prefix) {

	SheetData<DocumentRequest> data = new SheetData<DocumentRequest>(documents) {
	    @Override
	    protected void makeLine(DocumentRequest document) {
		ResourceBundle enumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
		addCell("Código", document.getRegistryCode().getCode());
		addCell("Tipo de Documento", enumeration.getString(document.getDocumentRequestType().name()));
		switch (document.getDocumentRequestType()) {
		case REGISTRY_DIPLOMA_REQUEST:
		    addCell("Ciclo", enumeration.getString(((RegistryDiplomaRequest) document).getRequestedCycle().name()));
		    break;
		case DIPLOMA_REQUEST:
		    CycleType cycle = ((DiplomaRequest) document).getWhatShouldBeRequestedCycle();
		    addCell("Ciclo", cycle != null ? enumeration.getString(cycle.name()) : null);
		    break;
		case DIPLOMA_SUPPLEMENT_REQUEST:
		    addCell("Ciclo", enumeration.getString(((DiplomaSupplementRequest) document).getRequestedCycle().name()));
		    break;
		default:
		    addCell("Ciclo", null);
		}
		addCell("Tipo de Curso", enumeration.getString(document.getDegreeType().name()));
		addCell("Nº de Aluno", document.getRegistration().getNumber());
		addCell("Nome", document.getPerson().getName());
		if (!(document instanceof DiplomaRequest)) {
		    addCell("Ficheiro", document.getLastGeneratedDocument().getFilename());
		}
	    }
	};

	try {
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + prefix + calculateMin(codes) + "-"
		    + calculateMax(codes) + "(" + codes.size()
		    + ")" + ".xls");
	    final ServletOutputStream writer = response.getOutputStream();
	    new SpreadsheetBuilder().addSheet("lote", data).build(WorkbookExportFormat.EXCEL, writer);
	    writer.flush();
	    response.flushBuffer();
	} catch (IOException e) {
	    throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata", e);
	}
    }

}
