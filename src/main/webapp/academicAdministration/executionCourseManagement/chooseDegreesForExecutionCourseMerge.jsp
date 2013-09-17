<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<p>
	<h2>
		<bean:message key="title.execution.course.merge" bundle="SOP_RESOURCES"/>
	</h2>
</p>

<logic:messagesPresent message="true" property="error">
	<br />
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
		<div class="error2"><bean:write name="messages" /></div>
	</html:messages>
	<br />
</logic:messagesPresent>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="success">
	<div class="mvert15">
		<span class="success0">
			<html:messages id="messages" message="true" bundle="SOP_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</div>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="errorFenixException">
	<br/>
	<html:messages id="messages" message="true" property="errorFenixException">
		<div class="error2"><bean:write name="messages"/></div>
	</html:messages>
	<br/>
</logic:messagesPresent>

<html:form action="/chooseDegreesForExecutionCourseMerge.do?method=chooseDegreesAndExecutionPeriod" styleId="submitForm">
	<fr:edit id="degreeBean" name="degreeBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction$DegreesMergeBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="academicInterval" layout="menu-select" key="label.mergedegrees.academicinterval" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		        <fr:property name="providerClass"
		            value="net.sourceforge.fenixedu.presentationTier.renderers.providers.AcademicIntervalProvider" />
		        <fr:property name="format" value="${pathName}" />
		        <fr:property name="nullOptionHidden" value="true" />
	    	</fr:slot>
			<fr:slot name="sourceDegree" layout="menu-select" key="label.mergedegrees.source" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DegreesAcademicAdminProvider" />
				<fr:property name="format" value="${presentationName}" />
			</fr:slot>
			<fr:slot name="destinationDegree" layout="menu-select" key="label.mergedegrees.destination" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DegreesAcademicAdminProvider" />
				<fr:property name="format" value="${presentationName}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle0 thleft mtop15"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.continue" />
	</html:submit>
</html:form>

