<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page	import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId" />
<bean:define id="withRules" name="withRules" />

<logic:equal name="withRules" value="true">
	<h2><strong><bean:message key="label.course.moveEnrolments.with.rules" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
</logic:equal>
<logic:equal name="withRules" value="false">
	<h2><strong><bean:message key="label.course.moveEnrolments.without.rules" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
</logic:equal>

<fr:form action="<%="/curriculumLinesLocationManagement.do?scpID=" + studentCurricularPlanId.toString() + "&amp;withRules=" + withRules.toString() %>">
	<input type="hidden" name="method" />
	
	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="ACADEMIC_OFFICE_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br/>
	</logic:messagesPresent>

	<fr:edit name="studentCurricularPlan">
		<fr:layout>
			<fr:property name="organizedBy" value="<%=OrganizationType.GROUPS.name()%>" />
			<fr:property name="detailed" value="false" />
			<fr:property name="selectable" value="true" />
			<fr:property name="selectionName" value="selectedCurriculumLineIds" />
		</fr:layout>
	</fr:edit>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='chooseNewDestination';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.continue" />
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" onclick="this.form.method.value='backToStudentEnrolments';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
		</html:submit>
	</p>

</fr:form>