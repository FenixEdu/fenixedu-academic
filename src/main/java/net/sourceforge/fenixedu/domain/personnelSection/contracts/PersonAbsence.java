package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import pt.ist.bennu.core.domain.Bennu;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonAbsence extends PersonAbsence_Base {

    public PersonAbsence(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate, final LocalDate endDate,
            final Absence absence, final String absenceGiafId, final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setAbsence(absence);
        setAbsenceGiafId(absenceGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    @Override
    public boolean isValid() {
        return getAbsence() != null && getBeginDate() != null && getEndDate() != null && !getBeginDate().isAfter(getEndDate());
    }

    @Override
    public boolean getIsSabaticalOrEquivalent() {
        return getAbsence().getIsSabaticalOrEquivalent();
    }

    @Override
    public boolean getHasMandatoryCredits() {
        return getAbsence().getHasMandatoryCredits();
    }

    @Override
    public boolean getGiveCredits() {
        return getAbsence().getGiveCredits();
    }

    @Deprecated
    public boolean hasAbsence() {
        return getAbsence() != null;
    }

    @Deprecated
    public boolean hasAbsenceGiafId() {
        return getAbsenceGiafId() != null;
    }

}
