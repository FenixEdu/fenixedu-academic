package pt.utl.ist.scripts.process.updateData.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.RegentInquiryTemplate;

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.DateTime;

public class CreateResponsibleInquiry extends CustomTask {

    @Override
    public void runTask() throws Exception {
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
        DateTime begin = new DateTime(2012, 5, 16, 0, 0, 0, 0);
        DateTime end = new DateTime(2012, 5, 20, 0, 0, 0, 0);

        RegentInquiryTemplate newRegentInquiryTemplate = new RegentInquiryTemplate(begin, end);
        newRegentInquiryTemplate.setExecutionPeriod(currentExecutionSemester);

        RegentInquiryTemplate previousRegentInquiryTemplate =
                RegentInquiryTemplate.getTemplateByExecutionPeriod(currentExecutionSemester.getPreviousExecutionPeriod());
        for (InquiryBlock inquiryBlock : previousRegentInquiryTemplate.getInquiryBlocksSet()) {
            newRegentInquiryTemplate.addInquiryBlocks(inquiryBlock);
        }
    }
}
