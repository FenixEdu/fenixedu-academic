/*
 * Created on 31/Mar/2005
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoRoomWithInfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.InquiriesUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class FillInquiryAction extends FenixDispatchAction {

    public ActionForward defaultMethod(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "error.message.inquiries.javascript.disabled");

        return actionMapping.findForward("unavailableInquiry");
    }

    public ActionForward prepare(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		DynaActionForm inquiryForm = (DynaActionForm) actionForm;

		loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);

        if (attendingCourseTeachers.size() == 0) {
			request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.unavailable.course");
            return actionMapping.findForward("unavailableInquiry");
		}
		
		InfoExecutionCourse executionCourse = (InfoExecutionCourse) request.getAttribute(InquiriesUtil.ATTENDING_EXECUTION_COURSE);
		if((executionCourse.getTheoPratHours() == 0) &&
				(executionCourse.getTheoreticalHours() == 0) &&
				(executionCourse.getPraticalHours() == 0) &&
				(executionCourse.getLabHours() == 0)) {
			request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.unavailable.course");
            return actionMapping.findForward("unavailableInquiry");
		}
			
        request.setAttribute(InquiriesUtil.ANCHOR, "inquiry");
        return actionMapping.findForward("fillInquiry");
    }	

	public ActionForward prepareCourses(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		
        IUserView userView = SessionUtils.getUserView(request);

        final Registration registration = userView.getPerson().getStudentsSet().iterator().next();
        if (registration == null) {
            throw new InvalidSessionActionException();
        }

        if (registration.getDegreeType() != DegreeType.DEGREE) {
            request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.not.open.for.non.degrees");     
            return actionMapping.findForward("inquiryIntroduction");            
        }

        // Obtaining the current execution period
 		InfoExecutionPeriod currentExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadCurrentExecutionPeriod", null);
        final Date inquiryResponseBegin = currentExecutionPeriod.getInquiryResponseBegin();
        final Date inquiryResponseEnd = currentExecutionPeriod.getInquiryResponseEnd();
        if (inquiryResponseBegin == null || inquiryResponseEnd == null) {
            request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.period.not.defined");     
            return actionMapping.findForward("inquiryIntroduction");            
        }
        if (!insidePeriod(inquiryResponseBegin, inquiryResponseEnd)) {
            request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.not.inside.period");     
            return actionMapping.findForward("inquiryIntroduction");            
        }

		//THIS IS ONLY READING THE ENROLLED COURSES, AND NOT ALL THE ATTENDING ONES
		Object[] argsStudentIdExecutionPeriodId = { registration.getIdInternal(), currentExecutionPeriod.getIdInternal(), Boolean.TRUE, Boolean.TRUE };
		List<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers> studentAttends =
			(List<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers>) ServiceUtils.executeService(userView, "student.ReadAttendsByStudentIdAndExecutionPeriodId", argsStudentIdExecutionPeriodId);
		//Order by execution course name
		Collections.sort(studentAttends, new BeanComparator("disciplinaExecucao.nome"));
		
		//Removing attends with no specified class types
        Object[] argsStudent = { registration };
		List<InfoInquiriesRegistry> studentInquiriesResgistries =
			(List<InfoInquiriesRegistry>) ServiceUtils.executeService(userView, "inquiries.ReadInquiriesRegistriesByStudent", argsStudent);

        List<InfoFrequenta> evaluatedAttends = new ArrayList<InfoFrequenta>();

        // removing the attending courses which inquiries were already answered
        for (InfoInquiriesRegistry iir : studentInquiriesResgistries) {
            for (InfoFrequenta iattends : studentAttends) {
				if(iir.getExecutionCourse().equals(iattends.getDisciplinaExecucao()) &&
						iir.getExecutionPeriod().equals(currentExecutionPeriod)) {
                    evaluatedAttends.add(iattends);
                }

            }
        }

        studentAttends.removeAll(evaluatedAttends);
        request.setAttribute(InquiriesUtil.STUDENT_ATTENDS, studentAttends);
        request.setAttribute(InquiriesUtil.EVALUATED_STUDENT_ATTENDS, evaluatedAttends);

        return actionMapping.findForward("inquiryIntroduction");

    }

    private boolean insidePeriod(final Date inquiryResponseBegin, final Date inquiryResponseEnd) {
        final String now = DateFormatUtil.format("yyyy/MM/dd HH:mm:ss", new Date());
        final String begin = DateFormatUtil.format("yyyy/MM/dd HH:mm:ss", inquiryResponseBegin);
        final String end = DateFormatUtil.format("yyyy/MM/dd HH:mm:ss", inquiryResponseEnd);
        return begin.compareTo(now) <= 0 && now.compareTo(end) < 0;
    }

    public ActionForward editInquiry(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }

    public ActionForward prepareNewTeacher(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

		Integer currentAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");

        if (!validateForm(request, inquiryForm)) {

            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, currentAttendingCourseTeacherFormPosition);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        } else {

            // Finding the new teacher
            InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes attendingCourseTeacher = null;

			Integer newAttendingCourseTeacherId = (Integer) inquiryForm.get("newAttendingCourseTeacherId");
			Integer newAttendingCourseNonAffiliatedTeacherId = (Integer) inquiryForm.get("newAttendingCourseNonAffiliatedTeacherId");
            if ((newAttendingCourseTeacherId != null) && (newAttendingCourseTeacherId > 0)) {
                for (InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacher : attendingCourseTeachers) {
					if((teacher.getTeacher() != null) &&
							(teacher.getIdInternal().equals(newAttendingCourseTeacherId))) {
                        attendingCourseTeacher = teacher;
                        break;
                    }
                }

			} else if((newAttendingCourseNonAffiliatedTeacherId != null) && (newAttendingCourseNonAffiliatedTeacherId > 0)) {
                for (InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacher : attendingCourseTeachers) {
					if((teacher.getNonAffiliatedTeacher() != null) &&
							(teacher.getIdInternal().equals(newAttendingCourseNonAffiliatedTeacherId))) {
                        attendingCourseTeacher = teacher;
                        break;
                    }
                }
            }

            if (attendingCourseTeacher.getRemainingClassTypes().size() == 0) {

				InfoInquiriesTeacher currentAttendingCourseTeacher = (currentAttendingCourseTeacherFormPosition != null) ?
						selectedAttendingCourseTeachers.get(currentAttendingCourseTeacherFormPosition) : null;

				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, currentAttendingCourseTeacherFormPosition);
				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
                if (attendingCourseTeacher.getTeacher() != null) {
					request.setAttribute(InquiriesUtil.COMPLETE_ATTENDING_COURSE_TEACHER_ID, newAttendingCourseTeacherId);

                } else if (attendingCourseTeacher.getNonAffiliatedTeacher() != null) {
					request.setAttribute(InquiriesUtil.COMPLETE_ATTENDING_COURSE_NON_AFFILIATED_TEACHER_ID,
                            newAttendingCourseNonAffiliatedTeacherId);
                }
				request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR);

            } else {

                clearCurrentTeacherForm(inquiryForm);

                // Adding the new teacher to the selected ones
                InfoInquiriesTeacher newAttendingCourseTeacher = new InfoInquiriesTeacher();
                newAttendingCourseTeacher.setTeacherOrNonAffiliatedTeacher(attendingCourseTeacher);
                selectedAttendingCourseTeachers.add(newAttendingCourseTeacher);

                int teacherFormPosition = selectedAttendingCourseTeachers.size() - 1;

				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, teacherFormPosition);
				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, newAttendingCourseTeacher);
				request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ANCHOR);
            }

        }

        // cleaning the form entry
        inquiryForm.set("newAttendingCourseTeacherId", null);
        inquiryForm.set("newAttendingCourseNonAffiliatedTeacherId", null);

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }



	public ActionForward prepareNewRoom(ActionMapping actionMapping,
	        ActionForm actionForm, HttpServletRequest request,
	        HttpServletResponse response) throws Exception {
		
	
        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

        if (!validateForm(request, inquiryForm)) {
            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

            request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        } else {

            // Finding the new room
            Integer newAttendingCourseRoomId = (Integer) inquiryForm.get("newAttendingCourseRoomId");
			InfoRoomWithInfoInquiriesRoom attendingCourseRoom = null;
			for(InfoRoomWithInfoInquiriesRoom room : attendingCourseRooms) {
                if (room.getIdInternal().equals(newAttendingCourseRoomId)) {
                    attendingCourseRoom = room;
                    break;
                }
            }

            boolean alreadySelected = false;
            // Check if the new room had already been selected
            for (InfoInquiriesRoom iir : selectedAttendingCourseRooms) {
                if (iir.getRoom().getIdInternal().equals(newAttendingCourseRoomId)) {
                    alreadySelected = true;
                    break;
                }
            }

            if (alreadySelected) {
                InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                        selectedAttendingCourseTeachers, inquiryForm);

                InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                        selectedAttendingCourseRooms, inquiryForm);

                request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
						inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

				request.setAttribute(InquiriesUtil.COMPLETE_ATTENDING_COURSE_ROOM_ID, newAttendingCourseRoomId);

            } else {

                clearCurrentRoomForm(inquiryForm);

                // Adding the new room to the selected ones
                InfoInquiriesRoom newAttendingCourseRoom = new InfoInquiriesRoom();
                newAttendingCourseRoom.setRoom(attendingCourseRoom);
//				attendingCourseRoom.setAlreadyEvaluatedFlag(true);
				attendingCourseRoom.setInquiriesRoom(newAttendingCourseRoom);
                selectedAttendingCourseRooms.add(newAttendingCourseRoom);

				request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, newAttendingCourseRoom);
				request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM_FORM_ANCHOR);
            }

        }

        // cleaning the form entry
        inquiryForm.set("newAttendingCourseRoomId", null);

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }


	public ActionForward editTeacher(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

		Integer currentAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");

        if (!validateForm(request, inquiryForm)) {
            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, currentAttendingCourseTeacherFormPosition);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        } else {
			Integer selectedAttendingCourseTeacherFormPosition =
				(Integer) inquiryForm.get("selectedAttendingCourseTeacherFormPosition");

			InfoInquiriesTeacher selectedAttendingCourseTeacher = 
				selectedAttendingCourseTeachers.get(selectedAttendingCourseTeacherFormPosition);

            updateCurrentTeacherForm(inquiryForm, selectedAttendingCourseTeacher);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, selectedAttendingCourseTeacherFormPosition);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, selectedAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ANCHOR);
        }

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }

	public ActionForward editRoom(ActionMapping actionMapping,
	        ActionForm actionForm, HttpServletRequest request,
	        HttpServletResponse response) throws Exception {

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

        if (!validateForm(request, inquiryForm)) {
            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

            request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        } else {
			final Integer selectedAttendingCourseRoomId =
				(Integer) inquiryForm.get("selectedAttendingCourseRoomId");

            // Finding the selected room
			InfoInquiriesRoom selectedAttendingCourseRoom =
				(InfoInquiriesRoom) CollectionUtils.find(selectedAttendingCourseRooms, new Predicate() {

                        public boolean evaluate(Object obj) {
                            if (obj instanceof InfoInquiriesRoom) {
                                InfoInquiriesRoom iir = (InfoInquiriesRoom) obj;
							return iir.getRoom().getIdInternal().equals(selectedAttendingCourseRoomId);
                            }
                            return false;
                        }

                    });

            updateCurrentRoomForm(inquiryForm, selectedAttendingCourseRoom);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, selectedAttendingCourseRoom);
			request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM_FORM_ANCHOR);
        }

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }


	public ActionForward removeTeacher(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

		Integer selectedAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("selectedAttendingCourseTeacherFormPosition");
		Integer currentAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");

        // removing the teacher
        removeTeacherFromSelectedTeachersForm(inquiryForm, selectedAttendingCourseTeacherFormPosition);
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);

		if((currentAttendingCourseTeacherFormPosition != null) &&
				(!selectedAttendingCourseTeacherFormPosition.equals(currentAttendingCourseTeacherFormPosition))) {

            // update the current teacher form position
            if (selectedAttendingCourseTeacherFormPosition < currentAttendingCourseTeacherFormPosition)
                currentAttendingCourseTeacherFormPosition--;

			inquiryForm.set("currentAttendingCourseTeacherFormPosition", currentAttendingCourseTeacherFormPosition);

            // saving the answers of the previous current form
            saveCurrentFormToSelectedForm(inquiryForm);

            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, currentAttendingCourseTeacherFormPosition);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);

        }

        // updating the current room parameters
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);
        InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                selectedAttendingCourseRooms, inquiryForm);
        request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        // saving the selected courses and teachers list
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR);

        return actionMapping.findForward("fillInquiry");

    }


	public ActionForward removeRoom(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        Integer currentAttendingCourseRoomId = (Integer) inquiryForm.get("currentAttendingCourseRoomId");
		Integer selectedAttendingCourseRoomId = (Integer) inquiryForm.get("selectedAttendingCourseRoomId");

        // removing the room
		removeRoomFromSelectedRoomForm(inquiryForm, getRoomFormPosition(inquiryForm, selectedAttendingCourseRoomId));
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

		if((currentAttendingCourseRoomId != null) &&
				(!selectedAttendingCourseRoomId.equals(currentAttendingCourseRoomId))) {

            // saving the answers of the previous current form
            saveCurrentFormToSelectedForm(inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        }

        // updating the current teacher parameters
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
        InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                selectedAttendingCourseTeachers, inquiryForm);
        request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
				inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
		request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);


		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR);

        return actionMapping.findForward("fillInquiry");

    }


	public ActionForward closeTeacher(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

		Integer currentAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");

        if (!validateForm(request, inquiryForm)) {
            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION, currentAttendingCourseTeacherFormPosition);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        } else {

            clearCurrentRoomForm(inquiryForm);
			request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR);
        }

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }

	public ActionForward closeRoom(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

		List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers =
			(List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms =
			(List<InfoRoomWithInfoInquiriesRoom>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(inquiryForm, attendingCourseTeachers);
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(inquiryForm, attendingCourseRooms);

        if (!validateForm(request, inquiryForm)) {
            InfoInquiriesTeacher currentAttendingCourseTeacher = getCurrentAttendingCourseTeacherFromSelectedTeachers(
                    selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

            request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

        } else {
            clearCurrentTeacherForm(inquiryForm);
            request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR);

        }

		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);

        return actionMapping.findForward("fillInquiry");
    }


	public ActionForward submitInquiry(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

        List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers = (List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request
                .getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms = (List<InfoRoomWithInfoInquiriesRoom>) request
                .getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

        // saving the answers of the previous current form
        saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
        List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(
                inquiryForm, attendingCourseTeachers);
        List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(
                inquiryForm, attendingCourseRooms);

        ActionForward forward;

        if (!validateForm(request, inquiryForm)) {
			InfoInquiriesTeacher currentAttendingCourseTeacher =
				getCurrentAttendingCourseTeacherFromSelectedTeachers(selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

            request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

            forward = actionMapping.findForward("fillInquiry");

        } else if (selectedAttendingCourseTeachers.size() == 0) {
			InfoInquiriesTeacher currentAttendingCourseTeacher =
				getCurrentAttendingCourseTeacherFromSelectedTeachers(selectedAttendingCourseTeachers, inquiryForm);

            InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
                    selectedAttendingCourseRooms, inquiryForm);

            request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

            request.setAttribute(InquiriesUtil.NO_ATTENDING_COURSE_TEACHER_FORM_ERROR, true);
			request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR);

            forward = actionMapping.findForward("fillInquiry");

        } else {

			List<InfoClass> attendingCourseSchoolClasses =
				(List<InfoClass>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_SCHOOL_CLASSES);
			List<InfoExecutionDegree> attendingCourseExecutionDegrees =
				(List<InfoExecutionDegree>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES);
            InfoInquiriesCourse infoInquiriesCourse = new InfoInquiriesCourse();
			readStudentAndCourseFormFormToInfoInquiriesCourse(
					inquiryForm, infoInquiriesCourse, attendingCourseSchoolClasses, attendingCourseExecutionDegrees);

            request.setAttribute(InquiriesUtil.INFO_ATTENDING_INQUIRIES_COURSE, infoInquiriesCourse);

            forward = actionMapping.findForward("confirmInquirySubmition");
        }
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);
		request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);

        return forward;

    }

	public ActionForward saveInquiry(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response)  throws Exception {

        DynaActionForm inquiryForm = (DynaActionForm) actionForm;
        loadInitialInformation(request, inquiryForm);

        List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers = (List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes>) request
                .getAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms = (List<InfoRoomWithInfoInquiriesRoom>) request
                .getAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS);

		// saving the answers of the previous current form
		saveCurrentFormToSelectedForm(inquiryForm);

        // obtaining the selected teachers and rooms
        List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = getSelectedAttendingCourseTeachers(
                inquiryForm, attendingCourseTeachers);
        List<InfoInquiriesRoom> selectedAttendingCourseRooms = getSelectedAttendingCourseRooms(
                inquiryForm, attendingCourseRooms);

		if (!validateForm(request, inquiryForm)) {
			InfoInquiriesTeacher currentAttendingCourseTeacher =
				getCurrentAttendingCourseTeacherFromSelectedTeachers(selectedAttendingCourseTeachers, inquiryForm);

			InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
					selectedAttendingCourseRooms, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

			request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);
			request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);

			return actionMapping.findForward("fillInquiry");

		} else if(selectedAttendingCourseTeachers.size() == 0){
			InfoInquiriesTeacher currentAttendingCourseTeacher =
				getCurrentAttendingCourseTeacherFromSelectedTeachers(selectedAttendingCourseTeachers, inquiryForm);

			InfoInquiriesRoom currentAttendingCourseRoom = getCurrentAttendingCourseRoomFromSelectedRooms(
					selectedAttendingCourseRooms, inquiryForm);

			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION,
					inquiryForm.get("currentAttendingCourseTeacherFormPosition"));
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER, currentAttendingCourseTeacher);
			request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM, currentAttendingCourseRoom);

			request.setAttribute(InquiriesUtil.NO_ATTENDING_COURSE_TEACHER_FORM_ERROR, true);
			request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR);
			
			request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS, selectedAttendingCourseRooms);
			request.setAttribute(InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS, selectedAttendingCourseTeachers);

			return actionMapping.findForward("fillInquiry");
		}

		
		List<InfoClass> attendingCourseSchoolClasses =
			(List<InfoClass>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_SCHOOL_CLASSES);
		List<InfoExecutionDegree> attendingCourseExecutionDegrees =
			(List<InfoExecutionDegree>) request.getAttribute(InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES);

        InfoInquiry inquiry = new InfoInquiry();
		readStudentAndCourseFormFormToInfoInquiriesCourse(
				inquiryForm, inquiry.getInquiriesCourse(), attendingCourseSchoolClasses, attendingCourseExecutionDegrees);

        IUserView userView = SessionUtils.getUserView(request);

        InfoStudent infoStudent = (InfoStudent) request.getAttribute(InquiriesUtil.INFO_STUDENT);
		InfoExecutionPeriod executionPeriod = (InfoExecutionPeriod) request.getAttribute(InquiriesUtil.CURRENT_EXECUTION_PERIOD);
		InfoExecutionCourse executionCourse = (InfoExecutionCourse) request.getAttribute(InquiriesUtil.ATTENDING_EXECUTION_COURSE);
		InfoExecutionDegree executionDegreeStudent = (InfoExecutionDegree) request.getAttribute(InquiriesUtil.STUDENT_EXECUTION_DEGREE);

        inquiry.setExecutionPeriod(executionPeriod);
        inquiry.setExecutionCourse(executionCourse);
        inquiry.setExecutionDegreeStudent(executionDegreeStudent);
        inquiry.setInquiriesRoomsList(selectedAttendingCourseRooms);
        inquiry.setInquiriesTeachersList(selectedAttendingCourseTeachers);

        Object[] argsInquiryAndInfoStudent = { inquiry, infoStudent };
		try {
        ServiceUtils.executeService(userView, "inquiries.WriteInquiry", argsInquiryAndInfoStudent);
			request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.submition.ok");

		} catch (FenixServiceException e) {
			request.setAttribute(InquiriesUtil.INQUIRY_MESSAGE_KEY, "message.inquiries.already.evaluated.course");

		}
		
        return actionMapping.findForward("inquirySubmitionResult");
    }


	private List<InfoExecutionDegree> getAttendingCourseExecutionDegrees(
			IUserView userView, InfoFrequenta attends, Integer executionPeriodId)
	throws FenixFilterException, FenixServiceException {
		
		List<InfoExecutionDegree> attendingCourseExecutionDegrees;
        if (attends.getInfoEnrolment() != null) {
			Object[] argsDegreeCPId = { attends.getInfoEnrolment().getInfoCurricularCourse().getInfoDegreeCurricularPlan().getIdInternal() };
			InfoExecutionDegree infoExecutionDegreeCourse = 
				(InfoExecutionDegree) ServiceUtils.executeService(userView, "ReadActiveExecutionDegreebyDegreeCurricularPlanID", argsDegreeCPId);
			attendingCourseExecutionDegrees = new ArrayList<InfoExecutionDegree>(1);
            attendingCourseExecutionDegrees.add(infoExecutionDegreeCourse);

        } else {

            Object[] argsAttendingCourse = { attends.getDisciplinaExecucao() };
			List<InfoCurricularCourse> attendingCourseCurricularCourses =
				(List<InfoCurricularCourse>) ServiceUtils.executeService(userView, "ReadCurricularCourseListOfExecutionCourse", argsAttendingCourse);
			attendingCourseExecutionDegrees = new ArrayList<InfoExecutionDegree>(attendingCourseCurricularCourses.size());
            for (InfoCurricularCourse attendingCurricularCourse : attendingCourseCurricularCourses) {
				Object[] argsDegreeCPId = { attendingCurricularCourse.getInfoDegreeCurricularPlan().getIdInternal() };
				InfoExecutionDegree infoExecutionDegreeCourse = 
					(InfoExecutionDegree) ServiceUtils.executeService(userView, "ReadActiveExecutionDegreebyDegreeCurricularPlanID", argsDegreeCPId);
                attendingCourseExecutionDegrees.add(infoExecutionDegreeCourse);
            }
        }
        return attendingCourseExecutionDegrees;

    }

    private void loadInitialInformation(HttpServletRequest request, DynaActionForm inquiryForm)
            throws FenixFilterException, FenixServiceException, InvalidSessionActionException {

        IUserView userView = SessionUtils.getUserView(request);

        // Obtaining the information on the student
        Object args[] = { userView.getUtilizador() };
		InfoStudent infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "ReadStudentByUsername", args);
        if (infoStudent == null) {
            throw new InvalidSessionActionException();
        }

        // FIXME: THIS SHOULD BE PARAMETRIZABLE!!!!!
        // Obtaining the current execution period
		InfoExecutionPeriod currentExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadCurrentExecutionPeriod", null);

		Integer studentExecutionDegreeId = (Integer) inquiryForm.get("studentExecutionDegreeId");
		InfoExecutionDegree infoExecutionDegreeStudent;
		if(studentExecutionDegreeId == null) {
	        // Obtaining the active student curricular plan
	        Object[] argsStudentNumberDegreeType = { infoStudent.getNumber(), infoStudent.getDegreeType() };
				InfoStudentCurricularPlan infoStudentCurricularPlan =
					(InfoStudentCurricularPlan) ServiceUtils.executeService(userView,
							"student.ReadActiveStudentCurricularPlanByNumberAndDegreeType", argsStudentNumberDegreeType);
	        // Obtaining the student execution degree
			Object[] argsDegreeCPId = { infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getIdInternal() };
			infoExecutionDegreeStudent = 
				(InfoExecutionDegree) ServiceUtils.executeService(userView, "ReadActiveExecutionDegreebyDegreeCurricularPlanID", argsDegreeCPId);

		} else {
			Object[] argsExecutionDegreeId = { studentExecutionDegreeId };
			infoExecutionDegreeStudent = 
				(InfoExecutionDegree) ServiceUtils.executeService(userView, "ReadExecutionDegreeByOID", argsExecutionDegreeId);
			
		}
		
        // Obtaining the selected attends
		Integer attendsId = new Integer((String) InquiriesUtil.getFromRequest(InquiriesUtil.STUDENT_ATTENDS_ID, request));
        Object[] argsAttendsId = { attendsId };
		InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers attends =
			(InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers)
			ServiceUtils.executeService(userView, "student.ReadAttendsByOID", argsAttendsId);

        // Obtaining all School Classes associated with the attending course
        Object[] argsAttendingCourse = { attends.getDisciplinaExecucao() };
		List<InfoClass> attendingCourseSchoolClasses =
			(List<InfoClass>) ServiceUtils.executeService(userView, "ReadClassesByExecutionCourse", argsAttendingCourse);
		//sort by school class name
        InquiriesUtil.removeDuplicates(attendingCourseSchoolClasses);
		Collections.sort(attendingCourseSchoolClasses, new BeanComparator("nome"));

        final InfoExecutionCourse finalCourse = attends.getDisciplinaExecucao();

		List attendingCourseTeachers = new ArrayList(attends.getTeachers().size() + attends.getNonAffiliatedTeachers().size());
		attendingCourseTeachers.addAll(attends.getTeachers());
		attendingCourseTeachers.addAll(attends.getNonAffiliatedTeachers());
		
        CollectionUtils.transform(attendingCourseTeachers, new Transformer() {

            public Object transform(Object infoObject) {
                return new InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes((InfoObject)infoObject, finalCourse);
            }
        });
		//sort by teacher name
		Collections.sort(attendingCourseTeachers, new BeanComparator("teacherName"));

        // Obtaining the rooms associated with the attending course
		List<InfoLesson> attendingClassLessons =
			(List<InfoLesson>) ServiceUtils.executeService(userView, "LerAulasDeDisciplinaExecucao", argsAttendingCourse);
		List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms = new ArrayList<InfoRoomWithInfoInquiriesRoom>();
        for (InfoLesson lesson : attendingClassLessons) {
            if(lesson.getInfoSala() != null) {
                if (!attendingCourseRooms.contains(lesson.getInfoSala())) {
    				attendingCourseRooms.add(new InfoRoomWithInfoInquiriesRoom(lesson.getInfoSala().getRoom()));
                }
            }
        }
		//sort by class room name
		Collections.sort(attendingCourseRooms, new BeanComparator("nome"));

        List<InfoExecutionDegree> attendingCourseExecutionDegrees = getAttendingCourseExecutionDegrees(
                userView, attends, currentExecutionPeriod.getIdInternal());

        inquiryForm.set("studentExecutionDegreeId", infoExecutionDegreeStudent.getIdInternal());
		inquiryForm.set("attendingExecutionCourseId", attends.getDisciplinaExecucao().getIdInternal());

        request.setAttribute(InquiriesUtil.INFO_STUDENT, infoStudent);
        request.setAttribute(InquiriesUtil.CURRENT_EXECUTION_PERIOD, currentExecutionPeriod);
        request.setAttribute(InquiriesUtil.STUDENT_EXECUTION_DEGREE, infoExecutionDegreeStudent);
        request.setAttribute(InquiriesUtil.ATTENDING_EXECUTION_COURSE, attends.getDisciplinaExecucao());
		request.setAttribute(InquiriesUtil.ATTENDING_COURSE_SCHOOL_CLASSES, attendingCourseSchoolClasses);
        request.setAttribute(InquiriesUtil.ATTENDING_COURSE_TEACHERS, attendingCourseTeachers);
        request.setAttribute(InquiriesUtil.ATTENDING_COURSE_ROOMS, attendingCourseRooms);
		request.setAttribute(InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES, attendingCourseExecutionDegrees);
        if (attendingCourseExecutionDegrees.size() == 1) {
			request.setAttribute(InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE, attendingCourseExecutionDegrees.get(0));
        }
    }

    private InfoInquiriesRoom getCurrentAttendingCourseRoomFromSelectedRooms(
            List<InfoInquiriesRoom> selectedAttendingCourseRooms, DynaActionForm inquiryForm) {

        Integer currentAttendingCourseRoomId = (Integer) inquiryForm.get("currentAttendingCourseRoomId");
        // Finding the current attending course room
        InfoInquiriesRoom currentAttendingCourseRoom = null;
        for (InfoInquiriesRoom room : selectedAttendingCourseRooms) {
            if (room.getRoom().getIdInternal().equals(currentAttendingCourseRoomId)) {
                currentAttendingCourseRoom = room;
                break;
            }
        }
        return currentAttendingCourseRoom;
    }

    private InfoInquiriesTeacher getCurrentAttendingCourseTeacherFromSelectedTeachers(
            List<InfoInquiriesTeacher> selectedAttendingCourseTeachers, DynaActionForm inquiryForm) {
		Integer currentAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");

		InfoInquiriesTeacher currentAttendingCourseTeacher = (currentAttendingCourseTeacherFormPosition != null) ?
				selectedAttendingCourseTeachers.get(currentAttendingCourseTeacherFormPosition) : null;

        return currentAttendingCourseTeacher;
    }

    private boolean validateForm(HttpServletRequest request, DynaActionForm inquiryForm) {

		Integer currentAttendingCourseTeacherFormPosition = (Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");
        boolean validCurrentTeacherForm = true;
        if (currentAttendingCourseTeacherFormPosition != null) {
            validCurrentTeacherForm = validateCurrentTeacherForm(inquiryForm);
            if (!validCurrentTeacherForm) {
                request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ERROR, true);
				request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ANCHOR);
            }
        }

        Integer currentAttendingCourseRoomId = (Integer) inquiryForm.get("currentAttendingCourseRoomId");
        boolean validCurrentRoomForm = true;
        if (currentAttendingCourseRoomId != null) {
            validCurrentRoomForm = validateCurrentRoomForm(inquiryForm);
            if (!validCurrentRoomForm) {
                request.setAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM_FORM_ERROR, true);
				request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM_FORM_ANCHOR);
            }
        }

        boolean validCourseForm = validateCourseForm(inquiryForm);
        if (!validCourseForm) {
            request.setAttribute(InquiriesUtil.COURSE_FORM_ERROR, true);
            request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR);
        }

        boolean validStudentForm = validateStudentForm(inquiryForm);
        if (!validStudentForm) {
            request.setAttribute(InquiriesUtil.STUDENT_FORM_ERROR, true);
            request.setAttribute(InquiriesUtil.ANCHOR, InquiriesUtil.STUDENT_FORM_ANCHOR);
        }

        return ((validCourseForm) && (validStudentForm) && (validCurrentTeacherForm) && (validCurrentRoomForm));
    }

    private boolean validateStudentForm(DynaActionForm inquiryForm) {

        Integer curricularYear = (Integer) inquiryForm.get("curricularYear");
        Boolean firstEnrollment = (Boolean) inquiryForm.get("firstEnrollment");

		return ((curricularYear != null) &&
				(firstEnrollment != null));
    }

    private boolean validateCourseForm(DynaActionForm inquiryForm) {

        Double executionCourseQuestion22 = (Double) inquiryForm.get("executionCourseQuestion22");
        Double executionCourseQuestion23 = (Double) inquiryForm.get("executionCourseQuestion23");
        Double executionCourseQuestion24 = (Double) inquiryForm.get("executionCourseQuestion24");
        Double executionCourseQuestion25 = (Double) inquiryForm.get("executionCourseQuestion25");
        Double executionCourseQuestion26 = (Double) inquiryForm.get("executionCourseQuestion26");
        Integer executionCourseQuestion27 = (Integer) inquiryForm.get("executionCourseQuestion27");
        Double executionCourseQuestion28 = (Double) inquiryForm.get("executionCourseQuestion28");

		return ((executionCourseQuestion22 != null) ||
				(executionCourseQuestion23 != null) ||
				(executionCourseQuestion24 != null) ||
				(executionCourseQuestion25 != null) ||
				(executionCourseQuestion26 != null) ||
				(executionCourseQuestion27 != null) ||
				(executionCourseQuestion28 != null));
    }

    private boolean validateCurrentTeacherForm(DynaActionForm inquiryForm) {
		String[] currentAttendingCourseTeacherClassType = (String[]) inquiryForm.get("currentAttendingCourseTeacherClassType");
        boolean validateClassType = currentAttendingCourseTeacherClassType.length > 0;

		Integer currentAttendingCourseTeacherQuestion33 = (Integer) inquiryForm.get("currentAttendingCourseTeacherQuestion33");
		Integer currentAttendingCourseTeacherQuestion34 = (Integer) inquiryForm.get("currentAttendingCourseTeacherQuestion34");
		Double currentAttendingCourseTeacherQuestion35 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion35");
		Double currentAttendingCourseTeacherQuestion36 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion36");
		Double currentAttendingCourseTeacherQuestion37 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion37");
		Double currentAttendingCourseTeacherQuestion38 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion38");
		Double currentAttendingCourseTeacherQuestion39 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion39");
		Double currentAttendingCourseTeacherQuestion310 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion310");
		Double currentAttendingCourseTeacherQuestion311 = (Double) inquiryForm.get("currentAttendingCourseTeacherQuestion311");
		boolean validateAnswers =
			((currentAttendingCourseTeacherQuestion33 != null) ||
					(currentAttendingCourseTeacherQuestion34 != null) ||
					(currentAttendingCourseTeacherQuestion35 != null) ||
					(currentAttendingCourseTeacherQuestion36 != null) ||
					(currentAttendingCourseTeacherQuestion37 != null) ||
					(currentAttendingCourseTeacherQuestion38 != null) ||
					(currentAttendingCourseTeacherQuestion39 != null) ||
					(currentAttendingCourseTeacherQuestion310 != null) ||
					(currentAttendingCourseTeacherQuestion311 != null));

        return (validateAnswers && validateClassType);

    }

    private boolean validateCurrentRoomForm(DynaActionForm inquiryForm) {
		Integer currentAttendingCourseRoomQuestion41 =
			(Integer) inquiryForm.get("currentAttendingCourseRoomQuestion41");
		Integer currentAttendingCourseRoomQuestion42 =
			(Integer) inquiryForm.get("currentAttendingCourseRoomQuestion42");
		Integer currentAttendingCourseRoomQuestion43 =
			(Integer) inquiryForm.get("currentAttendingCourseRoomQuestion43");

		
		return ((currentAttendingCourseRoomQuestion41 != null) ||
				(currentAttendingCourseRoomQuestion42 != null) ||
				(currentAttendingCourseRoomQuestion43 != null));
    }

    private void clearCurrentTeacherForm(DynaActionForm inquiryForm) {
        inquiryForm.set("currentAttendingCourseTeacherFormPosition", null);
        inquiryForm.set("currentAttendingCourseTeacherClassType", ArrayUtils.EMPTY_STRING_ARRAY);
        inquiryForm.set("currentAttendingCourseTeacherQuestion33", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion34", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion35", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion36", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion37", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion38", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion39", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion310", null);
        inquiryForm.set("currentAttendingCourseTeacherQuestion311", null);

    }

    private void clearCurrentRoomForm(DynaActionForm inquiryForm) {
        inquiryForm.set("currentAttendingCourseRoomId", null);
        inquiryForm.set("currentAttendingCourseRoomQuestion41", null);
        inquiryForm.set("currentAttendingCourseRoomQuestion42", null);
        inquiryForm.set("currentAttendingCourseRoomQuestion43", null);
    }

    private void saveCurrentTeacherFormToSelectedTeachersForm(DynaActionForm inquiryForm) {
		Integer position =
			(Integer) inquiryForm.get("currentAttendingCourseTeacherFormPosition");

        if (position == null)
            return;

        // Attending course teacher class types
		String[] currentAttendingCourseTeacherClassType =
			(String[]) inquiryForm.get("currentAttendingCourseTeacherClassType");

		Boolean[] selectedAttendingCourseTeachersClassTypeT =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeT");

		selectedAttendingCourseTeachersClassTypeT[position] =
			ArrayUtils.contains(currentAttendingCourseTeacherClassType, ShiftType.TEORICA.getName());
              
		Boolean[] selectedAttendingCourseTeachersClassTypeP =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeP");
		selectedAttendingCourseTeachersClassTypeP[position] =
			ArrayUtils.contains(currentAttendingCourseTeacherClassType, ShiftType.PRATICA.getName());
			
		Boolean[] selectedAttendingCourseTeachersClassTypeL =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeL");
		selectedAttendingCourseTeachersClassTypeL[position] =
			ArrayUtils.contains(currentAttendingCourseTeacherClassType, ShiftType.LABORATORIAL.getName());

		Boolean[] selectedAttendingCourseTeachersClassTypeTP =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeTP");
		selectedAttendingCourseTeachersClassTypeTP[position] =
			ArrayUtils.contains(currentAttendingCourseTeacherClassType, ShiftType.TEORICO_PRATICA.getName());
		
        // Answers
		Integer currentAttendingCourseTeacherQuestion33 =
			(Integer) inquiryForm.get("currentAttendingCourseTeacherQuestion33");
		Integer[] selectedAttendingCourseTeachersQuestion33 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion33");
        selectedAttendingCourseTeachersQuestion33[position] = currentAttendingCourseTeacherQuestion33;

		Integer currentAttendingCourseTeacherQuestion34 =
			(Integer) inquiryForm.get("currentAttendingCourseTeacherQuestion34");
		Integer[] selectedAttendingCourseTeachersQuestion34 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion34");
        selectedAttendingCourseTeachersQuestion34[position] = currentAttendingCourseTeacherQuestion34;

		Double currentAttendingCourseTeacherQuestion35 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion35");
		Double[] selectedAttendingCourseTeachersQuestion35 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion35");
        selectedAttendingCourseTeachersQuestion35[position] = currentAttendingCourseTeacherQuestion35;

		Double currentAttendingCourseTeacherQuestion36 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion36");
		Double[] selectedAttendingCourseTeachersQuestion36 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion36");
        selectedAttendingCourseTeachersQuestion36[position] = currentAttendingCourseTeacherQuestion36;

		Double currentAttendingCourseTeacherQuestion37 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion37");
		Double[] selectedAttendingCourseTeachersQuestion37 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion37");
        selectedAttendingCourseTeachersQuestion37[position] = currentAttendingCourseTeacherQuestion37;

		Double currentAttendingCourseTeacherQuestion38 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion38");
		Double[] selectedAttendingCourseTeachersQuestion38 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion38");
        selectedAttendingCourseTeachersQuestion38[position] = currentAttendingCourseTeacherQuestion38;

		Double currentAttendingCourseTeacherQuestion39 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion39");
		Double[] selectedAttendingCourseTeachersQuestion39 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion39");
        selectedAttendingCourseTeachersQuestion39[position] = currentAttendingCourseTeacherQuestion39;

		Double currentAttendingCourseTeacherQuestion310 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion310");
		Double[] selectedAttendingCourseTeachersQuestion310 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion310");
        selectedAttendingCourseTeachersQuestion310[position] = currentAttendingCourseTeacherQuestion310;

		Double currentAttendingCourseTeacherQuestion311 =
			(Double) inquiryForm.get("currentAttendingCourseTeacherQuestion311");
		Double[] selectedAttendingCourseTeachersQuestion311 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion311");
        selectedAttendingCourseTeachersQuestion311[position] = currentAttendingCourseTeacherQuestion311;
    }

    private void saveCurrentRoomFormToSelectedRoomsForm(DynaActionForm inquiryForm) {

        // Attending course room Id
		Integer currentAttendingCourseRoomId =
			(Integer) inquiryForm.get("currentAttendingCourseRoomId");

        if (currentAttendingCourseRoomId == null)
            return;

		
		Integer[] selectedAttendingCourseRoomsId =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsId");
        int position = ArrayUtils.indexOf(selectedAttendingCourseRoomsId, currentAttendingCourseRoomId);

        // Answers
		Integer currentAttendingCourseRoomQuestion41 =
			(Integer) inquiryForm.get("currentAttendingCourseRoomQuestion41");
		Integer[] selectedAttendingCourseRoomsQuestion41 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion41");
        selectedAttendingCourseRoomsQuestion41[position] = currentAttendingCourseRoomQuestion41;

		Integer currentAttendingCourseRoomQuestion42 =
			(Integer) inquiryForm.get("currentAttendingCourseRoomQuestion42");
		Integer[] selectedAttendingCourseRoomsQuestion42 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion42");
        selectedAttendingCourseRoomsQuestion42[position] = currentAttendingCourseRoomQuestion42;

		Integer currentAttendingCourseRoomQuestion43 =
			(Integer) inquiryForm.get("currentAttendingCourseRoomQuestion43");
		Integer[] selectedAttendingCourseRoomsQuestion43 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion43");
        selectedAttendingCourseRoomsQuestion43[position] = currentAttendingCourseRoomQuestion43;

    }

    private void saveCurrentFormToSelectedForm(DynaActionForm inquiryForm) {
        saveCurrentRoomFormToSelectedRoomsForm(inquiryForm);
        saveCurrentTeacherFormToSelectedTeachersForm(inquiryForm);
    }

	private List<InfoInquiriesTeacher> getSelectedAttendingCourseTeachers(DynaActionForm inquiryForm, List<InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes> attendingCourseTeachers) {
        // OldBuilding the selected teachers list
		Integer[] selectedAttendingCourseTeachersId = (Integer[]) inquiryForm.get("selectedAttendingCourseTeachersId");
		Boolean[] selectedAttendingCourseTeacherIsAffiliated = (Boolean[]) inquiryForm.get("selectedAttendingCourseTeacherIsAffiliated");
		List<InfoInquiriesTeacher> selectedAttendingCourseTeachers = new ArrayList<InfoInquiriesTeacher>(selectedAttendingCourseTeachersId.length);
        int position = 0;
        for (Integer teacherId : selectedAttendingCourseTeachersId) {
            // Finding the selected teachers
            for (InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacher : attendingCourseTeachers) {
				if((selectedAttendingCourseTeacherIsAffiliated[position] &&
						teacher.getTeacher() != null &&
						teacher.getIdInternal().equals(teacherId)) ||
						(!selectedAttendingCourseTeacherIsAffiliated[position] &&
								teacher.getNonAffiliatedTeacher() != null &&
								teacher.getIdInternal().equals(teacherId))) {

                    InfoInquiriesTeacher infoInquiriesTeacher = new InfoInquiriesTeacher();
                    infoInquiriesTeacher.setTeacherOrNonAffiliatedTeacher(teacher);
					teacher.setHasEvaluations(true);
                    readTeacherFormToInfoInquiriesTeacher(inquiryForm, infoInquiriesTeacher, position);
                    selectedAttendingCourseTeachers.add(infoInquiriesTeacher);
                    break;
                }
            }
            position++;
        }

        return selectedAttendingCourseTeachers;
    }


	private List<InfoInquiriesRoom> getSelectedAttendingCourseRooms(DynaActionForm inquiryForm, List<InfoRoomWithInfoInquiriesRoom> attendingCourseRooms) {
        // OldBuilding the selected rooms list
		Integer[] selectedAttendingCourseRoomsId = (Integer[]) inquiryForm.get("selectedAttendingCourseRoomsId");		
		List<InfoInquiriesRoom> selectedAttendingCourseRooms = new ArrayList<InfoInquiriesRoom>(selectedAttendingCourseRoomsId.length);
        int position = 0;
        for (Integer roomId : selectedAttendingCourseRoomsId) {
            // Finding the selected rooms
			for(InfoRoomWithInfoInquiriesRoom room : attendingCourseRooms) {
                if (room.getIdInternal().equals(roomId.intValue())) {
                    InfoInquiriesRoom infoInquiriesRoom = new InfoInquiriesRoom();
                    infoInquiriesRoom.setRoom(room);
                    readRoomFormToInfoInquiriesRoom(inquiryForm, infoInquiriesRoom, position);
                    selectedAttendingCourseRooms.add(infoInquiriesRoom);
                    break;
                }
            }
            position++;
        }

        return selectedAttendingCourseRooms;
    }

    private void readStudentAndCourseFormFormToInfoInquiriesCourse(DynaActionForm inquiryForm,
            InfoInquiriesCourse infoInquiriesCourse, List<InfoClass> attendingCourseSchoolClasses,
            List<InfoExecutionDegree> attendingCourseExecutionDegrees) {

        // Obtaining the student and course forms information
        Integer curricularYear = (Integer) inquiryForm.get("curricularYear");
        Integer attendingCourseSchoolClassId = (Integer) inquiryForm.get("attendingCourseSchoolClassId");
        Boolean firstEnrollment = (Boolean) inquiryForm.get("firstEnrollment");
		Integer attendingCourseExecutionDegreeId = (Integer) inquiryForm.get("attendingCourseExecutionDegreeId");

        Double executionCourseQuestion22 = (Double) inquiryForm.get("executionCourseQuestion22");
        Double executionCourseQuestion23 = (Double) inquiryForm.get("executionCourseQuestion23");
        Double executionCourseQuestion24 = (Double) inquiryForm.get("executionCourseQuestion24");
        Double executionCourseQuestion25 = (Double) inquiryForm.get("executionCourseQuestion25");
        Double executionCourseQuestion26 = (Double) inquiryForm.get("executionCourseQuestion26");
        Integer executionCourseQuestion27 = (Integer) inquiryForm.get("executionCourseQuestion27");
        Double executionCourseQuestion28 = (Double) inquiryForm.get("executionCourseQuestion28");

        infoInquiriesCourse.setStudentCurricularYear(curricularYear);
		infoInquiriesCourse.setStudentSchoolClass((InfoClass) CollectionUtils.getByInternalId(attendingCourseSchoolClasses, attendingCourseSchoolClassId));
        infoInquiriesCourse.setStudentFirstEnrollment(firstEnrollment ? 1 : 0);
		infoInquiriesCourse.setExecutionDegreeCourse((InfoExecutionDegree) CollectionUtils.getByInternalId(attendingCourseExecutionDegrees, attendingCourseExecutionDegreeId));
        infoInquiriesCourse.setClassCoordination(executionCourseQuestion22);
        infoInquiriesCourse.setStudyElementsContribution(executionCourseQuestion23);
        infoInquiriesCourse.setPreviousKnowledgeArticulation(executionCourseQuestion24);
        infoInquiriesCourse.setContributionForGraduation(executionCourseQuestion25);
        infoInquiriesCourse.setEvaluationMethodAdequation(executionCourseQuestion26);
        infoInquiriesCourse.setWeeklySpentHours(executionCourseQuestion27);
        infoInquiriesCourse.setGlobalAppreciation(executionCourseQuestion28);

    }

	private void readTeacherFormToInfoInquiriesTeacher(
			DynaActionForm inquiryForm, InfoInquiriesTeacher infoInquiriesTeacher, int position) {

		Integer[] selectedAttendingCourseTeachersQuestion33 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion33");

		Integer[] selectedAttendingCourseTeachersQuestion34 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion34");

		Double[] selectedAttendingCourseTeachersQuestion35 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion35");

		Double[] selectedAttendingCourseTeachersQuestion36 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion36");

		Double[] selectedAttendingCourseTeachersQuestion37 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion37");

		Double[] selectedAttendingCourseTeachersQuestion38 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion38");

		Double[] selectedAttendingCourseTeachersQuestion39 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion39");

		Double[] selectedAttendingCourseTeachersQuestion310 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion310");

		Double[] selectedAttendingCourseTeachersQuestion311 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion311");

        // reading the classTypes
		Boolean[] selectedAttendingCourseTeachersClassTypeT =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeT");

		Boolean[] selectedAttendingCourseTeachersClassTypeP =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeP");

		Boolean[] selectedAttendingCourseTeachersClassTypeL =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeL");

		Boolean[] selectedAttendingCourseTeachersClassTypeTP =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeTP");

        if (selectedAttendingCourseTeachersClassTypeT[position]) {
            ShiftType classTypeT = ShiftType.TEORICA;

            infoInquiriesTeacher.getClassTypes().add(classTypeT);
			infoInquiriesTeacher.getTeacherOrNonAffiliatedTeacher().getRemainingClassTypes().remove(classTypeT);
        }

        if (selectedAttendingCourseTeachersClassTypeP[position]) {
            ShiftType classTypeP = ShiftType.PRATICA;
            infoInquiriesTeacher.getClassTypes().add(classTypeP);
			infoInquiriesTeacher.getTeacherOrNonAffiliatedTeacher().getRemainingClassTypes().remove(classTypeP);
        }

        if (selectedAttendingCourseTeachersClassTypeL[position]) {
            ShiftType classTypeL = ShiftType.LABORATORIAL;
            infoInquiriesTeacher.getClassTypes().add(classTypeL);
			infoInquiriesTeacher.getTeacherOrNonAffiliatedTeacher().getRemainingClassTypes().remove(classTypeL);
        }

        if (selectedAttendingCourseTeachersClassTypeTP[position]) {
            ShiftType classTypeTP = ShiftType.TEORICO_PRATICA;
            infoInquiriesTeacher.getClassTypes().add(classTypeTP);
			infoInquiriesTeacher.getTeacherOrNonAffiliatedTeacher().getRemainingClassTypes().remove(classTypeTP);
        }

        infoInquiriesTeacher.setStudentAssiduity(selectedAttendingCourseTeachersQuestion33[position]);
        infoInquiriesTeacher.setTeacherAssiduity(selectedAttendingCourseTeachersQuestion34[position]);
        infoInquiriesTeacher.setTeacherPunctuality(selectedAttendingCourseTeachersQuestion35[position]);
        infoInquiriesTeacher.setTeacherClarity(selectedAttendingCourseTeachersQuestion36[position]);
        infoInquiriesTeacher.setTeacherAssurance(selectedAttendingCourseTeachersQuestion37[position]);
		infoInquiriesTeacher.setTeacherInterestStimulation(selectedAttendingCourseTeachersQuestion38[position]);
        infoInquiriesTeacher.setTeacherAvailability(selectedAttendingCourseTeachersQuestion39[position]);
		infoInquiriesTeacher.setTeacherReasoningStimulation(selectedAttendingCourseTeachersQuestion310[position]);
        infoInquiriesTeacher.setGlobalAppreciation(selectedAttendingCourseTeachersQuestion311[position]);
    }


	private void readRoomFormToInfoInquiriesRoom(
			DynaActionForm inquiryForm, InfoInquiriesRoom infoInquiriesRoom, int position) {

		Integer[] selectedAttendingCourseRoomsQuestion41 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion41");

		Integer[] selectedAttendingCourseRoomsQuestion42 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion42");

		Integer[] selectedAttendingCourseRoomsQuestion43 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion43");
		
        infoInquiriesRoom.setSpaceAdequation(selectedAttendingCourseRoomsQuestion41[position]);
        infoInquiriesRoom.setEnvironmentalConditions(selectedAttendingCourseRoomsQuestion42[position]);
        infoInquiriesRoom.setEquipmentQuality(selectedAttendingCourseRoomsQuestion43[position]);
		infoInquiriesRoom.getRoom().setInquiriesRoom(infoInquiriesRoom);

    }

	private Integer[] removeFromArray(Integer[] array, Integer position) {
        if (position < array.length) {
			Integer[] removedArray = new Integer[array.length - 1];

            for (int i = 0; i < array.length; i++) {
				if(i < position) {
                    removedArray[i] = array[i];
                }
				if(i > position) {
					removedArray[i-1] = array[i];
            }
			}

            return removedArray;
        } 
        return array;
    }

	private Double[] removeFromArray(Double[] array, Integer position) {
        if (position < array.length) {
			Double[] removedArray = new Double[array.length - 1];

            for (int i = 0; i < array.length; i++) {
				if(i < position) {
                    removedArray[i] = array[i];
                }
				if(i > position) {
					removedArray[i-1] = array[i];
            }
			}

            return removedArray;

        }
        return array;
    }

	private Boolean[] removeFromArray(Boolean[] array, Integer position) {
        if (position < array.length) {
			Boolean[] removedArray = new Boolean[array.length - 1];

            for (int i = 0; i < array.length; i++) {
				if(i < position) {
                    removedArray[i] = array[i];
                }
				if(i > position) {
					removedArray[i-1] = array[i];
            }
			}

            return removedArray;

        } 
        return array;
    }

    private void removeTeacherFromSelectedTeachersForm(DynaActionForm inquiryForm, Integer position) {
        if (position == null)
            return;

        // Attending course teacher Id
		Integer[] selectedAttendingCourseTeachersId =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersId");
		inquiryForm.set("selectedAttendingCourseTeachersId",
				removeFromArray(selectedAttendingCourseTeachersId, position));

        // selectedAttendingCourseTeacherIsAffiliated
		Boolean[] selectedAttendingCourseTeacherIsAffiliated =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeacherIsAffiliated");
		inquiryForm.set("selectedAttendingCourseTeacherIsAffiliated",
				removeFromArray(selectedAttendingCourseTeacherIsAffiliated, position));

		Boolean[] selectedAttendingCourseTeachersClassTypeT =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeT");
		inquiryForm.set("selectedAttendingCourseTeachersClassTypeT",
				removeFromArray(selectedAttendingCourseTeachersClassTypeT, position));

		Boolean[] selectedAttendingCourseTeachersClassTypeP =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeP");
		inquiryForm.set("selectedAttendingCourseTeachersClassTypeP",
				removeFromArray(selectedAttendingCourseTeachersClassTypeP, position));

		Boolean[] selectedAttendingCourseTeachersClassTypeL =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeL");
		inquiryForm.set("selectedAttendingCourseTeachersClassTypeL",
				removeFromArray(selectedAttendingCourseTeachersClassTypeL, position));

		Boolean[] selectedAttendingCourseTeachersClassTypeTP =
			(Boolean[]) inquiryForm.get("selectedAttendingCourseTeachersClassTypeTP");
		inquiryForm.set("selectedAttendingCourseTeachersClassTypeTP",
				removeFromArray(selectedAttendingCourseTeachersClassTypeTP, position));

        // Answers
		Integer[] selectedAttendingCourseTeachersQuestion33 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion33");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion33",
				removeFromArray(selectedAttendingCourseTeachersQuestion33, position));

		Integer[] selectedAttendingCourseTeachersQuestion34 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion34");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion34",
				removeFromArray(selectedAttendingCourseTeachersQuestion34, position));

		Double[] selectedAttendingCourseTeachersQuestion35 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion35");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion35",
				removeFromArray(selectedAttendingCourseTeachersQuestion35, position));

		Double[] selectedAttendingCourseTeachersQuestion36 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion36");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion36",
				removeFromArray(selectedAttendingCourseTeachersQuestion36, position));

		Double[] selectedAttendingCourseTeachersQuestion37 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion37");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion37",
				removeFromArray(selectedAttendingCourseTeachersQuestion37, position));

		Double[] selectedAttendingCourseTeachersQuestion38 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion38");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion38",
				removeFromArray(selectedAttendingCourseTeachersQuestion38, position));

		Double[] selectedAttendingCourseTeachersQuestion39 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion39");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion39",
				removeFromArray(selectedAttendingCourseTeachersQuestion39, position));

		Double[] selectedAttendingCourseTeachersQuestion310 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion310");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion310",
				removeFromArray(selectedAttendingCourseTeachersQuestion310, position));

		Double[] selectedAttendingCourseTeachersQuestion311 =
			(Double[]) inquiryForm.get("selectedAttendingCourseTeachersQuestion311");
		inquiryForm.set("selectedAttendingCourseTeachersQuestion311",
				removeFromArray(selectedAttendingCourseTeachersQuestion311, position));

    }

    private void removeRoomFromSelectedRoomForm(DynaActionForm inquiryForm, int position) {

		Integer[] selectedAttendingCourseRoomsId = 
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsId");

        if ((position < 0) || (position > selectedAttendingCourseRoomsId.length))
            return;

        // Attending course room Id
		inquiryForm.set("selectedAttendingCourseRoomsId",
				removeFromArray(selectedAttendingCourseRoomsId, position));

		Integer[] selectedAttendingCourseRoomsQuestion41 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion41");
		inquiryForm.set("selectedAttendingCourseRoomsQuestion41",
				removeFromArray(selectedAttendingCourseRoomsQuestion41, position));

		Integer[] selectedAttendingCourseRoomsQuestion42 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion42");
		inquiryForm.set("selectedAttendingCourseRoomsQuestion42",
				removeFromArray(selectedAttendingCourseRoomsQuestion42, position));

		Integer[] selectedAttendingCourseRoomsQuestion43 =
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsQuestion43");
		inquiryForm.set("selectedAttendingCourseRoomsQuestion43",
				removeFromArray(selectedAttendingCourseRoomsQuestion43, position));

    }

    private int getRoomFormPosition(DynaActionForm inquiryForm, Integer id) {
        if (id == null)
            return -1;

		Integer[] selectedAttendingCourseRoomsId = 
			(Integer[]) inquiryForm.get("selectedAttendingCourseRoomsId");

        return ArrayUtils.indexOf(selectedAttendingCourseRoomsId, id);

    }

    private void updateCurrentTeacherForm(DynaActionForm inquiryForm, InfoInquiriesTeacher attendingCourseTeacher) {

		inquiryForm.set("currentAttendingCourseTeacherQuestion33", attendingCourseTeacher.getStudentAssiduity());
		inquiryForm.set("currentAttendingCourseTeacherQuestion34", attendingCourseTeacher.getTeacherAssiduity());
		inquiryForm.set("currentAttendingCourseTeacherQuestion35", attendingCourseTeacher.getTeacherPunctuality());
		inquiryForm.set("currentAttendingCourseTeacherQuestion36", attendingCourseTeacher.getTeacherClarity());
		inquiryForm.set("currentAttendingCourseTeacherQuestion37", attendingCourseTeacher.getTeacherAssurance());
		inquiryForm.set("currentAttendingCourseTeacherQuestion38", attendingCourseTeacher.getTeacherInterestStimulation());
		inquiryForm.set("currentAttendingCourseTeacherQuestion39", attendingCourseTeacher.getTeacherAvailability());
		inquiryForm.set("currentAttendingCourseTeacherQuestion310", attendingCourseTeacher.getTeacherReasoningStimulation());
		inquiryForm.set("currentAttendingCourseTeacherQuestion311", attendingCourseTeacher.getGlobalAppreciation());

        List<ShiftType> classTypes = attendingCourseTeacher.getClassTypes();
        String[] classTypesArray = new String[classTypes.size()];
        Iterator<ShiftType> iter = classTypes.iterator();
        int i = 0;
        while (iter.hasNext()) {
            classTypesArray[i++] = iter.next().name();
        }

        inquiryForm.set("currentAttendingCourseTeacherClassType", classTypesArray);

    }

	private void updateCurrentRoomForm(DynaActionForm inquiryForm, InfoInquiriesRoom selectedAttendingCourseRoom) {
        inquiryForm.set("currentAttendingCourseRoomId", selectedAttendingCourseRoom.getIdInternal());
		inquiryForm.set("currentAttendingCourseRoomQuestion41", selectedAttendingCourseRoom.getSpaceAdequation());
		inquiryForm.set("currentAttendingCourseRoomQuestion42", selectedAttendingCourseRoom.getEnvironmentalConditions());
		inquiryForm.set("currentAttendingCourseRoomQuestion43", selectedAttendingCourseRoom.getEquipmentQuality());

    }

}