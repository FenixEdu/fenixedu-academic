/*
 * Created on 16/Set/2003, 15:30:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.teacher;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoAttendWithEnrollment;
import DataBeans.InfoAttendsSummary;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoGroupProjectStudents;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoShift;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteStudents;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 16/Set/2003, 15:30:39
 *  
 */
public class DownloadStudentList extends FenixAction {
    static final String COLUMNS_HEADERS = "Nº\tNúmero total de Inscrições\tCurso\tNome\tE-mail";

    static final String RESUME_COLUMNS_HEADERS = "Número total de inscrições\tNúmero de Alunos";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = null;
        Integer shiftID = null;
        try {
            shiftID = new Integer(request.getParameter("shiftCode"));
        } catch (NumberFormatException ex) {
            //ok, we don't want to view a shift's student list
        }
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        objectCode = new Integer(objectCodeString);
        Integer scopeCode = null;
        String scopeCodeString = request.getParameter("scopeCode");
        if (scopeCodeString == null) {
            scopeCodeString = (String) request.getAttribute("scopeCode");
        }
        if (scopeCodeString != null) {
            scopeCode = new Integer(scopeCodeString);
        }
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, scopeCode };
        Object argsProjects[] = { objectCode };
        Object argsInfos[] = { objectCode };
        TeacherAdministrationSiteView siteView = null;
        InfoSiteProjects projects = null;
        List infosGroups = null;
        List attendacies = null;
        List shiftStudents = null;
        InfoAttendsSummary infoAttendsSummary = null;

        try {
            siteView = (TeacherAdministrationSiteView) ServiceManagerServiceFactory.executeService(
                    userView, "ReadStudentsByCurricularCourse", args);

            projects = (InfoSiteProjects) ServiceManagerServiceFactory.executeService(userView,
                    "teacher.ReadExecutionCourseProjects", argsProjects);

            infosGroups = (List) ServiceManagerServiceFactory.executeService(userView,
                    "teacher.GetProjectsGroupsByExecutionCourseID", argsInfos);
            InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
            Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));
            if (shiftID != null) {
                //the objectCode is needed by the filter...doing this is awfull
                // !!!
                //please read
                // http://www.dcc.unicamp.br/~oliva/fun/prog/resign-patterns
                Object[] argsReadShiftStudents = { objectCode, shiftID };
                shiftStudents = (List) ServiceManagerServiceFactory.executeService(userView,
                        "teacher.ReadStudentsByShiftID", argsReadShiftStudents);
                infoSiteStudents.setStudents(shiftStudents);
            }
            Object[] argsAttendacies = { objectCode, infoSiteStudents.getStudents() };
            infoAttendsSummary = ((InfoAttendsSummary) ServiceManagerServiceFactory.executeService(
                    userView, "teacher.GetAttendaciesByStudentList", argsAttendacies));
            attendacies = infoAttendsSummary.getAttends();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        String result = new String(COLUMNS_HEADERS);
        if (projects != null && projects.getInfoGroupPropertiesList() != null) {
            for (Iterator iter = projects.getInfoGroupPropertiesList().iterator(); iter.hasNext();) {
                InfoGroupProperties infoGroupProperties = (InfoGroupProperties) iter.next();
                result += "\t" + "\"" + "Grupo do Projecto " + infoGroupProperties.getName() + "\"";
            }
        }

        InfoExecutionCourse infoExecutionCourse = ((InfoSiteCommon) siteView.getCommonComponent())
                .getExecutionCourse();
        if (infoExecutionCourse.getTheoreticalHours().intValue() > 0) {
            result += "\tTurno Teórico";
        }
        if (infoExecutionCourse.getPraticalHours().intValue() > 0) {
            result += "\tTurno Prático";
        }
        if (infoExecutionCourse.getTheoPratHours().intValue() > 0) {
            result += "\tTurno Teórico-Prático";
        }
        if (infoExecutionCourse.getLabHours().intValue() > 0) {
            result += "\tTurno Laboratorial";
        }
        result += "\n";

        for (Iterator attendaciesIterator = attendacies.iterator(); attendaciesIterator.hasNext();) {
            InfoAttendWithEnrollment infoFrequenta = (InfoAttendWithEnrollment) attendaciesIterator
                    .next();
            result += infoFrequenta.getAluno().getNumber();
            result += "\t";

            result += infoFrequenta.getEnrollments();
            //			if (infoFrequenta.getInfoEnrolment() == null)
            //				result += "Não";
            //			else
            //				result += "Sim";
            result += "\t";
            if (infoFrequenta.getInfoEnrolment() == null)
                result += "N/A";
            else
                result += "\""
                        + infoFrequenta.getInfoEnrolment().getInfoStudentCurricularPlan()
                                .getInfoDegreeCurricularPlan().getName() + "\"";
            result += "\t" + "\"" + infoFrequenta.getAluno().getInfoPerson().getNome() + "\"";
            result += "\t" + "\"" + infoFrequenta.getAluno().getInfoPerson().getEmail() + "\"";
            if (projects != null && projects.getInfoGroupPropertiesList() != null) {
                for (Iterator projectsIterator = projects.getInfoGroupPropertiesList().iterator(); projectsIterator
                        .hasNext();) {
                    InfoGroupProperties infoGroupProperties = (InfoGroupProperties) projectsIterator
                            .next();
                    int projectIdInternal = infoGroupProperties.getIdInternal().intValue();
                    int groupNumber = -1;
                    //                    int shiftCode = -1;
                    //                    int executionCourseCode = -1;
                    //                    int groupPropertiesCode = -1;
                    //                    int studentGroupCode = -1;
                    Integer studentNumber = infoFrequenta.getAluno().getNumber();
                    for (Iterator groupsIterator = infosGroups.iterator(); groupsIterator.hasNext();) {
                        InfoGroupProjectStudents groupInfo = (InfoGroupProjectStudents) groupsIterator
                                .next();
                        if (projectIdInternal == groupInfo.getStudentGroup().getInfoAttendsSet().getInfoGroupProperties()
                                .getIdInternal().intValue()
                                && groupInfo.isStudentMemberOfThisGroup(studentNumber)) {
                            groupNumber = groupInfo.getStudentGroup().getGroupNumber().intValue();
                            //                            studentGroupCode =
                            // groupInfo.getStudentGroup().getIdInternal().intValue();
                            //                            shiftCode =
                            //                                groupInfo.getStudentGroup().getInfoShift().getIdInternal().intValue();
                            //                            executionCourseCode =
                            //                                groupInfo
                            //                                    .getStudentGroup()
                            //                                    .getInfoGroupProperties()
                            //                                    .getInfoExecutionCourse()
                            //                                    .getIdInternal()
                            //                                    .intValue();
                            break;
                        }
                    }
                    if (groupNumber != -1)
                        result += "\t" + groupNumber;
                    else
                        result += "\t" + "N/A";
                }
            }
            //TODO: add shifts names by type
            if (infoExecutionCourse.getTheoreticalHours().intValue() > 0) {
                InfoShift infoShift = (InfoShift) infoFrequenta.getInfoShifts().get("T");
                if (infoShift != null) {
                    result += "\t" + infoShift.getNome();
                } else {
                    result += "\t" + "N/A";
                }

            }
            if (infoExecutionCourse.getPraticalHours().intValue() > 0) {
                InfoShift infoShift = (InfoShift) infoFrequenta.getInfoShifts().get("P");
                if (infoShift != null) {
                    result += "\t" + infoShift.getNome();
                } else {
                    result += "\t" + "N/A";
                }
            }
            if (infoExecutionCourse.getTheoPratHours().intValue() > 0) {
                InfoShift infoShift = (InfoShift) infoFrequenta.getInfoShifts().get("TP");
                if (infoShift != null) {
                    result += "\t" + infoShift.getNome();
                } else {
                    result += "\t" + "N/A";
                }
            }
            if (infoExecutionCourse.getLabHours().intValue() > 0) {
                InfoShift infoShift = (InfoShift) infoFrequenta.getInfoShifts().get("L");
                if (infoShift != null) {
                    result += "\t" + infoShift.getNome();
                } else {
                    result += "\t" + "N/A";
                }
            }
            result += "\n";
        }
        Iterator numberOfEnrollmentsIt = infoAttendsSummary.getNumberOfEnrollments().iterator();
        if (infoAttendsSummary.getNumberOfEnrollments() != null
                && infoAttendsSummary.getNumberOfEnrollments().size() != 0) {
            result += "\n\n" + RESUME_COLUMNS_HEADERS + "\n";
            while (numberOfEnrollmentsIt.hasNext()) {
                Integer enrollmentNumber = (Integer) numberOfEnrollmentsIt.next();
                result += enrollmentNumber;
                result += "\t" + infoAttendsSummary.getEnrollmentDistribution().get(enrollmentNumber);
                result += "\n";
            }
        }

        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("plain/text");
            StringBuffer fileName= new StringBuffer();
            Calendar now = new GregorianCalendar();
            fileName.append("listaDeAlunos_");
            fileName.append(infoExecutionCourse.getSigla()).append("_").append(now.get(Calendar.DAY_OF_MONTH));
            fileName.append("-").append(now.get(Calendar.MONTH)).append("-").append(now.get(Calendar.YEAR));
            fileName.append(".csv");
            response.setHeader("Content-disposition", "attachment; filename="+fileName);
            writer.print(result);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e1) {
            throw new FenixActionException();
        }
        return null;
    }
}