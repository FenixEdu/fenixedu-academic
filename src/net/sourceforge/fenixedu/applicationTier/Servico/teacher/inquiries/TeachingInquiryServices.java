package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.TeachingInquiryDTO;
import net.sourceforge.fenixedu.domain.oldInquiries.teacher.TeachingInquiry;
import pt.ist.fenixWebFramework.services.Service;

public class TeachingInquiryServices {

    @Service
    static public TeachingInquiry saveAnswers(final TeachingInquiryDTO inquiryDTO) {

        TeachingInquiry teachingInquiry =
                inquiryDTO.getProfessorship().hasTeachingInquiry() ? inquiryDTO.getProfessorship().getTeachingInquiry() : new TeachingInquiry();
        teachingInquiry.setProfessorship(inquiryDTO.getProfessorship());
        teachingInquiry.setAnswerDuration(inquiryDTO.getAnswerDuration());

        teachingInquiry.setAnswers(inquiryDTO);

        return teachingInquiry;
    }

}
