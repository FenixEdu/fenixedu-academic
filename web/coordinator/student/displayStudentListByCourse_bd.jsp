	<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
 <%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>

<h2><bean:message key="link.coordinator.studentListByCourse.title" /></h2>

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
</logic:present>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>
	
  <bean:define id="enrolmentList" name="enrolment_list" scope="request" />
  <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
  <bean:define id="link">/studentCurriculum.do?method=getCurriculumForCoordinator<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
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
        		<bean:write name="link"/><bean:write name="enrolment" property="infoStudentCurricularPlan.idInternal"/>&amp;
				<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>executionDegreeId=<bean:write name="infoExecutionDegree" property="idInternal"/>&degreeCurricularPlanID=<%=degreeCurricularPlanID%>
	    		</logic:present>
        	</bean:define>
        <tr>
        	<logic:equal name="viewPhoto" value="true">
				<th>
					<bean:define id="personID" name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.idInternal"/>
					<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
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
