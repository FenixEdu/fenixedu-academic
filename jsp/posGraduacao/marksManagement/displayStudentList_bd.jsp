<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.EnrolmentState" %>



<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>

   <span class="error"><html:errors/></span>
	<br />

  <bean:define id="studentList" name="studentList" scope="request" />
  <bean:define id="link">/changeMarkDispatchAction.do?method=chooseStudentMarks<%= "&" %>page=0<%= "&" %>studentNumber=</bean:define>
  <p>
    <h3><%= ((List) studentList).size()%> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
    <% if (((List) studentList).size() != 0) { %>
        </p>
        <bean:message key="label.masterDegree.chooseOne"/><br><br><br>
    
        <table>
        	<tr>
    			<td class="listClasses-header"><bean:message key="label.candidate.number" /></td>
    			<td class="listClasses-header"><bean:message key="label.person.name" /></td>
    			<td class="listClasses-header"><bean:message key="label.mark" /></td>
    			<td class="listClasses-header"><bean:message key="label.examDate" /></td>
    			
    		</tr>
     	<logic:iterate id="enrolment" name="studentList">
        	<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
        	</bean:define>
        <tr>
        	<td class="listClasses">
        	<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>
    			<bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
    		</html:link>
            </td>
            <td class="listClasses">
    	        <bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
    	    </td>
            <td class="listClasses">
  				<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>			
    	    </td>
    	    <td class="listClasses">
  				<bean:write name="enrolment" property="infoEnrolmentEvaluation.examDate"/>			
    	    </td>
    	    <td class="listClasses">
  				<bean:write name="enrolment" property="infoEnrolmentEvaluation.gradeAvailableDate"/>			
    	    </td>
    	</tr>
        </logic:iterate>
      	</table>    	
   	<% } %>  
