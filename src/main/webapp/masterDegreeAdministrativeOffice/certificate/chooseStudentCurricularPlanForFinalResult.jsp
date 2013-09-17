<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><bean:message key="label.certificate.finalResult.create" /></h2>
<br/>

<logic:present name="registrations">
	<logic:iterate id="registration" name="registrations">
		<strong>
			<bean:write name="registration" property="student.person.name"/>
			 (<bean:write name="registration" property="student.number"/>) - 
		</strong>
		<bean:write name="registration" property="degree.name"/>
		 (<bean:write name="registration" property="number"/>)
		<br/>
		<logic:iterate id="studentCurricularPlan" name="registration" property="studentCurricularPlans">
			<bean:define id="studentCurricularPlanID" name="studentCurricularPlan" property="externalId" />
			- <html:link page="<%= "/chooseFinalResultInfoAction.do?method=chooseFinal&amp;studentCurricularPlanID=" + studentCurricularPlanID %>" ><bean:write name="studentCurricularPlan" property="name"/></html:link>
			<br/>
		</logic:iterate>
		<br/>
	</logic:iterate>

</logic:present>
