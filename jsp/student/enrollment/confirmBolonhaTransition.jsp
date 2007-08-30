<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message key="label.enrollment.courses.confirmBolonhaTransition" bundle="STUDENT_RESOURCES"/></h2>

<div class="warning0 mbottom1" style="padding: 0.5em;">
	<strong><bean:message  key="label.attention.nonCaps" bundle="STUDENT_RESOURCES"/>:</strong><br/>
	<bean:message  key="label.enrollment.courses.confirmBolonhaTransition.warning1" bundle="STUDENT_RESOURCES"/>
</div>

<table class="tstyle2">
	<tr>
		<td><strong><bean:message  key="label.enrollment.courses.preBolonhaRegistration" bundle="STUDENT_RESOURCES"/>:</strong></td>
		<td><bean:write name="sourceRegistrationForTransition" property="lastStudentCurricularPlan.degreeCurricularPlan.degree.tipoCurso.localizedName"/> - <bean:write name="sourceRegistrationForTransition" property="lastStudentCurricularPlan.degreeCurricularPlan.degree.name"/></td>
	</tr>
	<tr>
		<td><strong><bean:message  key="label.enrollment.courses.bolonhaRegistrations" bundle="STUDENT_RESOURCES"/>:</strong></td>
		<td>
			<logic:iterate id="targetTransitionRegistration" name="sourceRegistrationForTransition" property="targetTransitionRegistrations">
				<bean:define id="registrationId" name="targetTransitionRegistration" property="idInternal" />
				<bean:write name="targetTransitionRegistration" property="lastStudentCurricularPlan.degreeCurricularPlan.degree.tipoCurso.localizedName"/> - <bean:write name="targetTransitionRegistration" property="lastStudentCurricularPlan.degreeCurricularPlan.degree.name"/> 
				(<html:link target="_blank" action="<%="/bolonhaTransitionManagement.do?method=showStudentCurricularPlan&amp;registrationId=" + registrationId.toString()%>"><bean:message  key="label.enrollment.courses.confirmBolonhaTransition.viewTransitionPlan" bundle="STUDENT_RESOURCES"/></html:link>)<br/>
			</logic:iterate>
		</td>
	</tr>
	</table>


<p class="mtop15 mbottom05"><strong><bean:message  key="label.enrollment.courses.confirmBolonhaTransition.warning2" bundle="STUDENT_RESOURCES"/>:</strong></p>
<table>
	<tr>
		<td>
			<bean:define id="sourceRegistrationForTransitionId" name="sourceRegistrationForTransition" property="idInternal" />
			<html:form action="<%="/studentEnrollmentManagement.do?method=confirmBolonhaTransition&amp;sourceRegistrationForTransitionId=" + sourceRegistrationForTransitionId.toString() %>">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.enrollment.courses.confirmBolonhaTransition.transitToBolonha" bundle="STUDENT_RESOURCES"/></html:submit>
			</html:form>
		</td>
		<td>
			<html:form action="/studentEnrollmentManagement.do?method=prepare">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message  key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
			</html:form>
		</td>
	</tr>
</table>


</logic:present>

