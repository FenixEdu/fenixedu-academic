package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

public class MobilityAgreement extends MobilityAgreement_Base {

    private MobilityAgreement() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public MobilityAgreement(MobilityProgram program, UniversityUnit university) {
        this();
        if (university == null) {
            throw new NullPointerException("error.university.cannot.be.null");
        }
        setMobilityProgram(program);
        setUniversityUnit(university);
    }

    public void delete() {
        setMobilityProgram(null);
        setUniversityUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static MobilityAgreement getOrCreateAgreement(MobilityProgram mobilityProgram, UniversityUnit universityUnit) {
        MobilityAgreement mobilityAgreementByUniversityUnit =
                mobilityProgram.getMobilityAgreementByUniversityUnit(universityUnit);

        if (mobilityAgreementByUniversityUnit == null) {
            return new MobilityAgreement(mobilityProgram, universityUnit);
        }

        return mobilityAgreementByUniversityUnit;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest> getOutboundMobilityCandidacyContest() {
        return getOutboundMobilityCandidacyContestSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyContest() {
        return !getOutboundMobilityCandidacyContestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityQuota> getMobilityQuotas() {
        return getMobilityQuotasSet();
    }

    @Deprecated
    public boolean hasAnyMobilityQuotas() {
        return !getMobilityQuotasSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUniversityUnit() {
        return getUniversityUnit() != null;
    }

    @Deprecated
    public boolean hasMobilityProgram() {
        return getMobilityProgram() != null;
    }

}
