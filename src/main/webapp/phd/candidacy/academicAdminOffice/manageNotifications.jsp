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
<h2><bean:message key="label.phd.notifications" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdProgramCandidacyProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>


<%--  ### End Of Context Information  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<strong><bean:message  key="label.phd.notifications" bundle="PHD_RESOURCES"/></strong>
<br/>
<logic:empty name="process" property="notifications">
	<bean:message  key="label.phd.noNotifications" bundle="PHD_RESOURCES"/>
</logic:empty>
<logic:notEmpty name="process" property="notifications">
	<fr:view schema="PhdNotification.view" name="process" property="notifications">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="linkFormat(markAsSent)" value="/phdProgramCandidacyProcess.do?method=markNotificationAsSent&notificationId=${externalId}&processId=${candidacyProcess.externalId}"/>
			<fr:property name="key(markAsSent)" value="label.mark.notification.as.sent"/>
			<fr:property name="bundle(markAsSent)" value="PHD_RESOURCES"/>
			<fr:property name="order(markAsSent)" value="0"/>
			<fr:property name="visibleIfNot(markAsSent)" value="sent"/>
			
			<fr:property name="linkFormat(printPT)" value="/phdProgramCandidacyProcess.do?method=printNotification&language=pt&notificationId=${externalId}&processId=${candidacyProcess.externalId}"/>
			<fr:property name="key(printPT)" value="label.print.pt"/>
			<fr:property name="bundle(printPT)" value="PHD_RESOURCES"/>
			<fr:property name="order(printPT)" value="1"/>
			<fr:property name="linkFormat(printEN)" value="/phdProgramCandidacyProcess.do?method=printNotification&language=en&&notificationId=${externalId}&processId=${candidacyProcess.externalId}"/>
			<fr:property name="key(printEN)" value="label.print.en"/>
			<fr:property name="bundle(printEN)" value="PHD_RESOURCES"/>
			<fr:property name="order(printEN)" value="2"/>
		</fr:layout>
		

	</fr:view>
</logic:notEmpty>


<%--  ### End of Operation Area (e.g. Create Candidacy)  ### --%>


	
<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


<br/><br/>

<html:link action="/phdProgramCandidacyProcess.do?method=prepareCreateNotification" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.createNotification"/>
</html:link>

<%--  ### End of Operation Area  ### --%>
