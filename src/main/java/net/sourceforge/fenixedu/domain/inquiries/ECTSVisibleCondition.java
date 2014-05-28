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
package net.sourceforge.fenixedu.domain.inquiries;

public class ECTSVisibleCondition extends ECTSVisibleCondition_Base {

    final static public String UC_ECTS_MARKER = "_!##!_";
    final static public String CALCULATED_ECTS_MARKER = "_$%%$_";
    final static public int UC_ECTS_MARKER_INDEX = 0;
    final static public int CALCULATED_ECTS_MARKER_INDEX = 1;

    public ECTSVisibleCondition() {
        super();
    }

    public boolean isVisible(StudentInquiryRegistry studentInquiryRegistry) {
        if (studentInquiryRegistry.getEstimatedECTS() == null) {
            return false;
        }
        if (getWorkLoadExcessive()) {
            Double difference =
                    studentInquiryRegistry.getEstimatedECTS()
                            - studentInquiryRegistry.getCurricularCourse().getEctsCredits(
                                    studentInquiryRegistry.getExecutionPeriod());
            return difference > getEctsDifference();
        }
        Double difference =
                studentInquiryRegistry.getCurricularCourse().getEctsCredits(studentInquiryRegistry.getExecutionPeriod())
                        - studentInquiryRegistry.getEstimatedECTS();
        return difference > getEctsDifference();
    }

    /**
     * Returns an Array of strings, with 2 positions,
     * - the first one is the curricular course ECTS for the given execution period
     * - the second one is the calculated ECTS from the student answers
     * 
     * @param studentInquiryRegistry
     * @return
     */
    public String[] getConditionValues(StudentInquiryRegistry studentInquiryRegistry) {
        String[] conditionValues = new String[2];
        conditionValues[UC_ECTS_MARKER_INDEX] =
                studentInquiryRegistry.getCurricularCourse().getEctsCredits(studentInquiryRegistry.getExecutionPeriod())
                        .toString();
        conditionValues[CALCULATED_ECTS_MARKER_INDEX] =
                studentInquiryRegistry.getEstimatedECTS() != null ? studentInquiryRegistry.getEstimatedECTS().toString() : null;
        return conditionValues;
    }

    @Deprecated
    public boolean hasWorkLoadExcessive() {
        return getWorkLoadExcessive() != null;
    }

    @Deprecated
    public boolean hasEctsDifference() {
        return getEctsDifference() != null;
    }

}
