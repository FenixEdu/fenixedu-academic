package net.sourceforge.fenixedu.domain.phd.seminar;

public enum PublicPresentationSeminarProcessStateType {

    /*
     * Check this states
     */

    PRESENTATION_REQUESTED,

    PRESENTATION_REQUEST_SENT_TO_SUPERVISOR,

    SUPERVISOR_SEND_COMMISSION_PROPOSAL, // To department or coordinator?

    COMMISSION_PROPOSAL_WAITING_FOR_RATIFICATION,

    COMMISSION_PROPOSAL_RATIFIED_BY_SCIENTIFIC_COUNCIL,

    REPORT_WAITING_FOR_RATIFICATION,

    REPORT_PROPOSAL_RATIFIED_BY_SCIENTIFIC_COUNCIL,

    CONCLUDED; // ?

}
