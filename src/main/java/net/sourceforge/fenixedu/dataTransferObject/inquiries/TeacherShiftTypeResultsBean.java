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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;

public class TeacherShiftTypeResultsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Professorship professorship;
    private ShiftType shiftType;
    private List<BlockResultsSummaryBean> blockResults = new ArrayList<BlockResultsSummaryBean>();

    public TeacherShiftTypeResultsBean(Professorship professorship, ShiftType shiftType, ExecutionSemester executionPeriod,
            List<InquiryResult> inquiryResults, Person person, ResultPersonCategory personCategory) {
        setProfessorship(professorship);
        setShiftType(shiftType);

        StudentTeacherInquiryTemplate inquiryTemplate =
                StudentTeacherInquiryTemplate.getTemplateByExecutionPeriod(executionPeriod);
        setBlockResults(new ArrayList<BlockResultsSummaryBean>());
        for (InquiryBlock inquiryBlock : inquiryTemplate.getInquiryBlocks()) {
            getBlockResults().add(new BlockResultsSummaryBean(inquiryBlock, inquiryResults, person, personCategory));
        }
        Collections.sort(getBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    public Professorship getProfessorship() {
        return professorship;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorship = professorship;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public List<BlockResultsSummaryBean> getBlockResults() {
        return blockResults;
    }

    public void setBlockResults(List<BlockResultsSummaryBean> blockResults) {
        this.blockResults = blockResults;
    }
}
