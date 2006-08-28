package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Ivo Brandão
 */
public class EditSectionDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        InfoSection currentSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
        List all = (List) session.getAttribute(SessionConstants.SECTIONS);
        List allSections = new ArrayList();
        allSections.addAll(all);

        session.removeAttribute(SessionConstants.POSSIBLE_PARENT_SECTIONS);

        //remove parent section, current section and all of it's daughters
        allSections.remove(currentSection.getSuperiorInfoSection());
        allSections.remove(currentSection);

        try {
            allSections = this.removeDaughters(userView, infoSite, currentSection, allSections);
        } catch (FenixActionException fenixActionException) {
            throw fenixActionException;
        }

        session.setAttribute(SessionConstants.POSSIBLE_PARENT_SECTIONS, allSections);

        //relative to children sections
        List sections;
        Object args[] = { infoSite, currentSection.getSuperiorInfoSection() };
        try {
            sections = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSectionsBySiteAndSuperiorSection", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (sections.size() != 0) {
            sections.remove(currentSection);
            Collections.sort(sections);
            session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);
            session.setAttribute(SessionConstants.CHILDREN_SECTIONS, sections);
        } else
            session.removeAttribute(SessionConstants.CHILDREN_SECTIONS);
        //end - relative to children sections

        return mapping.findForward("editSection");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm sectionForm = (DynaValidatorForm) form;
        String sectionName = (String) sectionForm.get("name");
        Integer order = (Integer) sectionForm.get("sectionOrder");
        order = new Integer(order.intValue() - 1);
        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        InfoSection oldSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

        Object readArgs[] = { infoSite };
        List sections;
        if (order.intValue() == -2) {
            //inserir no fim

            try {
                sections = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                        "ReadSections", readArgs);
            } catch (FenixServiceException fenixServiceException) {
                throw new FenixActionException(fenixServiceException.getMessage());
            }
            if (sections != null && sections.size() != 0) {
                order = new Integer(sections.size() - 2);
            } else {
                order = new Integer(0);
            }

        }

        InfoSection newSection = new InfoSection(sectionName, order, infoSite, oldSection
                .getSuperiorInfoSection());

        //perform edition
        Object editionArgs[] = { oldSection, newSection };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditSection", editionArgs);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        //read sections
        try {
            sections = (ArrayList) ServiceManagerServiceFactory.executeService(userView, "ReadSections",
                    readArgs);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        Collections.sort(sections);
        session.setAttribute(SessionConstants.SECTIONS, sections);

        return mapping.findForward("viewSite");
    }

    public ActionForward prepareChangeParent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        return mapping.findForward("changeParent");
    }

    public ActionForward changeParent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        DynaActionForm sectionForm = (DynaValidatorForm) form;
        Integer index = (Integer) sectionForm.get("sectionIndex");
        IUserView userView = getUserView(request);

        List allSections = (List) session.getAttribute(SessionConstants.POSSIBLE_PARENT_SECTIONS);

        InfoSection newParent = null;
        if (index != null)
            newParent = (InfoSection) allSections.get(index.intValue());

        InfoSection oldSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
        InfoSection newSection = new InfoSection(oldSection.getName(), oldSection.getSectionOrder(),
                oldSection.getInfoSite(), newParent);

        //the new order should be after the last child section
        //read child sections for newParent, get the size of the list and use
        // size as section order
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        Object args[] = { infoSite, newParent };
        List sisters = null;
        try {
            sisters = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSectionsBySiteAndSuperiorSection", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        if (sisters != null)
            newSection.setSectionOrder(new Integer(sisters.size()));
        else
            newSection.setSectionOrder(new Integer(0));

        //perform edition
        Object editionArgs[] = { oldSection, newSection };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditSection", editionArgs);
        } catch (ExistingServiceException fenixServiceException) {
            throw new ExistingActionException("Uma Subsecção com este nome e essa seccção pai ",
                    fenixServiceException);
        }

        catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        session.removeAttribute(SessionConstants.INFO_SECTION);
        session.setAttribute(SessionConstants.INFO_SECTION, newSection);
        List sections = null;
        Object readArgs[] = { newSection.getInfoSite() };
        try {
            sections = (List) ServiceManagerServiceFactory.executeService(userView, "ReadSections",
                    readArgs);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        session.removeAttribute(SessionConstants.SECTIONS);
        session.setAttribute(SessionConstants.SECTIONS, sections);

        return mapping.findForward("viewSite");
    }

    private List removeDaughters(IUserView userView, InfoSite infoSite, InfoSection infoSection,
            List allSections) throws FenixActionException, FenixFilterException {

        List sections = new ArrayList();
        Object args[] = { infoSite, infoSection };
        try {
            sections = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadSectionsBySiteAndSuperiorSection", args);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        allSections.removeAll(sections);

        Iterator iterator = sections.iterator();
        while (iterator.hasNext()) {
            InfoSection infoSection2 = (InfoSection) iterator.next();
            allSections = removeDaughters(userView, infoSite, infoSection2, allSections);
        }

        return allSections;
    }
}