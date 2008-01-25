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

	<bean:define id="attendsId" name="attends" property="idInternal" />
	<bean:define id="projectId" name="project" property="idInternal" />

	<logic:notEqual name="project" property="submissionPeriodOpen" value="true">
		<p>
		 	<span class="warning0"><!-- Error messages go here --><bean:message key="error.project.submissionPeriodAlreadyExpired" bundle="APPLICATION_RESOURCES"/>.</span>
	 	</p>
	</logic:notEqual>

	<logic:present name="submission">
		<div style="border: 1px solid #ddd; padding: 0.5em 1em; background: #fafafa;">
			<p class="mvert05"><strong><bean:message key="label.projectSubmissions.observations"/></strong>:</p>
			<p class="mvert05">
				<fr:view name="submission" property="teacherObservation"/>	
			</p>
		</div>
	</logic:present>

	<logic:empty name="projectSubmissions">
		<p class="mvert15">
			<span class="warning0"><!-- Error messages go here --> <bean:message
				key="label.projectSubmissions.viewProjectSubmissions.noProjectSubmissions" />.
			</span>
		</p>
	</logic:empty>

	<logic:notEmpty name="projectSubmissions">		
		<bean:define id="classForRow" value="" type="java.lang.String" toScope="request"/>
		<logic:present name="rowClasses">
				<bean:define id="classForRow" name="rowClasses" type="java.lang.String" toScope="request"/>
		</logic:present>
		
		<fr:view name="projectSubmissions" schema="projectSubmission.view-full">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight width100" />
				<fr:property name="rowClasses" value="<%= classForRow %>"/>
				<fr:property name="columnClasses" value=",,,acenter" />
				<fr:property name="linkFormat(observation)" value="<%="/projectSubmission.do?method=viewObservation&attendsId="  + attendsId + "&projectId=" + projectId + "&projectSubmissionId=${idInternal}" %>"/>
				<fr:property name="key(observation)" value="label.projectSubmissions.seeTeacherObservation"/>
				<fr:property name="visibleIf(observation)" value="teacherObservationAvailable"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	

	<logic:equal name="project" property="submissionPeriodOpen" value="true">
		<ul class="list5">
			<li>
				<html:link action="<%="/projectSubmission.do?method=prepareProjectSubmission&amp;attendsId="  + attendsId + "&amp;projectId=" + projectId %>">
					<bean:message key="link.projectSubmissions.viewProjectSubmissions.submitProject" />
				</html:link>
			</li>
		</ul>
	</logic:equal>

</logic:notPresent>

<logic:present name="noStudentGroupForGrouping">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:message key="label.projectSubmissions.viewProjectSubmissions.noStudentGroupForGrouping"/></span>
	</p>
</logic:present>

