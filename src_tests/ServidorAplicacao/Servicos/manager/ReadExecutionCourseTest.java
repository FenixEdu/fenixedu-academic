/*
 * Created on 15/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadExecutionCourseTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionCourse";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(100) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(24) };
        return args;
    }

    protected Object getObjectToCompare() {

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setYear("2002/2003");

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(new Integer(1));
        infoExecutionPeriod.setName("2º Semestre");

        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
        infoExecutionPeriod.setSemester(new Integer(1));

        infoExecutionCourse.setNome("Trabalho Final de Curso I");
        infoExecutionCourse.setTheoreticalHours(new Double(1.5));
        infoExecutionCourse.setPraticalHours(new Double(2.0));
        infoExecutionCourse.setTheoPratHours(new Double(1.5));
        infoExecutionCourse.setLabHours(new Double(2.0));
        infoExecutionCourse.setSigla("TFCI");
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        return infoExecutionCourse;
    }
}