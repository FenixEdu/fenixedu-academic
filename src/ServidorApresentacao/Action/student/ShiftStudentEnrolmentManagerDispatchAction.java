/*
 * Created on 16/Mai/2003
 *
 */
package ServidorApresentacao.Action.student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.InfoShiftStudentEnrolment;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

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
		public int size() {
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
			mainE = (Element) mainIt.next();
			if (mainE.getType().equals(type)) {
				// found it, now we need to search for the shift type 'spot'
				Iterator subIt = mainE.getList().iterator();
				while (subIt.hasNext()) {
					subE = (Element) subIt.next();
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
		int size = 0;
		while (it.hasNext()) {
			size += ((Element) it.next()).size();
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
			return ((InfoClass) input).getNome().equals(s);
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
	public ActionForward initializeEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Initiate a "transactional" action 
		// FIXME: tdi-dev (bruno) -> this is broken 
		// createToken(request);

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		// retrieve the student information to fill the info 
		// to be passed to the readShifts service
		InfoStudent infoStudent = null;
		infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "ReadStudentByUsername", new Object[] { userView.getUtilizador()});

		InfoShiftStudentEnrolment infoShiftStudentEnrolment = new InfoShiftStudentEnrolment();
		infoShiftStudentEnrolment.setInfoStudent(infoStudent);
		infoShiftStudentEnrolment = (InfoShiftStudentEnrolment) ServiceUtils.executeService(userView, "ReadStudentShiftEnrolment", new Object[] { infoShiftStudentEnrolment });

		session.setAttribute(SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY, infoShiftStudentEnrolment);

		// if the user has chosen to enrol by class we show a jsp , otherwise, 
		// proceed to the showAvailableShifts action 
		// WONDER: tdi-dev (bruno) -> is this correct? to get a 'variable' directly...
		String divisor = request.getParameter("divideMethod");

		List infoLessons = (List) extractInfoLessons(infoShiftStudentEnrolment.getCurrentEnrolment());
		request.setAttribute("infoLessons", infoLessons);


		if (divisor.equals("classes")) {
			Collections.sort(infoShiftStudentEnrolment.getAllowedClasses(), new BeanComparator("anoCurricular"));
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
	public ActionForward showAvailableShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//validateToken(request, form, mapping);

		HttpSession session = request.getSession();

		InfoShiftStudentEnrolment infoShiftStudentEnrolment = (InfoShiftStudentEnrolment) session.getAttribute(SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY);

		if (infoShiftStudentEnrolment == null) {
			throw new FenixTransactionException("Erro. Por favor nao faça isto.");
			// TODO: tdi-dev (bruno) -> change this to something 'normal'. this is a placeholder
		}
		// now we group the shifts
		InfoShiftDividedList infoShiftDividedList = new InfoShiftDividedList();
		Iterator i = infoShiftStudentEnrolment.getAvailableShift().iterator();
		// FIXME: tdi-dev (bruno) -> this wanted class is not reset after use, causing some problems
		String wantedClass = (String) ((DynaActionForm) form).get("class");

		while (i.hasNext()) {
			InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftClassesWithLessons = (InfoShiftWithAssociatedInfoClassesAndInfoLessons) i.next();
			List classes = shiftClassesWithLessons.getInfoClasses();
			InfoShift shift = shiftClassesWithLessons.getInfoShift();
			// if the user selected a class 
			if (wantedClass != null && !wantedClass.equals("")) {
				// we see if the current shift has that class0
				for (int j = 0; j < classes.size(); j++) {
					if (((InfoClass) classes.get(j)).getNome().equals(wantedClass)) {
						infoShiftDividedList.put(shift.getInfoDisciplinaExecucao().getNome(), shiftClassesWithLessons);
						continue; // it has, no need to continue searching
					}
				}
			} else {
				// otherwise, just add the shift to the list
				infoShiftDividedList.put(shift.getInfoDisciplinaExecucao().getNome(), shiftClassesWithLessons);
			}
		}

		infoShiftStudentEnrolment.setDividedList(infoShiftDividedList);
	
		List infoLessons = (List) extractInfoLessons(infoShiftStudentEnrolment.getCurrentEnrolment());
		request.setAttribute("infoLessons", infoLessons);
		
		initializeForm(infoShiftStudentEnrolment, (DynaActionForm) form);
		return mapping.findForward("showAvailableShifts");
	}

	/**
	 * @param list
	 * @return
	 */
	private List extractInfoLessons(List infoShiftList) {
		Iterator iterator = infoShiftList.iterator();
		List infoLessons = new ArrayList();
		while (iterator.hasNext()) {
			InfoShift infoShift = (InfoShift) iterator.next();
			infoLessons.addAll(infoShift.getInfoLessons());
		}
		return infoLessons;
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
	public ActionForward validateAndConfirmShiftEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//validateToken(request, form, mapping);

		Integer[] shifts = (Integer[]) ((DynaActionForm) form).get("shifts");
		ArrayList toEnroll = new ArrayList();

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		InfoShiftStudentEnrolment infoShiftStudentEnrolment = (InfoShiftStudentEnrolment) session.getAttribute(SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY);

		List avaliableShifts = infoShiftStudentEnrolment.getAvailableShift();

		// Insert into the wanted shifts list the shifts the user has selected
		InfoShift shift;
		for (int i = 0; i < shifts.length; i++) {
			Iterator it = avaliableShifts.iterator();
			if (shifts[i] == null) {
				continue; // the user may have wished not to select a particular shift group
			}
			while (it.hasNext()) {
				shift = (InfoShift) ((InfoShiftWithAssociatedInfoClassesAndInfoLessons) it.next()).getInfoShift();
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
			infoShiftStudentEnrolment = (InfoShiftStudentEnrolment) ServiceUtils.executeService(userView, "ValidateAndConfirmShiftStudentEnrolment", new Object[] { infoShiftStudentEnrolment });
		} catch (Exception e) {
			// TODO: tdi-dev (bruno) -> here we will catch the errors that say "hey, you made a boo boo". or not
		}
		return mapping.findForward("validateAndConfirmShiftEnrolment");
		// TODO: tdi-dev (bruno) -> clean up session variable?
	}

	/**
	 * @param request   
	 */
	private void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping) throws FenixTransactionException {
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
	private void initializeForm(InfoShiftStudentEnrolment infoShiftStudentEnrolment, DynaActionForm form) {
		List currentEnrolment = infoShiftStudentEnrolment.getCurrentEnrolment();
		List avaliableShift = infoShiftStudentEnrolment.getAvailableShift();
		InfoStudent infoStudent = infoShiftStudentEnrolment.getInfoStudent();
		form.set("shifts", new Integer[((InfoShiftDividedList) infoShiftStudentEnrolment.getDividedList()).leafSize() * 2]);
		// DANGER: each divided 'sector' has two sublists, each a type of shift . Hardcoding 2 is dangerous
	}
}
