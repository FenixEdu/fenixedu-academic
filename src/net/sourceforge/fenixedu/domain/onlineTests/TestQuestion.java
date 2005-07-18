/*
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class TestQuestion extends TestQuestion_Base {

    private CorrectionFormula formula;

    public CorrectionFormula getCorrectionFormula() {
        return formula;
    }

    public void setCorrectionFormula(CorrectionFormula formula) {
        this.formula = formula;
    }

    public void delete() {
        removeQuestion();
        removeTest();
        deleteDomainObject();
    }

}
