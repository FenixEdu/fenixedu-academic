/*
 * Created on 27/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

/**
 * @author lmac1
 */
public class InsertProfessorShipTest extends TestCaseManagerInsertAndEditServices {

    public InsertProfessorShipTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertProfessorShip";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer(8));

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(new Integer(24));

        InfoProfessorship infoProfessorShip = new InfoProfessorship();
        infoProfessorShip.setInfoExecutionCourse(infoExecutionCourse);
        infoProfessorShip.setInfoTeacher(infoTeacher);

        Object[] args = { infoProfessorShip, Boolean.FALSE };
        return args;
    }

    //	try to inssert a teacher that doesn't exist in DB
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer(25));

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(new Integer(24));

        InfoProfessorship infoProfessorShip = new InfoProfessorship();
        infoProfessorShip.setInfoExecutionCourse(infoExecutionCourse);
        infoProfessorShip.setInfoTeacher(infoTeacher);

        Object[] args = { infoProfessorShip };
        return args;
    }

}

