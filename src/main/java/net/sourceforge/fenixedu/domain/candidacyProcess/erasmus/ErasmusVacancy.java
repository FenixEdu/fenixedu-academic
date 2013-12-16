package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import pt.ist.fenixframework.Atomic;

public class ErasmusVacancy extends ErasmusVacancy_Base {

    private ErasmusVacancy() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ErasmusVacancy(final MobilityApplicationPeriod period, Degree degree, UniversityUnit unit, Integer numberOfVacancies) {
        this();

        setCandidacyPeriod(period);
        setDegree(degree);
        setUniversityUnit(unit);

        setNumberOfVacancies(numberOfVacancies);

        check();
    }

    private void check() {
        if (getCandidacyPeriod() == null) {
            throw new DomainException("error.erasmus.vacancy.candidacy.period.must.not.be.null");
        }

        if (getDegree() == null) {
            throw new DomainException("error.erasmus.vacancy.degree.must.not.be.null");
        }

        if (getUniversityUnit() == null) {
            throw new DomainException("error.erasmus.vacancy.university.unit.must.not.be.null");
        }

        if (getNumberOfVacancies() == null) {
            throw new DomainException("error.erasmus.vacancy.number.of.vacancies.must.not.be.null");
        }
    }

    @Atomic
    public static ErasmusVacancy createVacancy(final MobilityApplicationPeriod period, Degree degree, UniversityUnit unit,
            Integer numberOfVacancies) {
        return new ErasmusVacancy(period, degree, unit, numberOfVacancies);
    }

    public List<MobilityIndividualApplicationProcess> getStudentApplicationProcesses() {
        List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();

        for (MobilityStudentData data : getCandidacies()) {
            processList.add(data.getMobilityIndividualApplication().getCandidacyProcess());
        }

        return processList;
    }

    public boolean isVacancyAssociatedToAnyCandidacy() {
        return hasAnyCandidacies();
    }

    public void delete() {
        if (isVacancyAssociatedToAnyCandidacy()) {
            throw new DomainException("error.erasmus.vacancy.is.associated.to.candidacies");
        }

        setUniversityUnit(null);
        setDegree(null);
        setCandidacyPeriod(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData> getCandidacies() {
        return getCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacies() {
        return !getCandidaciesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumberOfVacancies() {
        return getNumberOfVacancies() != null;
    }

    @Deprecated
    public boolean hasUniversityUnit() {
        return getUniversityUnit() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasCandidacyPeriod() {
        return getCandidacyPeriod() != null;
    }

}
