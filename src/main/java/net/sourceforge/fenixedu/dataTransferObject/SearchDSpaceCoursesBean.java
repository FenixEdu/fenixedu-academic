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
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

public class SearchDSpaceCoursesBean implements Serializable {
    private static final long serialVersionUID = 7504933511981016145L;

    protected static final int pageSize = 15;

    Vector<FileContent> results = null;

    private int page = 1;

    private ExecutionCourse course = null;
    private ExecutionYear executionYear = null;
    private ExecutionSemester executionSemester = null;
    private List<EducationalResourceType> educationalResourceTypes = new ArrayList<>();

    private String searchText = null;

    public int getPageSize() {
        return pageSize;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

//
//    public FileSearchCriteria getSearchCriteria(int start) {
//
//        FileSearchCriteria criteria = new FileSearchCriteria(start, pageSize);
////        List<SearchElement> elements = getSearchElements();
////
////        if (hasSearchElements()) {
////            for (EducationalResourceType type : getEducationalResourceTypes()) {
////                criteria.addExactMatchOrCriteria(SearchField.TYPE, type.getType());
////            }
////            for (SearchElement element : elements) {
////                if (element.getConjunction().equals(ConjunctionType.AND)) {
////                    criteria.addAndCriteria(element.getSearchField(), element.getQueryValue());
////                }
////                if (element.getConjunction().equals(ConjunctionType.OR)) {
////                    criteria.addOrCriteria(element.getSearchField(), element.getQueryValue());
////                }
////            }
////        }
//        return criteria;
//    }
//
//    protected boolean hasSearchElements() {
////        for (SearchElement element : getSearchElements()) {
////            if (element.queryValue.length() > 0) {
////                return true;
////            }
////        }
//        return false;
//    }

    public List<FileContent> getResults() {
        if (results == null) {
            return null;
        }
        return results.subList(0 + ((page - 1) * pageSize), Math.min(results.size(), pageSize + ((page - 1) * pageSize)));
    }

    public int getTotalItems() {
        return results == null ? 0 : results.size();
    }

    public int getNumberOfPages() {
        if (results == null) {
            return 0;
        }
        int numberOfPages = results.size() / getPageSize();
        numberOfPages += (results.size() % getPageSize() != 0) ? 1 : 0;
        return numberOfPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(1, Math.min(getNumberOfPages(), page));
    }

    public List<EducationalResourceType> getEducationalResourceTypes() {
        return educationalResourceTypes;
    }

    public void setEducationalResourceTypes(List<EducationalResourceType> educationalResourceTypes) {
        this.educationalResourceTypes = educationalResourceTypes;
    }

    public ExecutionCourse getCourse() {
        return course;
    }

    public void setCourse(ExecutionCourse course) {
        this.course = course;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionSemester;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public void search() {
        results = new Vector<>();
        page = 1;
        if (course.getExecutionPeriod().equals(executionSemester) && course.getExecutionYear().equals(executionYear)) {
            search(course.getSite());
        } else {
            for (CurricularCourse curricular : course.getAssociatedCurricularCoursesSet()) {
                for (ExecutionCourse execution : curricular.getAssociatedExecutionCoursesSet()) {
                    if (executionSemester == null || execution.getExecutionPeriod().equals(executionSemester)) {
                        if (executionYear == null || execution.getExecutionYear().equals(executionYear)) {
                            search(execution.getSite());
                        }
                    }
                }
            }
        }
    }

    protected void search(Site site) {
        for (Section section : site.getAssociatedSectionSet()) {
            search(section);
        }
    }

    protected void search(Section container) {
        for (Section child : container.getChildrenSections()) {
            search(child);
        }
        for (FileContent file : container.getFileContentSet()) {
            if (!educationalResourceTypes.isEmpty() && !educationalResourceTypes.contains(file.getResourceType())) {
                continue;
            }
            if (!Strings.isNullOrEmpty(searchText) && !file.getFilename().contains(searchText)
                    && !file.getDisplayName().contains(searchText)) {
                continue;
            }
            results.add(file);
        }
    }

    public String getSearchElementsAsParameters() {
        StringBuilder parameters = new StringBuilder();
        if (course != null) {
            parameters.append("&amp;executionCourse=" + course.getExternalId());
        }
        if (executionYear != null) {
            parameters.append("&amp;executionYearId=" + executionYear.getExternalId());
        }
        if (executionSemester != null) {
            parameters.append("&amp;executionPeriodId=" + executionSemester.getExternalId());
        }
        for (EducationalResourceType type : getEducationalResourceTypes()) {
            parameters.append("&amp;type=" + type.toString());
        }
        if (!Strings.isNullOrEmpty(searchText)) {
            parameters.append("&amp;text=" + searchText);
        }
        if (parameters.length() == 0) {
            parameters.append("&amp;");
        }
        return parameters.toString();
    }

    public static SearchDSpaceCoursesBean reconstructFromRequest(HttpServletRequest request) {
        SearchDSpaceCoursesBean bean = new SearchDSpaceCoursesBean();

        String executionCourseId = request.getParameter("executionCourse");
        if (!Strings.isNullOrEmpty(executionCourseId)) {
            bean.setCourse(FenixFramework.<ExecutionCourse> getDomainObject(executionCourseId));
        }

        String executionYearId = request.getParameter("executionYearId");
        if (!Strings.isNullOrEmpty(executionYearId)) {
            bean.setExecutionYear(FenixFramework.<ExecutionYear> getDomainObject(executionYearId));
        }

        String executionPeriodId = request.getParameter("executionPeriodId");
        if (!Strings.isNullOrEmpty(executionPeriodId)) {
            bean.setExecutionPeriod(FenixFramework.<ExecutionSemester> getDomainObject(executionPeriodId));
        }

        List<EducationalResourceType> typesList = new ArrayList<>();
        String[] resourceTypes = request.getParameterValues("type");
        if (resourceTypes != null) {
            for (String type : resourceTypes) {
                typesList.add(EducationalResourceType.valueOf(type));
            }
        }
        bean.setEducationalResourceTypes(typesList);

        String text = request.getParameter("text");
        if (!Strings.isNullOrEmpty(text)) {
            bean.setSearchText(text);
        }

        return bean;
    }
}
