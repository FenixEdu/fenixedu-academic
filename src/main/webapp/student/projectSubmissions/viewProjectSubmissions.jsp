<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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

	<bean:define id="attendsId" name="attends" property="externalId" />
	<bean:define id="projectId" name="project" property="externalId" />

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
				<fr:property name="linkFormat(observation)" value="<%="/projectSubmission.do?method=viewObservation&attendsId="  + attendsId + "&projectId=" + projectId + "&projectSubmissionId=${externalId}" %>"/>
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

