<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.EnrolmentState" %>
  <span class="error"><html:errors/></span>
  <bean:define id="curriculum" name="<%= SessionConstants.CURRICULUM %>" scope="request" />
  <bean:define id="student" name="<%= SessionConstants.STUDENT_CURRICULAR_PLAN %>" scope="request" />
  <table>
  	<tr>
		 <td width="100px"><strong><bean:message key="label.person.name" /></strong></td>
		 <td><bean:write name="student" property="infoStudent.infoPerson.nome"/></td>
  	</tr>
  	<tr>
	  	<td width="100px"><strong><bean:message key="label.degree" />:</strong></td>
	  	<td><bean:write name="student" property="infoDegreeCurricularPlan.infoDegree.nome"/></td>
  	</tr>
  	<tr>
  	 	<td width="100px"><strong><bean:message key="property.number" /></strong></td>
  	 	<td><bean:write name="student" property="infoStudent.number"/><td>
  	</tr>
</table>
  <br />
  <table>
  	<logic:iterate id="enrolment" name="curriculum">
  		<tr>
		  <td>
		    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
		  </td>
		  <td>
		    <bean:write name="enrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
		  </td>
		  <td>
			<logic:notEqual name="enrolment" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
				<bean:message name="enrolment" property="enrolmentState.name" bundle="ENUMERATION_RESOURCES" />
			</logic:notEqual>
			
			<logic:equal name="enrolment" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
				<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
			</logic:equal>

		  </td>
  		</tr>
    </logic:iterate>
  </table>    	
    	
    		
