package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "bolonhaManager", path = "/competenceCourses/searchCompetenceCourses")
@Forwards({ @Forward(name = "searchCompetenceCourses", path = "/bolonhaManager/competenceCourses/searchCompetenceCourses.jsp", tileProperties = @Tile(title = "private.bologna.competencecourses.search")) })
public class SearchCompetenceCoursesDA extends FenixDispatchAction {

    public static class SearchCompetenceCourseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String searchName = "";
	private String searchCode = "";

	public SearchCompetenceCourseBean() {
	}

	public SearchCompetenceCourseBean(String searchName, String searchCode) {
	    setSearchName(searchName);
	    setSearchCode(searchCode);
	}

	public String getSearchName() {
	    return searchName;
	}

	public void setSearchName(String searchName) {
	    this.searchName = searchName;
	}

	public void setSearchCode(String searchCode) {
	    this.searchCode = searchCode;
	}

	public String getSearchCode() {
	    return searchCode;
	}
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SearchCompetenceCourseBean searchBean = getOrCreateSearchBean(request);
	String searchName = searchBean.getSearchName();
	String searchCode = searchBean.getSearchCode();
	if ((!searchName.isEmpty()) || (!searchCode.isEmpty())) {
	    request.setAttribute("searchResults", search(searchName, searchCode));
	}
	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("searchCompetenceCourses");
    }

    public ActionForward sortSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String searchName = (String) getFromRequest(request, "searchName");
	String searchCode = (String) getFromRequest(request, "searchCode");
	if ((!searchName.isEmpty()) || (!searchCode.isEmpty())) {
	    request.setAttribute("searchResults", search(searchName, searchCode));
	}

	request.setAttribute("sortBy", getFromRequest(request, "sortBy"));
	request.setAttribute("searchBean", new SearchCompetenceCourseBean(searchName, searchCode));
	return mapping.findForward("searchCompetenceCourses");
    }

    private SearchCompetenceCourseBean getOrCreateSearchBean(HttpServletRequest request) {
	SearchCompetenceCourseBean searchBean = getRenderedObject("searchBean");
	RenderUtils.invalidateViewState();
	if (searchBean == null) {
	    searchBean = new SearchCompetenceCourseBean();
	}
	return searchBean;
    }

    private static Collection<CompetenceCourse> search(String searchName, String searchCode) {
	return CompetenceCourse.searchBolonhaCompetenceCourses(searchName, searchCode);
    }
}