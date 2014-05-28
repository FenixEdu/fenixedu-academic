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

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<html:link action="<%= "/phdIndividualProgramProcess.do?method=manageProcesses" %>">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<logic:notEmpty name="alertMessages">
	<fr:view name="alertMessages">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage" bundle="PHD_RESOURCES">	
			<fr:slot name="whenCreated" layout="no-time" />
			<fr:slot name="process" layout="link">
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="linkFormat" value="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewAlertMessages&processId=${externalId}" />
				<fr:property name="format" value="${processNumber}"/>
			</fr:slot>
			<fr:slot name="subject" layout="link">
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="useParent" value="true"/>
				<fr:property name="linkFormat" value="<%= "/phdIndividualProgramProcess.do?method=viewAlertMessageFromAllAlertMessages&global=true&alertMessageId=${externalId}" %>" />
			</fr:slot>
			<fr:slot name="readed" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value=",,,acenter"/>
		</fr:layout>
	</fr:view>	
</logic:notEmpty>

<logic:empty name="alertMessages">
	<p><em><bean:message  key="label.phd.noAlertMessages" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
