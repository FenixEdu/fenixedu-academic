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

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.CheckBoxQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesBlock;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.QuestionChoice;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.QuestionHeader;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.RadioGroupQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TextBoxQuestion;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesQuestionBlockOutputRenderer extends OutputRenderer {

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

            final HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            link.setUrl("/images/cross03.gif");
            HtmlImage crossImage = new HtmlImage();
            crossImage.setSource(link.calculateUrl());

            for (MetaSlot metaSlot : metaObject.getAllSlots()) {

                InquiriesQuestion inquiriesQuestion = (InquiriesQuestion) metaSlot.getMetaObject().getObject();

                if (inquiriesQuestion.hasHeader()) {
                    createHeaderRow(inquiriesQuestion.getHeader(), mainTable, block);
                }

                if (inquiriesQuestion.getNewRow()) {
                    questionRow = mainTable.createRow();
                    final HtmlTableCell labelCell = questionRow.createCell(CellType.HEADER);
                    labelCell.setBody(new HtmlText(getResource(inquiriesQuestion.getLabel())
                            + getQuestionToolTip(inquiriesQuestion.getToolTip()), false));
                    labelCell.addClass("width300px brightccc");
                }

                final int scaleHeadersCount = block.hasHeader() ? block.getHeader().getScaleHeadersCount() : 0;
                if (inquiriesQuestion instanceof TextBoxQuestion) {
                    final TextBoxQuestion textBoxQuestion = (TextBoxQuestion) inquiriesQuestion;
                    final HtmlTableCell cell = questionRow.createCell();
                    cell.setColspan(textBoxQuestion.getAutofit() ? scaleHeadersCount : 1);
                    cell.setBody(new HtmlText(textBoxQuestion.getValue()));
                    cell.setClasses("aleft");

                } else if (inquiriesQuestion instanceof RadioGroupQuestion) {
                    final RadioGroupQuestion radioGroupQuestion = (RadioGroupQuestion) inquiriesQuestion;
                    for (final QuestionChoice questionChoice : radioGroupQuestion.getChoices()) {
                        questionRow.createCell().setBody(
                                questionChoice.getValue().equals(radioGroupQuestion.getValue()) ? crossImage : null);
                    }

                } else if (inquiriesQuestion instanceof CheckBoxQuestion) {
                    final HtmlTableCell cell = questionRow.createCell();
                    if (inquiriesQuestion.getValueAsBoolean()) {
                        cell.setBody(crossImage);
                    }
                    if (scaleHeadersCount > 1 && inquiriesQuestion.getAutofit()) {
                        questionRow.createCell().setColspan(scaleHeadersCount - 1);
                    }
                }

                applyStyles(questionRow);

            }

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

        private String getQuestionToolTip(final String tooltip) {
            return (!StringUtils.isEmpty(tooltip) ? "<a href=\"#\" class=\"help\"> [?] <span>" + getResource(tooltip)
                    + "</span></a>" : StringUtils.EMPTY);
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
