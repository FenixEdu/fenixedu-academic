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
package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

/**
 * Type of {@GeneratedDocument}s
 * 
 * @author Pedro Santos (pmrsa)
 */
public enum GeneratedDocumentType {
    CREDIT_NOTE,

    RECEIPT,

    SCHOOL_REGISTRATION_CERTIFICATE,

    ENROLMENT_CERTIFICATE,

    APPROVEMENT_CERTIFICATE,

    APPROVEMENT_MOBILITY_CERTIFICATE,

    DEGREE_FINALIZATION_CERTIFICATE,

    PHD_FINALIZATION_CERTIFICATE,

    EXAM_DATE_CERTIFICATE,

    SCHOOL_REGISTRATION_DECLARATION,

    ENROLMENT_DECLARATION,

    IRS_DECLARATION,

    ANNUAL_IRS_DECLARATION,

    REGISTRY_DIPLOMA_REQUEST,

    DIPLOMA_REQUEST,

    DIPLOMA_SUPPLEMENT_REQUEST,

    COURSE_LOAD,

    EXTERNAL_COURSE_LOAD,

    PROGRAM_CERTIFICATE,

    EXTERNAL_PROGRAM_CERTIFICATE,

    EXTRA_CURRICULAR_CERTIFICATE,

    UNDER_23_TRANSPORTS_REQUEST,

    STANDALONE_ENROLMENT_CERTIFICATE,

    LIBRARY_MISSING_CARDS,

    LIBRARY_MISSING_LETTERS,

    LIBRARY_MISSING_LETTERS_STUDENTS,

    QUEUE_JOB;

    public static GeneratedDocumentType determineType(DocumentRequestType documentRequestType) {
        return valueOf(documentRequestType.name());
    }
}
