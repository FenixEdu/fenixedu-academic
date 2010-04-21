<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean"%>

<bean:define id="searchProcessBean" name="searchProcessBean"/>

<logic:notEmpty name="processes">
	<bean:size id="size" name="processes" />
	<p class="mbottom05"><bean:message key="label.phd.number.searchResults.found.for" bundle="PHD_RESOURCES" arg0="<%= size.toString() %>" arg1="<%= ((SearchPhdIndividualProgramProcessBean) searchProcessBean).getSearchValue() %>"/></p>

	<fr:view name="processes" schema="PhdIndividualProgramProcess.view.resume">
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
			<fr:slot name="activeState" layout="phd-enum-renderer" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft" />
			<fr:link label="label.view,PHD_RESOURCES" name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:notEmpty>
<logic:empty name="processes">
	<p class="mbottom05"><bean:message bundle="PHD_RESOURCES" key="message.no.processes.found"/></p>
</logic:empty>
