/*
 * Created on 15/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftPercentage;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author jpvl
 */
public class ReadTeacherShiftPercentageTest extends TestCaseServices {

    /**
     * @param testName
     */
    public ReadTeacherShiftPercentageTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadTeacherExecutionCourseShiftsPercentage";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/testDataSetForTeacherCredits.xml";
    }

    public void testSucessfullExecution() {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Execution Course
            InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(new Integer(1));

            //Teacher
            ITeacher teacher;

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, new Integer(1));

            InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            Object[] args = { infoTeacher, infoExecutionCourse };

            List infoShiftPercentageList = (List) ServiceManagerServiceFactory.executeService(
                    authorizedUserView(), getNameOfServiceToBeTested(), args);

            assertNotNull("is null!", infoShiftPercentageList);

            assertEquals("size not 10!", 10, infoShiftPercentageList.size());

            Iterator iterator = infoShiftPercentageList.iterator();
            while (iterator.hasNext()) {
                InfoShiftPercentage infoShiftPercentage = (InfoShiftPercentage) iterator.next();
                int internalCode = infoShiftPercentage.getShift().getIdInternal().intValue();
                switch (internalCode) {
                case 1:
                    assertEquals("shift 1 - 1", new Double(0), infoShiftPercentage
                            .getAvailablePercentage());
                    assertEquals("shift 1 - 2", 2, infoShiftPercentage.getInfoShiftProfessorshipList()
                            .size());
                    break;
                case 2:
                    assertEquals("shift 2 - 1", new Double(75), infoShiftPercentage
                            .getAvailablePercentage());
                    assertEquals("shift 2 - 2", 1, infoShiftPercentage.getInfoShiftProfessorshipList()
                            .size());
                    break;
                default:
                    assertEquals("shift " + internalCode + " - 1", new Double(100), infoShiftPercentage
                            .getAvailablePercentage());
                    assertEquals("shift " + internalCode + " - 2", 0, infoShiftPercentage
                            .getInfoShiftProfessorshipList().size());
                    break;
                }
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            fail("Reading database!");
        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Executing  Service!");
        }
    }

    public IUserView authorizedUserView() {
        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TEACHER);

        Collection roles = new ArrayList();
        roles.add(infoRole);

        UserView userView = new UserView("user", roles);

        return userView;
    }
}