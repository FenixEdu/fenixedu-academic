/*
 * Created on 10/Set/2003, 18:36:21
 * changed on 4/Jan/2004, 19:45:11 (generalize for any execution course)
 * changed on 13/Out/2004 20:12:33 (changed servlet output, giving more information)
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 18:36:21
 *  
 */

public class ShowStudentGroupInfo extends Action {
    public String buildInfo(Integer curricularCourseID, String username, String password) {
        String result = new String();
        if (username == null)
            return new String();

        try {
            Object argsAutenticacao[] = { username, password, "" };
            IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", argsAutenticacao);

            InfoSiteProjects projects = null;
            try {
                projects = (InfoSiteProjects) ServiceUtils.executeService(userView,
                        "teacher.ReadExecutionCourseProjects", new Integer[] { curricularCourseID });

            } catch (FenixServiceException e) {
                e.printStackTrace();
                throw new FenixActionException(e);
            }

            if (projects == null)
                return new String("-1");

            for (Iterator iterProjects = projects.getInfoGroupPropertiesList().iterator(); iterProjects
                    .hasNext();) {
                InfoGroupProperties project = (InfoGroupProperties) iterProjects.next();

                if (project != null) {

                    InfoSiteShiftsAndGroups shiftsAndGroups = null;
                    try {
                        shiftsAndGroups = (InfoSiteShiftsAndGroups) ServiceUtils.executeService(
                                userView, "ReadShiftsAndGroups",
                                new Object[] { project.getIdInternal(),username});

                    } catch (NotAuthorizedException e) {
                        shiftsAndGroups = new InfoSiteShiftsAndGroups();
                        shiftsAndGroups.setInfoSiteGroupsByShiftList(new ArrayList());
                    }catch (FenixServiceException e) {
                        e.printStackTrace();
                        throw new FenixActionException(e);
                    }

                    List shiftsAndGroupsList = shiftsAndGroups.getInfoSiteGroupsByShiftList();

                    for (Iterator iter = shiftsAndGroupsList.iterator(); iter.hasNext();) {
                        InfoSiteGroupsByShift infoSiteShiftAndGroups = (InfoSiteGroupsByShift) iter
                                .next();
                        for (Iterator iterator2 = infoSiteShiftAndGroups.getInfoSiteStudentGroupsList()
                                .iterator(); iterator2.hasNext();) {
                            InfoSiteStudentGroup infoSiteStudentGroup = (InfoSiteStudentGroup) iterator2
                                    .next();
                            for (Iterator iterator3 = infoSiteStudentGroup
                                    .getInfoSiteStudentInformationList().iterator(); iterator3.hasNext();) {
                                InfoSiteStudentInformation infoSiteStudentInformation = (InfoSiteStudentInformation) iterator3
                                        .next();
                                if (username.equalsIgnoreCase(infoSiteStudentInformation.getUsername())) {
                                    result = projects.getInfoExecutionCourse().getNome() + " ( ";
                                    int counter = 0;
                                    for (Iterator iteratorCurricularCourses = projects.getInfoExecutionCourse()
                                            .getAssociatedInfoCurricularCourses().iterator(); iteratorCurricularCourses
                                            .hasNext();) {
                                        InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iteratorCurricularCourses
                                                .next();
                                        if (counter > 0)
                                            result += ",";
                                        result += infoCurricularCourse.getInfoDegreeCurricularPlan()
                                                .getName();
                                        counter++;
                                    }
                                    result += " )\n";
                                    result += project.getName() + "\n";
                                    int remainingStudents = project.getMaximumCapacity().intValue();
                                    for (Iterator iteratorWritingResult = infoSiteStudentGroup
                                            .getInfoSiteStudentInformationList().iterator(); iteratorWritingResult
                                            .hasNext();) {
                                        InfoSiteStudentInformation studentInfo = (InfoSiteStudentInformation) iteratorWritingResult
                                                .next();
                                        result += studentInfo.getNumber();
                                        Object[] args = {
                                                new MockUserView(studentInfo.getUsername(), new ArrayList()),
                                                TipoCurso.LICENCIATURA_OBJ };
                                        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
                                        try {
                                            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceUtils
                                                    .executeService(
                                                            userView,
                                                            "ReadActiveStudentCurricularPlanByDegreeType",
                                                            args);
                                        } catch (FenixServiceException e) {
                                            e.printStackTrace();
                                            throw new FenixActionException(e);
                                        }
                                        result += "-"
                                                + infoStudentCurricularPlan
                                                        .getInfoDegreeCurricularPlan().getName() + "\n";
                                        remainingStudents--;
                                    }
                                    while (remainingStudents > 0) {
                                        result += "N/A\n";
                                        remainingStudents--;
                                    }
                                    result += infoSiteStudentGroup.getInfoStudentGroup()
                                            .getGroupNumber()
                                            + "\n"
                                            + infoSiteStudentGroup.getInfoStudentGroup().getInfoShift()
                                                    .getNome() + "\n";
                                    for (Iterator iteratorLessons = infoSiteStudentGroup
                                            .getInfoStudentGroup().getInfoShift().getInfoLessons()
                                            .iterator(); iteratorLessons.hasNext();) {
                                        InfoLesson infoLesson = (InfoLesson) iteratorLessons.next();
                                        result += infoLesson.getDiaSemana().toString() + " "
                                                + infoLesson.getInicio().get(Calendar.HOUR_OF_DAY);
                                        int minute = infoLesson.getInicio().get(Calendar.MINUTE);
                                        result += ":";
                                        if (minute < 10)
                                            result += "0";
                                        result += minute + "\n\n";
                                    }
                                }

                            }

                        }
                    }
                } else
                    return new String();

            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "-1";
        }

        return result;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String studentUsername = request.getParameter("username");
        String studentPassword = request.getParameter("password");
        String idsString = request.getParameter("ids");

        Integer[] coursesIds = { new Integer(34811), new Integer(34661), new Integer(34950) };
        if (idsString != null) {
            String[] ids = idsString.split(",");
            coursesIds = new Integer[ids.length];
            for (int i = 0; i < ids.length; i++)
                coursesIds[i] = new Integer(ids[i]);

        }

        String result = new String();

        for (int i = 0; i < coursesIds.length; i++) {

            result += this.buildInfo(coursesIds[i], studentUsername, studentPassword);
        }
        if (result.equals(""))
            result = "-1";
        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("text/plain");
            writer.print(result);
            writer.flush();
            response.flushBuffer();
        } catch (IOException ex) {
            throw new FenixActionException();
        }
        return null;
    }
}