/*
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

/**
 * @author lmac1
 */
public class EditExecutionDegreeTest extends TestCaseManagerInsertAndEditServices {

    public EditExecutionDegreeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditExecutionDegree";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(new Integer(3));

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(6));

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        //infoExecutionDegree.setInfoCoordinator(infoTeacher);
        infoExecutionDegree.setTemporaryExamMap(new Boolean(true));
        infoExecutionDegree.setIdInternal(new Integer(12));

        Object[] args = { infoExecutionDegree };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(new Integer(1));

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(6));

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
        //infoExecutionDegree.setInfoCoordinator(infoTeacher);
        infoExecutionDegree.setTemporaryExamMap(new Boolean(true));
        infoExecutionDegree.setIdInternal(new Integer(14));

        Object[] args = { infoExecutionDegree };
        return args;
    }
}