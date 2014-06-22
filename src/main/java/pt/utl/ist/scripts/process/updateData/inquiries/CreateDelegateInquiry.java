package pt.utl.ist.scripts.process.updateData.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;

import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.joda.time.DateTime;

public class CreateDelegateInquiry extends CustomTask {

    @Override
    public void runTask() throws Exception {
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
        DateTime begin = new DateTime(2012, 5, 16, 0, 0, 0, 0);
        DateTime end = new DateTime(2012, 5, 20, 0, 0, 0, 0);

        DelegateInquiryTemplate newDelegateInquiryTemplate = new DelegateInquiryTemplate(begin, end);
        newDelegateInquiryTemplate.setExecutionPeriod(currentExecutionSemester);

        DelegateInquiryTemplate previousDelegateInquiryTemplate =
                DelegateInquiryTemplate.getTemplateByExecutionPeriod(currentExecutionSemester.getPreviousExecutionPeriod());
        for (InquiryBlock inquiryBlock : previousDelegateInquiryTemplate.getInquiryBlocksSet()) {
            newDelegateInquiryTemplate.addInquiryBlocks(inquiryBlock);
        }
    }
}
