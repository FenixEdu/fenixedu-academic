/*
 * Created on Dec 17, 2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.gep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class SearchCoursesInformationAction extends SearchAction {

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#doAfterSearch(presentationTier.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest, java.util.Collection)
     */
    protected void doAfterSearch(SearchActionMapping mapping, HttpServletRequest request,
            Collection result) throws Exception {
        final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request
                .getAttribute("infoExecutionDegree");

        // sort the execution course list
        ComparatorChain comparatorChain1 = new ComparatorChain();
        comparatorChain1.addComparator(new Comparator() {

            private List getInfoScopes(List infoCurricularCourses,
                    InfoExecutionDegree infoExecutionDegree) {
                Iterator iter = infoCurricularCourses.iterator();
                List infoScopes = new ArrayList();
                while (iter.hasNext()) {
                    InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iter.next();
                    if (infoExecutionDegree == null
                            || infoExecutionDegree.getInfoDegreeCurricularPlan().equals(
                                    infoCurricularCourse.getInfoDegreeCurricularPlan()))
                        infoScopes.addAll(infoCurricularCourse.getInfoScopes());
                }
                return infoScopes;
            }

            public int compare(Object o1, Object o2) {
                InfoSiteCourseInformation information1 = (InfoSiteCourseInformation) o1;
                InfoSiteCourseInformation information2 = (InfoSiteCourseInformation) o2;
                List infoScopes1 = getInfoScopes(information1.getInfoCurricularCourses(),
                        infoExecutionDegree);
                List infoScopes2 = getInfoScopes(information2.getInfoCurricularCourses(),
                        infoExecutionDegree);
                ComparatorChain comparatorChain2 = new ComparatorChain();
                comparatorChain2.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        return infoScope1.getInfoCurricularSemester().getInfoCurricularYear().getYear()
                                .compareTo(
                                        infoScope2.getInfoCurricularSemester().getInfoCurricularYear()
                                                .getYear());
                    }
                });
                comparatorChain2.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        return infoScope1.getInfoCurricularSemester().getSemester().compareTo(
                                infoScope2.getInfoCurricularSemester().getSemester());
                    }
                });

                comparatorChain2.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                        InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                        if (infoScope1.getInfoBranch().getAcronym() == null
                                || infoScope2.getInfoBranch().getAcronym() == null)
                            return 0;
                        return infoScope1.getInfoBranch().getAcronym().compareTo(
                                infoScope2.getInfoBranch().getAcronym());
                    }
                });
                Collections.sort(infoScopes1, comparatorChain2);
                Collections.sort(infoScopes2, comparatorChain2);
                InfoCurricularCourseScope infoScope1 = null;
                InfoCurricularCourseScope infoScope2 = null;
                if(infoScopes1.isEmpty()){
                    return 0;
                }   
                infoScope1 = (InfoCurricularCourseScope) infoScopes1.get(0);
                if(infoScopes2.isEmpty()){
                    return 0;
                }
                infoScope2 = (InfoCurricularCourseScope) infoScopes2.get(0);
                return comparatorChain2.compare(infoScope1, infoScope2);
            }
        });

        comparatorChain1.addComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                InfoSiteCourseInformation information1 = (InfoSiteCourseInformation) o1;
                InfoSiteCourseInformation information2 = (InfoSiteCourseInformation) o2;

                List infoCurricularCourses1 = getInfoCurricularCourses(information1);
                List infoCurricularCourses2 = getInfoCurricularCourses(information2);

                ComparatorChain comparatorChain = new ComparatorChain();
                comparatorChain.addComparator(new Comparator() {

                    public int compare(Object o1, Object o2) {
                        InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) o1;
                        InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) o2;
                        return infoCurricularCourse1.getName()
                                .compareTo(infoCurricularCourse2.getName());
                    }

                });

                InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) infoCurricularCourses1
                        .get(0);
                InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) infoCurricularCourses2
                        .get(0);

                return comparatorChain.compare(infoCurricularCourse1, infoCurricularCourse2);
            }

            /**
             * @param information1
             * @return
             */
            private List getInfoCurricularCourses(InfoSiteCourseInformation information1) {
                List infoCurricularCourses = information1.getInfoCurricularCourses();
                return (List) CollectionUtils.select(infoCurricularCourses, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) arg0;
                        return infoExecutionDegree == null
                                || infoCurricularCourse.getInfoDegreeCurricularPlan().equals(
                                        infoExecutionDegree.getInfoDegreeCurricularPlan());
                    }
                });
            }
        });
        Collections.sort((List) result, comparatorChain1);

        // collect information for the jsp in a especific, proper data bean
        List infoSiteCoursesInformation = (List) result;
        HashMap statistics = new HashMap();
        Integer numberOfCourses = new Integer(infoSiteCoursesInformation.size());
        List infoCourses = new ArrayList();
        
        Iterator infoSiteCoursesInformationIter = infoSiteCoursesInformation.iterator();
        while (infoSiteCoursesInformationIter.hasNext()) {
            InfoSiteCourseInformation infoSiteCourseInformation = (InfoSiteCourseInformation) infoSiteCoursesInformationIter
                    .next();

            // add info to statistics
            Integer numberOfFields = infoSiteCourseInformation.getNumberOfFieldsFilled();
			if (!statistics.containsKey(numberOfFields))
				statistics.put(numberOfFields, new Integer(1));
			else
			{
				int value = ((Integer) statistics.get(numberOfFields)).intValue();
				value++;
				statistics.put(numberOfFields, new Integer(value));
			}
            
			InfoCourse infoCourse = new InfoCourse();
			// iterate through the courses
            Iterator infoCurricularCourseIterator = infoSiteCourseInformation.getInfoCurricularCourses()
                    .iterator();
            while (infoCurricularCourseIterator.hasNext()) {
                InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) infoCurricularCourseIterator
                        .next();

                // ignore this iteration if the curricular course doesn't refer to the execution degree's degree curricular plan
                if (infoExecutionDegree != null && infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal() != infoCurricularCourse.getInfoDegreeCurricularPlan().getIdInternal())
                	continue;
                
                // set yearSemesterBranch field
                List yearSemesterBranch = new ArrayList();
                Iterator infoCurricularCourseScopeIterator = infoCurricularCourse.getInfoScopes()
                        .iterator();
                while (infoCurricularCourseScopeIterator.hasNext()) {
                    InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoCurricularCourseScopeIterator
                            .next();

                    String year = infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().toString();
                    String semester = infoCurricularCourseScope.getInfoCurricularSemester().getSemester().toString();
                    String branch = infoCurricularCourseScope.getInfoBranch().getAcronym();
                    if(branch == null) branch = new String();
                    String yearSemesterBranchElement = year + " " + semester  + " " + branch;
                    yearSemesterBranch.add(yearSemesterBranchElement);
                }
                infoCourse.setYearSemesterBranch(yearSemesterBranch);

                // set executionCourseID
                infoCourse.setExecutionCourseID(infoSiteCourseInformation.getInfoExecutionCourse().getIdInternal());
                
                // set curricularCourseNameAndCode field
                String curricularCourseNameAndCode = infoCurricularCourse.getName() + " - "
                        + infoCurricularCourse.getCode();
                infoCourse.setCurricularCourseNameAndCode(curricularCourseNameAndCode);

                // set executionCourseCode
                String executionCourseCode = infoSiteCourseInformation.getInfoExecutionCourse()
                        .getSigla();
                infoCourse.setExecutionCourseCode(executionCourseCode);

                // set executionPeriod
                infoCourse.setExecutionPeriod(infoSiteCourseInformation.getInfoExecutionCourse().getInfoExecutionPeriod().getName());
                
                // set basic field
                infoCourse.setBasic(infoCurricularCourse.getBasic().booleanValue());
                
                // set degree curricular plan name
                if(infoExecutionDegree == null)
                    infoCourse.setDegreeCurricularPlanName(infoCurricularCourse.getInfoDegreeCurricularPlan().getName());
                
                // set teachers
                List teachers = new ArrayList();
                Iterator infoLecturingTeachersIter = infoSiteCourseInformation.getInfoLecturingTeachers()
                        .iterator();
                while (infoLecturingTeachersIter.hasNext()) {
                    InfoTeacher infoTeacher = (InfoTeacher) infoLecturingTeachersIter.next();
                    
                    InfoSimpleTeacher teacher = new InfoSimpleTeacher();
                    teacher.setName(infoTeacher.getInfoPerson().getNome());
                    teacher.setResponsible(infoSiteCourseInformation.getInfoResponsibleTeachers().contains(infoTeacher));
                    
                    Object[] args = { infoTeacher };
                    IUserView userView = SessionUtils.getUserView(request);
                    
                    InfoDepartment infoDepartment = (InfoDepartment) ServiceUtils.executeService(userView, "ReadDepartmentByTeacher", args);
                    String department = null;
                    if (infoDepartment == null) {
                        final MessageResources messages = MessageResources.getMessageResources("resources/ApplicationResources");
                        department = messages.getMessage("UNKNOWN");
                    } else {
                        department = infoDepartment.getName();
                    }
                    teacher.setDepartment(department);
                    teachers.add(teacher);
                }
                infoCourse.setTeachers(teachers);
                
                // set numberFieldsFilled
                infoCourse.setNumberFieldsFilled(infoSiteCourseInformation.getNumberOfFieldsFilled());

                // set lastModificationDate; mighty be null
                Date lastModificationDate = infoSiteCourseInformation.getLastModificationDate();
                if(lastModificationDate != null) infoCourse.setLastModificationDate(lastModificationDate.getTime());
            }
            infoCourses.add(infoCourse);
        }

        request.setAttribute("statistics", statistics);
        request.setAttribute("numberOfCourses", numberOfCourses);
        request.setAttribute("infoCourses", infoCourses);
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

        /**
         * @return Returns the executionCourseID.
         */
        public Integer getExecutionCourseID() {
            return executionCourseID;
        }

        /**
         * @param executionCourseID
         *            The executionCourseID to set.
         */
        public void setExecutionCourseID(Integer executionCourseID) {
            this.executionCourseID = executionCourseID;
        }

        /**
         * @return Returns the basic.
         */
        public boolean isBasic() {
            return basic;
        }

        /**
         * @param basic
         *            The basic to set.
         */
        public void setBasic(boolean basic) {
            this.basic = basic;
        }

        /**
         * @return Returns the code.
         */
        public String getExecutionCourseCode() {
            return executionCourseCode;
        }

        /**
         * @param code
         *            The code to set.
         */
        public void setExecutionCourseCode(String executionCourseCode) {
            this.executionCourseCode = executionCourseCode;
        }

        /**
         * @return Returns the executionPeriod.
         */
        public String getExecutionPeriod() {
            return executionPeriod;
        }

        /**
         * @param executionPeriod
         *            The executionPeriod to set.
         */
        public void setExecutionPeriod(String executionPeriod) {
            this.executionPeriod = executionPeriod;
        }

        /**
         * @return Returns the lastModificationDate.
         */
        public long getLastModificationDate() {
            return lastModificationDate;
        }

        /**
         * @param lastModificationDate
         *            The lastModificationDate to set.
         */
        public void setLastModificationDate(long lastModificationDate) {
            this.lastModificationDate = lastModificationDate;
        }

        /**
         * @return Returns the degreeCurricularPlanName.
         */
        public String getDegreeCurricularPlanName() {
            return degreeCurricularPlanName;
        }

        /**
         * @param degreeCurricularPlanName
         *            The degreeCurricularPlanName to set.
         */
        public void setDegreeCurricularPlanName(String degreeCurricularPlanName) {
            this.degreeCurricularPlanName = degreeCurricularPlanName;
        }

        
        /**
         * @return Returns the lecturingTeachers.
         */
        public List getTeachers() {
            return teachers;
        }

        /**
         * @param lecturingTeachers
         *            The lecturingTeachers to set.
         */
        public void setTeachers(List teachers) {
            this.teachers = teachers;
        }

        /**
         * @return Returns the name.
         */
        public String getCurricularCourseNameAndCode() {
            return curricularCourseNameAndCode;
        }

        /**
         * @param name
         *            The name to set.
         */
        public void setCurricularCourseNameAndCode(String curricularCourseNameAndCode) {
            this.curricularCourseNameAndCode = curricularCourseNameAndCode;
        }

        /**
         * @return Returns the numberFieldsFilled.
         */
        public Integer getNumberFieldsFilled() {
            return numberFieldsFilled;
        }

        /**
         * @param numberFieldsFilled
         *            The numberFieldsFilled to set.
         */
        public void setNumberFieldsFilled(Integer numberFieldsFilled) {
            this.numberFieldsFilled = numberFieldsFilled;
        }

        /**
         * @return Returns the yearSemesterBranch.
         */
        public List getYearSemesterBranch() {
            return yearSemesterBranch;
        }

        /**
         * @param yearSemesterBranch
         *            The yearSemesterBranch to set.
         */
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

        /**
         * @return Returns the degreeCurricularPlanName.
         */
        public String getName() {
            return name;
        }

        /**
         * @param degreeCurricularPlanName
         *            The degreeCurricularPlanName to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return Returns the responsible.
         */
        public boolean isResponsible() {
            return responsible;
        }

        /**
         * @param responsible
         *            The responsible to set.
         */
        public void setResponsible(boolean responsible) {
            this.responsible = responsible;
        }
        
        /**
         * @return Returns the department.
         */
        public String getDepartment() {
            return department;
        }

        /**
         * @param department
         *            The department to set.
         */
        public void setDepartment(String department) {
            this.department = department;
        }
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#materializeSearchCriteria(presentationTier.mapping.framework.SearchActionMapping,
     *      javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected void materializeSearchCriteria(SearchActionMapping mapping, HttpServletRequest request,
            ActionForm form) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        if (!request.getParameter("executionDegreeId").equals("all")) {
            Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

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

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#getSearchServiceArgs(javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
    protected Object[] getSearchServiceArgs(HttpServletRequest request, ActionForm form)
            throws Exception {

        Integer executionDegreeId = null;

        if (!request.getParameter("executionDegreeId").equals("all"))
            executionDegreeId = new Integer(request.getParameter("executionDegreeId"));

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

    /*
     * (non-Javadoc)
     * 
     * @see presentationTier.Action.framework.SearchAction#prepareFormConstants(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest,
     *      org.apache.struts.action.ActionForm)
     */
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