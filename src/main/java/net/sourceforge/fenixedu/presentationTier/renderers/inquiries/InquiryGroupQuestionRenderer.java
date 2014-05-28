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

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryGroupQuestionBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiryQuestionDTO;
import net.sourceforge.fenixedu.domain.inquiries.ECTSVisibleCondition;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCheckBoxQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestionHeader;
import net.sourceforge.fenixedu.domain.inquiries.InquiryRadioGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTextBoxQuestion;

import org.apache.commons.lang.StringUtils;

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
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryGroupQuestionRenderer extends InputRenderer {

    private String columnClasses;
    private boolean readOnly = false;
    private String postBackMethod;
    private String postBackAnchor;

    public String getColumnClasses() {
        return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setPostBackMethod(String postBackMethod) {
        this.postBackMethod = postBackMethod;
    }

    public String getPostBackMethod() {
        return postBackMethod;
    }

    public void setPostBackAnchor(String postBackAnchor) {
        this.postBackAnchor = postBackAnchor;
    }

    public String getPostBackAnchor() {
        return postBackAnchor;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new InquiryQuestionLayout(object);
    }

    private class InquiryQuestionLayout extends Layout {

        public InquiryQuestionLayout(Object object) {
        }

        private Integer colNumber = null;
        private int colSpan;;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {

            final HtmlTable mainTable = new HtmlTable();
            InquiryGroupQuestionBean inquiryGroupQuestion =
                    (InquiryGroupQuestionBean) getInputContext().getMetaObject().getObject();

            if (!inquiryGroupQuestion.isVisible()) {
                return mainTable;
            }
            String[] groupConditionValues = inquiryGroupQuestion.getConditionValues();

            MetaObject metaObject =
                    MetaObjectFactory.createObject(inquiryGroupQuestion.getInquiryQuestions(), RenderKit.getInstance()
                            .findSchema("inquiryQuestionDTO.answer"));

            boolean groupHeader = false;
            for (MetaSlot metaSlot : metaObject.getAllSlots()) {

                InquiryQuestionDTO inquiryQuestion = (InquiryQuestionDTO) metaSlot.getMetaObject().getObject();

                if (!inquiryQuestion.isVisible()) {
                    continue;
                }
                String[] questionConditionValues = inquiryQuestion.getConditionValues();

                RenderKit kit = RenderKit.getInstance();
                HtmlFormComponent formComponent = null;
                getContext().getViewState().setMetaObject(metaObject);
                PresentationContext newContext = getContext().createSubContext(metaSlot);
                newContext.setSchema(metaSlot.getSchema());

                InquiryQuestionHeader questionHeader = null;

                if (inquiryGroupQuestion.getInquiryGroupQuestion().hasInquiryQuestionHeader()) {
                    questionHeader = inquiryGroupQuestion.getInquiryGroupQuestion().getInquiryQuestionHeader();
                    groupHeader = true;
                }
                if (inquiryQuestion.getInquiryQuestion().hasInquiryQuestionHeader()) {
                    questionHeader = inquiryQuestion.getInquiryQuestion().getInquiryQuestionHeader();
                    groupHeader = false;
                }

                if (groupHeader && inquiryQuestion.getInquiryQuestion().getQuestionOrder() == 1) {
                    createHeaderRow(questionHeader, mainTable, inquiryGroupQuestion.getInquiryGroupQuestion().getRequired(),
                            groupConditionValues, inquiryQuestion.getInquiryQuestion());
                } else if (!groupHeader && questionHeader != null) {
                    createHeaderRow(questionHeader, mainTable, false, questionConditionValues,
                            inquiryQuestion.getInquiryQuestion());
                }

                HtmlTableRow questionRow = mainTable.createRow();
                final HtmlTableCell labelCell = questionRow.createCell(CellType.HEADER);

                labelCell.setBody(new HtmlText(getFinalMLString(inquiryQuestion.getInquiryQuestion().getLabel(),
                        questionConditionValues)
                        + getQuestionRequiredIndication(inquiryQuestion.getInquiryQuestion())
                        + getQuestionToolTip(inquiryQuestion.getInquiryQuestion().getToolTip()), false));
                labelCell.addClass("firstcol");

                if (inquiryQuestion.getInquiryQuestion() instanceof InquiryTextBoxQuestion) {
                    final InquiryTextBoxQuestion textBoxQuestion = (InquiryTextBoxQuestion) inquiryQuestion.getInquiryQuestion();

                    newContext.setLayout("inquiry-textbox-question");
                    Properties properties = new Properties(metaSlot.getProperties());
                    properties.put("textArea", textBoxQuestion.getTextArea());
                    properties.put("readOnly", isReadOnly());
                    newContext.setProperties(properties);

                    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());

                    final HtmlTableCell cell = questionRow.createCell();
                    cell.setColspan(textBoxQuestion.getAutofit() ? colSpan : 1);
                    cell.setStyle("text-align: left;");
                    cell.setBody(formComponent);

                    if (getColNumber() == null) {
                        setColNumber(1);
                    }

                } else if (inquiryQuestion.getInquiryQuestion() instanceof InquiryRadioGroupQuestion) {

                    newContext.setLayout("inquiry-radiogroup-question");
                    Properties properties = new Properties(metaSlot.getProperties());
                    properties.put("radioQuestion", inquiryQuestion);
                    properties.put("questionHeader", questionHeader);
                    properties.put("readOnly", isReadOnly());
                    newContext.setProperties(properties);

                    InquiryRadioGroupQuestion radioGroupQuestion =
                            (InquiryRadioGroupQuestion) inquiryQuestion.getInquiryQuestion();
                    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());
                    boolean hasGroupQuestionContions = radioGroupQuestion.hasGroupDependentQuestionCondition();
                    int iter = 0;
                    for (HtmlRadioButton htmlRadioButton : ((HtmlRadioButtonGroup) formComponent).getRadioButtons()) {
                        htmlRadioButton.bind(metaSlot);
                        if (hasGroupQuestionContions && !isReadOnly()) {
                            htmlRadioButton.setOnClick(getOnclickJS());
                            htmlRadioButton.setOnDblClick(getOnclickJS());
                        }
                        htmlRadioButton.setDisabled(isReadOnly());

                        if (radioGroupQuestion.getIsMatrix()) {
                            questionRow.createCell().setBody(htmlRadioButton);
                            iter++;
                        } else {
                            HtmlTableRow radioRow = mainTable.createRow();
                            HtmlTableCell firstCell = radioRow.createCell(CellType.HEADER);
                            firstCell.setBody(new HtmlText(htmlRadioButton.getText()));
                            firstCell.addClass("firstcol");
                            htmlRadioButton.setText(null); //removing the label, so that it only appears in the right place
                            radioRow.createCell().setBody(htmlRadioButton);
                            labelCell.setColspan(2); //to adjust
                            applyStyles(radioRow);
                            iter = 1;
                        }
                    }

                    setColNumber(iter);

                } else if (inquiryQuestion.getInquiryQuestion() instanceof InquiryCheckBoxQuestion) {

                    newContext.setLayout("inquiry-checkbox-question");
                    Properties properties = new Properties(metaSlot.getProperties());
                    properties.put("readOnly", isReadOnly());
                    newContext.setProperties(properties);

                    formComponent = (HtmlFormComponent) kit.render(newContext, metaSlot.getObject(), metaSlot.getType());

                    final HtmlTableCell cell = questionRow.createCell();
                    cell.setBody(formComponent);
                    if (getColNumber() == null) {
                        setColNumber(1);
                    }
                }
                formComponent.bind(metaSlot);
                applyStyles(questionRow);
            }

            getInputContext().getForm().getCancelButton().setVisible(false);
            if (isReadOnly()) {
                getInputContext().getForm().getSubmitButton().setVisible(false);
            }
            return mainTable;
        }

        private String getFinalMLString(MultiLanguageString label, String[] conditionValues) {
            String text = label.toString();
            if (conditionValues != null) {
                text =
                        text.replace(ECTSVisibleCondition.UC_ECTS_MARKER,
                                conditionValues[ECTSVisibleCondition.UC_ECTS_MARKER_INDEX]);
                text =
                        text.replace(ECTSVisibleCondition.CALCULATED_ECTS_MARKER,
                                conditionValues[ECTSVisibleCondition.CALCULATED_ECTS_MARKER_INDEX]);
            }
            return text;
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

        private String getQuestionToolTip(final MultiLanguageString multiLanguageString) {
            return multiLanguageString != null ? "<a href=\"#\" class=\"help\"> [?] <span>" + multiLanguageString + "</span></a>" : StringUtils.EMPTY;
        }

        private String getQuestionRequiredIndication(final InquiryQuestion inquiryQuestion) {
            return inquiryQuestion.getRequired() && inquiryQuestion.getShowRequiredMark() ? "<span class=\"required\"> * </span>" : StringUtils.EMPTY;
        }

        private void createHeaderRow(final InquiryQuestionHeader inquiryQuestionHeader, final HtmlTable mainTable,
                boolean required, String[] conditionValues, InquiryQuestion inquiryQuestion) {
            if (inquiryQuestion instanceof InquiryRadioGroupQuestion) {
                if (!((InquiryRadioGroupQuestion) inquiryQuestion).getIsMatrix()) {
                    return;
                }
            }
            final HtmlTableRow headerRow = mainTable.createRow();

            final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);

            StringBuilder headerTitle = new StringBuilder();
            if (inquiryQuestionHeader.getTitle() != null) {
                headerTitle.append(getFinalMLString(inquiryQuestionHeader.getTitle(), conditionValues));
                if (required) {
                    headerTitle.append("<span class=\"required\"> * </span>");
                }
            }
            headerTitle.append(getQuestionToolTip(inquiryQuestionHeader.getToolTip()));

            firstHeaderCell.setBody(new HtmlText(headerTitle.toString(), false));
            firstHeaderCell.addClass("firstcol");

            if (inquiryQuestionHeader.getScaleHeaders() != null && inquiryQuestionHeader.getScaleHeaders().getScaleLength() > 0) {
                String cssClass = "col";
                int iter = 1;
                for (MultiLanguageString scale : inquiryQuestionHeader.getScaleHeaders().getScale()) {
                    HtmlTableCell headerCell = headerRow.createCell(CellType.HEADER);
                    headerCell.setBody(new HtmlText(scale.toString(), false));
                    headerCell.addClass(cssClass + iter);
                    iter++;
                }
                colSpan = iter - 1;
            } else if (inquiryQuestion instanceof InquiryCheckBoxQuestion) {
                firstHeaderCell.setColspan(2);
            } else {
                headerRow.createCell(CellType.HEADER);
            }
        }

        private String getOnclickJS() {
            StringBuilder sb = new StringBuilder();
            if (getPostBackAnchor() != null) {
                sb.append("this.form.action=this.form.action + '#").append(getPostBackAnchor()).append("';");
            }
            sb.append("this.form.method.value='").append(getPostBackMethod()).append("';this.form.submit();");
            return sb.toString();
        }

        @Override
        public String getClasses() {
            return "question " + "q" + getColNumber() + "col";
        }

        public void setColNumber(Integer colNumber) {
            this.colNumber = colNumber;
        }

        public Integer getColNumber() {
            return colNumber;
        }
    }
}
