<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
	<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
 <%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="link.coordinator.studentListByCourse.title" /></h2>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
</logic:present>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>
	
  <bean:define id="enrolmentList" name="enrolment_list" scope="request" />
  <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
  <bean:define id="link">/viewStudentCurriculum.do?method=getCurriculumForCoordinator<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
	<p>
		<strong>
			<%= ((List) enrolmentList).size()%> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/>
			<% if (((List) enrolmentList).size() != 0) { %>
		</strong>
	</p>
	
	<p>
		<bean:message key="label.masterDegree.chooseOne"/>
	</p>
        
        <logic:equal name="viewPhoto" value="true">
	        <html:link page="<%= "/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&amp;viewPhoto=false&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
    	    	<bean:message key="label.notViewPhoto"/>
        	</html:link>
        </logic:equal>
        <logic:notEqual name="viewPhoto" value="true">
	        <html:link page="<%= "/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0&amp;viewPhoto=true&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
    	    	<bean:message key="label.viewPhoto"/>
        	</html:link>
        </logic:notEqual>
    
        <table class="tstyle4">
        	<tr>
        		<logic:equal name="viewPhoto" value="true">
					<th><bean:message key="label.photo" /></th>
			 	</logic:equal>
    			<th><bean:message key="label.candidate.number" /></th>
    			<th><bean:message key="label.person.name" /></th>
    			<th><bean:message key="label.masterDegree.administrativeOffice.mark" /></th>
    			
    		</tr>
     	<logic:iterate id="enrolment" name="enrolmentList">
        	<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="enrolment" property="infoStudentCurricularPlan.externalId"/>&amp;
				<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>executionDegreeId=<bean:write name="infoExecutionDegree" property="externalId"/>&degreeCurricularPlanID=<%=degreeCurricularPlanID%>
	    		</logic:present>
        	</bean:define>
        <tr>
        	<logic:equal name="viewPhoto" value="true">
				<th>
					<bean:define id="personID" name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.externalId"/>
					<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
			   </th>
			</logic:equal>
        	<td>
        	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
    			<bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
    		</html:link>
            </td>
            <td>
    	        <bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
    	    </td>
            <td>
    	       	<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.gradeValue"/>
				</logic:equal>
    	    </td>
    	</tr>
        </logic:iterate>
      	</table>    	
   	<% } %>  
