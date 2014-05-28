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

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextArea;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryGroupResultsResumeInputRenderer extends InquiryGroupResultsResumeRenderer {

    private MetaObject metaObject;

    public void setMetaObject(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    public MetaObject getMetaObject() {
        return metaObject;
    }

    @Override
    public boolean isShowComments() {
        return true;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new InquiryGroupResultLayout(object);
    }

    private class InquiryGroupResultLayout extends Layout {

        public InquiryGroupResultLayout(Object object) {
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            return renderComponent(object);
        }

        @Override
        public String getClasses() {
            return getTopClass();
        }
    }

    @Override
    protected HtmlComponent prepareAndRenderScaledGroups(GroupResultsSummaryBean groupResultsSummaryBean) {
        MetaObject metaObject =
                MetaObjectFactory.createObject(groupResultsSummaryBean.getQuestionsResults(),
                        RenderKit.getInstance().findSchema("questionResultsSummaryBean.input"));
        getContext().getViewState().setMetaObject(metaObject);
        setMetaObject(metaObject);
        return super.prepareAndRenderScaledGroups(groupResultsSummaryBean);
    }

    @Override
    protected void renderComments(final HtmlTable mainTable, QuestionResultsSummaryBean questionResultsSummaryBean,
            HtmlTableRow row) {
        if (questionResultsSummaryBean.getResultClassification() != null
                && questionResultsSummaryBean.getResultClassification().isMandatoryComment()) {

            MetaSlot metaSlot = getMetaSlot(getMetaObject(), questionResultsSummaryBean);
            createCommentsRow(mainTable, row, metaSlot, questionResultsSummaryBean);
        }
    }

    private MetaSlot getMetaSlot(MetaObject metaObject, QuestionResultsSummaryBean questionResultsSummaryBean) {
        for (MetaSlot metaSlot : metaObject.getAllSlots()) {
            QuestionResultsSummaryBean bean = (QuestionResultsSummaryBean) metaSlot.getMetaObject().getObject();
            if (bean.getInquiryQuestion() == questionResultsSummaryBean.getInquiryQuestion()) {
                return metaSlot;
            }
        }
        return null;
    }

    private void createCommentsRow(final HtmlTable mainTable, final HtmlTableRow row, MetaSlot metaSlot,
            QuestionResultsSummaryBean questionResultsSummaryBean) {
        HtmlTableRow commentRow = mainTable.createRow();
        HtmlTableCell commentCell = commentRow.createCell();
        commentCell.setColspan(row.getCells().size());
        commentCell.setClasses("comment");

        HtmlBlockContainer allCommentsBlock = super.createComments(questionResultsSummaryBean);

        HtmlBlockContainer commentBlock = new HtmlBlockContainer();
        String commentHeader = "Comentário";
        if (questionResultsSummaryBean.getPersonCategory().equals(ResultPersonCategory.REGENT)
                && questionResultsSummaryBean.getQuestionResult().getProfessorship() != null) {
            InquiryResultComment resultComment =
                    questionResultsSummaryBean.getQuestionResult().getInquiryResultComment(
                            questionResultsSummaryBean.getCommentPerson(), ResultPersonCategory.TEACHER);
            if (resultComment != null && !StringUtils.isEmpty(resultComment.getComment())) {
                commentHeader = "Se desejar, pode acrescentar um comentário (opcional)";
            }
        }
        HtmlText commentText = new HtmlText("<p class=\"mbottom05\">" + commentHeader + "</p>");
        commentText.setEscaped(false);
        HtmlTextArea commentTextArea = new HtmlTextArea();
        commentTextArea.setColumns(70);
        commentTextArea.setRows(5);
        commentTextArea.setValue(metaSlot.getObject() != null ? metaSlot.getObject().toString() : null);
        commentTextArea.bind(metaSlot);

        commentBlock.addChild(commentText);
        commentBlock.addChild(commentTextArea);

        allCommentsBlock.addChild(commentBlock);
        commentCell.setBody(allCommentsBlock);
    }
}
