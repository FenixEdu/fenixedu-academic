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
  <bean:size id="enrolmentNumber" name="<%= SessionConstants.CURRICULUM %>" scope="request" />
  <bean:define id="student" name="<%= SessionConstants.STUDENT_CURRICULAR_PLAN %>" scope="request" />
  

  <table width="100%">  
	<tr>
		<td align="center">
		  <h4><bean:message key="label.masterDegree.administrativeOffice.information.only" /></h4>
		</td>
	</tr>
  </table>
  
  <bean:message key="label.person.name" />
  <bean:write name="student" property="infoStudent.infoPerson.nome"/>
  </br>
  
  <bean:message key="label.degree.name" />:
  <bean:write name="student" property="infoDegreeCurricularPlan.infoDegree.nome"/>
  </br>

  <bean:message key="label.number" />
  <bean:write name="student" property="infoStudent.number"/>
  </br>
  </br>
  
  
  
  <logic:notEqual name="enrolmentNumber" value="0">
	  <table>
	  	<tr>
		  	<td class="listClasses-header">
		  		<bean:message key="label.executionYear" />
		  	</td >
		  	<td class="listClasses-header">
		  		<bean:message key="label.degree.name" />
		  	</td>
		  	<td class="listClasses-header">
		  		<bean:message key="label.curricular.course.name" />
		  	</td>
		  	<td class="listClasses-header">
		  		<bean:message key="label.finalEvaluation" />
		  	</td>
	  	</tr>
	  
	  	<logic:iterate id="enrolment" name="curriculum">
	  		<logic:notEqual name="enrolment" property="enrolmentState" value="<%= EnrolmentState.ANNULED.toString() %>">
	  		<tr>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
			  </td>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>
			  </td>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
			  </td>
			  <td class="listClasses">	    
					<logic:notEqual name="enrolment" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
						<bean:message name="enrolment" property="enrolmentState.name" bundle="ENUMERATION_RESOURCES" />
					</logic:notEqual>	
				<logic:equal name="enrolment" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
				</logic:equal>
	
			  </td>
	  		</tr>
	  		</logic:notEqual>
	    </logic:iterate>
	  </table>    	
  </logic:notEqual>
  <logic:equal name="enrolmentNumber" value="0">
		<bean:message key="message.no.enrolments" />
  </logic:equal>
    	
    		
