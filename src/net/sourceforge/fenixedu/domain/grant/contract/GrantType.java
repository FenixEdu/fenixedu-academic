package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.State;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class GrantType extends GrantType_Base {

    public GrantType(String sigla, String name, Integer minPeriodDays, Integer maxPeriodDays, Double indicativeValue,
            String source, LocalDate validUntil) {
        init(sigla, name, minPeriodDays, maxPeriodDays, indicativeValue, source, validUntil);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(String sigla, String name, Integer minPeriodDays, Integer maxPeriodDays, Double indicativeValue,
            String source, LocalDate validUntil) {
        check(sigla, "message.grant.grantType.emptySigla");
        checkSigla(sigla);
        check(name, "message.grant.grantType.emptyName");
        setSigla(sigla);
        setName(name);
        setMinPeriodDays(minPeriodDays);
        setMaxPeriodDays(maxPeriodDays);
        setIndicativeValue(indicativeValue);
        setSource(source);
        setValidUntil(validUntil);
    }

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public void editGrantType(String sigla, String name, Integer minPeriodDays, Integer maxPeriodDays, Double indicativeValue,
            String source, LocalDate validUntil) {
        init(sigla, name, minPeriodDays, maxPeriodDays, indicativeValue, source, validUntil);
    }

    protected void checkSigla(String sigla) {
        for (final GrantType grantType : RootDomainObject.getInstance().getGrantTypes()) {
            if (grantType.getSigla().equalsIgnoreCase(sigla) && !grantType.equals(this)) {
                throw new DomainException("message.grant.grantType.alreadyExistsSigla");
            }
        }
    }

    public static GrantType readBySigla(String sigla) {
        for (GrantType grantType : RootDomainObject.getInstance().getGrantTypes()) {
            if (grantType.getSigla().equals(sigla)) {
                return grantType;
            }
        }
        return null;
    }

    public String getState() {
        if (getValidUntil() == null || (!getValidUntil().isBefore(new LocalDate()))) {
            return State.ACTIVE_STRING;
        }
        return State.INACTIVE_STRING;
    }

    public int countGrantContractsByActiveAndDate(final Boolean activeContracts, final Date beginContractDate,
            final Date endContractDate) {

        int count = 0;
        for (final GrantContract grantContract : this.getAssociatedGrantContractsSet()) {
            if (activeContracts != null && activeContracts) {
                if (!grantContract.hasActiveRegimes() || grantContract.getEndContractMotive().length() != 0) {
                    continue;
                }
            } else if (activeContracts != null && !activeContracts) {
                if (grantContract.hasActiveRegimes() || grantContract.getEndContractMotive().length() == 0) {
                    continue;
                }
            }
            if (!grantContract.hasRegimesInPeriod(beginContractDate, endContractDate)) {
                continue;
            }
            count++;
        }
        return count;
    }

}
