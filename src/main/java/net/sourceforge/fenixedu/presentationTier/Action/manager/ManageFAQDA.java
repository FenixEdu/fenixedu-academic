/*
 * Created on 2004/08/27
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateFAQEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateFAQSection;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteFAQEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteFAQSection;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadFAQEntries;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadFAQSections;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Crus
 */
@Mapping(module = "publico", path = "/support", input = "/support.do?method=prepare&page=0", scope = "session",
        parameter = "method")
@Forwards(value = { @Forward(name = "Manage", path = "df.page.support-faq") })
public class ManageFAQDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        List infoFAQSections = (List) ReadFAQSections.run();
        request.setAttribute("infoFAQSections", infoFAQSections);

        List infoFAQEntries = (List) ReadFAQEntries.run();
        request.setAttribute("infoFAQEntries", infoFAQEntries);

        List rootInfoFAQSections = new ArrayList(infoFAQSections);
        Map processedSection = new HashMap();
        getSortedInfoFAQSections(rootInfoFAQSections, processedSection, null, 0);
        request.setAttribute("rootInfoFAQSections", rootInfoFAQSections);

        List rootEntries = getSortedInfoFAQEnrtries(infoFAQEntries, processedSection);
        request.setAttribute("rootInfoFAQEntries", rootEntries);

        return mapping.findForward("Manage");
    }

    private List getSortedInfoFAQEnrtries(List infoFAQEntries, Map processedSection) {
        List rootEntries = new ArrayList();

        for (int i = 0; i < infoFAQEntries.size(); i++) {
            InfoFAQEntry infoFAQEntry = (InfoFAQEntry) infoFAQEntries.get(i);
            InfoFAQSection infoFAQSection = infoFAQEntry.getParentSection();
            if (infoFAQSection != null) {
                InfoFAQSection infoFAQParentSection = (InfoFAQSection) processedSection.get(infoFAQSection.getExternalId());
                if (infoFAQParentSection.getEntries() == null) {
                    infoFAQParentSection.setEntries(new ArrayList());
                }
                infoFAQParentSection.getEntries().add(infoFAQEntry);
            } else {
                rootEntries.add(infoFAQEntry);
            }
        }

        return rootEntries;
    }

    private void getSortedInfoFAQSections(List copyOfInfoFAQSections, Map processedSection, List sectionsInPreviousLevel,
            int numberRootSections) {
        List sectionsToRemove = new ArrayList();
        List nextSectionsInPreviousLevel = new ArrayList();
        for (int i = 0; i < copyOfInfoFAQSections.size(); i++) {
            InfoFAQSection infoFAQSection = (InfoFAQSection) copyOfInfoFAQSections.get(i);
            if (sectionsInPreviousLevel == null && infoFAQSection.getParentSection() == null) {
                nextSectionsInPreviousLevel.add(infoFAQSection.getExternalId());
                processedSection.put(infoFAQSection.getExternalId(), infoFAQSection);
            } else if (sectionsInPreviousLevel != null && infoFAQSection.getParentSection() != null
                    && sectionsInPreviousLevel.contains(infoFAQSection.getParentSection().getExternalId())) {
                InfoFAQSection infoFAQParentSection =
                        (InfoFAQSection) processedSection.get(infoFAQSection.getParentSection().getExternalId());
                if (infoFAQParentSection != null) {
                    if (infoFAQParentSection.getSubSections() == null) {
                        infoFAQParentSection.setSubSections(new ArrayList());
                    }
                    infoFAQParentSection.getSubSections().add(infoFAQSection);
                    sectionsToRemove.add(infoFAQSection);
                    nextSectionsInPreviousLevel.add(infoFAQSection.getExternalId());
                    processedSection.put(infoFAQSection.getExternalId(), infoFAQSection);
                }
            }
        }
        if (sectionsInPreviousLevel == null) {
            numberRootSections = nextSectionsInPreviousLevel.size();
        }
        copyOfInfoFAQSections.removeAll(sectionsToRemove);
        if (copyOfInfoFAQSections.size() != numberRootSections) {
            getSortedInfoFAQSections(copyOfInfoFAQSections, processedSection, nextSectionsInPreviousLevel, numberRootSections);
        }
    }

    public ActionForward createFAQSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String parentSectionId = (String) dynaActionForm.get("parentSectionId");
        String sectionName = (String) dynaActionForm.get("sectionName");

        InfoFAQSection infoFAQSection = new InfoFAQSection();
        if (parentSectionId != null && parentSectionId.length() > 0 && StringUtils.isNumeric(parentSectionId)) {
            infoFAQSection.setParentSection(new InfoFAQSection());
            infoFAQSection.getParentSection().setExternalId(parentSectionId);
        }
        infoFAQSection.setSectionName(sectionName);

        CreateFAQSection.run(infoFAQSection);

        return mapping.findForward("Manage");
    }

    public ActionForward deleteFAQSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String sectionIdString = request.getParameter("sectionId");

        if (sectionIdString != null) {
            DeleteFAQSection.run(sectionIdString);
        }

        return mapping.findForward("Manage");
    }

    public ActionForward createFAQEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm dynaActionForm = (DynaActionForm) form;
        String parentSectionId = (String) dynaActionForm.get("parentSectionId");
        String question = (String) dynaActionForm.get("question");
        String answer = (String) dynaActionForm.get("answer");

        InfoFAQSection infoFAQSection = null;
        if (parentSectionId != null && parentSectionId.length() > 0 && StringUtils.isNumeric(parentSectionId)) {
            infoFAQSection = new InfoFAQSection();
            infoFAQSection.setExternalId(parentSectionId);
        }

        InfoFAQEntry infoFAQEntry = new InfoFAQEntry();
        infoFAQEntry.setQuestion(question);
        infoFAQEntry.setAnswer(answer);
        infoFAQEntry.setParentSection(infoFAQSection);

        CreateFAQEntry.run(infoFAQEntry);

        return mapping.getInputForward();
    }

    public ActionForward deleteFAQEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String entryIdString = request.getParameter("entryId");

        if (entryIdString != null) {
            DeleteFAQEntry.run(entryIdString);
        }

        return mapping.getInputForward();
    }

}