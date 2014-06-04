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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class PhdStudyPlan extends PhdStudyPlan_Base {

    static {
        getRelationPhdStudyPlanPhdIndividualProgramProcess().addListener(
                new RelationAdapter<PhdStudyPlan, PhdIndividualProgramProcess>() {
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
        setRootDomainObject(Bennu.getInstance());
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

        String[] args = {};
        if (process == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.process.cannot.be.null", args);
        }
        super.setProcess(process);

        init(degree, exempted);
    }

    private void init(Degree degree, boolean exempted) {
        super.setExempted(exempted);

        if (!exempted) {
            String[] args = {};
            if (degree == null) {
                throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.PhdStudyPlan.degree.cannot.be.null", args);
            }

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
    public Set<PhdStudyPlanEntry> getEntriesSet() {
        return Collections.unmodifiableSet(super.getEntriesSet());
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

        for (; hasAnyEntries(); getEntries().iterator().next().delete()) {
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
            return BundleUtil.getString(Bundle.PHD, "label.PhdStudyPlan.description.exempted");
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntry> getEntries() {
        return getEntriesSet();
    }

    @Deprecated
    public boolean hasAnyEntries() {
        return !getEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasCreatedBy() {
        return getCreatedBy() != null;
    }

    @Deprecated
    public boolean hasExempted() {
        return getExempted() != null;
    }

}
