package ServidorApresentacao.Action.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteMarks;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class WriteMarksAction extends DispatchAction
{
    List invalidRecords = null;
    public ActionForward loadFile(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception, NotExecuteException
    {

        HttpSession session = request.getSession(false);
        ActionErrors actionErrors = new ActionErrors();

        String lineReader = null;

        Integer objectCode = getObjectCode(request);
        Integer evaluationCode = getEvaluationCode(request);

        //	Read uploaded file
        DynaActionForm marksForm = (DynaActionForm)form;

        FormFile formFile = (FormFile)marksForm.get("theFile");
        if (!(formFile.getContentType().equals("text/plain")))
        {
            prepareInputForward(request, session, objectCode, evaluationCode);
            actionErrors.add("FileNotExist", new ActionError("error.ficheiro.impossivelLer"));

            saveErrors(request, actionErrors);
            return mapping.findForward("loadMarks");
        }

        InputStreamReader input = new InputStreamReader(formFile.getInputStream());
        BufferedReader reader = new BufferedReader(input);

        //parsing uploaded file
        int n = 0;
        try
        {
            lineReader = reader.readLine();
        } catch (IOException e)
        {
            throw new NotExecuteException("error.ficheiro.impossivelLer");
        }
        do
        {

            if ((lineReader != null) && (lineReader.length() != 0))
            {
                n++;
            }
            try
            {
                lineReader = reader.readLine();
            } catch (IOException e)
            {
                throw new NotExecuteException("error.ficheiro.impossivelLer");
            }
        } while ((lineReader != null));
        if (n == 0)
        {
            prepareInputForward(request, session, objectCode, evaluationCode);
            actionErrors.add("BadFormatFile", new ActionError("error.file.badFormat"));
            saveErrors(request, actionErrors);
            return mapping.findForward("loadMarks");

        }
        reader.close();
        input = new InputStreamReader(formFile.getInputStream());
        reader = new BufferedReader(input);

        String[] studentsNumbers = new String[n];
        String[] marks = new String[n];

        int j = 0;
        try
        {
            lineReader = reader.readLine();
        } catch (IOException e)
        {
            throw new NotExecuteException("error.ficheiro.impossivelLer");
        }
        StringTokenizer stringTokenizer = null;
        do
        {

            /* leitura do ficheiro de notas linha a linha */
            if ((lineReader != null) && (lineReader.length() != 0))
            {
                stringTokenizer = new StringTokenizer(lineReader);
                try
                {
                    studentsNumbers[j] = stringTokenizer.nextToken().trim();
                }
				catch (NumberFormatException e2)
				{
					prepareInputForward(request, session, objectCode, evaluationCode);
					actionErrors.add("BadFormatFile", new ActionError("error.file.badFormat"));

					saveErrors(request, actionErrors);
					return mapping.findForward("loadMarks");

				}
            
                try
                {
                    marks[j] = stringTokenizer.nextToken().trim();

                } catch (NoSuchElementException e1)
                {
                    prepareInputForward(request, session, objectCode, evaluationCode);
                    actionErrors.add("BadFormatFile", new ActionError("error.file.badFormat"));

                    saveErrors(request, actionErrors);
                    return mapping.findForward("loadMarks");

                }
                
                j++;
            }
            try
            {
                lineReader = reader.readLine();
            } catch (IOException e)
            {
                throw new NotExecuteException("error.ficheiro.impossivelLer");
            }

        } while ((lineReader != null));
        reader.close();

        IUserView userView = (IUserView)session.getAttribute(SessionConstants.U_VIEW);

        List marksList = new ArrayList();
        invalidRecords = new ArrayList();
        for (int i = 0; i < marks.length; i++)
        {

            InfoMark infoMark = new InfoMark();
            infoMark = getMarkStudent(studentsNumbers, marks, i, userView);

            marksList.add(infoMark);
        }

        Object[] args = { objectCode, evaluationCode, marksList };
        TeacherAdministrationSiteView siteView = null;

        try
        {
            siteView =
                (TeacherAdministrationSiteView)ServiceManagerServiceFactory.executeService(userView, "InsertEvaluationMarks", args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("evaluationCode", evaluationCode);

        // check for errors in service
        InfoSiteMarks infoSiteMarks = (InfoSiteMarks)siteView.getComponent();
        if (infoSiteMarks.getStudentsListErrors() != null
            && infoSiteMarks.getStudentsListErrors().size() > 0)
        {
            ListIterator iterator = infoSiteMarks.getStudentsListErrors().listIterator();
            int i = 0;
            while (iterator.hasNext())
            {

                InfoMark infoMark = (InfoMark)iterator.next();
                if (infoMark != null)
                {
                    actionErrors.add(
                        "studentNonExistence",
                        new ActionError(
                            "errors.student.nonExisting",
                            String.valueOf(
                                (infoMark.getInfoFrequenta().getAluno().getNumber()).intValue())));
                } else
                {

                    actionErrors.add(
                        "studentInvalid",
                        new ActionError("errors.registo.invalid", invalidRecords.get(i)));
                    i++;
                }
            }
            saveErrors(request, actionErrors);

        }
        if (infoSiteMarks.getMarksListErrors() != null && infoSiteMarks.getMarksListErrors().size() > 0)
        {
            ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
            while (iterator.hasNext())
            {
                InfoMark infoMark = (InfoMark)iterator.next();
                actionErrors.add(
                    "invalidMark",
                    new ActionError(
                        "errors.invalidMark",
                        infoMark.getMark(),
                        String.valueOf(
                            (infoMark.getInfoFrequenta().getAluno().getNumber()).intValue())));

            }
            saveErrors(request, actionErrors);

        }
        if (infoSiteMarks.getMarksListErrors() != null || infoSiteMarks.getStudentsListErrors() != null)
        {
            return mapping.getInputForward();
        }
        return mapping.findForward("success");
    }

    private void prepareInputForward(
        HttpServletRequest request,
        HttpSession session,
        Integer objectCode,
        Integer evaluationCode)
        throws FenixActionException
    {
        UserView userView = (UserView)session.getAttribute(SessionConstants.U_VIEW);
        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args =
            { objectCode, commonComponent, new InfoEvaluation(), null, evaluationCode, null };

        try
        {
            TeacherAdministrationSiteView siteView =
                (TeacherAdministrationSiteView)ServiceUtils.executeService(
                    userView,
                    "TeacherAdministrationSiteComponentService",
                    args);

            request.setAttribute("siteView", siteView);
            request.setAttribute(
                "objectCode",
                ((InfoSiteCommon)siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        request.setAttribute("evaluationCode", evaluationCode);
    }

    public ActionForward writeMarks(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);
        ActionErrors actionErrors = new ActionErrors();

        List marksList = new ArrayList();

        Integer sizeList = getSizeList(request);

        //transform form into list with student's number and students's mark
        for (int i = 0; i < sizeList.intValue(); i++)
        {
            InfoMark infoMark = new InfoMark();
            infoMark = getMark(request, i);
            marksList.add(infoMark);
        }

        Integer objectCode = getObjectCode(request);
        Integer evaluationCode = getEvaluationCode(request);

        IUserView userView = (IUserView)session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { objectCode, evaluationCode, marksList };
        TeacherAdministrationSiteView siteView = null;

        try
        {
            siteView =
                (TeacherAdministrationSiteView)ServiceManagerServiceFactory.executeService(userView, "InsertEvaluationMarks", args);
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("evaluationCode", evaluationCode);

        InfoSiteMarks infoSiteMarks = (InfoSiteMarks)siteView.getComponent();
        //Check if ocurr errors in service
        if ((infoSiteMarks.getMarksListErrors() != null
            && infoSiteMarks.getMarksListErrors().size() > 0)
            || (infoSiteMarks.getStudentsListErrors() != null
                && infoSiteMarks.getStudentsListErrors().size() > 0))
        {

            if (infoSiteMarks.getMarksListErrors() != null
                && infoSiteMarks.getMarksListErrors().size() > 0)
            {
                //list with errors Invalid Marks
                ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
                while (iterator.hasNext())
                {
                    InfoMark infoMark = (InfoMark)iterator.next();

                    actionErrors.add(
                        "invalidMark",
                        new ActionError(
                            "errors.invalidMark",
                            infoMark.getMark(),
                            String.valueOf(
                                (infoMark.getInfoFrequenta().getAluno().getNumber()).intValue())));
                }
            }

            if (infoSiteMarks.getStudentsListErrors() != null
                && infoSiteMarks.getStudentsListErrors().size() > 0)
            {
                //list with errors Student Existence
                ListIterator iterator = infoSiteMarks.getMarksListErrors().listIterator();
                while (iterator.hasNext())
                {
                    InfoMark infoMark = (InfoMark)iterator.next();

                    actionErrors.add(
                        "studentExistence",
                        new ActionError(
                            "errors.student.nonExisting",
                            String.valueOf(
                                (infoMark.getInfoFrequenta().getAluno().getNumber()).intValue())));
                }
            }
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        return mapping.findForward("viewMarksOptions");
    }

    private InfoMark getMark(HttpServletRequest request, int index)
    {
        String mark = request.getParameter("markElem[" + index + "].mark");
        Integer studentCode =
            Integer.valueOf(request.getParameter("markElem[" + index + "].studentCode"));
		Integer studentNumber =
					Integer.valueOf(request.getParameter("markElem[" + index + "].studentNumber"));

        if (studentCode != null)
        {
            InfoStudent infoStudent = new InfoStudent();
            infoStudent.setIdInternal(studentCode);
            infoStudent.setNumber(studentNumber);

            InfoFrequenta infoFrequenta = new InfoFrequenta();
            infoFrequenta.setAluno(infoStudent);

            InfoMark infoMark = new InfoMark();
            infoMark.setInfoFrequenta(infoFrequenta);

            infoMark.setMark(mark);

            return infoMark;
        }
        return null;
    }

    private Integer getSizeList(HttpServletRequest request)
    {
        Integer objectCode = null;
        String objectCodeString = (String)request.getAttribute("sizeList");
        if (objectCodeString == null)
        {
            objectCodeString = request.getParameter("sizeList");
        }
        if (objectCodeString != null)
        {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

    private Integer getEvaluationCode(HttpServletRequest request)
    {
        String evaluationCodeString = (String)request.getAttribute("evaluationCode");
        if (evaluationCodeString == null)
        {
            evaluationCodeString = request.getParameter("evaluationCode");
        }
        Integer evaluationCode = new Integer(evaluationCodeString);
        return evaluationCode;
    }

    private Integer getObjectCode(HttpServletRequest request)
    {
        Integer objectCode = null;
        String objectCodeString = (String)request.getAttribute("objectCode");
        if (objectCodeString == null)
        {
            objectCodeString = request.getParameter("objectCode");
        }
        if (objectCodeString != null)
        {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

    private InfoMark getMarkStudent(String[] student, String[] mark, int index, IUserView userView)
        throws FenixActionException
    {
        String markString = mark[index];
        Integer studentInt = null;

        InfoStudent infoStudent = new InfoStudent();
        InfoFrequenta infoFrequenta = new InfoFrequenta();
        try
        {
            studentInt = new Integer(student[index]);
        } catch (NumberFormatException e)
        {
            invalidRecords.add(student[index]);
        }
        if (markString != null && studentInt != null)
        {
            //infoMark with only student code and mark
            infoStudent.setNumber(studentInt);
            infoFrequenta.setAluno(infoStudent);

            InfoMark infoMark = new InfoMark();
            infoMark.setInfoFrequenta(infoFrequenta);

            infoMark.setMark(markString);

            return infoMark;
        }
        return null;
    }
}