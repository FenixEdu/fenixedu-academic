/*
 * Created on Feb 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.util.DiaSemana;
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

    public void copyFromDomain(WrittenTest writtenTest) {
	super.copyFromDomain(writtenTest);
	if (writtenTest != null) {
	    setDescription(writtenTest.getDescription());
	    setEvaluationType(EvaluationType.TEST_TYPE);
	}
    }

    public static InfoWrittenTest newInfoFromDomain(WrittenTest writtenTest) {
	InfoWrittenTest infoWrittenTest = null;
	if (writtenTest != null) {
	    infoWrittenTest = new InfoWrittenTest();
	    infoWrittenTest.copyFromDomain(writtenTest);
	}
	return infoWrittenTest;
    }

    public DiaSemana getDiaSemana() {
	Calendar day = this.getDay();
	return new DiaSemana(day.get(Calendar.DAY_OF_WEEK));
    }

}