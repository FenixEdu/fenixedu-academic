package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/importIdentificationCardData", module = "identificationCardManager", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "showIdentificationCardImportForm", path = "/identificationCardManager/cardGeneration/importIdentificationCard.jsp"),
	@Forward(name = "showImportResults", path = "/identificationCardManager/cardGeneration/showImportResults.jsp") })
public class ImportIdentificationCardDataDA extends FenixDispatchAction {

    public ActionForward prepareIdentificationCardDataImportation(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	setImportDataBean(request);
	RenderUtils.invalidateViewState();

	return mapping.findForward("showIdentificationCardImportForm");
    }

    private void setImportDataBean(HttpServletRequest request) {
	ImportIdentificationCardDataBean bean = getImportDataBean() != null ? getImportDataBean()
		: new ImportIdentificationCardDataBean();
	bean.setSourceData(null);
	request.setAttribute("importIdentificationCardDataBean", bean);
    }

    public ActionForward importIdentificationCardDataFromFile(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	ImportIdentificationCardDataBean importBean = getImportDataBean();

	CardGenerationBatch cardGenerationBatch = importBean.getSelectedCardGenerationBatch();

	System.out.println("Current Encoding: " + Charset.defaultCharset().name());

	if (cardGenerationBatch == null) {
	    addActionMessage(request, "error.card.generation.batch.required");
	    return prepareIdentificationCardDataImportation(mapping, actionForm, request, response);
	}

	byte[] data = new byte[importBean.getFileSize().intValue()];
	importBean.getSourceData().read(data);

	try {
	    CardGenerationBatch.ImportationReport report = cardGenerationBatch
		    .importCardIdentificationsFromFile(new String(data));

	    request.setAttribute("import.identification.card.report", report);
	} catch (DomainException exception) {
	    if (exception.getMessage().startsWith(CardGenerationBatch.ERROR_IMPORT_IDENTIFICATION_CARD_DESCRIPTION)) {
		addActionMessage(request, "error.import.identification.card");
		return prepareIdentificationCardDataImportation(mapping, actionForm, request, response);
	    }
	}

	return mapping.findForward("showImportResults");
    }

    private ImportIdentificationCardDataBean getImportDataBean() {
	return (ImportIdentificationCardDataBean) this.getObjectFromViewState("import.data");
    }

    public static class ImportIdentificationCardDataBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InputStream sourceData;
	private Long fileSize;
	private String fileName;

	private ExecutionYear selectedExecutionYear;
	private CardGenerationBatch selectedCardGenerationBatch;

	public ImportIdentificationCardDataBean() {

	}

	public InputStream getSourceData() {
	    return sourceData;
	}

	public void setSourceData(InputStream sourceData) {
	    this.sourceData = sourceData;
	}

	public Long getFileSize() {
	    return fileSize;
	}

	public void setFileSize(Long fileSize) {
	    this.fileSize = fileSize;
	}

	public String getFileName() {
	    return fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	public ExecutionYear getSelectedExecutionYear() {
	    return this.selectedExecutionYear;
	}

	public void setSelectedExecutionYear(ExecutionYear executionYear) {
	    this.selectedExecutionYear = (executionYear);
	}

	public CardGenerationBatch getSelectedCardGenerationBatch() {
	    return this.selectedCardGenerationBatch;
	}

	public void setSelectedCardGenerationBatch(CardGenerationBatch cardGenerationBatch) {
	    this.selectedCardGenerationBatch = cardGenerationBatch;
	}
    }

    private static final String PROBLEMS_GENERATION_BATCH_NAME = "Com Problemas";

    public static class CardGenerationBatchProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new DomainObjectKeyConverter();
	}

	@Override
	public Object provide(Object source, Object currentValue) {
	    ImportIdentificationCardDataBean bean = (ImportIdentificationCardDataBean) source;

	    if (bean.getSelectedExecutionYear() == null)
		return new java.util.ArrayList<CardGenerationBatch>();

	    return CollectionUtils.select(bean.getSelectedExecutionYear().getCardGenerationBatches(), new Predicate() {

		@Override
		public boolean evaluate(Object arg0) {
		    return !PROBLEMS_GENERATION_BATCH_NAME.equals(((CardGenerationBatch) arg0).getDescription());
		}
	    });
	}
    }
}
