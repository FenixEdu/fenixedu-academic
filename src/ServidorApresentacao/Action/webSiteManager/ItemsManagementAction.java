package ServidorApresentacao.Action.webSiteManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoWebSite;
import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Fernanda Quitério
 * 24/Set/2003
 * 
 */
public class ItemsManagementAction extends FenixDispatchAction {

	//****************************************** add item into section *******************************
	public ActionForward prepareAddItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		InfoWebSite infoWebSite = null;
		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		try {
			Object args[] = { objectCode };
			infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteBySectionCode", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("infoWebSite", infoWebSite);

		return mapping.findForward("addItem");
	}

	public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		InfoWebSite webSite = null;
		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		request.setAttribute("objectCode", objectCode);
		InfoWebSiteItem infoWebSiteItem = new InfoWebSiteItem();

		fillInfoWebSiteItem(form, infoWebSiteItem);

		ActionErrors errors = new ActionErrors();
		try {
			Object args[] = { objectCode, infoWebSiteItem, userView.getUtilizador()};
			webSite = (InfoWebSite) ServiceUtils.executeService(userView, "AddItem", args);
		} catch (InvalidSituationServiceException e) {
			errors.add("excerpt", new ActionError("error.excerptSize"));
			saveErrors(request, errors);
		} catch (InvalidArgumentsServiceException e) {
			errors.add("notFilled", new ActionError("error.notFilled"));
			saveErrors(request, errors);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("infoWebSite", webSite);

		if (!errors.isEmpty()) {
			return mapping.getInputForward();
		}

		if (infoWebSiteItem.getPublished() != null && infoWebSiteItem.getPublished().equals(Boolean.TRUE)) {
			// build file to send to ist server
			try {
				Object args[] = { objectCode };
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

		infoWebSiteItem.setKeywords((String) itemForm.get("keywords"));
		infoWebSiteItem.setMainEntryText((String) itemForm.get("mainEntryText"));

		String onlineBeginDayString = (String) itemForm.get("onlineBeginDay");
		infoWebSiteItem.setOnlineBeginDay(convertStringDate(onlineBeginDayString));

		String onlineEndDayString = (String) itemForm.get("onlineEndDay");
		infoWebSiteItem.setOnlineEndDay(convertStringDate(onlineEndDayString));

		infoWebSiteItem.setPublished((Boolean) itemForm.get("publish"));

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

	//	*************************** list items belonging to section *******************************
	public ActionForward listItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		InfoWebSite infoWebSite = null;
		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		try {
			Object args[] = { objectCode };
			infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteBySectionCode", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("infoWebSite", infoWebSite);

		return mapping.findForward("listItems");
	}

	//	****************************************** delete items from section *******************************
	public ActionForward deleteItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {
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
		return listItems(mapping, form, request, response);
	}

	private Integer itemToDelete(HttpServletRequest request, int index) {
		Integer itemToDelete = null;
		String toDelete = request.getParameter("item[" + index + "].toDelete");
		if (request.getParameter("item[" + index + "].itemCode") != null
			&& toDelete != null
			&& (toDelete.equals("on") || toDelete.equals("true") || toDelete.equals("yes"))) {
			itemToDelete = new Integer(request.getParameter("item[" + index + "].itemCode"));
		}
		return itemToDelete;
	}
	//	****************************************** edit item belonging to section *******************************
	public ActionForward prepareEditItem(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		InfoWebSite infoWebSite = null;
		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		Integer itemCode = Integer.valueOf(request.getParameter("itemCode"));
		try {
			Object args[] = { objectCode };
			infoWebSite = (InfoWebSite) ServiceUtils.executeService(userView, "ReadWebSiteBySectionCode", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
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

		DynaActionForm itemForm = (DynaActionForm) form;

		itemForm.set("title", infoWebSiteItem.getTitle());
		itemForm.set("mainEntryText", infoWebSiteItem.getMainEntryText());
		itemForm.set("excerpt", infoWebSiteItem.getExcerpt());
		itemForm.set("keywords", infoWebSiteItem.getKeywords());
		if (infoWebSiteItem.getItemBeginDayCalendar() != null) {
			itemForm.set("itemBeginDay", getDateFormatted(infoWebSiteItem.getItemBeginDayCalendar().getTime()));
		}
		if (infoWebSiteItem.getItemEndDayCalendar() != null) {
			itemForm.set("itemEndDay", getDateFormatted(infoWebSiteItem.getItemEndDayCalendar().getTime()));
		}
		if (infoWebSiteItem.getOnlineBeginDay() != null) {
			itemForm.set("onlineBeginDay", getDateFormatted(infoWebSiteItem.getOnlineBeginDay()));
		}
		if (infoWebSiteItem.getOnlineEndDay() != null) {
			itemForm.set("onlineEndDay", getDateFormatted(infoWebSiteItem.getOnlineEndDay()));
		}
		itemForm.set("publish", infoWebSiteItem.getPublished());

		request.setAttribute("objectCode", objectCode);
		request.setAttribute("itemCode", itemCode);
		request.setAttribute("infoWebSite", infoWebSite);

		return mapping.findForward("editItem");
	}

	private String getDateFormatted(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String dateFormatted =
			String.valueOf(
				calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
		return dateFormatted;
	}

	public ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		IUserView userView = SessionUtils.getUserView(request);

		Integer objectCode = Integer.valueOf(request.getParameter("objectCode"));
		request.setAttribute("objectCode", objectCode);
		Integer itemCode = Integer.valueOf(request.getParameter("itemCode"));
		request.setAttribute("itemCode", objectCode);
		InfoWebSiteItem infoWebSiteItem = new InfoWebSiteItem();

		fillInfoWebSiteItem(form, infoWebSiteItem);

		infoWebSiteItem.setIdInternal(itemCode);

		ActionErrors errors = new ActionErrors();
		try {
			Object args[] = { objectCode, infoWebSiteItem, userView.getUtilizador()};
			ServiceUtils.executeService(userView, "EditWebSiteItem", args);
		} catch (InvalidSituationServiceException e) {
			errors.add("excerpt", new ActionError("error.excerptSize"));
			saveErrors(request, errors);
		} catch (InvalidArgumentsServiceException e) {
			errors.add("notFilled", new ActionError("error.notFilled"));
			saveErrors(request, errors);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (!errors.isEmpty()) {
			return mapping.getInputForward();
		}

		if (infoWebSiteItem.getPublished() != null && infoWebSiteItem.getPublished().equals(Boolean.TRUE)) {
			// build file to send to ist server
			try {
				Object args[] = { objectCode };
				ServiceUtils.executeService(userView, "SendWebSiteSectionFileToServer", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
		}
		return listItems(mapping, form, request, response);
	}
}