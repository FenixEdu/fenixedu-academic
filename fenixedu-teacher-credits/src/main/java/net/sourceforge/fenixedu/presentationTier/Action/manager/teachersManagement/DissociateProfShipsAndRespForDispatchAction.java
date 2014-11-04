/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager.teachersManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.manager.teachersManagement.DissociateProfessorShipsAndResponsibleFor;
import org.fenixedu.academic.dto.InfoProfessorship;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author Fernanda Quitério 16/Dez/2003
 * 
 */
@Mapping(module = "manager", path = "/dissociateProfShipsAndRespFor",
        input = "/dissociateProfShipsAndRespFor.do?method=prepareDissociateEC&page=0", formBean = "teacherManagementForm",
        functionality = TeachersManagementAction.class)
@Forwards(value = {
        @Forward(name = "prepareDissociateEC", path = "/manager/teachersManagement/prepareDissociateEC.jsp"),
        @Forward(name = "prepareDissociateECShowProfShipsAndRespFor",
                path = "/manager/teachersManagement/prepareDissociateECShowProfShipsAndRespFor.jsp") })
public class DissociateProfShipsAndRespForDispatchAction extends FenixDispatchAction {
    public ActionForward prepareDissociateEC(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("prepareDissociateEC");
    }

    public ActionForward prepareDissociateECShowProfShipsAndRespFor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        DynaActionForm teacherIdForm = (DynaActionForm) form;

        // Integer teacherNumber = Integer.valueOf((String)
        // teacherNumberForm.get("teacherNumber"));
        String personId = (String) teacherIdForm.get("teacherId");
        // InfoTeacher infoTeacher = null;
        Person person = null;

        // infoTeacher = (InfoTeacher)
        // ReadInfoTeacherByTeacherNumber.run(teacherNumber);
        person = Person.readPersonByUsername(personId);

        if (person == null) {
            addErrorMessage(request, "chosenTeacher", "error.manager.teachersManagement.noTeacher", personId);
            return mapping.getInputForward();
        }

        request.setAttribute("person", person);
        return mapping.findForward("prepareDissociateECShowProfShipsAndRespFor");
    }

    public ActionForward dissociateProfShipsAndRespFor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm teacherForm = (DynaActionForm) form;
        String personNumber = (String) teacherForm.get("teacherId");
        Integer professorshipsListSize = (Integer) teacherForm.get("professorshipsListSize");
        Integer responsibleForListSize = (Integer) teacherForm.get("responsibleForListSize");

        List professorshipsToDelete =
                getInformationToDissociate(request, professorshipsListSize, "professorship", "externalId", "toDelete");
        List responsibleForsToDelete =
                getInformationToDissociate(request, responsibleForListSize, "responsibleFor", "externalId", "toDelete");

        ActionErrors errors = new ActionErrors();
        HashMap professorshipsNotRemoved = null;

        try {
            professorshipsNotRemoved =
                    (HashMap) DissociateProfessorShipsAndResponsibleFor.run(personNumber, professorshipsToDelete,
                            responsibleForsToDelete);

        } catch (NonExistingServiceException e) {
            if (e.getMessage().equals("noTeacher")) {
                addErrorMessage(request, "chosenTeacher", "error.manager.teachersManagement.noPerson", personNumber);
            } else {
                throw new NonExistingActionException("");
            }
        } catch (FenixServiceException e) {
            if (e.getMessage().equals("nullTeacherNumber")) {
                addErrorMessage(request, "nullCode", "error.manager.teachersManagement.noPersonNumber");
            } else if (e.getMessage().equals("nullPSNorRF")) {
                addErrorMessage(request, "nullPSNorRF", "error.manager.teachersManagement.nullPSNorRF", personNumber);
            } else if (e.getMessage().equals("notPSNorRFTeacher")) {
                addErrorMessage(request, "notPSNorRFTeacher", "error.manager.teachersManagement.notPSNorRFPerson");
            } else {
                throw new FenixActionException(e);
            }
        }
        /*
         * if (!errors.isEmpty()) { return mapping.getInputForward(); }
         */

        if (professorshipsNotRemoved != null && professorshipsNotRemoved.size() > 0) {
            errors =
                    createErrors(request, professorshipsNotRemoved, "supportLessons", "PSWithSL",
                            "error.manager.teachersManagement.PSWithSL", errors);
            errors =
                    createErrors(request, professorshipsNotRemoved, "shifts", "PSWithS",
                            "error.manager.teachersManagement.PSWithS", errors);
            return prepareDissociateECShowProfShipsAndRespFor(mapping, form, request, response);
        }
        // must only set this to null when we're certain of not returning to the
        // previous form
        teacherForm.set("teacherId", null);

        return prepareDissociateEC(mapping, form, request, response);
    }

    private ActionErrors createErrors(HttpServletRequest request, HashMap hash, String hashKey, String errorKey, String message,
            ActionErrors errors) {
        List professorships = (List) hash.get(hashKey);

        if (professorships != null) {
            Iterator iterProfessorships = professorships.iterator();
            while (iterProfessorships.hasNext()) {
                InfoProfessorship infoProfessorship = (InfoProfessorship) iterProfessorships.next();
                addErrorMessage(request, errorKey, message, infoProfessorship.getInfoExecutionCourse().getNome());
            }
        }
        return errors;
    }

    private List getInformationToDissociate(HttpServletRequest request, Integer professorshipsListSize, String what,
            String property, String formProperty) {
        List informationToDeleteList = new ArrayList();
        for (int i = 0; i < professorshipsListSize.intValue(); i++) {
            Integer informationToDelete = dataToDelete(request, i, what, property, formProperty);
            if (informationToDelete != null) {
                informationToDeleteList.add(informationToDelete);
            }
        }
        return informationToDeleteList;
    }

    private Integer dataToDelete(HttpServletRequest request, int index, String what, String property, String formProperty) {
        Integer itemToDelete = null;
        String checkbox = request.getParameter(what + "[" + index + "]." + "responsibleFor");
        String toDelete = null;
        if (checkbox != null && (checkbox.equals("on") || checkbox.equals("yes") || checkbox.equals("true"))) {
            toDelete = request.getParameter(what + "[" + index + "]." + property);
        }
        if (toDelete != null) {
            itemToDelete = new Integer(toDelete);
        }
        return itemToDelete;
    }
}