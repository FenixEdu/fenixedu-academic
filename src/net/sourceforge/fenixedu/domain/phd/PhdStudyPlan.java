package net.sourceforge.fenixedu.domain.phd;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class PhdStudyPlan extends PhdStudyPlan_Base {

    static {
	PhdStudyPlanPhdIndividualProgramProcess.addListener(new RelationAdapter<PhdStudyPlan, PhdIndividualProgramProcess>() {
	    @Override
	    public void beforeAdd(PhdStudyPlan studyPlan, PhdIndividualProgramProcess process) {

		if (studyPlan != null && process != null) {
		    if (process.hasStudyPlan()) {
			throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.process.already.has.study.play");
		    }
		}

	    }
	});
    }

    protected PhdStudyPlan() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
	if (AccessControl.getPerson() != null) {
	    setCreatedBy(AccessControl.getPerson().getUsername());
	}
    }

    public PhdStudyPlan(PhdIndividualProgramProcess process, Degree degree) {
	this();

	init(process, degree);
    }

    private void init(PhdIndividualProgramProcess process, Degree degree) {
	check(process, "error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.process.cannot.be.null");
	check(degree, "error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.degree.cannot.be.null");

	if (!degree.isEmpty() && !degree.isDEA()) {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.degree.must.be.of.type.DEA");
	}

	super.setProcess(process);
	super.setDegree(degree);
    }

    @Override
    public void setProcess(PhdIndividualProgramProcess process) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.enclosing_type.cannot.modify.process");
    }

    @Override
    public void addEntries(PhdStudyPlanEntry entry) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.cannot.add.entry");
    }

    @Override
    public List<PhdStudyPlanEntry> getEntries() {
	return Collections.unmodifiableList(super.getEntries());
    }

    @Override
    public Set<PhdStudyPlanEntry> getEntriesSet() {
	return Collections.unmodifiableSet(super.getEntriesSet());
    }

    @Override
    public Iterator<PhdStudyPlanEntry> getEntriesIterator() {
	return getEntries().iterator();
    }

    @Override
    public void removeEntries(PhdStudyPlanEntry entry) {
	throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.cannot.remove.entry");
    }

    public Set<PhdStudyPlanEntry> getNormalEntries() {
	final Set<PhdStudyPlanEntry> result = new HashSet<PhdStudyPlanEntry>();

	for (final PhdStudyPlanEntry entry : getEntries()) {
	    if (entry.isNormal()) {
		result.add(entry);
	    }
	}

	return result;
    }

    public Set<PhdStudyPlanEntry> getPropaedeuticEntries() {
	final Set<PhdStudyPlanEntry> result = new HashSet<PhdStudyPlanEntry>();

	for (final PhdStudyPlanEntry entry : getEntries()) {
	    if (entry.isPropaedeutic()) {
		result.add(entry);
	    }
	}

	return result;
    }

    public boolean hasSimilarEntry(PhdStudyPlanEntry entry) {
	for (final PhdStudyPlanEntry each : getEntries()) {
	    if (each.isSimilar(entry)) {
		return true;
	    }
	}

	return false;
    }

    public void createEntries(PhdStudyPlanEntryBean bean) {
	if (bean.getInternalEntry().booleanValue()) {
	    for (final CompetenceCourse each : bean.getCompetenceCourses()) {
		new InternalPhdStudyPlanEntry(bean.getEntryType(), bean.getStudyPlan(), each);
	    }
	} else {
	    new ExternalPhdStudyPlanEntry(bean.getEntryType(), bean.getStudyPlan(), bean.getCourseName());
	}

    }

    public void delete() {

	for (; hasAnyEntries(); getEntries().get(0).delete())
	    ;

	super.setProcess(null);
	super.setDegree(null);
	super.setRootDomainObject(null);

	super.deleteDomainObject();

    }

}
