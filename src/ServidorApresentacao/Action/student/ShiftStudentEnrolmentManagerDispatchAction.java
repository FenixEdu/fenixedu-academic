/*
 * Created on 16/Mai/2003
 *
 *
 */
package ServidorApresentacao.Action.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoShift;
import DataBeans.InfoShiftStudentEnrolment;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoStudent;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author tdi-dev (bruno)
 *
 * This class is used to group shifts by 'main types' (such as 
 * classes or courses) and then subdivide by shift types 
 */
class InfoShiftDividedList extends ArrayList
{
    public class Element
    {
        /* this class should probably extend an ArrayList, as it is just a named ArrayList */
        private String type;
        private ArrayList list;

        public Element(String type)
        {
            this.type = type;
            list = new ArrayList();
        }
        /**
         * @return corresponding type set at construction time 
         */
        public String getType()
        {
            return type;
        }

        public boolean add(Object element)
        {
            return list.add(element);
        }
        /**
         * @return the corresponding list
         */
        public ArrayList getList()
        {
            return list;
        }
        /** stub */
        public void setList(ArrayList list)
        {
        }
        /** stub */
        public void setType(String string)
        {
        }
        public int size()
        {
            return list.size();
        }
    }
    /*
    
    ((InfoShiftWithAssociatedInfoClassesAndInfoLessons)infoAvailableShiftsFiltered.get(0)).getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getInfoExecutionYear().getYear()
    
    */

    /**
     * We divide the <i>shifts</i> by main <i>type</i>, and then we need to 
     * subdivide by shift type, in order to be able to select one of which 
     * on the presentation level.
     * @param type the 'division' on which we should put the shift
     * @param shift to shift to be stored
     * @return true if the main type and shift type already existed, else false 
     */
    public boolean put(String type, InfoShiftWithAssociatedInfoClassesAndInfoLessons info)
    {
        // tdi-dev (bruno) -> honestly, this is the best way, without flags or others...  
        Element mainE, subE;
        Iterator mainIt = this.iterator();

        InfoShift shift = info.getInfoShift();

        while (mainIt.hasNext())
        {
            //search for the element that has the main type we want
            mainE = (Element) mainIt.next();
            if (mainE.getType().equals(type))
            {
                // found it, now we need to search for the shift type 'spot'
                Iterator subIt = mainE.getList().iterator();
                while (subIt.hasNext())
                {
                    subE = (Element) subIt.next();
                    if (subE.getType().equals(shift.getTipo().getFullNameTipoAula()))
                    {
                        // ok, found shift type list, let's add the shift
                        subE.add(info);
                        return true;
                    }
                }
                // ups, subtype doesn't exist . create and add
                subE = new Element(shift.getTipo().getFullNameTipoAula());
                subE.add(info);
                mainE.add(subE);
                return true;
            }
        }
        // main type doesn't exist iet...
        mainE = new Element(type);
        subE = new Element(shift.getTipo().getFullNameTipoAula());
        subE.add(info);
        mainE.add(subE);
        this.add(mainE);
        return false;
    }
    /**
     * Danger Will Robinson Danger. 
     * tdi-dev (bruno) -> not sure if this is 'correct' 
     * @see java.util.Collection#size()
     */
    public int leafSize()
    {
        Iterator it = this.iterator();
        int size = 0;
        while (it.hasNext())
        {
            size += ((Element) it.next()).size();
        }
        return size;
    }

}

public class ShiftStudentEnrolmentManagerDispatchAction extends TransactionalDispatchAction
{
    private final String TRANSACTION_ERROR_MESSAGE_KEY = "error.transaction.enrolment";

    public ActionForward start(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        IUserView userView = SessionUtils.getUserView(request);
        //TODO:FIXME:THIS IS JUST A TEMPORARY BYPASS TO PREVENT 1ST YEAR STUDENTS FROM ENROLLING IN SHIFTS
        if ((new Integer(userView.getUtilizador().substring(1))).intValue() > 53227)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "notAuthorizedShiftEnrollment",
                new ActionError("error.notAuthorized.ShiftEnrollment"));
            saveErrors(request, actionErrors);
            return mapping.findForward("studentFirstPage");
        }

        boolean firstTime = request.getParameter("firstTime") != null;
        if (firstTime)
        {
            createToken(request);
        } else
        {
            validateToken(request, form, mapping, TRANSACTION_ERROR_MESSAGE_KEY);
        }

        InfoStudent infoStudent = getInfoStudent(userView);
        List infoAttendingCourses = getInfoAttendingCoursesList(request, userView, infoStudent);

        initializeRequestAndFormForCourseChoose((DynaActionForm) form, request, infoAttendingCourses);
        return mapping.findForward("selectCourses");
    }

    public ActionForward addCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException, FenixServiceException
    {

        DynaActionForm studentShiftEnrolmentForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);
        InfoStudent infoStudent = getInfoStudent(userView);

        String[] wantedCourse = (String[]) studentShiftEnrolmentForm.get("course");
        String[] previousWantedCourse = (String[]) studentShiftEnrolmentForm.get("previousWantedCourse");
        List wantedCoursesList =
            (List) CollectionUtils.union(
                Arrays.asList(wantedCourse),
                Arrays.asList(previousWantedCourse));

        enrollStudent(studentShiftEnrolmentForm, request, infoStudent, wantedCoursesList);

        List attendingCourses = getInfoAttendingCoursesList(request, userView, infoStudent);

        initializeRequestAndFormForCourseChoose(studentShiftEnrolmentForm, request, attendingCourses);

        return mapping.findForward("selectCourses");
    }

    public ActionForward removeCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException, FenixServiceException
    {
        DynaActionForm studentShiftEnrolmentForm = (DynaActionForm) form;

        String[] unWantedCourses = (String[]) studentShiftEnrolmentForm.get("wantedCourse");
        String[] previousWantedCourses =
            (String[]) studentShiftEnrolmentForm.get("previousWantedCourse");

        List unWantedCoursesList = Arrays.asList(unWantedCourses);
        List previousWantedCoursesList = Arrays.asList(previousWantedCourses);
        List wantedCoursesList =
            (List) CollectionUtils.subtract(previousWantedCoursesList, unWantedCoursesList);

        InfoStudent infoStudent = getInfoStudent(SessionUtils.getUserView(request));

        enrollStudent(studentShiftEnrolmentForm, request, infoStudent, wantedCoursesList);

        return start(mapping, form, request, response);
    }

    /**
     * @param studentShiftEnrolmentForm
     * @param request
     */
    private void initializeRequestAndFormForCourseChoose(
        DynaActionForm studentShiftEnrolmentForm,
        HttpServletRequest request,
        List wantedInfoExecutionCourses)
        throws FenixActionException
    {
        List degreeList = getInfoExecutionDegreeList(SessionUtils.getUserView(request));
        request.setAttribute("degreeList", degreeList);

        Collections.sort(degreeList, new ComparatorByNameForInfoExecutionDegree());

        request.setAttribute("wantedInfoExecutionCourseList", wantedInfoExecutionCourses);

        List infoExecutionCoursesOIDList =
            (List) CollectionUtils.collect(wantedInfoExecutionCourses, new Transformer()
        {
            public Object transform(Object input)
            {
                InfoObject infoObject = (InfoObject) input;
                return infoObject.getIdInternal().toString();
            }
        });

        studentShiftEnrolmentForm.set("wantedCourse", new String[] {
        });
        studentShiftEnrolmentForm.set("course", new String[] {
        });

        studentShiftEnrolmentForm
            .set("previousWantedCourse", infoExecutionCoursesOIDList.toArray(new String[] {
        }));

        InfoExecutionDegree infoExecutionDegree = null;
        final String degreeCode = (String) studentShiftEnrolmentForm.get("degree");
        if (degreeCode == null)
        {
            infoExecutionDegree = (InfoExecutionDegree) degreeList.get(0);
        } else
        {
            List auxList = (List) CollectionUtils.select(degreeList, new Predicate()
            {

                public boolean evaluate(Object input)
                {
                    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
                    return infoExecutionDegree
                        .getInfoDegreeCurricularPlan()
                        .getInfoDegree()
                        .getSigla()
                        .equals(
                        degreeCode);
                }
            });
            if (!auxList.isEmpty())
            {
                infoExecutionDegree = (InfoExecutionDegree) auxList.get(0);
            } else
            {
                infoExecutionDegree = (InfoExecutionDegree) degreeList.get(0);
            }
        }
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionPeriod currentExecutionPeriod = getCurrentExecutionPeriod(userView);

        List infoExecutionCourses =
            getExecutionCourses(userView, infoExecutionDegree, currentExecutionPeriod);

        request.setAttribute(
            "courseList",
            CollectionUtils.subtract(infoExecutionCourses, wantedInfoExecutionCourses));

    }

    /**
     * Enroll the student on a particular set of classes as selected by him of
     * <code>Degree</code>  
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws FenixActionException
     */
    public ActionForward enrollCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        boolean firstTime = request.getParameter("firstTime") != null;

        if (firstTime)
        {
            createToken(request);
        } else
        {
            validateToken(request, form, mapping, TRANSACTION_ERROR_MESSAGE_KEY);
        }

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        // retrieve the student information to fill the info 
        // to be passed to the readShifts service
        InfoStudent infoStudent = getInfoStudent(userView);

        //Update the student's attending courses:
        //		enrollStudent(form, request, firstTime, userView, infoStudent);

        List degreeList = getInfoExecutionDegreeList(userView);

        // get the student's own course to show first, if there is not one
        // already selected
        String selectedDegreeAbbrev = request.getParameter("degree");

        InfoExecutionDegree selectedDegree = null, idtemp = null;

        Collections.sort(degreeList, new ComparatorByNameForInfoExecutionDegree());

        // if there is no selected degree
        if (selectedDegreeAbbrev == null)
        {
            // retrieve the student's own curricular plan
            try
            {
                selectedDegreeAbbrev =
                    ((InfoDegree) ServiceUtils
                        .executeService(
                            userView,
                            "ReadCourseByStudent",
                            new Object[] { infoStudent.getNumber(), infoStudent.getDegreeType()}))
                        .getSigla();
            } catch (FenixServiceException e3)
            {
                throw new FenixActionException(e3);
            }
        }

        Iterator i = degreeList.iterator();
        // we assume that the Abbrev ( sigla ) is unique amongst the degrees
        while (i.hasNext())
        {
            idtemp = (InfoExecutionDegree) i.next();
            if (idtemp
                .getInfoDegreeCurricularPlan()
                .getInfoDegree()
                .getSigla()
                .equals(selectedDegreeAbbrev))
            {
                selectedDegree = idtemp;
                i.remove();
            }
        }

        if (selectedDegree == null)
        {
            // didn't found the degree, doesn't exist. someone did a boo boo
            throw new FenixActionException("Wrong Degree Selected. Please enroll in another college.");
        } // end of if ()

        // add to the beginning of the list the selected degree
        degreeList.add(0, selectedDegree);

        request.setAttribute("degreeList", degreeList);

        InfoExecutionPeriod currentExecutionPeriod = getCurrentExecutionPeriod(userView);

        List infoExecutionCourses =
            getExecutionCourses(userView, selectedDegree, currentExecutionPeriod);

        request.setAttribute("courseList", infoExecutionCourses);

        List attendingCourses = getInfoAttendingCoursesList(request, userView, infoStudent);
        request.setAttribute("wantedCourse", attendingCourses);

        return mapping.findForward("selectCourses");
    }

    private InfoExecutionPeriod getCurrentExecutionPeriod(IUserView userView)
        throws FenixActionException
    {
        InfoExecutionPeriod currentExecutionPeriod;
        try
        {
            currentExecutionPeriod =
                (
                    InfoExecutionPeriod) ServiceUtils
                        .executeService(userView, "ReadCurrentExecutionPeriod", new Object[] {
            });
        } catch (FenixServiceException e3)
        {
            throw new FenixActionException(e3);
        }
        return currentExecutionPeriod;
    }

    private List getExecutionCourses(
        IUserView userView,
        InfoExecutionDegree selectedDegree,
        InfoExecutionPeriod currentExecutionPeriod)
        throws FenixActionException
    {
        // retrieve all courses pertaining to the selected degree
        List infoExecutionCourses = new ArrayList();

        try
        {
            infoExecutionCourses =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionCoursesByDegreeAndExecutionPeriod",
                    new Object[] { selectedDegree, currentExecutionPeriod });
        } catch (FenixServiceException e4)
        {
            throw new FenixActionException(e4);
        }
        return infoExecutionCourses;
    }

    private List getInfoAttendingCoursesList(
        HttpServletRequest request,
        IUserView userView,
        InfoStudent infoStudent)
        throws FenixActionException
    {
        // and finally retrieve all courses the student is currently enrolled in
        List attendingCourses;
        try
        {
            attendingCourses =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadDisciplinesByStudent",
                    new Object[] { infoStudent.getNumber(), infoStudent.getDegreeType()});
        } catch (FenixServiceException e4)
        {
            throw new FenixActionException(e4);
        }
        return attendingCourses;
    }

    private List getInfoExecutionDegreeList(IUserView userView) throws FenixActionException
    {
        // Retrieve the list of existing degrees
        List degreeList;
        try
        {
            degreeList =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadNonMasterExecutionDegreesByExecutionYear",
                    new Object[] { null });
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        return degreeList;
    }

    private void enrollStudent(
        DynaActionForm studentShiftEnrolmentForm,
        HttpServletRequest request,
        InfoStudent infoStudent,
        List wantedCourses)
        throws FenixActionException, FenixServiceException
    {

        IUserView userView = SessionUtils.getUserView(request);

        if (wantedCourses.size() <= 8)
        {
            //Build up the arrayList

            Boolean attendenceChanged = writeAttendingCourses(userView, infoStudent, wantedCourses);

            if (!attendenceChanged.booleanValue())
            {
                throw new FenixActionException("Can't Change Degree Attending.");
            }
        } else
        {
            // TODO: actionErrors

        }
    }

    private InfoStudent getInfoStudent(IUserView userView) throws FenixActionException
    {
        InfoStudent infoStudent = null;
        try
        {
            infoStudent =
                (InfoStudent) ServiceUtils.executeService(
                    userView,
                    "ReadStudentByUsername",
                    new Object[] { userView.getUtilizador()});
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        return infoStudent;
    }

    private Boolean writeAttendingCourses(
        IUserView userView,
        InfoStudent infoStudent,
        List wantedCoursesList)
        throws FenixServiceException
    {
        Boolean attendenceChanged;
        //		try {
        attendenceChanged =
            (Boolean) (ServiceUtils
                .executeService(
                    userView,
                    "WriteStudentAttendingCourses",
                    new Object[] { infoStudent, wantedCoursesList }));
        //		} catch (FenixServiceException e) {
        //			if (e instanceof WriteStudentAttendingCourses.AlreadyEnroledServiceException || e instanceof WriteStudentAttendingCourses.AlreadyEnroledInGroupServiceException) {
        //				throw e;
        //			}else {
        //				throw new FenixActionException(e);
        //			}
        //		}
        return attendenceChanged;
    }

    public ActionForward proceedToShiftEnrolment(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        validateToken(request, form, mapping, TRANSACTION_ERROR_MESSAGE_KEY);

        // inscrever nas cadeiras 
        // EXPLICAÇÂO: supostamente haveria um passo intermedio onde se inscreve nas cadeiras que escolheu 
        //antes de escolher as turmas  mas acho q deixou de ser necessário ao tratar disso na action anterior.
        // ou não...                            

        IUserView userView = SessionUtils.getUserView(request);

        // retrieve the student information to fill the info 
        // to be passed to the readShifts service
        InfoStudent infoStudent = null;
        try
        {
            infoStudent =
                (InfoStudent) ServiceUtils.executeService(
                    userView,
                    "ReadStudentByUsername",
                    new Object[] { userView.getUtilizador()});
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        //Update the student's attending courses:

        //		String[] wantedCourses = ((String[]) ((DynaActionForm) form).get("wantedCourse"));
        //
        //		if (wantedCourses != null) {
        //
        //			Boolean attendenceChanged = writeAttendingCourses(userView, infoStudent, Arrays.asList(wantedCourses));
        //			if (!attendenceChanged.booleanValue()) {
        //				System.out.println("Nao foi possivel actualizar as disciplinas do aluno");
        //				throw new FenixActionException("Can't Change Degree Attending.");
        //			}
        //		}
        return initializeShiftEnrolment(mapping, form, request, infoStudent);
    }
    public ActionForward initializeShiftEnrolment(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        validateToken(request, form, mapping, TRANSACTION_ERROR_MESSAGE_KEY);
        InfoStudent infoStudent = null;
        IUserView userView = SessionUtils.getUserView(request);
        try
        {
            infoStudent =
                (InfoStudent) ServiceUtils.executeService(
                    userView,
                    "ReadStudentByUsername",
                    new Object[] { userView.getUtilizador()});
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);

        }

        return initializeShiftEnrolment(mapping, form, request, infoStudent);
    }

    private ActionForward initializeShiftEnrolment(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        InfoStudent infoStudent)
        throws FenixActionException
    {

        HttpSession session = request.getSession();
        IUserView userView = SessionUtils.getUserView(request);
        InfoShiftStudentEnrolment infoShiftStudentEnrolment = new InfoShiftStudentEnrolment();

        infoShiftStudentEnrolment.setInfoStudent(infoStudent);
        try
        {
            infoShiftStudentEnrolment =
                (InfoShiftStudentEnrolment) ServiceUtils.executeService(
                    userView,
                    "ReadStudentShiftEnrolment",
                    new Object[] { infoShiftStudentEnrolment });
        } catch (FenixServiceException e1)
        {
            throw new FenixActionException(e1);
        }

        session.setAttribute(
            SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY,
            infoShiftStudentEnrolment);

        // if the user has chosen to enrol by class we show a jsp , otherwise, 
        // proceed to the showAvailableShifts action 
        // WONDER: tdi-dev (bruno) -> is this correct? to get a 'variable' directly...
        //		String divisor = request.getParameter("divideMethod");

        //TODO:  tdi-dev (edgar.goncalves2bruno.lopes) -> divideMethod isn't necessary. remove? (change if below)
        //ANSWER: tdi-dev (bruno.lopes2edgar.goncalves) -> indeed. (when (stable) (remove offending-code)) 
        //if (divisor == null || divisor.equals("classes")) {
        return mapping.findForward("selectClass");
        //		} else { // assume that if the user doesn't want to choose a class, then he wants to choose by course
        //			return showAvailableShifts(mapping, form, request, response);
        //		}
    }

    /**
     * Show avaliable shifts divided by <i>dividedMethod</i> (<b>classes<b> or <b>courses<b>)
     * Optionally, filter by <i>class</i>
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward showAvailableShifts(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        validateToken(request, form, mapping, TRANSACTION_ERROR_MESSAGE_KEY);

        HttpSession session = request.getSession();

        InfoShiftStudentEnrolment infoShiftStudentEnrolment =
            (InfoShiftStudentEnrolment) session.getAttribute(
                SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY);

        //		dividedList
        //		System.out.println("infoShiftStudentEnrolment"+infoShiftStudentEnrolment);
        //		System.out.println("infoShiftStudentEnrolment"+infoShiftStudentEnrolment);
        //		System.out.println("infoShiftStudentEnrolment"+infoShiftStudentEnrolment);

        if (infoShiftStudentEnrolment == null)
        {
            throw new FenixTransactionException("Erro. Por favor nao faça isto.");
            // TODO: tdi-dev (bruno) -> change this to something 'normal'. this is a placeholder
        }
        // now we group the shifts
        InfoShiftDividedList infoShiftDividedList = new InfoShiftDividedList();
        Iterator i = infoShiftStudentEnrolment.getAvailableShift().iterator();
        // FIXME: tdi-dev (bruno) -> this wanted class is not reset after use, causing some problems
        String wantedClass = (String) ((DynaActionForm) form).get("class");
        //		ClassNamePredicate filter = new ClassNamePredicate(wantedClass);

        while (i.hasNext())
        {
            InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftClassesWithLessons =
                (InfoShiftWithAssociatedInfoClassesAndInfoLessons) i.next();
            List classes = shiftClassesWithLessons.getInfoClasses();
            InfoShift shift = shiftClassesWithLessons.getInfoShift();
            // if the user selected a class 
            if (wantedClass != null && !wantedClass.equals(""))
            {
                // we see if the current shift has that class0
                for (int j = 0; j < classes.size(); j++)
                {
                    if (((InfoClass) classes.get(j)).getNome().equals(wantedClass))
                    {
                        infoShiftDividedList.put(
                            shift.getInfoDisciplinaExecucao().getNome(),
                            shiftClassesWithLessons);
                        continue; // it has, no need to continue searching
                    }
                }
            } else
            {
                // otherwise, just add the shift to the list
                infoShiftDividedList.put(
                    shift.getInfoDisciplinaExecucao().getNome(),
                    shiftClassesWithLessons);
            }
        }
        //request.setAttribute(wantedClass, filter)
        infoShiftStudentEnrolment.setDividedList(infoShiftDividedList);

        session.setAttribute(
            SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY,
            infoShiftStudentEnrolment);
        initializeForm(infoShiftStudentEnrolment, (DynaActionForm) form);
        return mapping.findForward("showAvailableShifts");
    }

    /**
     * Insert and validate the chosen shifts gotten from <i>shifts</i> present on the form 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward validateAndConfirmShiftEnrolment(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        validateToken(request, form, mapping, TRANSACTION_ERROR_MESSAGE_KEY, false);

        Integer[] shifts = (Integer[]) ((DynaActionForm) form).get("shifts");
        ArrayList toEnroll = new ArrayList();

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoShiftStudentEnrolment infoShiftStudentEnrolment =
            (InfoShiftStudentEnrolment) session.getAttribute(
                SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY);

        List avaliableShifts = infoShiftStudentEnrolment.getAvailableShift();

        // Insert into the wanted shifts list the shifts the user has selected
        InfoShift shift;
        for (int i = 0; i < shifts.length; i++)
        {
            Iterator it = avaliableShifts.iterator();
            if (shifts[i] == null)
            {
                continue;
                // the user may have wished not to select a particular shift group
            }
            while (it.hasNext())
            {
                shift = ((InfoShiftWithAssociatedInfoClassesAndInfoLessons) it.next()).getInfoShift();
                if (shifts[i].equals(shift.getIdInternal()))
                {
                    toEnroll.add(shift);
                }
            }
        }
        // Insert into the wanted shifts list the shifts the users is already enroled in
        Iterator it = infoShiftStudentEnrolment.getCurrentEnrolment().iterator();
        while (it.hasNext())
        {
            toEnroll.add(it.next());
        }

        infoShiftStudentEnrolment.setWantedShifts(toEnroll);

        try
        {
            infoShiftStudentEnrolment =
                (InfoShiftStudentEnrolment) ServiceUtils.executeService(
                    userView,
                    "ValidateAndConfirmShiftStudentEnrolment",
                    new Object[] { infoShiftStudentEnrolment });
        } catch (FenixServiceException e)
        {
            // TODO: tdi-dev (bruno) -> here we will catch the errors that say "hey, you made a boo boo". or not
            throw new FenixActionException(e);
        }
        session.setAttribute(
            SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY,
            infoShiftStudentEnrolment);
        return mapping.findForward("validateAndConfirmShiftEnrolment");
        // TODO: tdi-dev (bruno) -> clean up session variable?
    }

    public ActionForward viewClassTimeTable(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String classIdString = request.getParameter("classId");
        Integer classId = new Integer(classIdString);
        InfoClass infoClass = new InfoClass();
        infoClass.setIdInternal(classId);
        List infoLessons;
        try
        {
            infoLessons =
                (List) ServiceUtils.executeService(
                    userView,
                    "LerAulasDeTurma",
                    new Object[] { infoClass });
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        InfoClass infoSchoolClass;
        try
        {
            infoSchoolClass =
                (InfoClass) ServiceUtils.executeService(
                    userView,
                    "ReadSchoolClass",
                    new Object[] { infoClass });
        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoSchoolClass", infoSchoolClass);
        request.setAttribute("infoLessons", infoLessons);

        return mapping.findForward("classTimeTable");
    }

    /**
     * @param infoShiftStudentEnrolment
     * @param form
     */
    private void initializeForm(
        InfoShiftStudentEnrolment infoShiftStudentEnrolment,
        DynaActionForm form)
    {

        form.set(
            "shifts",
            new Integer[((InfoShiftDividedList) infoShiftStudentEnrolment.getDividedList()).leafSize()
                * 2]);
        // DANGER: each divided 'sector' has two sublists, each a type of shift . Hardcoding 2 is dangerous
    }
}
