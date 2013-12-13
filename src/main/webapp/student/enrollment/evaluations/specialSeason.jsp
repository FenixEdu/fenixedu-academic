<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.specialSeason" /></h2>

<logic:notPresent role="role(STUDENT)">
	<span class="error"><bean:message key="error.exception.notAuthorized" bundle="STUDENT_RESOURCES" /></span>
</logic:notPresent>


<logic:messagesPresent message="true" property="warning" >
	<div class="warning0" style="padding: 0.5em;">
	<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.warnings.in.enrolment" />:</strong></p>
	<ul class="mvert05">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="warning">
			<li><span><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<div class="error0" style="padding: 0.5em;">
	<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.achtung.in.enrolment" />:</strong></p>
	<ul class="mvert05">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
			<li><span><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	</div>
	<bean:define id="noFurtherAccess" value="true"/>
</logic:messagesPresent>


<logic:present role="role(STUDENT)">
	<div class="infoop2">
		<bean:message key="label.student.SpecialSeasonEnrollment.description" bundle="STUDENT_RESOURCES"/><br/><br/>
	</div>
	<logic:notPresent name="disableContinue">
		<html:link page="/enrollment/evaluations/specialSeason.do?method=pickSCP"><bean:message key="button.continue" bundle="STUDENT_RESOURCES"/></html:link>
	</logic:notPresent>
</logic:present>