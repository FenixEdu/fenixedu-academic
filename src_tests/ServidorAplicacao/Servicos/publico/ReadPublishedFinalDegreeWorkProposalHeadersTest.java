/*
 * Created on 2004/04/09
 *
 */
package ServidorAplicacao.Servicos.publico;

import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Luis Cruz
 *  
 */
public class ReadPublishedFinalDegreeWorkProposalHeadersTest extends ServiceTestCase {

    public ReadPublishedFinalDegreeWorkProposalHeadersTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadPublishedFinalDegreeWorkProposalHeaders";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/finalDegreeWork/testDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets_templates/servicos/finalDegreeWork/testDataSet.xml";
    }

    public void testSuccessfullCreateExamNew() {
    }

}