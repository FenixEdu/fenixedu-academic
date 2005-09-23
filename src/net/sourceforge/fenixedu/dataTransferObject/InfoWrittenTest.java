/*
 * Created on Feb 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IWrittenTest;
import net.sourceforge.fenixedu.util.EvaluationType;

/**
 * @author Luis&Nuno
 *  
 */
public class InfoWrittenTest extends InfoWrittenEvaluation {
    
    protected String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void copyFromDomain(IWrittenTest writtenTest) {
        super.copyFromDomain(writtenTest);
        if (writtenTest != null) {
            setDescription(writtenTest.getDescription());
            setEvaluationType(EvaluationType.TEST_TYPE);
        }
    }

    public static InfoWrittenTest newInfoFromDomain(IWrittenTest writtenTest) {
        InfoWrittenTest infoWrittenTest = null;
        if (writtenTest != null) {
            infoWrittenTest = new InfoWrittenTest();
            infoWrittenTest.copyFromDomain(writtenTest);
        }
        return infoWrittenTest;
    }

}