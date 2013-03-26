package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.SearchDegreeLogBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeLog;
import net.sourceforge.fenixedu.domain.DegreeLog.DegreeLogTypes;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "coordinator", path = "/searchDLog", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "search", path = "/coordinator/viewLogSearch.jsp", tileProperties = @Tile(
        navLocal = "/coordinator/coordinatorMainMenu.jsp", title = "private.coordinator.activityregistry.consult")) })
public class SearchDegreeLogAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.newSetCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final String degreeCurricularPlanOID = newFindDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        final DegreeCurricularPlan degreeCurricularPlan = DegreeCurricularPlan.fromExternalId(degreeCurricularPlanOID);

        final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

        final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);

        Degree degree = executionDegree.getDegree();

        // Degree degree = getDomainObject(request, "degreeCurricularPlanID");
        SearchDegreeLogBean sdlb = new SearchDegreeLogBean(degree);
        sdlb.setDegreeLogTypes(new ArrayList<DegreeLogTypes>());

        request.setAttribute("searchBean", sdlb);
        request.setAttribute("degree", sdlb.getDegree());
        // final InfoExecutionDegree infoExecutionDegree =
        // InfoExecutionDegree.newInfoFromDomain(executionDegree);
        // request.setAttribute(PresentationConstants.MASTER_DEGREE, degree);

        /* Teste */
        /*
         * request.getSession().setAttribute(SetUserViewFilter.
         * USER_SESSION_ATTRIBUTE,
         * degree.getCoordinators().get(0).getTeacher().getPerson());
         */

        return mapping.findForward("search");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Degree degree = getDomainObject(request, "degreeCurricularPlanID");
        SearchDegreeLogBean sdlb = readSearchBean(request, degree);

        searchLogs(sdlb);
        request.setAttribute("searchBean", sdlb);
        request.setAttribute("degree", sdlb.getDegree());

        prepareAttendsCollectionPages(request, sdlb, sdlb.getDegree());
        return mapping.findForward("search");
    }

    private SearchDegreeLogBean readSearchBean(HttpServletRequest request, Degree degree) {
        String degreeID = request.getParameter("degree");
        Integer degreeIDInteger = (degreeID == null) ? null : Integer.parseInt(degreeID);
        if (degreeIDInteger != null) {
            @SuppressWarnings("deprecation")
            SearchDegreeLogBean sdlb = new SearchDegreeLogBean(rootDomainObject.readDegreeByOID(degreeIDInteger));

            String viewPhoto = request.getParameter("viewPhoto");
            if (viewPhoto != null && viewPhoto.equalsIgnoreCase("true")) {
                sdlb.setViewPhoto(true);
            } else {
                sdlb.setViewPhoto(false);
            }

            String logTypes = request.getParameter("degreeLogTypes");
            if (logTypes != null) {
                List<DegreeLogTypes> list = new ArrayList<DegreeLogTypes>();
                for (String logType : logTypes.split(":")) {
                    list.add(DegreeLogTypes.valueOf(logType));
                }
                sdlb.setDegreeLogTypes(list);
            }

            return sdlb;
        } else {
            return new SearchDegreeLogBean(degree);
        }
    }

    private void prepareAttendsCollectionPages(HttpServletRequest request, SearchDegreeLogBean seclb, Degree degree) {
        Collection<DegreeLog> dLogs = seclb.getDegreeLogs();
        List<DegreeLog> listDLogs = new ArrayList<DegreeLog>(dLogs);
        Collections.sort(listDLogs, DegreeLog.COMPARATOR_BY_WHEN_DATETIME);
        int entriesPerPage = 20;
        final CollectionPager<DegreeLog> pager = new CollectionPager<DegreeLog>(listDLogs, entriesPerPage);
        request.setAttribute("numberOfPages", (listDLogs.size() / entriesPerPage) + 1);

        final String pageParameter = request.getParameter("pageNumber");
        final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
        request.setAttribute("pageNumber", page);

        SearchDegreeLogBean logPagesBean = new SearchDegreeLogBean(degree);

        searchLogs(logPagesBean);

        logPagesBean.setDegreeLogs(pager.getPage(page));
        if (seclb.getViewPhoto()) {
            logPagesBean.setViewPhoto(true);
        }
        request.setAttribute("logPagesBean", logPagesBean);
    }

    private void searchLogs(SearchDegreeLogBean bean) {
        final Predicate<DegreeLog> filter = bean.getFilters();
        final Collection<DegreeLog> validLogs = new HashSet<DegreeLog>();
        for (final DegreeLog log : bean.getDegree().getDegreeLogs()) {
            if (filter.eval(log)) {
                validLogs.add(log);
            }
        }
        bean.setDegreeLogs(validLogs);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("degreeCurricularPlanID", request.getAttribute("degreeCurricularPlanID"));

        SearchDegreeLogBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();
        searchLogs(bean);

        request.setAttribute("searchBean", bean);
        request.setAttribute("degree", bean.getDegree());

        prepareAttendsCollectionPages(request, bean, bean.getDegree());

        return mapping.findForward("search");
    }

    private static String newFindDegreeCurricularPlanID(HttpServletRequest request) {
        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        if (degreeCurricularPlanID == null) {
            degreeCurricularPlanID = (String) request.getAttribute("degreeCurricularPlanID");
        }
        return degreeCurricularPlanID;
    }

}
