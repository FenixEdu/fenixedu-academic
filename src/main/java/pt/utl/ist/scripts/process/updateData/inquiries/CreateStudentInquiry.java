package pt.utl.ist.scripts.process.updateData.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.StudentTeacherInquiryTemplate;

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.DateTime;

public class CreateStudentInquiry extends CustomTask {

    @Override
    public void runTask() throws Exception {
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
        DateTime begin = new DateTime(2012, 5, 16, 0, 0, 0, 0);
        DateTime end = new DateTime(2012, 5, 20, 0, 0, 0, 0);

        // Curricular inquiry
        CurricularCourseInquiryTemplate newCourseInquiryTemplate = new CurricularCourseInquiryTemplate(begin, end);
        newCourseInquiryTemplate.setExecutionPeriod(currentExecutionSemester);

        CurricularCourseInquiryTemplate previousCourseInquiryTemplate =
                CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(currentExecutionSemester
                        .getPreviousExecutionPeriod());
        for (InquiryBlock inquiryBlock : previousCourseInquiryTemplate.getInquiryBlocksSet()) {
            newCourseInquiryTemplate.addInquiryBlocks(inquiryBlock);
        }

        // Teachers inquiry      
        StudentTeacherInquiryTemplate newStudentTeacherInquiryTemplate = new StudentTeacherInquiryTemplate(begin, end);
        newStudentTeacherInquiryTemplate.setExecutionPeriod(currentExecutionSemester);

        StudentTeacherInquiryTemplate previousStudentTeacherInquiryTemplate =
                StudentTeacherInquiryTemplate.getTemplateByExecutionPeriod(currentExecutionSemester.getPreviousExecutionPeriod());
        for (InquiryBlock inquiryBlock : previousStudentTeacherInquiryTemplate.getInquiryBlocksSet()) {
            newStudentTeacherInquiryTemplate.addInquiryBlocks(inquiryBlock);
        }
    }
}
