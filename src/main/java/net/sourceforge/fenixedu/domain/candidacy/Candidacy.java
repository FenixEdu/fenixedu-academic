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
package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public abstract class Candidacy extends Candidacy_Base {

    protected Candidacy() {
        super();
        setNumber(createCandidacyNumber());
        setRootDomainObject(Bennu.getInstance());
        setStartDate(new YearMonthDay());
    }

    public Candidacy(CandidacySituation candidacySituation) {
        this();
        if (candidacySituation == null) {
            throw new DomainException("candidacy situation cannot be null");
        }
        this.addCandidacySituations(candidacySituation);
    }

    public final Integer createCandidacyNumber() {
        if (Bennu.getInstance().getCandidaciesSet().size() == 0) {
            return Integer.valueOf(1);
        }
        Candidacy candidacy = Collections.max(Bennu.getInstance().getCandidaciesSet(), new BeanComparator("number"));
        return candidacy.getNumber() + 1;
    }

    public CandidacySituation getActiveCandidacySituation() {
        return hasAnyCandidacySituations() ? Collections.max(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR) : null;
    }

    public CandidacySituationType getActiveCandidacySituationType() {
        final CandidacySituation situation = getActiveCandidacySituation();
        return (situation != null) ? situation.getCandidacySituationType() : null;
    }

    private CandidacySituation getFirstCandidacySituation() {
        return Collections.min(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR);
    }

    // static methods

    public static Candidacy readByCandidacyNumber(Integer candidacyNumber) {
        for (Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy.getNumber().equals(candidacyNumber)) {
                return candidacy;
            }
        }
        return null;
    }

    public static Set<Candidacy> readCandidaciesBetween(final Integer from, final Integer to) {
        final Set<Candidacy> result = new HashSet<Candidacy>();
        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy.getNumber() >= from && candidacy.getNumber() <= to) {
                result.add(candidacy);
            }
        }
        return result;
    }

    public static Set<Candidacy> readDegreeCandidaciesBetween(final Integer from, final Integer to) {
        final Set<Candidacy> result = new HashSet<Candidacy>();
        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy instanceof DegreeCandidacy) {
                if (candidacy.getNumber() >= from && candidacy.getNumber() <= to) {
                    result.add(candidacy);
                }
            }
        }
        return result;
    }

    public static Set<Candidacy> readDFACandidaciesBetween(final Integer from, final Integer to) {
        final Set<Candidacy> result = new HashSet<Candidacy>();
        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy instanceof DFACandidacy) {
                if (candidacy.getNumber() >= from && candidacy.getNumber() <= to) {
                    result.add(candidacy);
                }
            }
        }
        return result;
    }

    abstract public String getDescription();

    abstract protected Set<Operation> getOperations(CandidacySituation candidacySituation);

    abstract protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person);

    abstract public boolean isConcluded();

    @Override
    public YearMonthDay getStartDate() {
        return super.getStartDate() != null ? super.getStartDate() : getFirstCandidacySituation().getSituationDate()
                .toYearMonthDay();
    }

    public void delete() {

        setPerson(null);

        for (; !getCandidacySituationsSet().isEmpty(); getCandidacySituationsSet().iterator().next().delete()) {
            ;
        }

        setRootDomainObject(null);
        deleteDomainObject();
    }

    public abstract Map<String, Set<String>> getStateMapping();

    public abstract String getDefaultState();

    public CandidacySituation nextState(String nextState) {
        CandidacySituationType situationType = CandidacySituationType.valueOf(nextState);

        if (!situationType.equals(this.getActiveCandidacySituation().getCandidacySituationType())
                && getValidNextStates().contains(nextState)) {

            switch (situationType) {
            case CANCELLED:
                return new CancelledCandidacySituation(this);
            case PRE_CANDIDACY:
                return new PreCandidacySituation(this);
            case STAND_BY:
                return new StandByCandidacySituation(this);
            case STAND_BY_CONFIRMED_DATA:
                return new StandByConfirmedDataCandidacySituation(this);
            case STAND_BY_FILLED_DATA:
                return new StandByFilledDataCandidacySituation(this);
            case REGISTERED:
                return new RegisteredCandidacySituation(this);
            case ADMITTED:
                return new AdmittedCandidacySituation(this);
            case NOT_ADMITTED:
                return new NotAdmittedCandidacySituation(this);
            case SUBSTITUTE:
                return new SubstituteCandidacySituation(this);
            default:
                break;
            }
        }
        return null;
    }

    public IState nextState() {
        return nextState(getDefaultState());
    }

    public void checkConditionsToForward() {
        checkConditionsToForward(getDefaultState());
    }

    public void checkConditionsToForward(String nextState) {

        switch (this.getActiveCandidacySituation().getCandidacySituationType()) {

        case CANCELLED:
            throw new DomainException("error.impossible.to.forward.from.cancelled");
        case ADMITTED:
            break;
        case STAND_BY:
            if (isCancelling(nextState)) {
                break;
            }

            if (!checkIfDataIsFilled()) {
                throw new DomainException("error.mandatory.data.not.filled.yet");
            }
            if (this instanceof DFACandidacy) {
                if (!checkIfPrecedenceDataIsFilled()) {
                    throw new DomainException("error.mandatory.data.not.filled.yet");
                }
            }
            break;
        default:
            break;
        }
    }

    private boolean isCancelling(final String nextState) {
        return CandidacySituationType.valueOf(nextState) == CandidacySituationType.CANCELLED;
    }

    public Set<String> getValidNextStates() {
        return getStateMapping().get(getActiveCandidacySituation().getCandidacySituationType().toString());
    }

    private boolean checkIfPrecedenceDataIsFilled() {
        if (!(this instanceof DFACandidacy)) {
            return false;
        }
        PrecedentDegreeInformation precedentDegreeInformation = ((DFACandidacy) this).getPrecedentDegreeInformation();
        return (precedentDegreeInformation.getConclusionGrade() != null && precedentDegreeInformation.getConclusionYear() != null
                && precedentDegreeInformation.getCountry() != null && precedentDegreeInformation.getDegreeDesignation() != null && precedentDegreeInformation
                    .getInstitution() != null);
    }

    protected boolean checkIfDataIsFilled() {
        Person person = getPerson();
        return (person.getGender() != null && person.getEmissionDateOfDocumentIdYearMonthDay() != null
                && person.getEmissionLocationOfDocumentId() != null && person.getExpirationDateOfDocumentIdYearMonthDay() != null
                && person.getSocialSecurityNumber() != null && person.getProfession() != null
                && person.getMaritalStatus() != null && person.getDateOfBirthYearMonthDay() != null
                && person.getCountry() != null && person.getParishOfBirth() != null
                && person.getDistrictSubdivisionOfBirth() != null && person.getDistrictOfBirth() != null
                && person.getCountryOfBirth() != null && person.getNameOfFather() != null && person.getNameOfMother() != null
                && person.hasDefaultPhysicalAddress() && person.getInstitutionalOrDefaultEmailAddressValue() != null);
    }

    public boolean isActive() {
        final CandidacySituationType situationType = getActiveCandidacySituationType();
        return situationType != null && situationType.isActive();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument> getCandidacyDocuments() {
        return getCandidacyDocumentsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyDocuments() {
        return !getCandidacyDocumentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.CandidacySituation> getCandidacySituations() {
        return getCandidacySituationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacySituations() {
        return !getCandidacySituationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStartDate() {
        return getStartDate() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
