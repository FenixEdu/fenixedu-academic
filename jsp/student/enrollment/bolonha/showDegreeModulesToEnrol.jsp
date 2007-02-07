<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">

	<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.pricesManagement" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	
	<fr:form action="/bolonhaStudentEnrollment.do">
		<input type="hidden" name="method" />
			
		<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
			<fr:layout name="bolonha-student-enrolment">
			
			</fr:layout>
		</fr:edit>
		
		
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='enrolInDegreeModules';"><bean:message bundle="APPLICATION_RESOURCES"  key="label.update"/></html:submit>
		</p>
	
	</fr:form>
	
	

	

</logic:present>
