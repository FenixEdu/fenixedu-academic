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
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QUCResultsLog;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorResultsBean extends GlobalCommentsResultsBean {

    private static final long serialVersionUID = 1L;
    private List<BlockResultsSummaryBean> curricularBlockResults;
    private ExecutionDegree executionDegree;

    public CoordinatorResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person coordinator,
            boolean backToResume) {
        super(executionCourse, coordinator, backToResume);
        setExecutionDegree(executionDegree);
        initResultComment(coordinator, true);
        initCurricularBlocksResults(executionCourse, executionDegree, coordinator);
    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person person) {
        CurricularCourseInquiryTemplate courseInquiryTemplate =
                CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
        setCurricularBlockResults(new ArrayList<BlockResultsSummaryBean>());
        List<InquiryResult> results = executionCourse.getInquiryResultsByExecutionDegreeAndForTeachers(executionDegree);
        if (results != null && results.size() > 5) {
            for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
                getCurricularBlockResults().add(new BlockResultsSummaryBean(inquiryBlock, results, person, getPersonCategory()));
            }
        }
        Collections.sort(getCurricularBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    @Override
    protected InquiryGlobalComment createGlobalComment() {
        return new InquiryGlobalComment(getExecutionCourse(), getExecutionDegree());
    }

    @Override
    protected ResultPersonCategory getPersonCategory() {
        return ResultPersonCategory.DEGREE_COORDINATOR;
    }

    @Override
    public InquiryGlobalComment getInquiryGlobalComment() {
        return getExecutionCourse().getInquiryGlobalComment(getExecutionDegree());
    }

    public List<BlockResultsSummaryBean> getCurricularBlockResults() {
        return curricularBlockResults;
    }

    public void setCurricularBlockResults(List<BlockResultsSummaryBean> curricularBlockResults) {
        this.curricularBlockResults = curricularBlockResults;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void qucResultsLog() {
        QUCResultsLog.createLog(getExecutionDegree().getDegree(), getExecutionDegree().getExecutionYear(),
                Bundle.MESSAGING, "log.degree.qucresults.comment", getExecutionDegree().getDegree()
                        .getPresentationName(), getExecutionCourse().getNameI18N().getContent());
    }
}
