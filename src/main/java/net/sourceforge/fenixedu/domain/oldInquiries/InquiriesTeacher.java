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
/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.AffiliatedTeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.NonAffiliatedTeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesTeacher extends InquiriesTeacher_Base {

    public InquiriesTeacher() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected InquiriesTeacher(InquiriesCourse inquiriesCourse, Professorship professorship, ShiftType shiftType,
            InfoInquiriesTeacher infoInquiriesTeacher) {
        this();
        if ((inquiriesCourse == null) || (professorship == null) || (shiftType == null)) {
            throw new DomainException("The inquiriesCourse, teacher and shiftType should not be null!");
        }

        this.setInquiriesCourse(inquiriesCourse);
        this.setProfessorship(professorship);
        this.setBasicProperties(shiftType, infoInquiriesTeacher);
    }

    protected InquiriesTeacher(InquiriesCourse inquiriesCourse, NonAffiliatedTeacher nonAffiliatedTeacher, ShiftType shiftType,
            InfoInquiriesTeacher infoInquiriesTeacher) {
        this();
        if ((inquiriesCourse == null) || (nonAffiliatedTeacher == null) || (shiftType == null)) {
            throw new DomainException("The inquiriesCourse, nonAffiliatedTeacher and shiftType should not be null!");
        }

        this.setInquiriesCourse(inquiriesCourse);
        this.setNonAffiliatedTeacher(nonAffiliatedTeacher);
        this.setBasicProperties(shiftType, infoInquiriesTeacher);
    }

    private void setBasicProperties(ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
        this.setShiftType(shiftType);
        this.setStudentAssiduity(infoInquiriesTeacher.getStudentAssiduity());
        this.setTeacherAssiduity(infoInquiriesTeacher.getTeacherAssiduity());
        this.setTeacherPunctuality(infoInquiriesTeacher.getTeacherPunctuality());
        this.setTeacherClarity(infoInquiriesTeacher.getTeacherClarity());
        this.setTeacherAssurance(infoInquiriesTeacher.getTeacherAssurance());
        this.setTeacherInterestStimulation(infoInquiriesTeacher.getTeacherInterestStimulation());
        this.setTeacherAvailability(infoInquiriesTeacher.getTeacherAvailability());
        this.setTeacherReasoningStimulation(infoInquiriesTeacher.getTeacherReasoningStimulation());
        this.setGlobalAppreciation(infoInquiriesTeacher.getGlobalAppreciation());

    }

    public static InquiriesTeacher makeNew(TeacherInquiryDTO inquiryDTO) {
        InquiriesTeacher inquiriesTeacher = new InquiriesTeacher();
        inquiriesTeacher.setShiftType(inquiryDTO.getShiftType());

        setAnswers(inquiryDTO, inquiriesTeacher);

        if (inquiryDTO.getTeacherDTO() instanceof AffiliatedTeacherDTO) {
            final Professorship professorship =
                    inquiryDTO.getExecutionCourse().getProfessorship((Person) inquiryDTO.getTeacherDTO().getTeacher());
            if (professorship == null) {
                throw new Error("This should never be possible.");
            }
            inquiriesTeacher.setProfessorship(professorship);
            // inquiriesTeacher.setTeacher((Teacher)
            // inquiryDTO.getTeacherDTO().getTeacher());
        } else if (inquiryDTO.getTeacherDTO() instanceof NonAffiliatedTeacherDTO) {
            inquiriesTeacher.setNonAffiliatedTeacher((NonAffiliatedTeacher) inquiryDTO.getTeacherDTO().getTeacher());
        }

        return inquiriesTeacher;
    }

    private static void setAnswers(TeacherInquiryDTO inquiryDTO, InquiriesTeacher inquiriesTeacher) {
        Map<String, InquiriesQuestion> answersMap = inquiryDTO.buildAnswersMap(false);

        inquiriesTeacher.setClassesFrequency(answersMap.get("classesFrequency").getValueAsInteger());
        inquiriesTeacher.setLowClassesFrequencyReasonSchedule(answersMap.get("lowClassesFrequencyReasonSchedule")
                .getValueAsBoolean());
        inquiriesTeacher.setLowClassesFrequencyReasonTeacher(answersMap.get("lowClassesFrequencyReasonTeacher")
                .getValueAsBoolean());
        inquiriesTeacher.setLowClassesFrequencyReasonContents(answersMap.get("lowClassesFrequencyReasonContents")
                .getValueAsBoolean());
        inquiriesTeacher.setLowClassesFrequencyReasonFlunkeeStudent(answersMap.get("lowClassesFrequencyReasonFlunkeeStudent")
                .getValueAsBoolean());
        inquiriesTeacher.setLowClassesFrequencyReasonOther(answersMap.get("lowClassesFrequencyReasonOther").getValueAsBoolean());
        inquiriesTeacher.setTeacherAcomplishedScheduleAndActivities(answersMap.get("teacherAcomplishedScheduleAndActivities")
                .getValueAsInteger());
        inquiriesTeacher.setSuitedClassesRythm(answersMap.get("suitedClassesRythm").getValueAsInteger());
        inquiriesTeacher.setTeacherCommited(answersMap.get("teacherCommited").getValueAsInteger());
        inquiriesTeacher.setTeacherExposedContentsAtractively(answersMap.get("teacherExposedContentsAtractively")
                .getValueAsInteger());
        inquiriesTeacher.setTeacherShowedSecurity(answersMap.get("teacherShowedSecurity").getValueAsInteger());
        inquiriesTeacher.setTeacherExposedContentsClearly(answersMap.get("teacherExposedContentsClearly").getValueAsInteger());
        inquiriesTeacher.setTeacherStimulatedParticipation(answersMap.get("teacherStimulatedParticipation").getValueAsInteger());
        inquiriesTeacher.setTeacherOpenToClearDoubts(answersMap.get("teacherOpenToClearDoubts").getValueAsInteger());
        inquiriesTeacher.setTeacherGlobalClassification(answersMap.get("teacherGlobalClassification").getValueAsInteger());
    }

    @Deprecated
    public boolean hasTeacherInterestStimulation() {
        return getTeacherInterestStimulation() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLowClassesFrequencyReasonTeacher() {
        return getLowClassesFrequencyReasonTeacher() != null;
    }

    @Deprecated
    public boolean hasClassesFrequency() {
        return getClassesFrequency() != null;
    }

    @Deprecated
    public boolean hasTeacherOpenToClearDoubts() {
        return getTeacherOpenToClearDoubts() != null;
    }

    @Deprecated
    public boolean hasLowClassesFrequencyReasonContents() {
        return getLowClassesFrequencyReasonContents() != null;
    }

    @Deprecated
    public boolean hasTeacherPunctuality() {
        return getTeacherPunctuality() != null;
    }

    @Deprecated
    public boolean hasNonAffiliatedTeacher() {
        return getNonAffiliatedTeacher() != null;
    }

    @Deprecated
    public boolean hasTeacherClarity() {
        return getTeacherClarity() != null;
    }

    @Deprecated
    public boolean hasTeacherAssurance() {
        return getTeacherAssurance() != null;
    }

    @Deprecated
    public boolean hasGlobalAppreciation() {
        return getGlobalAppreciation() != null;
    }

    @Deprecated
    public boolean hasInquiriesCourse() {
        return getInquiriesCourse() != null;
    }

    @Deprecated
    public boolean hasLowClassesFrequencyReasonOther() {
        return getLowClassesFrequencyReasonOther() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasTeacherReasoningStimulation() {
        return getTeacherReasoningStimulation() != null;
    }

    @Deprecated
    public boolean hasShiftType() {
        return getShiftType() != null;
    }

    @Deprecated
    public boolean hasTeacherStimulatedParticipation() {
        return getTeacherStimulatedParticipation() != null;
    }

    @Deprecated
    public boolean hasTeacherAcomplishedScheduleAndActivities() {
        return getTeacherAcomplishedScheduleAndActivities() != null;
    }

    @Deprecated
    public boolean hasTeacherCommited() {
        return getTeacherCommited() != null;
    }

    @Deprecated
    public boolean hasTeacherAvailability() {
        return getTeacherAvailability() != null;
    }

    @Deprecated
    public boolean hasTeacherAssiduity() {
        return getTeacherAssiduity() != null;
    }

    @Deprecated
    public boolean hasSuitedClassesRythm() {
        return getSuitedClassesRythm() != null;
    }

    @Deprecated
    public boolean hasTeacherExposedContentsAtractively() {
        return getTeacherExposedContentsAtractively() != null;
    }

    @Deprecated
    public boolean hasTeacherExposedContentsClearly() {
        return getTeacherExposedContentsClearly() != null;
    }

    @Deprecated
    public boolean hasStudentAssiduity() {
        return getStudentAssiduity() != null;
    }

    @Deprecated
    public boolean hasTeacherGlobalClassification() {
        return getTeacherGlobalClassification() != null;
    }

    @Deprecated
    public boolean hasTeacherShowedSecurity() {
        return getTeacherShowedSecurity() != null;
    }

    @Deprecated
    public boolean hasLowClassesFrequencyReasonSchedule() {
        return getLowClassesFrequencyReasonSchedule() != null;
    }

    @Deprecated
    public boolean hasLowClassesFrequencyReasonFlunkeeStudent() {
        return getLowClassesFrequencyReasonFlunkeeStudent() != null;
    }

}
