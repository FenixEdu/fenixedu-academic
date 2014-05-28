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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Results  ### --%>

<fr:edit id="select-period-bean" name="selectPeriodBean">
	<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram.PublicPhdProgramCandidacyProcessDA$SelectPhdCandidacyPeriodBean" bundle="PHD_RESOURCES" >
		<fr:slot name="phdCandidacyPeriod" key="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram.PublicPhdProgramCandidacyProcessDA$SelectPhdCandidacyPeriodBean.phdCandidacyPeriod" layout="menu-select-postback">
			<fr:property name="destination" value="phdCandidacyPeriodSelectionPostback" />
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram.PublicPhdProgramCandidacyProcessDA$PhdCandidacyPeriodDataProvider" />
			<fr:property name="format" value="${presentationName}" />
		</fr:slot>
	</fr:schema>
	
	<fr:destination name="phdCandidacyPeriodSelectionPostback" path="/candidacies/phdProgramCandidacyProcess.do?method=listProcesses" />
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight" />
	</fr:layout>	
</fr:edit>


<table>
	<tr>
		<td><bean:message key="label.phd.statistics.total.requests" bundle="PHD_RESOURCES" />: </td>
		<td><bean:write name="statistics" property="totalRequests" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.phd.statistics.total.with.candidacy" bundle="PHD_RESOURCES" />: </td>
		<td><bean:write name="statistics" property="totalCandidates" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.phd.statistics.total.validated" bundle="PHD_RESOURCES" />: </td>
		<td><bean:write name="statistics" property="totalValidated" /></td>
	</tr>
</table>

<html:link action="/candidacies/phdProgramCandidacyProcess.do?method=manageFocusAreas">
	<bean:message bundle="PHD_RESOURCES" key="link.phdProgramInformation.focusArea.thesisSubjects" />
</html:link>

<logic:notEmpty name="candidacyHashCodes">
	<fr:view schema="PublicPhdCandidacyBean.view" name="candidacyHashCodes">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:property name="linkFormat(view)" value="/candidacies/phdProgramCandidacyProcess.do?method=viewProcess&hashCodeId=${hashCode.externalId}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
			
			<fr:property name="sortParameter" value="sortBy"/>
	        <fr:property name="sortUrl" value="/candidacies/phdProgramCandidacyProcess.do?method=listProcesses" />
    	    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "email=asc" : request.getParameter("sortBy") %>"/>
			<fr:property name="ascendingImage" value="/images/upArrow.gif"/>
        	<fr:property name="descendingImage" value="/images/downArrow.gif"/>
			<fr:property name="sortableSlots" value="email, name, phdFocusArea, candidate, validated" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="candidacyHashCodes">
	<br/>
	<em><bean:message key="label.phd.no.processes" bundle="PHD_RESOURCES" /></em>
	<br/>
</logic:empty>

<%--  ### End of Results  ### --%>

</logic:present>
