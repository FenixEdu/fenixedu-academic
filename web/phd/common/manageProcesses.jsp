<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>

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
		<fr:view name="processList">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" bundle="PHD_RESOURCES">
				<fr:slot name="phdIndividualProcessNumber">
					<fr:property name="format" value="${number}/${year}" />
				</fr:slot>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear" layout="format">
					<fr:property name="format" value="${year}" />
				</fr:slot>
				<fr:slot name="phdProgram" layout="null-as-label">
					<fr:property name="subLayout" value="values" />
					<fr:property name="subSchema" value="PhdProgram.name" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft" />
				<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
			</fr:layout>
		</fr:view>
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
		<fr:view name="processList">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" bundle="PHD_RESOURCES">
				<fr:slot name="phdIndividualProcessNumber">
					<fr:property name="format" value="${number}/${year}" />
				</fr:slot>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear" layout="format">
					<fr:property name="format" value="${year}" />
				</fr:slot>
				<fr:slot name="phdProgram" layout="null-as-label">
					<fr:property name="subLayout" value="values" />
					<fr:property name="subSchema" value="PhdProgram.name" />
				</fr:slot>
				<fr:slot name="seminarProcess.activeState">
					<fr:property name="bundle" value="PHD_RESOURCES" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft" />
				<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
			</fr:layout>
		</fr:view>
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
		<fr:view name="processList">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" bundle="PHD_RESOURCES">
				<fr:slot name="phdIndividualProcessNumber">
					<fr:property name="format" value="${number}/${year}" />
				</fr:slot>
				<fr:slot name="person.name" />
				<fr:slot name="executionYear" layout="format">
					<fr:property name="format" value="${year}" />
				</fr:slot>
				<fr:slot name="phdProgram" layout="null-as-label">
					<fr:property name="subLayout" value="values" />
					<fr:property name="subSchema" value="PhdProgram.name" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft" />
				<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
			</fr:layout>
		</fr:view>
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
	<fr:view name="processList">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" bundle="PHD_RESOURCES">
			<fr:slot name="phdIndividualProcessNumber">
				<fr:property name="format" value="${number}/${year}" />
			</fr:slot>
			<fr:slot name="person.name" />
			<fr:slot name="executionYear" layout="format">
				<fr:property name="format" value="${year}" />
			</fr:slot>
			<fr:slot name="phdProgram" layout="null-as-label">
				<fr:property name="subLayout" value="values" />
				<fr:property name="subSchema" value="PhdProgram.name" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft" />
			<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
		</fr:layout>
	</fr:view>
</logic:notEqual>
