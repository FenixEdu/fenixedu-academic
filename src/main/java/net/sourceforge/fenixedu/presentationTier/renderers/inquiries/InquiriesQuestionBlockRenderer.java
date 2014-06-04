/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.Properties;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.CheckBoxQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesBlock;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.QuestionHeader;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.RadioGroupQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TextBoxQuestion;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlRadioButtonGroup;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesQuestionBlockRenderer extends InputRenderer {

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

            MetaObject metaObject =
                    MetaObjectFactory.createObject(block.getQuestions(),
                            RenderKit.getInstance().findSchema("inquiriesQuestion.answer"));

            getContext().getViewState().setMetaObject(metaObject);

            HtmlTableRow questionRow = null;

            for (MetaSlot metaSlot : metaObject.getAllSlots()) {

                InquiriesQuestion inquiriesQuestion = (InquiriesQuestion) metaSlot.getMetaObject().getObject();

                if (inquiriesQuestion.hasHeader()) {
                    createHeaderRow(inquiriesQuestion.getHeader(), mainTable, block);
                }

                if (inquiriesQuestion.getNewRow()) {
                    questionRow = mainTable.createRow();
                    final HtmlTableCell labelCell = questionRow.createCell(CellType.HEADER);
                    labelCell.setBody(new HtmlText(getResource(inquiriesQuestion.getLabel())
                            + getQuestionRequiredIndication(inquiriesQuestion)
                            + getQuestionToolTip(inquiriesQuestion.getToolTip()), false));
                    labelCell.addClass("width300px brightccc");
                }

                PresentationContext newContext = getContext().createSubContext(metaSlot);
                newContext.setSchema(metaSlot.getSchema());
                RenderKit kit = RenderKit.getInstance();
                HtmlFormComponent formComponent = null;

                final int scaleHeadersCount = block.hasHeader() ? block.getHeader().getScaleHeadersCount() : 0;
                if (inquiriesQuestion instanceof TextBoxQuestion) {
                    final TextBoxQuestion textBoxQuestion = (TextBoxQuestion) inquiriesQuestion;

                    newContext.setLayout("inquiries-answer-textbox");

                    Properties properties = new Properties(metaSlot.getProperties());
                    properties.put("textArea", textBoxQuestion.isTextArea());
                    newContext.setProperties(properties);

                    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());

                    final HtmlTableCell cell = questionRow.createCell();
                    cell.setColspan(textBoxQuestion.getAutofit() ? scaleHeadersCount : 1);
                    cell.setBody(formComponent);
                    cell.setClasses("aleft");

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
                    // cell.setColspan(block.getHeader().getScaleHeadersCount());
                    cell.setBody(formComponent);
                    if (scaleHeadersCount > 1 && inquiriesQuestion.getAutofit()) {
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

        private String getQuestionToolTip(final String tooltip) {
            return (!StringUtils.isEmpty(tooltip) ? "<a href=\"#\" class=\"help\"> [?] <span>" + getResource(tooltip)
                    + "</span></a>" : StringUtils.EMPTY);
        }

        private String getQuestionRequiredIndication(final InquiriesQuestion inquiriesQuestion) {
            return inquiriesQuestion.getRequired() && inquiriesQuestion.getShowRequiredMark() ? "<span class=\"required\"> *</span>" : StringUtils.EMPTY;
        }

        private String getResource(String label) {
            return BundleUtil.getString(Bundle.INQUIRIES, label);
        }

        private void createHeaderRow(final QuestionHeader header, final HtmlTable mainTable, InquiriesBlock block) {
            final HtmlTableRow headerRow = mainTable.createRow();

            final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
            firstHeaderCell
                    .setBody(new HtmlText(getResource(header.getTitle()) + getQuestionToolTip(header.getToolTip()), false));

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
