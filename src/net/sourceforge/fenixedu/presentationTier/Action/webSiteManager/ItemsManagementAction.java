package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério 24/Set/2003
 *  
 */
public class ItemsManagementAction extends FenixDispatchAction {

    //****************************************** add item into section
    // *******************************
    public ActionForward prepareAddItem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        InfoWebSite infoWebSite = null;
        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        try {
            Object args[] = { objectCode };
            infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView,
                    "ReadWebSiteBySectionCode", args);
        } catch (NonExistingServiceException e) {
            errors.add("website", new ActionError("error.impossibleReadWebsite"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("infoWebSite", infoWebSite);

        Calendar calendar = Calendar.getInstance();
        DynaValidatorForm itemForm = (DynaValidatorForm) form;
        itemForm.set("creationDate", getDateFormatted(calendar.getTime()));

        return mapping.findForward("addItem");
    }

    public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        MessageResources messages = getResources(request);

        InfoWebSite webSite = null;
        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        request.setAttribute("objectCode", objectCode);
        InfoWebSiteItem infoWebSiteItem = new InfoWebSiteItem();

        fillInfoWebSiteItem(form, infoWebSiteItem);

        ActionErrors errors = new ActionErrors();
        try {
            Object args[] = { objectCode, infoWebSiteItem, userView.getUtilizador() };
            webSite = (InfoWebSite) ServiceUtils.executeService(userView, "AddItem", args);
        } catch (InvalidSituationServiceException e) {
            errors.add("excerpt", new ActionError("error.excerptSize"));
            saveErrors(request, errors);
        } catch (InvalidArgumentsServiceException e) {
            errors.add("notFilled", new ActionError("error.notFilled", messages.getMessage(e
                    .getMessage())));
            saveErrors(request, errors);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoWebSite", webSite);

        if (!errors.isEmpty()) {
            return mapping.getInputForward();
        }

        if (infoWebSiteItem.getPublished() != null
                && infoWebSiteItem.getPublished().equals(Boolean.TRUE)) {
            // build file to send to ist server
            try {
                Object args[] = { objectCode, infoWebSiteItem };
                ServiceUtils.executeService(userView, "SendWebSiteSectionFileToServer", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        return mapping.findForward("sectionPage");
    }

    private void fillInfoWebSiteItem(ActionForm form, InfoWebSiteItem infoWebSiteItem) {
        DynaActionForm itemForm = (DynaActionForm) form;
        infoWebSiteItem.setExcerpt((String) itemForm.get("excerpt"));
        Calendar calendar = Calendar.getInstance();
        String itemBeginDayString = (String) itemForm.get("itemBeginDay");
        if (itemBeginDayString != null && itemBeginDayString.length() > 0) {
            Date date = convertStringDate(itemBeginDayString);
            calendar.setTime(date);
            infoWebSiteItem.setItemBeginDayCalendar(calendar);
        }
        String itemEndDayString = (String) itemForm.get("itemEndDay");
        if (itemEndDayString != null && itemEndDayString.length() > 0) {
            calendar = Calendar.getInstance();
            calendar.setTime(convertStringDate(itemEndDayString));
            infoWebSiteItem.setItemEndDayCalendar(calendar);
        }

        String creationDateString = (String) itemForm.get("creationDate");
        if (creationDateString != null && creationDateString.length() > 0) {
            calendar = Calendar.getInstance();
            calendar.setTime(convertStringDate(creationDateString));
            infoWebSiteItem.setCreationDate(new Timestamp(calendar.getTimeInMillis()));
        }

        infoWebSiteItem.setKeywords((String) itemForm.get("keywords"));
        infoWebSiteItem.setMainEntryText((String) itemForm.get("mainEntryText"));

        infoWebSiteItem.setAuthorName((String) itemForm.get("authorName"));
        infoWebSiteItem.setAuthorEmail((String) itemForm.get("authorEmail"));

        String onlineBeginDayString = (String) itemForm.get("onlineBeginDay");
        infoWebSiteItem.setOnlineBeginDay(convertStringDate(onlineBeginDayString));

        String onlineEndDayString = (String) itemForm.get("onlineEndDay");
        infoWebSiteItem.setOnlineEndDay(convertStringDate(onlineEndDayString));

        if ((String) itemForm.get("publish") != null && ((String) itemForm.get("publish")).length() > 0) {
            infoWebSiteItem.setPublished(new Boolean((String) itemForm.get("publish")));
        }

        infoWebSiteItem.setTitle((String) itemForm.get("title"));
    }

    private Date convertStringDate(String stringToConvert) {
        Date dateString = null;
        if (stringToConvert != null && stringToConvert.length() > 0) {
            String[] dateTokens = stringToConvert.split("/");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, (new Integer(dateTokens[0])).intValue());
            calendar.set(Calendar.MONTH, (new Integer(dateTokens[1])).intValue() - 1);
            calendar.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
            dateString = calendar.getTime();
        }
        return dateString;
    }

    //	*************************** list items belonging to section
    // *******************************
    public ActionForward listItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        InfoWebSite infoWebSite = null;
        final Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        try {
            Object args[] = { objectCode };
            infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView,
                    "ReadWebSiteBySectionCode", args);
        } catch (NonExistingServiceException e) {
            errors.add("website", new ActionError("error.impossibleReadWebsite"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        orderItems(request, infoWebSite, objectCode);

        request.setAttribute("objectCode", objectCode);
        request.setAttribute("infoWebSite", infoWebSite);

        return mapping.findForward("listItems");
    }

    /**
     * @param request
     * @param infoWebSite
     * @param objectCode
     */
    private void orderItems(HttpServletRequest request, InfoWebSite infoWebSite, final Integer objectCode) {
        String orderBy = request.getParameter("orderBy");
        if (orderBy == null) {
            orderBy = (String) request.getAttribute("orderBy");
        }
        if (orderBy != null) {

            InfoWebSiteSection sectionToSort = (InfoWebSiteSection) CollectionUtils.find(infoWebSite
                    .getSections(), new Predicate() {
                public boolean evaluate(Object object) {
                    InfoWebSiteSection infoWebSiteSection = (InfoWebSiteSection) object;
                    if (infoWebSiteSection.getIdInternal().equals(objectCode)) {
                        return true;
                    }
                    return false;
                }
            });
            if (!orderBy.equals("published")) {
                Collections.sort(sectionToSort.getInfoItemsList(), new BeanComparator(orderBy));
            } else {
                List orderedByPublished = new ArrayList();
                CollectionUtils.select(sectionToSort.getInfoItemsList(), new Predicate() {
                    public boolean evaluate(Object object) {
                        InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) object;
                        if (!infoWebSiteItem.getPublished().booleanValue()) {
                            return true;
                        }
                        return false;
                    }
                }, orderedByPublished);
                orderedByPublished.addAll(CollectionUtils.select(sectionToSort.getInfoItemsList(),
                        new Predicate() {
                            public boolean evaluate(Object object) {
                                InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) object;
                                if (infoWebSiteItem.getPublished().booleanValue()) {
                                    return true;
                                }
                                return false;
                            }
                        }));
                sectionToSort.setInfoItemsList(orderedByPublished);
            }
        }
    }

    //	****************************************** delete items from section
    // *******************************
    public ActionForward deleteItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        // get items to delete
        DynaActionForm itemForm = (DynaActionForm) form;
        Integer itemsListSize = Integer.valueOf((String) itemForm.get("itemsListSize"));

        List itemsToDelete = new ArrayList();
        for (int i = 0; i < itemsListSize.intValue(); i++) {
            Integer itemToDelete = itemToDelete(request, i);
            if (itemToDelete != null) {
                itemsToDelete.add(itemToDelete);
            }
        }

        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        try {
            Object args[] = { objectCode, itemsToDelete };
            ServiceUtils.executeService(userView, "DeleteItems", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        try {
            Object args[] = { objectCode, null };
            ServiceUtils.executeService(userView, "SendWebSiteSectionFileToServer", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return listItems(mapping, form, request, response);
    }

    private Integer itemToDelete(HttpServletRequest request, int index) {
        Integer itemToDelete = null;
        String toDelete = request.getParameter("item[" + index + "].toDelete");
        if (request.getParameter("item[" + index + "].itemCode") != null && toDelete != null
                && (toDelete.equals("on") || toDelete.equals("true") || toDelete.equals("yes"))) {
            itemToDelete = new Integer(request.getParameter("item[" + index + "].itemCode"));
        }
        return itemToDelete;
    }

    //	****************************************** edit item belonging to section
    // *******************************
    public ActionForward prepareEditItem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        ActionErrors errors = new ActionErrors();

        InfoWebSite infoWebSite = null;
        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        Integer itemCode = Integer.valueOf(request.getParameter("itemCode"));
        try {
            Object args[] = { objectCode };
            infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView,
                    "ReadWebSiteBySectionCode", args);
        } catch (NonExistingServiceException e) {
            errors.add("website", new ActionError("error.impossibleReadWebsite"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        InfoWebSiteSection infoWebSiteSection = null;
        Iterator iterSections = infoWebSite.getSections().iterator();
        while (iterSections.hasNext()) {
            infoWebSiteSection = (InfoWebSiteSection) iterSections.next();
            if (infoWebSiteSection.getIdInternal().equals(objectCode)) {
                break;
            }
        }
        InfoWebSiteItem infoWebSiteItem = null;
        Iterator iterItems = infoWebSiteSection.getInfoItemsList().iterator();
        while (iterItems.hasNext()) {
            infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
            if (infoWebSiteItem.getIdInternal().equals(itemCode)) {
                break;
            }
        }

        fillForm(form, infoWebSiteItem);

        request.setAttribute("objectCode", objectCode);
        request.setAttribute("itemCode", itemCode);
        request.setAttribute("infoWebSite", infoWebSite);

        return mapping.findForward("editItem");
    }

    /**
     * @param form
     * @param infoWebSiteItem
     */
    private void fillForm(ActionForm form, InfoWebSiteItem infoWebSiteItem) {
        DynaActionForm itemForm = (DynaActionForm) form;

        itemForm.set("title", infoWebSiteItem.getTitle());
        itemForm.set("mainEntryText", infoWebSiteItem.getMainEntryText());
        itemForm.set("excerpt", infoWebSiteItem.getExcerpt());
        itemForm.set("keywords", infoWebSiteItem.getKeywords());
        itemForm.set("authorName", infoWebSiteItem.getAuthorName());
        itemForm.set("authorEmail", infoWebSiteItem.getAuthorEmail());
        if (infoWebSiteItem.getItemBeginDayCalendar() != null) {
            itemForm.set("itemBeginDay", getDateFormatted(infoWebSiteItem.getItemBeginDayCalendar()
                    .getTime()));
        }
        if (infoWebSiteItem.getItemEndDayCalendar() != null) {
            itemForm.set("itemEndDay", getDateFormatted(infoWebSiteItem.getItemEndDayCalendar()
                    .getTime()));
        }
        if (infoWebSiteItem.getOnlineBeginDay() != null) {
            itemForm.set("onlineBeginDay", getDateFormatted(infoWebSiteItem.getOnlineBeginDay()));
        }
        if (infoWebSiteItem.getOnlineEndDay() != null) {
            itemForm.set("onlineEndDay", getDateFormatted(infoWebSiteItem.getOnlineEndDay()));
        }
        // this field should never be null, but just in case we test it
        if (infoWebSiteItem.getCreationDate() != null) {
            itemForm.set("creationDate", getDateFormatted(infoWebSiteItem.getCreationDate()));
        }
        if (infoWebSiteItem.getPublished() != null && infoWebSiteItem.getPublished().booleanValue())
            itemForm.set("publish", "true");
    }

    private String getDateFormatted(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dateFormatted = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        return dateFormatted;
    }

    public ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        MessageResources messages = getResources(request);

        Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
        request.setAttribute("objectCode", objectCode);
        Integer itemCode = Integer.valueOf(request.getParameter("itemCode"));
        request.setAttribute("itemCode", objectCode);
        InfoWebSiteItem infoWebSiteItem = new InfoWebSiteItem();

        fillInfoWebSiteItem(form, infoWebSiteItem);

        infoWebSiteItem.setIdInternal(itemCode);

        ActionErrors errors = new ActionErrors();
        Boolean result = Boolean.FALSE;
        try {
            Object args[] = { objectCode, infoWebSiteItem, userView.getUtilizador() };
            result = (Boolean) ServiceUtils.executeService(userView, "EditWebSiteItem", args);
        } catch (InvalidSituationServiceException e) {
            errors.add("excerpt", new ActionError("error.excerptSize"));
        } catch (InvalidArgumentsServiceException e) {
            errors.add("notFilled", new ActionError("error.notFilled", messages.getMessage(e
                    .getMessage())));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        if (infoWebSiteItem.getPublished() != null
                && infoWebSiteItem.getPublished().equals(Boolean.TRUE)) {

            // if the item being edited modified date whose value is used in
            // section ordering
            // all files must be sent to server
            if (result.booleanValue()) {
                infoWebSiteItem = null;
            }
            // build file to send to ist server
            try {
                Object args[] = { objectCode, infoWebSiteItem };
                ServiceUtils.executeService(userView, "SendWebSiteSectionFileToServer", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        return listItems(mapping, form, request, response);
    }
}