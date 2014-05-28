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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"%>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.candidacy.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdCandidacyPeriodManagement.do?method=list">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phd.candidacy.periods" bundle="PHD_RESOURCES"/></strong></p>

<fr:view name="phdCandidacyPeriod">
	<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod" bundle="PHD_RESOURCES">
			<fr:slot name="type" />
			<fr:slot name="executionInterval.name" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod.executionYear"/>
			<fr:slot name="start" />
			<fr:slot name="end" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
	</fr:layout>
</fr:view>

<p><strong><bean:message key="title.phd.candidacies" bundle="PHD_RESOURCES" /></strong></p>

<logic:empty name="phdCandidacyPeriod" property="phdProgramCandidacyProcesses" >
		<p><em><bean:message key="message.phd.candidacies.is.empty" bundle="PHD_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="phdCandidacyPeriod" property="phdProgramCandidacyProcesses" >
	<table class="tstyle2 thlight mtop15 thleft">
		<tr>
			<td><bean:message bundle="PHD_RESOURCES" key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdIndividualProcessNumber"/></td>
			<td><bean:message bundle="PHD_RESOURCES" key="label.net.sourceforge.fenixedu.dataTransferObject.person.PersonBean.name"/></td>
			<td><bean:message bundle="PHD_RESOURCES" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriodBean.phdProgram"/></td>
			<td><bean:message bundle="PHD_RESOURCES" key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.candidacyDate"/></td>
			<td></td>
		</tr>
		<logic:iterate id="process" name="phdCandidacyPeriod" property="phdProgramCandidacyProcesses"
				type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess">
			<tr>
				<td><bean:write name="process" property="processNumber"/></td>
				<td><bean:write name="process" property="person.name"/></td>
				<td>
					<logic:present name="process" property="individualProgramProcess.phdProgram">
						<bean:write name="process" property="individualProgramProcess.phdProgram.name"/>
					</logic:present>
				</td>
				<td><bean:write name="process" property="candidacyDate"/></td>
				<td>
					<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + process.getIndividualProgramProcess().getExternalId() %>">
						<bean:message bundle="PHD_RESOURCES" key="label.view"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
