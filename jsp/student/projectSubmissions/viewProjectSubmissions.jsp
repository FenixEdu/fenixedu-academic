<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:messages id="message" message="true">
	<span class="error"><!-- Error messages go here --> <bean:write name="message" /> </span>
</html:messages>
<h2><bean:message
	key="label.projectSubmissions.viewProjectSubmissions.title" /></h2>

<fr:view name="project"
	schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:view>

<br />

<logic:notPresent name="noStudentGroupForGrouping">
	<logic:empty name="projectSubmissions">
		<span class="error"><!-- Error messages go here --> <bean:message
			key="label.projectSubmissions.viewProjectSubmissions.noProjectSubmissions" />
		</span>
	</logic:empty>

	<logic:notEmpty name="projectSubmissions">		
		<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2" />
				<fr:property name="columnClasses" value=",,,acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<br />
	<br />
	<logic:equal name="project" property="submissionPeriodOpen" value="true">
		<bean:define id="attendsId" name="attends" property="idInternal" />
		<bean:define id="projectId" name="project" property="idInternal" />
		<html:link
			action="<%="/projectSubmission.do?method=prepareProjectSubmission&amp;attendsId="  + attendsId + "&amp;projectId=" + projectId %>">
			<bean:message
				key="link.projectSubmissions.viewProjectSubmissions.submitProject" />
		</html:link>
	</logic:equal>
	<logic:notEqual name="project" property="submissionPeriodOpen" value="true">
	 	<span class="error"><!-- Error messages go here --><bean:message key="error.project.submissionPeriodAlreadyExpired" bundle="APPLICATION_RESOURCES"/></span>
	</logic:notEqual>
</logic:notPresent>
<logic:present name="noStudentGroupForGrouping">
	<span class="error"><!-- Error messages go here --><bean:message key="label.projectSubmissions.viewProjectSubmissions.noStudentGroupForGrouping"/></span>
</logic:present>

