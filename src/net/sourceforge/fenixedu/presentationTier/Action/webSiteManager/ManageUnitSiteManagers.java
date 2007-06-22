package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class ManageUnitSiteManagers extends FenixDispatchAction {

    protected abstract UnitSite getSite(HttpServletRequest request);
	
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Site site = getSite(request);
        if (site == null) {
            return mapping.findForward("no-site");
        }
        else {
            request.setAttribute("site", site);
            return super.execute(mapping, actionForm, request, response);
        }
    }

    public ActionForward chooseManagers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
     
        request.setAttribute("managersBean", new UnitSiteManagerBean());
        request.setAttribute("managers", site.getManagers());
        
        return mapping.findForward("chooseManagers");
    }

    public ActionForward removeManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        
        Integer managerId = getId(request.getParameter("managerID"));
        for (Person manager : site.getManagers()) {
            if (manager.getIdInternal().equals(managerId)) {
                try {
                    removeUnitSiteManager(site, manager);
                } catch (DomainException e) {
                    addActionMessage("error", request, e.getKey(), e.getArgs());
                }
            }
        }
        
        return chooseManagers(mapping, actionForm, request, response);
    }

    public ActionForward addManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSiteManagerBean bean = (UnitSiteManagerBean) getRenderedObject("add");
        
        if (bean == null) {
            return chooseManagers(mapping, actionForm, request, response);
        }
        
        Person person = null;
        
        String alias = bean.getAlias();
        if (alias != null) {
            person = Person.readPersonByUsername(alias);
            
            if (person == null) {
                addActionMessage("addError", request, "error.site.managers.add.alias.notFound");
            }
        }
        else {
            String documentId = bean.getIdNumber();
            
            if (documentId != null) {
                ArrayList<Person> persons = new ArrayList<Person>(Person.readByDocumentIdNumber(documentId));
                
                if (persons.isEmpty()) {
                    person = null;
                }
                else {
                    // TODO: show a selection list
                    person = persons.get(new Random().nextInt() % persons.size());
                }
                
                if (person == null) {
                    addActionMessage("addError", request, "error.site.managers.add.idNumber.notFound");
                }
            }
        }

        if (person != null) {
            try {
                UnitSite site = getSite(request);
                
                addUnitSiteManager(site, person);
                RenderUtils.invalidateViewState("add");
            } catch (DomainException e) {
                addActionMessage("addError", request, e.getKey(), e.getArgs());
            }
        }
        
        return chooseManagers(mapping, actionForm, request, response);
    }

    protected Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
	
    /**
     * Remove the given person from the set of managers associated with the given site. This method invokes a service
     * given the site and the person. Only override this method if the service signature is changed otherwise override 
     * {@link #getRemoveServiceName()}.
     */
	protected void removeUnitSiteManager(UnitSite site, Person person) throws FenixFilterException, FenixServiceException {
		executeService(getRemoveServiceName(), site, person);
	}

    /**
     * Add the given person to the set of managers associated with the given site. This method invokes a service
     * given the site and the person. Only override this method if the service signature is changed otherwise override 
     * {@link #getAddServiceName()}.
     */
	protected void addUnitSiteManager(UnitSite site, Person person) throws FenixFilterException, FenixServiceException {
		executeService(getAddServiceName(), site, person);
	}

	/**
	 * @return name of the service to be called by {@link #removeUnitSiteManager(UnitSite, Person)}
	 */
	protected abstract String getRemoveServiceName();

	/**
	 * @return name of the service to be called by {@link #addUnitSiteManager(UnitSite, Person)}
	 */
	protected abstract String getAddServiceName();
    
}
