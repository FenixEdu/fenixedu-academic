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
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.NationalIdCardAvoidanceQuestion;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributesList;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class MobilityIndividualApplicationProcessBean extends IndividualCandidacyProcessBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Set<CurricularCourse> selectedCurricularCourses;

    private Degree degree;

    ExecutionYear executionYear;

    private MobilityStudentDataBean mobilityStudentDataBean;

    private boolean toAccessFenix;

    private Boolean validatedByGri;

    private Boolean validatedByErasmusCoordinator;

    private String alertSubject;

    private String alertBody;

    private Boolean createAlert;

    private Boolean sendEmail;

    private StorkAttributesList personalFieldsFromStork;

    private NationalIdCardAvoidanceQuestion nationalIdCardAvoidanceQuestion;

    private String idCardAvoidanceOtherReason;

    public MobilityIndividualApplicationProcessBean() {
        setCandidacyDate(new LocalDate());
        initializeDocumentUploadBeans();
        setSelectedCurricularCourses(new HashSet<CurricularCourse>());
        setSendEmail(false);

        this.toAccessFenix = false;
    }

    public MobilityIndividualApplicationProcessBean(CandidacyProcess candidacyProcess) {
        this();
        final MobilityApplicationProcess map = (MobilityApplicationProcess) candidacyProcess;
        setCandidacyProcess(map);
        setMobilityStudentDataBean(new MobilityStudentDataBean(getCandidacyProcess(), map.getForSemester()));
    }

    public MobilityIndividualApplicationProcessBean(final MobilityIndividualApplicationProcess process) {
        setIndividualCandidacyProcess(process);
        setCandidacyProcess(process.getCandidacyProcess());
        setSelectedCurricularCourses(new HashSet<CurricularCourse>(process.getCandidacy().getCurricularCoursesSet()));
        setMobilityStudentDataBean(new MobilityStudentDataBean(process.getCandidacy().getMobilityStudentData()));
        setCandidacyDate(process.getCandidacyDate());
        setObservations(process.getCandidacy().getObservations());
        setDegree(process.getCandidacySelectedDegree());
        setValidatedByErasmusCoordinator(process.getValidatedByMobilityCoordinator());
        setValidatedByGri(process.getValidatedByGri());

        setNationalIdCardAvoidanceQuestion(process.getCandidacy().getNationalIdCardAvoidanceQuestion());
        setIdCardAvoidanceOtherReason(process.getCandidacy().getIdCardAvoidanceOtherReason());
    }

    @Override
    protected void initializeDocumentUploadBeans() {
        setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

    public Set<CurricularCourse> getSelectedCurricularCourses() {
        return selectedCurricularCourses;
    }

    public void setSelectedCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
        this.selectedCurricularCourses = selectedCurricularCourses;
    }

    public MobilityStudentDataBean getMobilityStudentDataBean() {
        return mobilityStudentDataBean;
    }

    public void setMobilityStudentDataBean(MobilityStudentDataBean mobilityStudentDataBean) {
        this.mobilityStudentDataBean = mobilityStudentDataBean;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public void addCurricularCourse(final CurricularCourse curricularCourse) {
        for (CurricularCourse course : this.getSelectedCurricularCourses()) {
            if (curricularCourse.isEquivalent(course)) {
                return;
            }
        }

        this.getSelectedCurricularCourses().add(curricularCourse);
    }

    public void removeCurricularCourse(final CurricularCourse curricularCourse) {
        this.getSelectedCurricularCourses().remove(curricularCourse);
    }

    public List<CurricularCourse> getSortedSelectedCurricularCourses() {
        List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>(this.getSelectedCurricularCourses());
        Collections.sort(curricularCourses, CurricularCourse.COMPARATOR_BY_NAME);
        return curricularCourses;
    }

    public boolean isToAccessFenix() {
        return this.toAccessFenix;
    }

    public void willAccessFenix() {
        this.toAccessFenix = true;
    }

    public Boolean getValidatedByGri() {
        return validatedByGri;
    }

    public void setValidatedByGri(Boolean validatedByGri) {
        this.validatedByGri = validatedByGri;
    }

    public Boolean getValidatedByErasmusCoordinator() {
        return validatedByErasmusCoordinator;
    }

    public void setValidatedByErasmusCoordinator(Boolean validatedByErasmusCoordinator) {
        this.validatedByErasmusCoordinator = validatedByErasmusCoordinator;
    }

    public String getAlertSubject() {
        return alertSubject;
    }

    public void setAlertSubject(String alertSubject) {
        this.alertSubject = alertSubject;
    }

    public String getAlertBody() {
        return alertBody;
    }

    public void setAlertBody(String alertBody) {
        this.alertBody = alertBody;
    }

    public Boolean getCreateAlert() {
        return createAlert;
    }

    public void setCreateAlert(Boolean createAlert) {
        this.createAlert = createAlert;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public StorkAttributesList getPersonalFieldsFromStork() {
        return this.personalFieldsFromStork;
    }

    public void setPersonalFieldsFromStork(final StorkAttributesList value) {
        this.personalFieldsFromStork = value;
    }

    public NationalIdCardAvoidanceQuestion getNationalIdCardAvoidanceQuestion() {
        return nationalIdCardAvoidanceQuestion;
    }

    public void setNationalIdCardAvoidanceQuestion(NationalIdCardAvoidanceQuestion nationalIdCardAvoidanceQuestion) {
        this.nationalIdCardAvoidanceQuestion = nationalIdCardAvoidanceQuestion;
    }

    public String getIdCardAvoidanceOtherReason() {
        return idCardAvoidanceOtherReason;
    }

    public void setIdCardAvoidanceOtherReason(String idCardAvoidanceOtherReason) {
        this.idCardAvoidanceOtherReason = idCardAvoidanceOtherReason;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public MobilityQuota determineMobilityQuota() {
        MobilityApplicationPeriod period = (MobilityApplicationPeriod) getCandidacyProcess().getCandidacyPeriod();
        MobilityAgreement agreement = getMobilityStudentDataBean().getMobilityAgreement();

        Degree selectedDegree = null;

        if (getDegree() == null) {
            if (getSelectedCurricularCourses() != null && !getSelectedCurricularCourses().isEmpty()) {
                selectedDegree = getMostDominantDegreeFromCourses();
            } else {
                selectedDegree = null;
            }
        } else {
            if ((getSelectedCurricularCourses() == null || getSelectedCurricularCourses().isEmpty())
                    || (!getSelectedCurricularCourses().isEmpty() && getDegree() == getMostDominantDegreeFromCourses())) {
                selectedDegree = getDegree();
            } else {
                throw new DomainException("error.mobility.application.process.courses.and.degree.selection.dont.match");
            }
        }

        MobilityQuota quota = period.getAssociatedOpening(selectedDegree, agreement);

        if (quota == null) {
            throw new DomainException("error.mobility.application.process.no.courses.from.one.degree.selected");
        }

        return quota;
    }

    private Degree getMostDominantDegreeFromCourses() {
        Map<Degree, List<CurricularCourse>> coursesMappedByDegree = new HashMap<Degree, List<CurricularCourse>>();

        for (CurricularCourse curricularCourse : getSelectedCurricularCourses()) {
            if (!coursesMappedByDegree.containsKey(curricularCourse.getDegree())) {
                coursesMappedByDegree.put(curricularCourse.getDegree(), new ArrayList<CurricularCourse>());
            }

            coursesMappedByDegree.get(curricularCourse.getDegree()).add(curricularCourse);
        }

        List<Degree> candidateDegrees = new ArrayList<Degree>();
        int max = 0;

        for (Degree degree : coursesMappedByDegree.keySet()) {
            if (coursesMappedByDegree.get(degree).size() > max) {
                candidateDegrees = new ArrayList<Degree>();
                candidateDegrees.add(degree);
                max = coursesMappedByDegree.get(degree).size();
            } else if (coursesMappedByDegree.get(degree).size() == max) {
                candidateDegrees.add(degree);
            }
        }

        if (candidateDegrees.size() == 0) {
            return null;
        }

        if (candidateDegrees.size() > 1) {
            throw new DomainException("error.mobility.application.process.invalid.dominant.degree");
        }

        return candidateDegrees.iterator().next();
    }

    public List<Degree> getPossibleDegreesFromSelectedUniversity() {
        if (this.getMobilityStudentDataBean().getSelectedUniversity() == null) {
            return new ArrayList<Degree>();
        }

        MobilityApplicationPeriod period = (MobilityApplicationPeriod) this.getCandidacyProcess().getCandidacyPeriod();

        return period.getPossibleDegreesAssociatedToAgreement(this.getMobilityStudentDataBean().getMobilityAgreement());
    }

    public String getSelectedCourseNameForView() {
        try {
            MobilityQuota quota = determineMobilityQuota();
            return quota.getDegree().getNameI18N().getContent();
        } catch (DomainException e) {
            return BundleUtil.getString(Bundle.CANDIDATE, e.getMessage());
        }
    }

    @Override
    public boolean isErasmus() {
        return true;
    }
}
