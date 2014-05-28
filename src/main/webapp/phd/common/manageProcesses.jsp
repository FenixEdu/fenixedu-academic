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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>
<%@page import="pt.ist.fenixframework.DomainObject"%>


<style>
.col1 {  }
.col2 { width: 250px; }
.col3 {  }
.col4 {  }
.col5 {  }
.col6 { width: 200px; }
.col7 {  }
</style>


<bean:define id="searchProcessBean" name="searchProcessBean"/>

<%--  ### Search Criteria  ### --%>
<fr:form id="search" action="/phdIndividualProgramProcess.do?method=searchAllProcesses">

<table>
	<tr>
		<td>
			<fr:edit id="searchProcessBean" name="searchProcessBean">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean">
					<fr:slot name="searchCriterion">
						<fr:property name="defaultOptionHidden" value="true"/>
						<fr:property name="bundle" value="PHD_RESOURCES"/>
					</fr:slot>
					<fr:slot name="searchValue"/>
				</fr:schema>
				<fr:layout name="matrix">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05 mbottom05 thmiddle" />
					<fr:property name="slot(searchCriterion)" value="searchCriterion"/>
					<fr:property name="row(searchCriterion)" value="0"/>
					<fr:property name="column(searchCriterion)" value="0"/>
					<fr:property name="slot(searchValue)" value="searchValue"/>
					<fr:property name="labelHidden(searchValue)" value="true"/>
					<fr:property name="row(searchValue)" value="0"/>
					<fr:property name="column(searchValue)" value="1"/>
				</fr:layout>
			</fr:edit>
		</td>
		<td>
			<logic:messagesPresent message="true" property="searchError">
				<span class="error">
					<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="searchError">
						<bean:write name="messages" />
					</html:messages>
				</span>
			</logic:messagesPresent>
		</td>
		<td>
			<logic:messagesPresent message="true" property="searchResults">
				<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="searchResults">
					<bean:write name="messages" />
				</html:messages>
			</logic:messagesPresent>
		</td>
	</tr>
</table>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.search"/></html:submit>

</fr:form>

<h3 class="separator2 mtop15"><bean:message key="label.phd.candidacy" bundle="PHD_RESOURCES"/></h3>

<logic:iterate id="containerEnum" name="candidacyCategory">
	<phd:filterProcesses id="processList" predicateContainer="containerEnum" bean="searchProcessBean"/>
	<bean:size id="size" name="processList" />		
	<p class="mbottom05"><strong><%= BundleUtil.getEnumName((Enum<?>) containerEnum, "Phd") %> (<%= size %>)</strong></p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th class="col1"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdIndividualProcessNumber" bundle="PHD_RESOURCES"/></th>
				<th class="col2"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.person.name" bundle="PHD_RESOURCES"/></th>
				<th class="col3"><bean:message key="label.phd.messages" bundle="PHD_RESOURCES"/></th>
				<th class="col4"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.executionYear" bundle="PHD_RESOURCES"/></th>
				<th class="col5"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdProgram" bundle="PHD_RESOURCES"/></th>
				<th class="col7"></th>
			</tr>
			<logic:iterate id="process" name="processList">
				<tr>
					<td><bean:write name="process" property="phdIndividualProcessNumber.number"/>/<bean:write name="process" property="phdIndividualProcessNumber.year"/></td>
					<td><bean:write name="process" property="person.name"/></td>
					<bean:size id="messageSize" name="process" property="alertMessagesForLoggedPerson" />
					<logic:equal name="messageSize" value="0"><td class="center">-</td></logic:equal>
					<logic:notEqual name="messageSize" value="0">
						<fr:form id="<%= "form" + ((DomainObject) process).getExternalId() %>" action="<%= "/phdIndividualProgramProcess.do?method=viewProcessAlertMessages&processId=" + ((DomainObject) process).getExternalId() %>" >
						<td class="center" style="cursor: pointer;" onclick="<%="document.getElementById('form" + ((DomainObject) process).getExternalId() + "').submit()"%>" >
							<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId" >
								<bean:write name="messageSize"/>
							</html:link>
							<bean:size id="unreadMessageSize" name="process" property="unreadAlertMessagesForLoggedPerson" />
							<logic:notEqual name="unreadMessageSize" value="0">
								<span class="color888">
									<logic:equal name="unreadMessageSize" value="1">
										(<bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES"/>)
									</logic:equal>
									<logic:notEqual name="unreadMessageSize" value="1">
										(<bean:message key="message.pending.phd.alert.messages.notification.short.plural" bundle="PHD_RESOURCES" arg0="<%= unreadMessageSize.toString() %>"/>)
									</logic:notEqual>
								</span>
							</logic:notEqual>
						</td>
						</fr:form>
					</logic:notEqual>
					<td><bean:write name="process" property="executionYear.year"/></td>
					<td><logic:present name="process" property="phdProgram"><bean:write name="process" property="phdProgram.acronym"/></logic:present></td>
					<td>
						<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId" >
							<bean:message key="label.view" bundle="PHD_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
</logic:iterate>


<h3 class="separator2"><bean:message key="label.phd.publicPresentationSeminar" bundle="PHD_RESOURCES"/></h3>

<logic:iterate id="containerEnum" name="seminarCategory">
	<phd:filterProcesses id="processList" predicateContainer="containerEnum" bean="searchProcessBean" />
	<bean:size id="size" name="processList" />
	<p class="mbottom05"><strong><%= BundleUtil.getEnumName((Enum<?>) containerEnum, "Phd") %> (<%= size %>)</strong></p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th class="col1"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdIndividualProcessNumber" bundle="PHD_RESOURCES"/></th>
				<th class="col2"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.person.name" bundle="PHD_RESOURCES"/></th>
				<th class="col3"><bean:message key="label.phd.messages" bundle="PHD_RESOURCES"/></th>
				<th class="col4"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.executionYear" bundle="PHD_RESOURCES"/></th>
				<th class="col5"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdProgram" bundle="PHD_RESOURCES"/></th>
				<th class="col6"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.seminarProcess.activeState" bundle="PHD_RESOURCES"/></th>
				<th class="col7"></th>
			</tr>
			<logic:iterate id="process" name="processList">
				<tr>
					<td><bean:write name="process" property="phdIndividualProcessNumber.number"/>/<bean:write name="process" property="phdIndividualProcessNumber.year"/></td>
					<td><bean:write name="process" property="person.name"/></td>
					<bean:size id="messageSize" name="process" property="alertMessagesForLoggedPerson" />
					<logic:equal name="messageSize" value="0"><td class="center">-</td></logic:equal>
					<logic:notEqual name="messageSize" value="0">
						<fr:form id="<%= "form" + ((DomainObject) process).getExternalId() %>" action="<%= "/phdIndividualProgramProcess.do?method=viewProcessAlertMessages&processId=" + ((DomainObject) process).getExternalId() %>" >
						<td class="center" style="cursor: pointer;" onclick="<%="document.getElementById('form" + ((DomainObject) process).getExternalId() + "').submit()"%>" >
							<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId" >
								<bean:write name="messageSize"/>
							</html:link>
							<bean:size id="unreadMessageSize" name="process" property="unreadAlertMessagesForLoggedPerson" />
							<logic:notEqual name="unreadMessageSize" value="0">
								<span class="color888">
									<logic:equal name="unreadMessageSize" value="1">
										(<bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES"/>)
									</logic:equal>
									<logic:notEqual name="unreadMessageSize" value="1">
										(<bean:message key="message.pending.phd.alert.messages.notification.short.plural" bundle="PHD_RESOURCES" arg0="<%= unreadMessageSize.toString() %>"/>)
									</logic:notEqual>
								</span>
							</logic:notEqual>
						</td>
						</fr:form>
					</logic:notEqual>
					<td><bean:write name="process" property="executionYear.year"/></td>
					<td><logic:present name="process" property="phdProgram"><bean:write name="process" property="phdProgram.acronym"/></logic:present></td>
					<td><fr:view name="process" property="seminarProcess.activeState" layout="phd-enum-renderer"/></td>
					<td>
						<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId" >
							<bean:message key="label.view" bundle="PHD_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
</logic:iterate>

<h3 class="separator2"><bean:message key="label.phd.thesis" bundle="PHD_RESOURCES"/></h3>

<logic:iterate id="containerEnum" name="thesisCategory">
	<phd:filterProcesses id="processList" predicateContainer="containerEnum" bean="searchProcessBean" />
	<bean:size id="size" name="processList" />
	<p class="mbottom05"><strong><%= BundleUtil.getEnumName((Enum<?>) containerEnum, "Phd") %> (<%= size %>)</strong></p>
	
	<logic:equal name="size" value="0">
		<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
	</logic:equal>
	
	<logic:notEqual name="size" value="0">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th class="col1"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdIndividualProcessNumber" bundle="PHD_RESOURCES"/></th>
				<th class="col2"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.person.name" bundle="PHD_RESOURCES"/></th>
				<th class="col3"><bean:message key="label.phd.messages" bundle="PHD_RESOURCES"/></th>
				<th class="col4"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.executionYear" bundle="PHD_RESOURCES"/></th>
				<th class="col5"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdProgram" bundle="PHD_RESOURCES"/></th>
				<th class="col7"></th>
			</tr>
			<logic:iterate id="process" name="processList">
				<tr>
					<td><bean:write name="process" property="phdIndividualProcessNumber.number"/>/<bean:write name="process" property="phdIndividualProcessNumber.year"/></td>
					<td><bean:write name="process" property="person.name"/></td>
					<bean:size id="messageSize" name="process" property="alertMessagesForLoggedPerson" />
					<logic:equal name="messageSize" value="0"><td class="center">-</td></logic:equal>
					<logic:notEqual name="messageSize" value="0">
						<fr:form id="<%= "form" + ((DomainObject) process).getExternalId() %>" action="<%= "/phdIndividualProgramProcess.do?method=viewProcessAlertMessages&processId=" + ((DomainObject) process).getExternalId() %>" >
						<td class="center" style="cursor: pointer;" onclick="<%="document.getElementById('form" + ((DomainObject) process).getExternalId() + "').submit()"%>" >
							<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId" >
								<bean:write name="messageSize"/>
							</html:link>
							<bean:size id="unreadMessageSize" name="process" property="unreadAlertMessagesForLoggedPerson" />
							<logic:notEqual name="unreadMessageSize" value="0">
								<span class="color888">
									<logic:equal name="unreadMessageSize" value="1">
										(<bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES"/>)
									</logic:equal>
									<logic:notEqual name="unreadMessageSize" value="1">
										(<bean:message key="message.pending.phd.alert.messages.notification.short.plural" bundle="PHD_RESOURCES" arg0="<%= unreadMessageSize.toString() %>"/>)
									</logic:notEqual>
								</span>
							</logic:notEqual>
						</td>
						</fr:form>
					</logic:notEqual>
					<td><bean:write name="process" property="executionYear.year"/></td>
					<td><logic:present name="process" property="phdProgram"><bean:write name="process" property="phdProgram.acronym"/></logic:present></td>
					<td>
						<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId" >
							<bean:message key="label.view" bundle="PHD_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
</logic:iterate>

<h3 class="separator2"><bean:message key="label.phd.concluded" bundle="PHD_RESOURCES"/></h3>

<bean:define id="concludedThisYearContainer" name="concludedThisYearContainer"/>
<phd:filterProcesses id="processList" predicateContainer="concludedThisYearContainer" bean="searchProcessBean" />
<bean:size id="size" name="processList" />
<p class="mbottom05"><strong><%= BundleUtil.getEnumName((Enum<?>) concludedThisYearContainer, "Phd") %> (<%= size %>)</strong></p>

<logic:equal name="size" value="0">
	<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
</logic:equal>

<logic:notEqual name="size" value="0">
	<table class="tstyle2 thlight thleft">
		<tr>
			<th class="col1"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdIndividualProcessNumber" bundle="PHD_RESOURCES"/></th>
			<th class="col2"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.person.name" bundle="PHD_RESOURCES"/></th>
			<th class="col3"><bean:message key="label.phd.messages" bundle="PHD_RESOURCES"/></th>
			<th class="col4"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.executionYear" bundle="PHD_RESOURCES"/></th>
			<th class="col5"><bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdProgram" bundle="PHD_RESOURCES"/></th>
			<th class="col7"></th>
		</tr>
		<logic:iterate id="process" name="processList">
			<tr>
				<td><bean:write name="process" property="phdIndividualProcessNumber.number"/>/<bean:write name="process" property="phdIndividualProcessNumber.year"/></td>
				<td><bean:write name="process" property="person.name"/></td>
				<bean:size id="messageSize" name="process" property="alertMessagesForLoggedPerson" />
				<logic:equal name="messageSize" value="0"><td class="center">-</td></logic:equal>
				<logic:notEqual name="messageSize" value="0">
					<fr:form id="<%= "form" + ((DomainObject) process).getExternalId() %>" action="<%= "/phdIndividualProgramProcess.do?method=viewProcessAlertMessages&processId=" + ((DomainObject) process).getExternalId() %>" >
					<td class="center" style="cursor: pointer;" onclick="<%="document.getElementById('form" + ((DomainObject) process).getExternalId() + "').submit()"%>" >
						<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId" >
							<bean:write name="messageSize"/>
						</html:link>
						<bean:size id="unreadMessageSize" name="process" property="unreadAlertMessagesForLoggedPerson" />
						<logic:notEqual name="unreadMessageSize" value="0">
							<span class="color888">
								<logic:equal name="unreadMessageSize" value="1">
									(<bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES"/>)
								</logic:equal>
								<logic:notEqual name="unreadMessageSize" value="1">
									(<bean:message key="message.pending.phd.alert.messages.notification.short.plural" bundle="PHD_RESOURCES" arg0="<%= unreadMessageSize.toString() %>"/>)
								</logic:notEqual>
							</span>
						</logic:notEqual>
					</td>
					</fr:form>
				</logic:notEqual>
				<td><bean:write name="process" property="executionYear.year"/></td>
				<td><logic:present name="process" property="phdProgram"><bean:write name="process" property="phdProgram.acronym"/></logic:present></td>
				<td>
					<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId" >
						<bean:message key="label.view" bundle="PHD_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEqual>
