<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.title"/></h2>
<br/>

<logic:notEmpty name="registrations">
	<html:form action="/prepareStudentForMasterDegreeThesisAndProof?method=chooseSCP">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		
		<logic:iterate id="registration" name="registrations">
			<strong>
				<bean:write name="registration" property="student.person.name"/>
				 (<bean:write name="registration" property="student.number"/>) - 
			</strong>
			<bean:write name="registration" property="degree.name"/>
			 (<bean:write name="registration" property="number"/>)
			<br/>
			<logic:iterate id="studentCurricularPlan" name="registration" property="studentCurricularPlans">
				<html:radio property="scpID" value="idInternal" idName="studentCurricularPlan"/>
				<bean:write name="studentCurricularPlan" property="name"/>
				<logic:notEmpty name="studentCurricularPlan" property="masterDegreeThesis">
					<strong><bean:message key="label.masterDegree.administrativeOffice.thesis.hasThesis"/></strong>
				</logic:notEmpty>
				<br/>
			</logic:iterate>
			<br/>
		</logic:iterate>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/>
	</html:form>
</logic:notEmpty> 
<logic:empty name="registrations">
	
</logic:empty>