/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CheckBoxQuestion;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiriesBlock;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionHeader;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.RadioGroupQuestion;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TextBoxQuestion;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonGroup;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesQuestionBlockRenderer extends InputRenderer {

    private final ResourceBundle inquiriesResources = ResourceBundle.getBundle("resources.InquiriesResources", Language
	    .getLocale());

    private String columnClasses;

    public String getColumnClasses() {
	return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
	this.columnClasses = columnClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiriesQuestionBlockLayout(object);
    }

    private class InquiriesQuestionBlockLayout extends Layout {

	public InquiriesQuestionBlockLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {

	    final InquiriesBlock block = (InquiriesBlock) object;

	    final HtmlTable mainTable = new HtmlTable();

	    if (block.hasHeader()) {
		createHeaderRow(block.getHeader(), mainTable, block);
	    }

	    MetaObject metaObject = MetaObjectFactory.createObject(block.getQuestions(), RenderKit.getInstance().findSchema(
		    "inquiriesQuestion.answer"));

	    getContext().getViewState().setMetaObject(metaObject);

	    for (MetaSlot metaSlot : metaObject.getAllSlots()) {

		InquiriesQuestion inquiriesQuestion = (InquiriesQuestion) metaSlot.getMetaObject().getObject();

		if (inquiriesQuestion.hasHeader()) {
		    createHeaderRow(inquiriesQuestion.getHeader(), mainTable, block);
		}

		final HtmlTableRow questionRow = mainTable.createRow();
		final HtmlTableCell labelCell = questionRow.createCell(CellType.HEADER);
		labelCell.setBody(new HtmlText(getResource(inquiriesQuestion.getLabel())
			+ getQuestionRequiredIndication(inquiriesQuestion) + getQuestionToolTip(inquiriesQuestion), false));
		labelCell.addClass("width300px brightccc");

		PresentationContext newContext = getContext().createSubContext(metaSlot);
		newContext.setSchema(metaSlot.getSchema() != null ? metaSlot.getSchema().getName() : null);
		RenderKit kit = RenderKit.getInstance();
		HtmlFormComponent formComponent = null;

		final int scaleHeadersCount = block.getHeader().getScaleHeadersCount();
		if (inquiriesQuestion instanceof TextBoxQuestion) {
		    final TextBoxQuestion textBoxQuestion = (TextBoxQuestion) inquiriesQuestion;

		    newContext.setLayout("inquiries-answer-textbox");

		    Properties properties = new Properties(metaSlot.getProperties());
		    properties.put("textArea", textBoxQuestion.isTextArea());
		    newContext.setProperties(properties);

		    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());

		    final HtmlTableCell cell = questionRow.createCell();
		    cell.setColspan(scaleHeadersCount);
		    cell.setBody(formComponent);

		} else if (inquiriesQuestion instanceof RadioGroupQuestion) {

		    final RadioGroupQuestion radioGroupQuestion = (RadioGroupQuestion) inquiriesQuestion;

		    newContext.setLayout("inquiries-answer-radiogroup");

		    Properties properties = new Properties(metaSlot.getProperties());
		    properties.put("radioGroupChoices", radioGroupQuestion.getChoices());
		    newContext.setProperties(properties);

		    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());

		    for (HtmlRadioButton htmlRadioButton : ((HtmlRadioButtonGroup) formComponent).getRadioButtons()) {
			htmlRadioButton.bind(metaSlot);
			questionRow.createCell().setBody(htmlRadioButton);
		    }

		} else if (inquiriesQuestion instanceof CheckBoxQuestion) {

		    newContext.setLayout("inquiries-answer-checkbox");
		    // metaSlot.setLayout("inquiries-answer-checkbox");

		    newContext.setProperties(metaSlot.getProperties());

		    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());

		    final HtmlTableCell cell = questionRow.createCell();
		    //cell.setColspan(block.getHeader().getScaleHeadersCount());
		    cell.setBody(formComponent);
		    if (scaleHeadersCount > 1) {
			questionRow.createCell().setColspan(scaleHeadersCount - 1);
		    }

		}

		formComponent.bind(metaSlot);

		applyStyles(questionRow);

	    }

	    getInputContext().getForm().getCancelButton().setVisible(false);

	    return mainTable;

	}

	private void applyStyles(final HtmlTableRow questionRow) {
	    String[] cellClasses = null;
	    if (getColumnClasses() != null) {
		cellClasses = getColumnClasses().split(",", -1);
	    }

	    if (cellClasses != null) {
		int cellIndex = 0;
		for (HtmlTableCell cell : questionRow.getCells()) {
		    String chooseCellClass = cellClasses[cellIndex % cellClasses.length];
		    if (!chooseCellClass.equals("")) {
			cell.setClasses(chooseCellClass);
		    }
		    cellIndex++;
		}
	    }
	}

	private String getQuestionToolTip(final InquiriesQuestion inquiriesQuestion) {
	    return (inquiriesQuestion.hasToolTip() ? "<a href=\"#\" class=\"help\"> [?] <span>"
		    + getResource(inquiriesQuestion.getToolTip()) + "</span></a>" : StringUtils.EMPTY);
	}

	private String getQuestionRequiredIndication(final InquiriesQuestion inquiriesQuestion) {
	    return inquiriesQuestion.getRequired() ? "<span class=\"required\"> *</span>" : StringUtils.EMPTY;
	}

	private String getResource(String label) {
	    try {
		return inquiriesResources.getString(label);
	    } catch (MissingResourceException e) {
	    }
	    return label;
	}

	private void createHeaderRow(final QuestionHeader header, final HtmlTable mainTable, InquiriesBlock block) {
	    final HtmlTableRow headerRow = mainTable.createRow();

	    final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
	    firstHeaderCell.setBody(new HtmlText(getResource(header.getTitle()), false));
	    firstHeaderCell.addClass("width300px");

	    if (header.hasScaleHeaders()) {
		for (String scale : header.getScaleHeaders()) {
		    HtmlTableCell headerCell = headerRow.createCell(CellType.HEADER);
		    headerCell.setBody(new HtmlText(getResource(scale), false));
		    headerCell.addClass("acenter valignbottom");
		}
	    } else {
		headerRow.createCell(CellType.HEADER).setColspan(block.getHeader().getScaleHeadersCount());
	    }
	}

	@Override
	public String getClasses() {
	    return "tstyle2 thlight thleft tdcenter tdwith50px thpadding5px10px";
	}
    }

}
