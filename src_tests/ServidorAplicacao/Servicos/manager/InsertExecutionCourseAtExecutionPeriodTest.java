/*
 * Created on 27/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;

/**
 * @author lmac1
 */
public class InsertExecutionCourseAtExecutionPeriodTest extends TestCaseManagerInsertAndEditServices {

    public InsertExecutionCourseAtExecutionPeriodTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertExecutionCourseAtExecutionPeriod";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer(1));

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
        infoExecutionCourse.setNome("DisciplinaExecucaoSucesso");
        infoExecutionCourse.setSigla("DES");

        Object[] args = { infoExecutionCourse };
        return args;
    }

    //	insert execution course with code already existing in DB
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer(1));

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
        infoExecutionCourse.setNome("DisciplinaExecucaoSucesso");
        infoExecutionCourse.setSigla("TFCI");

        Object[] args = { infoExecutionCourse };
        return args;
    }

}

