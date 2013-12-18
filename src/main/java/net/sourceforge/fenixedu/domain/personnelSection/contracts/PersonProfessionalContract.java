package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonProfessionalContract extends PersonProfessionalContract_Base {

    public PersonProfessionalContract(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final ContractSituation contractSituation, final String contractSituationGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setContractSituation(contractSituation);
        setContractSituationGiafId(contractSituationGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    public boolean isValid() {
        return getContractSituation() != null && getBeginDate() != null
                && (getEndDate() == null || !getBeginDate().isAfter(getEndDate()));
    }
    @Deprecated
    public boolean hasModifiedDate() {
        return getModifiedDate() != null;
    }

    @Deprecated
    public boolean hasContractSituation() {
        return getContractSituation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasAnulationDate() {
        return getAnulationDate() != null;
    }

    @Deprecated
    public boolean hasImportationDate() {
        return getImportationDate() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasContractSituationGiafId() {
        return getContractSituationGiafId() != null;
    }

    @Deprecated
    public boolean hasGiafProfessionalData() {
        return getGiafProfessionalData() != null;
    }

}
