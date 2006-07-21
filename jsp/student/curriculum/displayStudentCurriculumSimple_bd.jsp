<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>

  <span class="error"><html:errors/></span>

  <bean:define id="curriculum" name="<%= SessionConstants.CURRICULUM %>" scope="request" />
  <bean:size id="enrolmentNumber" name="<%= SessionConstants.CURRICULUM %>" scope="request" />
  <bean:define id="student" name="<%= SessionConstants.STUDENT_CURRICULAR_PLAN %>" scope="request" />
  
  
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#FFFFFF" class="infoselected">
				<bean:message key="label.person.name" />:
				<bean:write name="student" property="infoStudent.infoPerson.nome"/>
				<br/>
				<bean:message key="label.degree.name" />:
				<bean:write name="student" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				<br/>
				<bean:message key="label.number" />:
				<bean:write name="student" property="infoStudent.number"/>
			</td>
		</tr>
	</table>

	<br/>
	<br/>
  
  
  <logic:notEqual name="enrolmentNumber" value="0">
	  <table>
	  	<tr>
		  	<th class="listClasses-header">
		  		<bean:message key="label.executionYear" />
		  	</th >
		  	<th class="listClasses-header">
		  		<bean:message key="label.semester" />
		  	</th >
		  	<th class="listClasses-header">
		  		<bean:message key="label.degree.name" />
		  	</th>
		  	<th class="listClasses-header">
		  		<bean:message key="label.curricular.course.name" />
		  	</th>
		  	<th class="listClasses-header">
		  		<bean:message key="label.finalEvaluation" />
		  	</th>
	  	</tr>
	  
	  	<logic:iterate id="enrolment" name="curriculum">
	  		<tr>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
			  </td>
			 
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoExecutionPeriod.semester"/>
			  </td>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
			  </td>
			  <td class="listClasses" style="text-align:left">
			    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
				<% if ( !((InfoEnrolment) enrolment).getEnrollmentTypeResourceKey().equals("option.curricularCourse.normal") ) {%>
					(<bean:message name="enrolment" property="enrollmentTypeResourceKey" bundle="APPLICATION_RESOURCES"/>)
				<% } %>
			  </td>
			  <td class="listClasses">
				<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
				</logic:notEqual>
				
				<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
				</logic:equal>
			  </td>
	  		</tr>
	    </logic:iterate>
	  </table>    	
  </logic:notEqual>
  <logic:equal name="enrolmentNumber" value="0">
		<bean:message key="message.no.enrolments" />
  </logic:equal>
    	
    		
