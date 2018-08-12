/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import org.fenixedu.academic.domain.degree.DegreeType;

public enum DocumentRequestType {

    SCHOOL_REGISTRATION_CERTIFICATE(true, false, true, false, false, false),

    ENROLMENT_CERTIFICATE(true, false, true, false, false, false),

    APPROVEMENT_CERTIFICATE(true, false),

    APPROVEMENT_MOBILITY_CERTIFICATE(true, false),

    DEGREE_FINALIZATION_CERTIFICATE(true, false, false, true, false, false),

    PHD_FINALIZATION_CERTIFICATE(false, false),

    EXAM_DATE_CERTIFICATE(true, false),

    SCHOOL_REGISTRATION_DECLARATION(false, true),

    ENROLMENT_DECLARATION(false, true),

    IRS_DECLARATION(true, true),

    GENERIC_DECLARATION(true, true),

    REGISTRY_DIPLOMA_REQUEST(true, false),

    DIPLOMA_REQUEST(true, false),

    DIPLOMA_SUPPLEMENT_REQUEST(true, false, false, false, false, true),

    PAST_DIPLOMA_REQUEST(true, false, false, false, true, false),

    PHOTOCOPY(false, false),

    COURSE_LOAD(true, false),

    EXTERNAL_COURSE_LOAD(true, false),

    PROGRAM_CERTIFICATE(true, false),

    EXTERNAL_PROGRAM_CERTIFICATE(true, false),

    EXTRA_CURRICULAR_CERTIFICATE(true, false),

    UNDER_23_TRANSPORTS_REQUEST(false, false),

    STANDALONE_ENROLMENT_CERTIFICATE(true, false);

    private boolean hasAdditionalInformation;

    private boolean allowedToQuickDeliver;

    private boolean studentRequestable;

    private boolean withBranch;

    private boolean preBolonha;

    private boolean bolonhaOnly;

    static private List<DocumentRequestType> CERTIFICATES = Arrays.asList(SCHOOL_REGISTRATION_CERTIFICATE, ENROLMENT_CERTIFICATE,
            APPROVEMENT_CERTIFICATE, APPROVEMENT_MOBILITY_CERTIFICATE, DEGREE_FINALIZATION_CERTIFICATE, EXAM_DATE_CERTIFICATE,
            COURSE_LOAD, EXTERNAL_COURSE_LOAD, PROGRAM_CERTIFICATE, EXTERNAL_PROGRAM_CERTIFICATE, EXTRA_CURRICULAR_CERTIFICATE,
            STANDALONE_ENROLMENT_CERTIFICATE);

    static private List<DocumentRequestType> DECLARATIONS = Arrays.asList(SCHOOL_REGISTRATION_DECLARATION, ENROLMENT_DECLARATION,
            IRS_DECLARATION, GENERIC_DECLARATION);

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver, boolean studentRequestable,
            boolean withBranch, boolean preBolonha, boolean bolonhaOnly) {
        this.hasAdditionalInformation = hasAdditionalInformation;
        this.allowedToQuickDeliver = allowedToQuickDeliver;
        this.studentRequestable = studentRequestable;
        this.withBranch = withBranch;
        this.preBolonha = preBolonha;
        this.bolonhaOnly = bolonhaOnly;
    }

    private DocumentRequestType(boolean hasAdditionalInformation, boolean allowedToQuickDeliver) {
        this(hasAdditionalInformation, allowedToQuickDeliver, false, false, false, false);
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return DocumentRequestType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return DocumentRequestType.class.getName() + "." + name();
    }

    public boolean isCertificate() {
        return CERTIFICATES.contains(this);
    }

    public boolean isDeclaration() {
        return DECLARATIONS.contains(this);
    }

    public boolean isDiplomaSupplement() {
        return this == DIPLOMA_SUPPLEMENT_REQUEST;
    }

    public boolean isDiploma() {
        return this == DIPLOMA_REQUEST;
    }

    public boolean isRegistryDiploma() {
        return this == REGISTRY_DIPLOMA_REQUEST;
    }

    public boolean isPastDiploma() {
        return this == PAST_DIPLOMA_REQUEST;
    }

    final public boolean getHasAdditionalInformation() {
        return hasAdditionalInformation;
    }

    final public boolean getHasCycleTypeDependency(final DegreeType degreeType) {
        return degreeType.getCycleTypes().size() > 1;
    }

    public boolean isAllowedToQuickDeliver() {
        return allowedToQuickDeliver;
    }

    public boolean isStudentRequestable() {
        return studentRequestable;
    }

    public boolean isPreBolonha() {
        return preBolonha;
    }

    public boolean isBolonhaOnly() {
        return bolonhaOnly;
    }

    public boolean withBranch() {
        return withBranch;
    }

    public boolean getCanBeFreeProcessed() {
        return isDeclaration() || this == SCHOOL_REGISTRATION_CERTIFICATE || this == DEGREE_FINALIZATION_CERTIFICATE;
    }

}
