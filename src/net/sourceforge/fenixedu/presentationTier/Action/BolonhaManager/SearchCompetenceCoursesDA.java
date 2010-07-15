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

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "bolonhaManager", path = "/competenceCourses/searchCompetenceCourses")
@Forwards( { @Forward(name = "searchCompetenceCourses", path = "/bolonhaManager/competenceCourses/searchCompetenceCourses.jsp") })
public class SearchCompetenceCoursesDA extends FenixDispatchAction {

    public static class SearchCompetenceCourseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String searchName = "";

	public String getSearchName() {
	    return searchName;
	}

	public void setSearchName(String searchName) {
	    this.searchName = searchName;
	}
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	SearchCompetenceCourseBean searchBean = getOrCreateSearchBean(request);
	if (!searchBean.getSearchName().isEmpty()) {
	    request.setAttribute("searchResults", search(searchBean.getSearchName()));
	}
	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("searchCompetenceCourses");
    }

    private SearchCompetenceCourseBean getOrCreateSearchBean(HttpServletRequest request) {
	SearchCompetenceCourseBean searchBean = (SearchCompetenceCourseBean) getRenderedObject("searchBean");
	if (searchBean == null) {
	    searchBean = new SearchCompetenceCourseBean();
	}
	return searchBean;
    }

    private static Collection<CompetenceCourse> search(String searchName) {
	return CompetenceCourse.searchBolonhaCompetenceCoursesByName(searchName);
    }
}