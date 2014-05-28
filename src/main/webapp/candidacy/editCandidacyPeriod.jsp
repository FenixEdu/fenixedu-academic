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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<style>
.MobilityApplicationProcessCreation-PreLoadConfigurations-Frame {
	border: 1px dotted #112233;
	display: inline-block;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Panel-Ok {
	background-color: rgb(240, 240, 240);
	margin: 5px;
	padding: 30px;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Panel-Warn {
	background-color: rgb(255, 230, 163);
	margin: 5px;
	padding: 30px;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Panel-Error {
	background-color: rgb(255, 110, 107);
	margin: 5px;
	padding: 30px;
}

a {border-bottom: none !important;}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Link {
	display: inline;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-MessageBox {
	display: inline-block;
	background: white;
	padding: 3px 8px;
	margin-left: 7px;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-MessageIcon {
	position:relative;
	top: 3px;
	padding-right: 4px;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Message-Ok {
	font-style: oblique;
	background: white;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Message-Warn {
	font-style: oblique;
	background: white;
}

.MobilityApplicationProcessCreation-PreLoadConfigurations-Message-Error {
	font-weight: bold;
	background: white;
}

</style>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message name="processName" bundle="CASE_HANDLING_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
	<br/>
</html:messages>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processEid" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<fr:hasMessages for="candidacyProcessBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<fr:edit id="candidacyProcessBean" name="candidacyProcessBean" schema='<%= processName.toString() + "Bean.manage" %>'
		action='<%= "/caseHandling" + processName.toString() + ".do?method=executeEditCandidacyPeriod&amp;processId=" + processId %>'>
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
        <fr:property name="requiredMarkShown" value="true" />
	</fr:layout>
	<fr:destination name="invalid" path='<%= "/caseHandling" + processName.toString() + ".do?method=executeEditCandidacyPeriodInvalid&amp;processId=" + processId %>' />
	<fr:destination name="cancel"  path='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId %>' />
</fr:edit>
<br/>

<logic:present name="preLoadLevel">
	<bean:define id="preloadLevel" name="preLoadLevel" />
	<h3><bean:message key='<%= "mobilityApplicationProcess.editProcess.preLoadTitle" %>' bundle="CASE_HANDLING_RESOURCES" /></h3>
	<div class="MobilityApplicationProcessCreation-PreLoadConfigurations-Frame">
		<div class="MobilityApplicationProcessCreation-PreLoadConfigurations-Panel-<%=preloadLevel%>">
			<div class="MobilityApplicationProcessCreation-PreLoadConfigurations-Link">
				<a href='<%= request.getContextPath() + "/academicAdministration/caseHandling" + processName.toString() + ".do?method=preLoadLastConfigurations&amp;processEid=" + processEid + "&amp;processId=" + processId %>'>
					<button <% if (preloadLevel == "Error") %> disabled="disabled" <% ; %>type='button'><bean:message key='<%= "mobilityApplicationProcess.editProcess.preLoadButton" %>' bundle="CASE_HANDLING_RESOURCES" /></button>
				</a>
			</div>
			<% 	String iconName = "";
				if(preloadLevel == "Ok") {
				    iconName = "/images/accept_gray.gif";
				}
				if(preloadLevel == "Warn") {
				    iconName = "/images/sign_notification.png";
				}
				if(preloadLevel == "Error") {
				    iconName = "/images/iconRemoveOn.png";
				}
			%>
			<div class="MobilityApplicationProcessCreation-PreLoadConfigurations-MessageBox">
				<img class="MobilityApplicationProcessCreation-PreLoadConfigurations-MessageIcon" src='<%= request.getContextPath() + iconName%>' /> <span class="MobilityApplicationProcessCreation-PreLoadConfigurations-Message-<%=preloadLevel%>"><bean:message key='<%= "mobilityApplicationProcess.editProcess.preLoadMessage" + preloadLevel%>' bundle="CASE_HANDLING_RESOURCES" /></span>
			</div>
		</div>
	</div>
</logic:present>

