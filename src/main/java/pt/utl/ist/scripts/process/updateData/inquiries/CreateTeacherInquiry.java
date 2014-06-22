package pt.utl.ist.scripts.process.updateData.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.DateTime;

public class CreateTeacherInquiry extends CustomTask {

    @Override
    public void runTask() throws Exception {
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
        DateTime begin = new DateTime(2012, 5, 16, 0, 0, 0, 0);
        DateTime end = new DateTime(2012, 5, 20, 0, 0, 0, 0);

        TeacherInquiryTemplate newTeacherInquiryTemplate = new TeacherInquiryTemplate(begin, end);
        newTeacherInquiryTemplate.setExecutionPeriod(currentExecutionSemester);

        TeacherInquiryTemplate previousTeacherInquiryTemplate =
                TeacherInquiryTemplate.getTemplateByExecutionPeriod(currentExecutionSemester.getPreviousExecutionPeriod());
        for (InquiryBlock inquiryBlock : previousTeacherInquiryTemplate.getInquiryBlocksSet()) {
            newTeacherInquiryTemplate.addInquiryBlocks(inquiryBlock);
        }
    }
}
