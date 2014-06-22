package pt.utl.ist.scripts.process.updateData.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.ResultsInquiryTemplate;

import org.fenixedu.bennu.scheduler.custom.CustomTask;

public class CreateCourseResultInquiry extends CustomTask {

    @Override
    public void runTask() throws Exception {
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();

        ResultsInquiryTemplate newResultsInquiryTemplate = new ResultsInquiryTemplate();
        newResultsInquiryTemplate.setExecutionPeriod(currentExecutionSemester);

        ResultsInquiryTemplate previousResultsInquiryTemplate =
                ResultsInquiryTemplate.getTemplateByExecutionPeriod(currentExecutionSemester.getPreviousExecutionPeriod());
        for (InquiryBlock inquiryBlock : previousResultsInquiryTemplate.getInquiryBlocksSet()) {
            newResultsInquiryTemplate.addInquiryBlocks(inquiryBlock);
        }
    }
}
