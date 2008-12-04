package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

abstract public class ConclusionProcess extends ConclusionProcess_Base {

    private SortedSet<ConclusionProcessVersion> getSortedVersions(final Comparator<ConclusionProcessVersion> comparator) {
	final SortedSet<ConclusionProcessVersion> sorted = new TreeSet<ConclusionProcessVersion>(comparator);
	sorted.addAll(getVersionsSet());
	return sorted;
    }

    private ConclusionProcessVersion getFirstVersion() {
	return getSortedVersions(ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME_AND_ID).first();
    }

    public ConclusionProcessVersion getLastVersion() {
	return getSortedVersions(ConclusionProcessVersion.COMPARATOR_BY_CREATION_DATE_TIME_AND_ID).last();
    }

    public DateTime getCreationDateTime() {
	return getFirstVersion().getCreationDateTime();
    }

    public DateTime getLastModificationDateTime() {
	return getLastVersion().getCreationDateTime();
    }

    public Person getResponsible() {
	return getFirstVersion().getResponsible();
    }

    public Person getLastResponsible() {
	return getLastVersion().getResponsible();
    }

    public Integer getFinalAverage() {
	return getLastVersion().getFinalAverage();
    }

    public BigDecimal getAverage() {
	return getLastVersion().getAverage();
    }

    public LocalDate getConclusionDate() {
	return getLastVersion().getConclusionDate();
    }

    @Deprecated
    public YearMonthDay getConclusionYearMonthDay() {
	return new YearMonthDay(getConclusionDate());
    }

    public String getNotes() {
	return getLastVersion().getNotes();
    }

    public ExecutionYear getIngressionYear() {
	return getLastVersion().getIngressionYear();
    }

    public BigDecimal getCredits() {
	return getLastVersion().getCredits();
    }

    abstract public void update(final Person responsible, final Integer finalAverage, final LocalDate conclusionDate,
	    final String notes);

    final public void addVersions(final RegistrationConclusionBean bean) {
	super.addVersions(new ConclusionProcessVersion(bean));
	addSpecificVersionInfo();
	super.setConclusionYear(getLastVersion().getConclusionYear());
    }

    abstract protected void addSpecificVersionInfo();

    public boolean isCycleConclusionProcess() {
	return false;
    }

    public boolean isRegistrationConclusionProcess() {
	return false;
    }

    @Override
    final public void addVersions(final ConclusionProcessVersion versions) {
	throw new DomainException("error.ConclusionProcess.must.use.addVersions.with.bean");
    }

    @Override
    public void removeVersions(ConclusionProcessVersion versions) {
	throw new DomainException("error.ConclusionProcess.method.not.allowed");
    }

    abstract public Registration getRegistration();

    public Degree getDegree() {
	return getRegistration().getDegree();
    }

    public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

}
