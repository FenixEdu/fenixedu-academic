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
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.Serializable;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UploadStudentInquiriesTeachingResultsBean extends UploadStudentInquiriesResultsBean implements Serializable {

    private String keyTeacherHeader;

    private String shiftTypeHeader;

    private String unsatisfactoryResultsAssiduityHeader;

    private String unsatisfactoryResultsPedagogicalCapacityHeader;

    private String unsatisfactoryResultsPresencialLearningHeader;

    private String unsatisfactoryResultsStudentInteractionHeader;

    private String internalDegreeDisclosureHeader;

    public String getKeyTeacherHeader() {
        return keyTeacherHeader;
    }

    public void setKeyTeacherHeader(String keyTeacherHeader) {
        this.keyTeacherHeader = keyTeacherHeader;
    }

    public String getShiftTypeHeader() {
        return shiftTypeHeader;
    }

    public void setShiftTypeHeader(String shiftTypeHeader) {
        this.shiftTypeHeader = shiftTypeHeader;
    }

    public String getUnsatisfactoryResultsAssiduityHeader() {
        return unsatisfactoryResultsAssiduityHeader;
    }

    public void setUnsatisfactoryResultsAssiduityHeader(String unsatisfactoryResultsAssiduityHeader) {
        this.unsatisfactoryResultsAssiduityHeader = unsatisfactoryResultsAssiduityHeader;
    }

    public String getUnsatisfactoryResultsPedagogicalCapacityHeader() {
        return unsatisfactoryResultsPedagogicalCapacityHeader;
    }

    public void setUnsatisfactoryResultsPedagogicalCapacityHeader(String unsatisfactoryResultsPedagogicalCapacityHeader) {
        this.unsatisfactoryResultsPedagogicalCapacityHeader = unsatisfactoryResultsPedagogicalCapacityHeader;
    }

    public String getUnsatisfactoryResultsPresencialLearningHeader() {
        return unsatisfactoryResultsPresencialLearningHeader;
    }

    public void setUnsatisfactoryResultsPresencialLearningHeader(String unsatisfactoryResultsPresencialLearningHeader) {
        this.unsatisfactoryResultsPresencialLearningHeader = unsatisfactoryResultsPresencialLearningHeader;
    }

    public String getUnsatisfactoryResultsStudentInteractionHeader() {
        return unsatisfactoryResultsStudentInteractionHeader;
    }

    public void setUnsatisfactoryResultsStudentInteractionHeader(String unsatisfactoryResultsStudentInteractionHeader) {
        this.unsatisfactoryResultsStudentInteractionHeader = unsatisfactoryResultsStudentInteractionHeader;
    }

    public String getInternalDegreeDisclosureHeader() {
        return internalDegreeDisclosureHeader;
    }

    public void setInternalDegreeDisclosureHeader(String internalDegreeDisclosureHeader) {
        this.internalDegreeDisclosureHeader = internalDegreeDisclosureHeader;
    }
}
