package net.sourceforge.fenixedu.domain.personnelSection.contracts;

import net.sourceforge.fenixedu.domain.Country;
import pt.ist.bennu.core.domain.Bennu;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class PersonGrantOwnerEquivalent extends PersonGrantOwnerEquivalent_Base {

    public PersonGrantOwnerEquivalent(final GiafProfessionalData giafProfessionalData, final LocalDate beginDate,
            final LocalDate endDate, final String motive, final String local, final String giafCountryName,
            final Country country, final GrantOwnerEquivalent grantOwnerEquivalent, final String grantOwnerEquivalentGiafId,
            final DateTime creationDate, final DateTime modifiedDate) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGiafProfessionalData(giafProfessionalData);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setMotive(motive);
        setLocal(local);
        setGiafCountryName(giafCountryName);
        setCountry(country);
        setGrantOwnerEquivalent(grantOwnerEquivalent);
        setGrantOwnerEquivalentGiafId(grantOwnerEquivalentGiafId);
        setCreationDate(creationDate);
        setModifiedDate(modifiedDate);
        setImportationDate(new DateTime());
    }

    @Override
    public boolean isValid() {
        return getCountry() != null && getBeginDate() != null && getEndDate() != null && !getBeginDate().isAfter(getEndDate())
                && getGrantOwnerEquivalent() != null;
    }

    @Override
    public boolean getIsSabaticalOrEquivalent() {
        return getGrantOwnerEquivalent().getIsSabaticalOrEquivalent();
    }

    @Override
    public boolean getHasMandatoryCredits() {
        return getGrantOwnerEquivalent().getHasMandatoryCredits();
    }

    @Override
    public boolean getGiveCredits() {
        return getGrantOwnerEquivalent().getGiveCredits();
    }

    @Deprecated
    public boolean hasLocal() {
        return getLocal() != null;
    }

    @Deprecated
    public boolean hasGiafCountryName() {
        return getGiafCountryName() != null;
    }

    @Deprecated
    public boolean hasGrantOwnerEquivalentGiafId() {
        return getGrantOwnerEquivalentGiafId() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

    @Deprecated
    public boolean hasMotive() {
        return getMotive() != null;
    }

    @Deprecated
    public boolean hasGrantOwnerEquivalent() {
        return getGrantOwnerEquivalent() != null;
    }

}
