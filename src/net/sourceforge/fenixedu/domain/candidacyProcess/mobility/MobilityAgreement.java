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
        removeMobilityProgram();
        removeUniversityUnit();
        removeRootDomainObject();
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

}
