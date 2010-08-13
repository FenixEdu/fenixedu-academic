<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>

<fr:form id="search" action="/phdIndividualProgramProcess.do?method=viewInactiveProcesses">
	<fr:edit id="searchProcessBean" name="searchProcessBean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean">
			<fr:slot name="executionYear" layout="menu-select-postback">
		 		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05 mbottom05 thmiddle" />
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<bean:define id="suspendedContainer" name="suspendedContainer"/>
<phd:filterProcesses id="processList" predicateContainer="suspendedContainer" bean="searchProcessBean" />
<bean:size id="size" name="processList" />
<h3 class="mbottom0"><strong><%= BundleUtil.getEnumName((Enum<?>) suspendedContainer, "Phd") %> (<%= size %>)</strong></h3>
	
<logic:equal name="size" value="0">
	<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
</logic:equal>
	
<logic:notEqual name="size" value="0">
	<fr:view name="processList" schema="PhdIndividualProgramProcess.view.resume">
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
			<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewInactiveProcesses&processId=${externalId}"/>
		</fr:layout>
	</fr:view>
</logic:notEqual>

<bean:define id="concludedContainer" name="concludedContainer"/>
<phd:filterProcesses id="processList" predicateContainer="concludedContainer" bean="searchProcessBean" />
<bean:size id="size" name="processList" />
<h3 class="mbottom0"><strong><%= BundleUtil.getEnumName((Enum<?>) concludedContainer, "Phd") %> (<%= size %>)</strong></h3>
	
<logic:equal name="size" value="0">
	<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
</logic:equal>
	
<logic:notEqual name="size" value="0">
	<fr:view name="processList" schema="PhdIndividualProgramProcess.view.resume">
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
			<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewInactiveProcesses&processId=${externalId}"/>
		</fr:layout>
	</fr:view>
</logic:notEqual>

<bean:define id="abolishedContainer" name="abolishedContainer"/>
<phd:filterProcesses id="processList" predicateContainer="abolishedContainer" bean="searchProcessBean" />
<bean:size id="size" name="processList" />
<h3 class="mbottom0"><strong><%= BundleUtil.getEnumName((Enum<?>) abolishedContainer, "Phd") %> (<%= size %>)</strong></h3>
	
<logic:equal name="size" value="0">
	<p class="mbottom2"><em><bean:message key="message.no.processes.in.state" bundle="PHD_RESOURCES"/></em></p>
</logic:equal>
	
<logic:notEqual name="size" value="0">
	<fr:view name="processList" schema="PhdIndividualProgramProcess.view.resume">
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
			<fr:slot name="activeState">
				<fr:property name="bundle" value="PHD_RESOURCES" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft" />
			<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewInactiveProcesses&processId=${externalId}"/>
		</fr:layout>
	</fr:view>
</logic:notEqual>
