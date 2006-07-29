<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
</logic:present>
<span class="error"><html:errors/></span>
<br />
<logic:present name="<%=SessionConstants.ENROLMENT_LIST%>">
	<bean:define id="enrolmentList" name="<%=SessionConstants.ENROLMENT_LIST%>" scope="request" />
	<logic:iterate id="enrolmentElem" name="enrolmentList" length="1">
		<table>
		  	<tr>
		  		<td>
					<strong><bean:message key="label.degree" /></strong>
		  		</td>
		  		<td>
		  			<bean:write name="enrolmentElem" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/> - 
		  			<bean:write name="enrolmentElem" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
		  		</td>
		  	</tr>
		  	<tr>
		  		<td>
		  			<strong><bean:message key="property.aula.disciplina" /></strong>
		  		</td>
		  		<td>
		  			<bean:write name="enrolmentElem" property="infoCurricularCourse.name"/>
		  		</td>
		  	</tr>
		</table>
	</logic:iterate>

	<bean:define id="link">/studentCurriculum.do?method=getCurriculum<%= "&" %>page=0<%= "&" %>studentCPID=</bean:define>
	<bean:size id="enrolmentsSize" name="enrolmentList" />
	<p>
		<h3><bean:write name="enrolmentsSize"/> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
	</p>
	<logic:notEqual name="enrolmentsSize" value="0">
		<bean:message key="label.masterDegree.chooseOne"/><br/><br/><br/>
		<table>
			<tr>
				<th class="listClasses-header"><bean:message key="label.candidate.number" /></th>
		    	<th class="listClasses-header"><bean:message key="label.person.name" /></th>
		    	<th class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.mark" /></th>
		    </tr>
		    <logic:iterate id="enrolment" name="enrolmentList">
		     	<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.ANNULED.toString() %>">
		        	<bean:define id="studentLink">
		        		<bean:write name="link"/><bean:write name="enrolment" property="infoStudentCurricularPlan.idInternal"/>
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
			    	       	<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
								<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
							</logic:notEqual>
							
							<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
								<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
							</logic:equal>
			    	    </td>
			    	</tr>
		    	</logic:notEqual>
		   	</logic:iterate>
		</table>    	
	</logic:notEqual>  
</logic:present>
	