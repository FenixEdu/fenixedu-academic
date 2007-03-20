<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.student.portalTitle" /></em>
<h2><bean:message key="label.projectSubmissions.viewProjectSubmissions.title" /></h2>

<html:messages id="message" message="true">
	<p>
		<span class="error0"><!-- Error messages go here --> <bean:write name="message" /> </span>
	</p>
</html:messages>

<fr:view name="project"
	schema="evaluation.project.view-with-name-description-and-grouping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright" />
		<fr:property name="rowClasses" value="bold,," />
	</fr:layout>
</fr:view>

<logic:notPresent name="noStudentGroupForGrouping">
	<logic:empty name="projectSubmissions">
		<p class="mvert15">
			<span class="warning0"><!-- Error messages go here --> <bean:message
				key="label.projectSubmissions.viewProjectSubmissions.noProjectSubmissions" />
			</span>
		</p>
	</logic:empty>

	<logic:notEmpty name="projectSubmissions">		
		<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2" />
				<fr:property name="columnClasses" value=",,,acenter" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	

	<logic:equal name="project" property="submissionPeriodOpen" value="true">
		<bean:define id="attendsId" name="attends" property="idInternal" />
		<bean:define id="projectId" name="project" property="idInternal" />
		<ul class="list5">
			<li>
				<html:link action="<%="/projectSubmission.do?method=prepareProjectSubmission&amp;attendsId="  + attendsId + "&amp;projectId=" + projectId %>">
					<bean:message key="link.projectSubmissions.viewProjectSubmissions.submitProject" />
				</html:link>
			</li>
		</ul>
	</logic:equal>
	<logic:notEqual name="project" property="submissionPeriodOpen" value="true">
		<p>
		 	<span class="error0"><!-- Error messages go here --><bean:message key="error.project.submissionPeriodAlreadyExpired" bundle="APPLICATION_RESOURCES"/></span>
	 	</p>
	</logic:notEqual>
</logic:notPresent>

<logic:present name="noStudentGroupForGrouping">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:message key="label.projectSubmissions.viewProjectSubmissions.noStudentGroupForGrouping"/></span>
	</p>
</logic:present>

