/*
 * Created on 16/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.student;



import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoShift;
import DataBeans.InfoShiftStudentEnrolment;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author tdi-dev (bruno)
 *
 * This class is used to group shifts by 'main types' (such as 
 * classes or courses) and then subdivide by shift types 
 */
class InfoShiftDividedList extends ArrayList {
    public class Element {
        /* this class should probably extend an ArrayList, as it is just a named ArrayList */
        private String type;
        private ArrayList list;

        public Element(String type) {
            this.type = type;
            list = new ArrayList();
        }
        /**
         * @return corresponding type set at construction time 
         */
        public String getType() {
            return type;
        }

        public boolean add(Object element) {
            return list.add(element);
        }
        /**
         * @return the corresponding list
         */
        public ArrayList getList() {
            return list;
        }
        /** stub */
        public void setList(ArrayList list) {
        }
        /** stub */
        public void setType(String string) {
        }
        public int size(){
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
    public boolean put(String type, InfoShiftWithAssociatedInfoClassesAndInfoLessons info) {
        // tdi-dev (bruno) -> honestly, this is the best way, without flags or others...  
        Element mainE, subE;
        Iterator mainIt = this.iterator();
        
        InfoShift shift = info.getInfoShift();
        
        while (mainIt.hasNext()) {
            //search for the element that has the main type we want
            mainE = (Element)mainIt.next();
            if (mainE.getType().equals(type)) {
                // found it, now we need to search for the shift type 'spot'
                Iterator subIt = mainE.getList().iterator();
                while (subIt.hasNext()) {
                    subE = (Element)subIt.next();
                    if (subE.getType().equals(shift.getTipo().getSiglaTipoAula())) {
                        // ok, found shift type list, let's add the shift
                        subE.add(info);
                        return true;
                    }
                }
                // ups, subtype doesn't exist . create and add
                subE = new Element(shift.getTipo().getSiglaTipoAula());
                subE.add(info);
                mainE.add(subE);
                return true;
            }
        }
        // main type doesn't exist iet...
        mainE = new Element(type);
        subE = new Element(shift.getTipo().getSiglaTipoAula());
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
    public int leafSize() {
        Iterator it = this.iterator();
        int size=0;
        while(it.hasNext()){
            size+=((Element)it.next()).size();
        }
        return size;
    }

}

public class ShiftStudentEnrolmentManagerDispatchAction extends DispatchAction {
    private class classFilter implements Predicate {
        private String s;
        public classFilter(String s) {
            super();
            this.s = s;
        }
        public boolean evaluate(Object input) {
            return ((InfoClass)input).getNome().equals(s);
        }
    }
    /**
     * Enroll the student on a particular set of classes as selected by him of
     * <code>Degree</code>  
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward enrollCourses(ActionMapping mapping,
				       ActionForm form,
				       HttpServletRequest request,
				       HttpServletResponse response)
        throws Exception {
	
        HttpSession session = request.getSession();
        IUserView userView = (IUserView)session.getAttribute(SessionConstants.U_VIEW);

        // retrieve the student information to fill the info 
        // to be passed to the readShifts service
        InfoStudent infoStudent = null;
        infoStudent =
            (InfoStudent)ServiceUtils.executeService(
						     userView,
						     "ReadStudentByUsername",
						     new Object[] { userView.getUtilizador()});
	
	// Retrieve the current executionYear to later get the existing degrees
	InfoExecutionYear currentExecutionYear = 
	    (InfoExecutionYear)ServiceUtils.executeService(userView,"ReadCurrentExecutionYear",new Object[]{});
	
	// Retrieve the list of existing degrees
	List degreeList =  
	    (List)ServiceUtils.executeService(userView,"ReadExecutionDegreesByExecutionYear",new Object[]{currentExecutionYear});
	
	// get the student's own course to show first, if there is not one
	// already selected
	String selectedDegreeAbbrev = request.getParameter("degree");
	InfoExecutionDegree selectedDegree = null, idtemp = null ;
	
	// if there is no selected degree
	if (request.getParameter("degree") != "") {
	    // retrieve the student's own curricular plan
	    selectedDegreeAbbrev = ((InfoDegree)ServiceUtils.executeService(userView,"ReadCourseByStudent",new Object[]{infoStudent.getNumber(),infoStudent.getDegreeType()})).getSigla();
	}

	Iterator i = degreeList.iterator();
	// we assume that the Abbrev ( sigla ) is unique amongst the degrees
	while (i.hasNext()){
	    idtemp = (InfoExecutionDegree) i.next();
	    if(idtemp.getInfoDegreeCurricularPlan().getInfoDegree().getSigla() == selectedDegreeAbbrev){
		selectedDegree = idtemp;
		i.remove();
	    }
	}
	if (selectedDegree == null) {
	    // didn't found the degree, doesn't exist. someone did a boo boo
	    throw new FenixActionException("Wrong Degree Selected. Please enroll in another college.");
	} // end of if ()
	
	// add to the beginning of the list the selected degree
	degreeList.add(0, selectedDegree);


	request.setAttribute("degreeList",degreeList);
	
	InfoExecutionPeriod currentExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
												    "ReadCurrentExecutionPeriod",
												    new Object[]{});
	
	// retrieve all courses pertaining to the selected degree
	ArrayList infoExecutionCourses = new ArrayList();
	for (int  j = 1; j < 6; j++) {
	    infoExecutionCourses.addAll( 
					(List) ServiceUtils.executeService(userView,
									   "SelectExecutionCourse",
									   new Object[]{selectedDegree,currentExecutionPeriod,new Integer(j)}));
	    
	} 

	request.setAttribute("courseList",infoExecutionCourses);

	// and finally retrieve all courses the student is currently enrolled in
	List attendingCourses = (List) ServiceUtils.executeService(userView,
								   "ReadDisciplinesByStudent",
								   new Object[]{infoStudent.getNumber(),infoStudent.getDegreeType()});
	
	System.out.println(request.getParameter("wantedCourse"));
	request.setAttribute("wantedCourse",attendingCourses);
	

	
	return mapping.findForward("selectCourses");
    }

    public ActionForward proceedToShiftEnrolment(
						  ActionMapping mapping,
						  ActionForm form,
						  HttpServletRequest request,
						  HttpServletResponse response)
        throws Exception {
	// inscrever nas cadeiras 
	
	// return initializeShiftEnrolment
	return initializeShiftEnrolment(mapping,form,request,response);
    }

    public ActionForward initializeShiftEnrolment(
						  ActionMapping mapping,
						  ActionForm form,
						  HttpServletRequest request,
						  HttpServletResponse response)
        throws Exception {


        HttpSession session = request.getSession();
        IUserView userView = (IUserView)session.getAttribute(SessionConstants.U_VIEW);

        // retrieve the student information to fill the info 
        // to be passed to the readShifts service
        InfoStudent infoStudent = null;
        infoStudent =
            (InfoStudent)ServiceUtils.executeService(
						     userView,
						     "ReadStudentByUsername",
						     new Object[] { userView.getUtilizador()});

        InfoShiftStudentEnrolment infoShiftStudentEnrolment =
            new InfoShiftStudentEnrolment();
        infoShiftStudentEnrolment.setInfoStudent(infoStudent);
        infoShiftStudentEnrolment =
            (InfoShiftStudentEnrolment)ServiceUtils.executeService(
								   userView,
								   "ReadStudentShiftEnrolment",
								   new Object[] { infoShiftStudentEnrolment });

        session.setAttribute(
			     SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY,
			     infoShiftStudentEnrolment);

        // if the user has chosen to enrol by class we show a jsp , otherwise, 
        // proceed to the showAvailableShifts action 
        // WONDER: tdi-dev (bruno) -> is this correct? to get a 'variable' directly...
        String divisor = request.getParameter("divideMethod");
        

		
        if (divisor.equals("classes")) {
            return mapping.findForward("selectClass");
        } else { // assume that if the user doesn't want to choose a class, then he wants to choose by course
            return showAvailableShifts(mapping, form, request, response);
        }
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
        throws Exception {

        //validateToken(request, form, mapping);
        
        HttpSession session = request.getSession();
        IUserView userView = (IUserView)session.getAttribute(SessionConstants.U_VIEW);

        InfoShiftStudentEnrolment infoShiftStudentEnrolment =
            (InfoShiftStudentEnrolment)session.getAttribute(
							    SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY);

        if (infoShiftStudentEnrolment == null) {
            throw new FenixTransactionException("Erro. Por favor nao faça isto.");
            // TODO: tdi-dev (bruno) -> change this to something 'normal'. this is a placeholder
        }
        // now we group the shifts
        InfoShiftDividedList infoShiftDividedList = new InfoShiftDividedList();
        Iterator i = infoShiftStudentEnrolment.getAvailableShift().iterator();
        // FIXME: tdi-dev (bruno) -> this wanted class is not reset after use, causing some problems
        String wantedClass = (String) ((DynaActionForm)form).get("class");
        classFilter filter = new classFilter(wantedClass);

        while (i.hasNext()) {
            InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftClassesWithLessons = (InfoShiftWithAssociatedInfoClassesAndInfoLessons)i.next();
            List classes = shiftClassesWithLessons.getInfoClasses();
            InfoShift shift = shiftClassesWithLessons.getInfoShift();
            // if the user selected a class 
            if (wantedClass != null && !wantedClass.equals("")) {
                // we see if the current shift has that class0
                for (int j = 0; j < classes.size(); j++) {
                    if (((InfoClass)classes.get(j)).getNome().equals(wantedClass)) {
                        infoShiftDividedList.put(
						 shift.getInfoDisciplinaExecucao().getNome(),
						 shiftClassesWithLessons);
                        continue; // it has, no need to continue searching
                    }
                }
            } else {
                // otherwise, just add the shift to the list
                infoShiftDividedList.put(
					 shift.getInfoDisciplinaExecucao().getNome(),
					 shiftClassesWithLessons);
            }
        }

        infoShiftStudentEnrolment.setDividedList(infoShiftDividedList);
        //        session.setAttribute(
        //            SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY,
        //            infoShiftStudentEnrolment);
	initializeForm(infoShiftStudentEnrolment, (DynaActionForm)form);
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
        throws Exception {

        //validateToken(request, form, mapping);

        Integer[] shifts = (Integer[]) ((DynaActionForm)form).get("shifts");
        ArrayList toEnroll = new ArrayList();

        HttpSession session = request.getSession();
        IUserView userView = (IUserView)session.getAttribute(SessionConstants.U_VIEW);

        InfoShiftStudentEnrolment infoShiftStudentEnrolment =
            (InfoShiftStudentEnrolment)session.getAttribute(
							    SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY);

        List avaliableShifts = infoShiftStudentEnrolment.getAvailableShift();

        // Insert into the wanted shifts list the shifts the user has selected
        InfoShift shift;
        for (int i = 0; i < shifts.length; i++) {
            Iterator it = avaliableShifts.iterator();
            if(shifts[i]==null){
                continue; // the user may have wished not to select a particular shift group
            }
            while (it.hasNext()) {
                shift = (InfoShift) ((InfoShiftWithAssociatedInfoClassesAndInfoLessons)it.next()).getInfoShift();
                if (shifts[i].equals(shift.getIdInternal())) {
                    toEnroll.add(shift);
                }
            }
        }
        // Insert into the wanted shifts list the shifts the users is already enroled in
        Iterator it = infoShiftStudentEnrolment.getCurrentEnrolment().iterator();
        while (it.hasNext()) {
            toEnroll.add(it.next());
        }

        infoShiftStudentEnrolment.setWantedShifts(toEnroll);

        try {
            infoShiftStudentEnrolment =
                (InfoShiftStudentEnrolment)ServiceUtils.executeService(
								       userView,
								       "ValidateAndConfirmShiftStudentEnrolment",
								       new Object[] { infoShiftStudentEnrolment });
        } catch (Exception e) {
            // TODO: tdi-dev (bruno) -> here we will catch the errors that say "hey, you made a boo boo". or not
        }
        return mapping.findForward("validateAndConfirmShiftEnrolment");
        // TODO: tdi-dev (bruno) -> clean up session variable?
    }

    /**
     * @param request   
     */
    private void validateToken(
			       HttpServletRequest request,
			       ActionForm form,
			       ActionMapping mapping)
        throws FenixTransactionException {
        if (!isTokenValid(request)) {
            form.reset(mapping, request);
            throw new FenixTransactionException("error.transaction.enrolment");
        } else {
            createToken(request);
        }
    }
    /**
     * @param request
     */
    private void createToken(HttpServletRequest request) {
        generateToken(request);
        saveToken(request);
    }
    /**
     * @param infoShiftStudentEnrolment
     * @param form
     */
    private void initializeForm(
				InfoShiftStudentEnrolment infoShiftStudentEnrolment,
				DynaActionForm form) {
        List currentEnrolment = infoShiftStudentEnrolment.getCurrentEnrolment();
        List avaliableShift = infoShiftStudentEnrolment.getAvailableShift();
        InfoStudent infoStudent = infoShiftStudentEnrolment.getInfoStudent();
        form.set("shifts",new Integer[((InfoShiftDividedList)infoShiftStudentEnrolment.getDividedList()).leafSize()*2]); // DANGER: each divided 'sector' has two sublists, each a type of shift . Hardcoding 2 is dangerous
    }
}
