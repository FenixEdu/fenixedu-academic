<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>

  <span class="error"><!-- Error messages go here --><html:errors /></span>

  <bean:define id="curriculum" name="<%= PresentationConstants.CURRICULUM %>" scope="request" />
  <bean:size id="enrolmentNumber" name="<%= PresentationConstants.CURRICULUM %>" scope="request" />
  <bean:define id="student" name="<%= PresentationConstants.STUDENT_CURRICULAR_PLAN %>" scope="request" />
  <bean:define id="classification" name="classification" scope="request" />
  
  
  <bean:message key="label.person.name" />
  <bean:write name="student" property="infoStudent.infoPerson.nome"/>
  <br/>
  
  <bean:message key="label.degree.name" />:
  <bean:write name="student" property="infoDegreeCurricularPlan.infoDegree.nome"/>
  <br/>

  <bean:message key="label.number" />
  <bean:write name="student" property="infoStudent.number"/>
  <br/>

  M�dia Aritm�tica: <bean:write name="classification" property="aritmeticClassification"/>
  <br/>
  
  Cadeiras feitas: <bean:write name="classification" property="completedCourses"/>
  <br/>

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
			    <bean:write name="enrolment" property="infoExecutionPeriod.name"/>
			  </td>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>
			  </td>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
			  </td>
			  <td class="listClasses">
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
  </logic:notEqual>
  <logic:equal name="enrolmentNumber" value="0">
		<bean:message key="message.no.enrolments" />
  </logic:equal>
    	
    		
