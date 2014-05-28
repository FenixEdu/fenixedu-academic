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

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.individual.program.process.configuration" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
  </tr>
</table>

<%--  ### End Of Context Information  ### --%>

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=savePhdConfiguration&processId=" + individualProgramProcessId %>">
	<fr:edit id="phdConfigurationIndividualProgramProcessBean" name="phdConfigurationIndividualProgramProcessBean" visible="false" />
	
	<fr:edit id="phdConfigurationIndividualProgramProcessBean-edit" 
		name="phdConfigurationIndividualProgramProcessBean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdConfigurationIndividualProgramProcessBean">
			<fr:slot name="generateAlerts" />
			<fr:slot name="migratedProcess" />
			<fr:slot name="isBolonha" />
		</fr:schema>

		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>" />
		<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=savePhdConfigurationInvalid&processId=" + individualProgramProcessId.toString() %>" />
	</fr:edit>
	<%--  ### Buttons (e.g. Submit)  ### --%>
  	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
  	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" ><bean:message bundle="PHD_RESOURCES" key="label.back"/></html:cancel>
	<%--  ### End of Buttons (e.g. Submit)  ### --%>
	
</fr:form>

<logic:equal name="process" property="transferable" value="true">
	<p>
		<html:link 	action="/phdIndividualProgramProcess.do?method=prepareChooseProcessToTransfer" 
					paramId="processId" paramName="process" paramProperty="externalId" >
			<bean:message key="label.phd.transfer.process" bundle="PHD_RESOURCES" />
		</html:link>
	</p>
</logic:equal>
