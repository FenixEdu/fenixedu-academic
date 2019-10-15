package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum EventExemptionJustificationType {

    // Directive council authorization dispatch
    DIRECTIVE_COUNCIL_AUTHORIZATION,

    ENROLMENT_AFTER_EQUIVALENCE,

    FINE_EXEMPTION,
    // Staff and Agents from institution

    INSTITUTION,

    // Institution or Institution Partners Grant Owners
    INSTITUTION_GRANT_OWNER,

    MILITARY_SCOPE,

    MIT_AGREEMENT,

    MOBILITY_SCOPE,

    NUCLEUS_COORDINATOR_AUTHORIZATION,

    // Staff from Institution Partners
    OTHER_INSTITUTION,

    // PALOP Teachers
    PALOP_TEACHER,

    PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION,

    // Used to mark exemptions created by separation cycles
    SEPARATION_CYCLES_AUTHORIZATION,

    SOCIAL_SHARE_GRANT_OWNER,

    SON_OF_DECORATED_MILITARY,

    // Students teaching classes
    STUDENT_TEACH,

    // Third party entity offers to pay the gratuity
    THIRD_PARTY_CONTRIBUTION,

    TIME,

    TRANSFERED_APPLICATION,

    CUSTOM_PAYMENT_PLAN,

    CANCELLED;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

}
