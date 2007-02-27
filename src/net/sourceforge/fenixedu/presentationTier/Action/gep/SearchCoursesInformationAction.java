/*
 * Created on Dec 17, 2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.framework.SearchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.mapping.framework.SearchActionMapping;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class SearchCoursesInformationAction extends SearchAction {

    protected void doAfterSearch(SearchActionMapping mapping, HttpServletRequest request, Collection result) {
        final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute("infoExecutionDegree");
        final InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree == null ? null : infoExecutionDegree.getInfoDegreeCurricularPlan();
        
        // sort the result list
        final Comparator comparator = new Comparator() {

            public int compare(Object o1, Object o2) {
                final InfoSiteCourseInformation information1 = (InfoSiteCourseInformation) o1;
                final InfoSiteCourseInformation information2 = (InfoSiteCourseInformation) o2;
                
                final ComparatorChain comparatorChain = new ComparatorChain();
                comparatorChain.addComparator(InfoCurricularCourseScope.COMPARATOR_BY_YEAR_SEMESTER_AND_BRANCH);
                comparatorChain.addComparator(new BeanComparator("infoCurricularCourse.name", Collator.getInstance()));
                
                final SortedSet<InfoCurricularCourseScope> infoScopes1 = getFilteredAndSortedInfoScopes(comparatorChain, information1);
                final SortedSet<InfoCurricularCourseScope> infoScopes2 = getFilteredAndSortedInfoScopes(comparatorChain, information2);

                if (infoScopes1.isEmpty() && infoScopes2.isEmpty()) {
                    return 0;
                } else if (!infoScopes1.isEmpty() && infoScopes2.isEmpty()) {
                    return -1;
                } else if (infoScopes1.isEmpty() && !infoScopes2.isEmpty()) {
                    return 1;
                } else {
                    return comparatorChain.compare(infoScopes1.first(), infoScopes2.first());
                }
            }
        
            private SortedSet<InfoCurricularCourseScope> getFilteredAndSortedInfoScopes(final ComparatorChain comparatorChain, final InfoSiteCourseInformation infoSiteCourseInformation) {
        	final SortedSet<InfoCurricularCourseScope> result = new TreeSet<InfoCurricularCourseScope>(comparatorChain);
                
                for (final InfoCurricularCourse infoCurricularCourse : infoSiteCourseInformation.getInfoCurricularCourses()) {
                    if (infoDegreeCurricularPlan == null || infoDegreeCurricularPlan.equals(infoCurricularCourse.getInfoDegreeCurricularPlan())) {
                	result.addAll(infoCurricularCourse.getInfoScopes());
                    }
                }
                
                return result;
            }

        };
        
        final List<InfoSiteCourseInformation> infoSiteCoursesInformation = (List<InfoSiteCourseInformation>) result;
        Collections.sort(infoSiteCoursesInformation, comparator);

        // collect information for the jsp in a especific, proper data bean
        final HashMap<Integer, Integer> statistics = new HashMap<Integer, Integer>();
        final List<InfoCourse> infoCourses = new ArrayList<InfoCourse>();
        for (final InfoSiteCourseInformation infoSiteCourseInformation : infoSiteCoursesInformation) {
            // add info to statistics
            Integer numberOfFields = infoSiteCourseInformation.getNumberOfFieldsFilled();
            if (!statistics.containsKey(numberOfFields)) {
        	statistics.put(numberOfFields, Integer.valueOf(1));
            } else {
        	int value = ((Integer) statistics.get(numberOfFields)).intValue();
        	value++;
        	statistics.put(numberOfFields, Integer.valueOf(value));
            }
            
            for (final InfoCurricularCourse infoCurricularCourse : infoSiteCourseInformation.getInfoCurricularCourses()) {
        	if (infoDegreeCurricularPlan == null || infoDegreeCurricularPlan.equals(infoCurricularCourse.getInfoDegreeCurricularPlan())) {
        	    final InfoCourse infoCourse = new InfoCourse();
                
        	    // set yearSemesterBranch field
        	    final List<String> yearSemesterBranch = new ArrayList<String>();
        	    for (InfoCurricularCourseScope infoCurricularCourseScope : infoCurricularCourse.getInfoScopes()) {
        		String year = infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().toString();
        		String semester = infoCurricularCourseScope.getInfoCurricularSemester().getSemester().toString();
        		String branch = infoCurricularCourseScope.getInfoBranch().getAcronym();
        		if (branch == null) branch = new String();
        		String yearSemesterBranchElement = year + " " + semester  + " " + branch;
        		yearSemesterBranch.add(yearSemesterBranchElement);
        	    }
        	    infoCourse.setYearSemesterBranch(yearSemesterBranch);

        	    infoCourse.setExecutionCourseID(infoSiteCourseInformation.getInfoExecutionCourse().getIdInternal());
                
        	    // set curricularCourseNameAndCode field
        	    StringBuilder nameAndCode = new StringBuilder();
        	    nameAndCode.append(infoCurricularCourse.getName());
        	    if (infoCurricularCourse.getCode() != null) {
        		nameAndCode.append(" - ").append(infoCurricularCourse.getCode());
        	    }
        	    infoCourse.setCurricularCourseNameAndCode(nameAndCode.toString());

        	    infoCourse.setExecutionCourseCode(infoSiteCourseInformation.getInfoExecutionCourse().getSigla());
        	    infoCourse.setExecutionPeriod(infoSiteCourseInformation.getInfoExecutionCourse().getInfoExecutionPeriod().getName());
        	    infoCourse.setBasic(infoCurricularCourse.getBasic().booleanValue());
        	    if (infoExecutionDegree == null) {
        		infoCourse.setDegreeCurricularPlanName(infoCurricularCourse.getInfoDegreeCurricularPlan().getName());
        	    }
                
        	    // set teachers
        	    final List<InfoSimpleTeacher> teachers = new ArrayList<InfoSimpleTeacher>();
        	    for (final InfoTeacher infoTeacher : infoSiteCourseInformation.getInfoLecturingTeachers()) {
        		final InfoSimpleTeacher teacher = new InfoSimpleTeacher();
        		teacher.setName(infoTeacher.getInfoPerson().getNome());
        		teacher.setResponsible(infoSiteCourseInformation.getInfoResponsibleTeachers().contains(infoTeacher));
                    
        		final InfoDepartment infoDepartment = InfoDepartment.newInfoFromDomain(infoTeacher.getTeacher().getCurrentWorkingDepartment());
        		if (infoDepartment == null) {
        		    final MessageResources messages = MessageResources.getMessageResources("resources/ApplicationResources");
        		    teacher.setDepartment(messages.getMessage("UNKNOWN"));
        		} else {
        		    teacher.setDepartment(infoDepartment.getName());
        		}
                    
        		teachers.add(teacher);
        	    }
        	    infoCourse.setTeachers(teachers);
                
        	    infoCourse.setNumberFieldsFilled(infoSiteCourseInformation.getNumberOfFieldsFilled());

        	    // set lastModificationDate; might be null
        	    Date lastModificationDate = infoSiteCourseInformation.getLastModificationDate();
        	    if (lastModificationDate != null) {
        		infoCourse.setLastModificationDate(lastModificationDate.getTime());
        	    }

        	    infoCourses.add(infoCourse);
                }
            }
        }

        request.setAttribute("statistics", statistics);
        request.setAttribute("infoCourses", infoCourses);
        request.setAttribute("numberOfCourses", infoSiteCoursesInformation.size());        
    }

    public class InfoCourse extends InfoObject {

        private List yearSemesterBranch;

        private Integer executionCourseID;
        
        private String curricularCourseNameAndCode;

        private String executionCourseCode;
        
        private String executionPeriod;

        private boolean basic;
        
        private String degreeCurricularPlanName;

        private List teachers;

        private Integer numberFieldsFilled;

        private long lastModificationDate;

        public InfoCourse() {
        }

        public Integer getExecutionCourseID() {
            return executionCourseID;
        }

        public void setExecutionCourseID(Integer executionCourseID) {
            this.executionCourseID = executionCourseID;
        }

        public boolean isBasic() {
            return basic;
        }

        public void setBasic(boolean basic) {
            this.basic = basic;
        }

        public String getExecutionCourseCode() {
            return executionCourseCode;
        }

        public void setExecutionCourseCode(String executionCourseCode) {
            this.executionCourseCode = executionCourseCode;
        }

        public String getExecutionPeriod() {
            return executionPeriod;
        }

        public void setExecutionPeriod(String executionPeriod) {
            this.executionPeriod = executionPeriod;
        }

        public long getLastModificationDate() {
            return lastModificationDate;
        }

        public void setLastModificationDate(long lastModificationDate) {
            this.lastModificationDate = lastModificationDate;
        }

        public String getDegreeCurricularPlanName() {
            return degreeCurricularPlanName;
        }

        public void setDegreeCurricularPlanName(String degreeCurricularPlanName) {
            this.degreeCurricularPlanName = degreeCurricularPlanName;
        }

        public List getTeachers() {
            return teachers;
        }

        public void setTeachers(List teachers) {
            this.teachers = teachers;
        }

        public String getCurricularCourseNameAndCode() {
            return curricularCourseNameAndCode;
        }

        public void setCurricularCourseNameAndCode(String curricularCourseNameAndCode) {
            this.curricularCourseNameAndCode = curricularCourseNameAndCode;
        }

        public Integer getNumberFieldsFilled() {
            return numberFieldsFilled;
        }

        public void setNumberFieldsFilled(Integer numberFieldsFilled) {
            this.numberFieldsFilled = numberFieldsFilled;
        }

        public List getYearSemesterBranch() {
            return yearSemesterBranch;
        }

        public void setYearSemesterBranch(List yearSemesterBranch) {
            this.yearSemesterBranch = yearSemesterBranch;
        }
        
    }

    public class InfoSimpleTeacher extends InfoObject {

        private String name;

        private boolean responsible;
        
        private String department;

        public InfoSimpleTeacher() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isResponsible() {
            return responsible;
        }

        public void setResponsible(boolean responsible) {
            this.responsible = responsible;
        }
        
        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
        
    }

    
    protected void materializeSearchCriteria(SearchActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        if (!request.getParameter("executionDegreeId").equals("all")) {
            Integer executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));

            Object[] args = { executionDegreeId };
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(
                    userView, "ReadExecutionDegreeByOID", args);
            request.setAttribute("infoExecutionDegree", infoExecutionDegree);
        }

        String basic = request.getParameter("basic");
        if (basic != null && basic.length() > 0)
            request.setAttribute("basic", basic);

        request.setAttribute("executionYear", request.getParameter("executionYear"));
    }

    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
            throws Exception {

        Integer executionDegreeId = null;

        if (!request.getParameter("executionDegreeId").equals("all"))
            executionDegreeId = Integer.valueOf(request.getParameter("executionDegreeId"));

        Boolean basic = null;
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("true")) {
            basic = Boolean.TRUE;
        }
        if ((request.getParameter("basic") != null) && request.getParameter("basic").equals("false")) {
            basic = Boolean.FALSE;
        }

        String executionYear = request.getParameter("executionYear");

        return new Object[] { executionDegreeId, basic, executionYear };
    }


    protected void prepareFormConstants(ActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        String executionYear = request.getParameter("executionYear");

        InfoExecutionYear infoExecutionYear = null;
        try {
            if (executionYear != null) {
                Object[] args = { executionYear };

                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        null, "ReadExecutionYear", args);
            } else {
                infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(userView,
                        "ReadCurrentExecutionYear", new Object[] {});
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        request.setAttribute("executionYear", infoExecutionYear.getYear());

        Object[] args = { executionYear, DegreeType.DEGREE };
        List infoExecutionDegrees = (List) ServiceUtils.executeService(userView,
                "ReadExecutionDegreesByExecutionYearAndDegreeType", args);
        Collections.sort(infoExecutionDegrees, new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) o1;
                InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) o2;
                return infoExecutionDegree1.getInfoDegreeCurricularPlan().getInfoDegree().getNome()
                        .compareTo(
                                infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree()
                                        .getNome());
            }
        });
        MessageResources messageResources = this.getResources(request, "ENUMERATION_RESOURCES");
        //infoExecutionDegrees = buildLabelValueBeans(infoExecutionDegrees);
        infoExecutionDegrees = InfoExecutionDegree.buildLabelValueBeansForList(infoExecutionDegrees, messageResources);

        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        request.setAttribute("showNextSelects", "true");
    }

}
