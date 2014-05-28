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

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manageAlerts" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<strong><bean:message  key="label.phd.alerts" bundle="PHD_RESOURCES"/></strong>
<br/><br/>
<html:link action="/phdIndividualProgramProcess.do?method=prepareCreateCustomAlert" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.createCustomAlert"/>
</html:link>
<br/>

<logic:notEmpty name="alerts">
<fr:view schema="PhdAlert.view" name="alerts">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteCustomAlert&alertId=${externalId}&processId=${process.externalId}"/>
		<fr:property name="key(delete)" value="label.delete"/>
		<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
		<fr:property name="visibleIfNot(delete)" value="systemAlert"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="alerts">
	<bean:message  key="label.phd.noAlerts" bundle="PHD_RESOURCES"/>
</logic:empty>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
