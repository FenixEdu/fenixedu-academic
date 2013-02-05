package net.sourceforge.fenixedu.domain.phd;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;
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

    public PhdStudyPlan(final PhdStudyPlanBean bean) {
        this();

        init(bean.getProcess(), bean.getDegree(), bean.isExempted());
    }

    private void init(PhdIndividualProgramProcess process, Degree degree, boolean exempted) {

        check(process, "error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.process.cannot.be.null");
        super.setProcess(process);

        init(degree, exempted);
    }

    private void init(Degree degree, boolean exempted) {
        super.setExempted(exempted);

        if (!exempted) {
            check(degree, "error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.degree.cannot.be.null");

            if (!degree.isEmpty() && !degree.isDEA()) {
                throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.degree.must.be.of.type.DEA");
            }

            super.setDegree(degree);
        }
    }

    public void edit(final PhdStudyPlanBean bean) {
        init(bean.getDegree(), bean.isExempted());
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

    public Set<PhdStudyPlanEntry> getExtraCurricularEntries() {
        final Set<PhdStudyPlanEntry> result = new HashSet<PhdStudyPlanEntry>();

        for (final PhdStudyPlanEntry entry : getEntries()) {
            if (entry.isExtraCurricular()) {
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

        if (getExempted().booleanValue()) {
            throw new DomainException("error.PhdStudyPlanEntry.cannot.add.entries.in.exempted.plan");
        }

        if (bean.getInternalEntry().booleanValue()) {
            for (final CompetenceCourse each : bean.getCompetenceCourses()) {
                new InternalPhdStudyPlanEntry(bean.getEntryType(), bean.getStudyPlan(), each);
            }
        } else {
            new ExternalPhdStudyPlanEntry(bean.getEntryType(), bean.getStudyPlan(), bean.getCourseName());
        }

    }

    public void delete() {

        for (; hasAnyEntries(); getEntries().get(0).delete()) {
            ;
        }

        super.setProcess(null);
        super.setDegree(null);
        super.setRootDomainObject(null);

        super.deleteDomainObject();

    }

    public boolean isToEnrolInCurricularCourses() {
        return hasAnyEntries();
    }

    public String getDescription() {
        if (getExempted().booleanValue()) {
            return ResourceBundle.getBundle("resources.PhdResources", Language.getLocale()).getString(
                    "label.PhdStudyPlan.description.exempted");
        } else {
            return getDegree().getPresentationName(getProcess().getExecutionYear());
        }
    }

    public boolean isExempted() {
        return getExempted() != null && getExempted().booleanValue();
    }

    public boolean hasAnyPropaeudeuticsOrExtraEntries() {
        for (final PhdStudyPlanEntry entry : getEntries()) {
            if (entry.isPropaedeutic() || entry.isExtraCurricular()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPropaeudeuticsOrExtraEntriesApproved() {
        final StudentCurricularPlan scp = getProcess().getRegistration().getLastStudentCurricularPlan();

        for (final PhdStudyPlanEntry entry : getEntries()) {

            if ((entry.isPropaedeutic() || entry.isExtraCurricular()) && entry.isInternalEntry()) {

                if (findEnrolment(scp, (InternalPhdStudyPlanEntry) entry) == null) {
                    return false;
                }
            }
        }

        return hasAnyEntries();
    }

    private Enrolment findEnrolment(final StudentCurricularPlan scp, final InternalPhdStudyPlanEntry entry) {
        for (final Enrolment enrolment : scp.getRoot().getEnrolments()) {
            if (enrolment.isApproved() && isFor(enrolment, entry.getCompetenceCourse())) {
                return enrolment;
            }
        }

        return null;
    }

    private boolean isFor(final Enrolment enrolment, final CompetenceCourse competenceCourse) {
        return enrolment.getCurricularCourse().getCompetenceCourse().equals(competenceCourse);
    }

}
