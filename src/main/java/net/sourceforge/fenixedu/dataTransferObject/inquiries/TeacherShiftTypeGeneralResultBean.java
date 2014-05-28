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

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;

public class TeacherShiftTypeGeneralResultBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Professorship professorship;
    private ShiftType shiftType;
    private InquiryResult inquiryResult;

    public TeacherShiftTypeGeneralResultBean(Professorship professorship, ShiftType shiftType, InquiryResult inquiryResult) {
        setProfessorship(professorship);
        setShiftType(shiftType);
        setInquiryResult(inquiryResult);
    }

    public String getTeacherId() {
        if (getProfessorship().getPerson().getTeacher() != null) {
            String identifier;
            if (getProfessorship().getPerson().getEmployee() != null) {
                identifier = getProfessorship().getPerson().getEmployee().getEmployeeNumber().toString();
            } else {
                identifier = getProfessorship().getPerson().getIstUsername();
            }
            return " (" + identifier + ") ";
        }
        return "";
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

    public InquiryResult getInquiryResult() {
        return inquiryResult;
    }

    public void setInquiryResult(InquiryResult inquiryResult) {
        this.inquiryResult = inquiryResult;
    }

}
