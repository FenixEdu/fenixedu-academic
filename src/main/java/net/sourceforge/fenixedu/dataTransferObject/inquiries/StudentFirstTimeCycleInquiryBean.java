package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryStudentCycleAnswer;
import net.sourceforge.fenixedu.domain.inquiries.MandatoryCondition;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionCondition;
import net.sourceforge.fenixedu.domain.inquiries.StudentCycleInquiryTemplate;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class StudentFirstTimeCycleInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<InquiryBlockDTO> studentInquiryBlocks;
    private Registration registration;
    private PhdIndividualProgramProcess phdProcess;
    private StudentCandidacy candidacy;
    private StudentCycleInquiryTemplate studentInquiryTemplate;

    public StudentFirstTimeCycleInquiryBean(StudentCycleInquiryTemplate studentInquiryTemplate, Registration registration) {
        initStudentInquiry(studentInquiryTemplate, registration, null);
        setGroupsVisibility();
    }

    public StudentFirstTimeCycleInquiryBean(StudentCycleInquiryTemplate studentInquiryTemplate,
            PhdIndividualProgramProcess phdProcess) {
        initStudentInquiry(studentInquiryTemplate, null, phdProcess);
        setGroupsVisibility();
    }

    private void initStudentInquiry(StudentCycleInquiryTemplate studentInquiryTemplate, Registration registration,
            PhdIndividualProgramProcess phdProcess) {
        setRegistration(registration);
        setPhdProcess(phdProcess);
        setStudentInquiryTemplate(studentInquiryTemplate);
        setStudentInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
        for (InquiryBlock inquiryBlock : studentInquiryTemplate.getInquiryBlocks()) {
            getStudentInquiryBlocks().add(new InquiryBlockDTO(inquiryBlock));
        }

    }

    public void setGroupsVisibility() {
        for (InquiryBlockDTO inquiryBlockDTO : getStudentInquiryBlocks()) {
            Set<InquiryGroupQuestionBean> groups = inquiryBlockDTO.getInquiryGroups();
            for (InquiryGroupQuestionBean group : groups) {
                setGroupVisibility(getStudentInquiryBlocks(), group);
            }
        }
    }

    private void setGroupVisibility(Set<InquiryBlockDTO> inquiryBlocks, InquiryGroupQuestionBean groupQuestionBean) {
        for (QuestionCondition questionCondition : groupQuestionBean.getInquiryGroupQuestion().getQuestionConditions()) {
            if (questionCondition instanceof MandatoryCondition) {
                MandatoryCondition condition = (MandatoryCondition) questionCondition;
                InquiryQuestionDTO inquiryDependentQuestionBean =
                        getInquiryQuestionBean(condition.getInquiryDependentQuestion(), inquiryBlocks);
                boolean isMandatory =
                        inquiryDependentQuestionBean.getFinalValue() == null ? false : condition.getConditionValuesAsList()
                                .contains(inquiryDependentQuestionBean.getFinalValue());
                if (isMandatory) {
                    groupQuestionBean.setVisible(true);
                } else {
                    groupQuestionBean.setVisible(false);
                    for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                        questionDTO.setResponseValue(null);
                    }
                }
            }
        }
    }

    private InquiryQuestionDTO getInquiryQuestionBean(InquiryQuestion inquiryQuestion, Set<InquiryBlockDTO> inquiryBlocks) {
        for (InquiryBlockDTO blockDTO : inquiryBlocks) {
            for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO inquiryQuestionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (inquiryQuestionDTO.getInquiryQuestion() == inquiryQuestion) {
                        return inquiryQuestionDTO;
                    }
                }
            }
        }
        return null;
    }

    public String validateInquiry() {
        String validationResult = null;
        for (InquiryBlockDTO inquiryBlockDTO : getStudentInquiryBlocks()) {
            validationResult = inquiryBlockDTO.validate(getStudentInquiryBlocks());
            if (!Boolean.valueOf(validationResult)) {
                return validationResult;
            }
        }
        return Boolean.toString(true);
    }

    @Atomic
    public void saveAnswers() {
        InquiryStudentCycleAnswer inquiryStudentCycleAnswer = null;
        for (InquiryBlockDTO blockDTO : getStudentInquiryBlocks()) {
            for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
                for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
                    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
                        if (inquiryStudentCycleAnswer == null) {
                            inquiryStudentCycleAnswer = createInquiryStudentCycleAnswer();
                            inquiryStudentCycleAnswer.setResponseDateTime(new DateTime());
                        }
                        new QuestionAnswer(inquiryStudentCycleAnswer, questionDTO.getInquiryQuestion(),
                                questionDTO.getFinalValue());
                    }
                }
            }
        }
    }

    private InquiryStudentCycleAnswer createInquiryStudentCycleAnswer() {
        if (getRegistration() != null) {
            return new InquiryStudentCycleAnswer(getRegistration());
        } else {
            return new InquiryStudentCycleAnswer(getPhdProcess());
        }
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public void setStudentInquiryBlocks(Set<InquiryBlockDTO> studentInquiryBlocks) {
        this.studentInquiryBlocks = studentInquiryBlocks;
    }

    public Set<InquiryBlockDTO> getStudentInquiryBlocks() {
        return studentInquiryBlocks;
    }

    public void setCandidacy(StudentCandidacy candidacy) {
        this.candidacy = candidacy;
    }

    public StudentCandidacy getCandidacy() {
        return candidacy;
    }

    public PhdIndividualProgramProcess getPhdProcess() {
        return phdProcess;
    }

    public void setPhdProcess(PhdIndividualProgramProcess phdProcess) {
        this.phdProcess = phdProcess;
    }

    public void setStudentInquiryTemplate(StudentCycleInquiryTemplate studentInquiryTemplate) {
        this.studentInquiryTemplate = studentInquiryTemplate;
    }

    public StudentCycleInquiryTemplate getStudentInquiryTemplate() {
        return studentInquiryTemplate;
    }

    public boolean hasInquiryStudentCycleAnswer() {
        if (getRegistration() != null && getRegistration().hasInquiryStudentCycleAnswer()) {
            return true;
        }
        if (getPhdProcess() != null && getPhdProcess().hasInquiryStudentCycleAnswer()) {
            return true;
        }
        return false;
    }
}
