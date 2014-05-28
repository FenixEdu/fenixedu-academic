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
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdParticipant"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.process.transfer" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=prepareChooseProcessToTransfer" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>
 
<p><strong><bean:message key="label.phd.transfer.process" bundle="PHD_RESOURCES"/></strong></p>

<table>
	<tr>
		<td>
			<p><bean:message  key="label.phd.actual.process" bundle="PHD_RESOURCES"/></p>
			<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<p><bean:message key="label.new.phd.process.to.transfer" bundle="PHD_RESOURCES" /></p>
			<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="phdIndividualProgramProcessBean" property="destiny">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
				</fr:layout>
			</fr:view>
		</td>
	</tr>
</table>

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=transferProcess&amp;processId=" + processId %>" >
	<fr:edit id="phdIndividualProgramProcessBean" name="phdIndividualProgramProcessBean" visible="false" />

	<fr:edit id="phdIndividualProgramProcessBean-remarks" name="phdIndividualProgramProcessBean">
		<fr:schema bundle="PHD_RESOURCES"  type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean">
			<fr:slot name="remarks" required="true" layout="longText" >
				<fr:property name="rows" value="4" />
				<fr:property name="columns" value="40" />
			</fr:slot>
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=transferProcessInvalid&amp;processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
