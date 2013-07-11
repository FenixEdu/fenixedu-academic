<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<h2>
		<bean:message key="title.execution.course.merge" bundle="SOP_RESOURCES"/>
	</h2>
</p>

<logic:equal name="previousOrEqualSemester" value="true">
	<div class="error0">
		<bean:write name="degreeBean" property="academicInterval.pathName"/>
	</div>
</logic:equal>
<logic:equal name="previousOrEqualSemester" value="false">
	<bean:write name="degreeBean" property="academicInterval.pathName"/>
</logic:equal>

<logic:messagesPresent message="true" property="error">
	<br />
	<html:messages id="messages" message="true" bundle="SOP_RESOURCES" property="error">
		<div class="error2"><bean:write name="messages"/></div>
	</html:messages>
	<br />
</logic:messagesPresent>

<span class="error"><!-- Error messages go here --><html:errors/></span>

<html:form action="/mergeExecutionCoursesForm.do?method=mergeExecutionCourses" styleId="submitForm">
	<p>
		<html:link page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
	</br>
	(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mergedegrees.source"/>) <br/>
	<bean:write name="degreeBean" property="sourceDegree.sigla" /> - <bean:write name="degreeBean" property="sourceDegree.presentationName"/>
	
	

	<fr:edit id="degreeBean" name="degreeBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction$DegreesMergeBean" bundle="ACADEMIC_OFFICE_RESOURCES">				
			<fr:slot name="sourceExecutionCourse" layout="menu-select" key="label.mergedegrees.mergecourses.source" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.SourceExecutionCoursesProvider"/>
				<fr:property name="format" value="${nome}" />
			</fr:slot>
			<fr:slot name="destinationExecutionCourse" layout="menu-select" key="label.mergedegrees.mergecourses.destination" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DestinationExecutionCoursesProvider"/>
				<fr:property name="format" value="${nome}"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle0 thleft mtop15"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<br/>
	<bean:write name="degreeBean" property="destinationDegree.sigla"/> - <bean:write name="degreeBean" property="destinationDegree.presentationName"/> <br/>
	(<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mergedegrees.destination"/>)
	<br/>
	<br/>
	<bean:define id="deleteConfirm">
		requestConfirmation('submitForm','<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="message.mergedegrees.mergecourses.confirmation"/>','<bean:message bundle="HTMLALT_RESOURCES" key="submit.confirm"/>');return false;
	</bean:define>	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="<%= deleteConfirm %>">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
</html:form>


<script src="../javaScript/alertHandlers.js"></script>
<script src="../javaScript/jquery.alerts.js"></script>
