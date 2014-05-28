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

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean" %>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.guidance.provider.PhdGuidanceDocumentTypeProvider" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<br/>

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=uploadGuidanceDocument&processId=" + processId %>" encoding="multipart/form-data">
	<fr:edit name="documentBean" id="documentBean" >
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramDocumentUploadBean.class.getName() %>">
			<fr:slot name="type" layout="menu-select" required="true">
				<fr:property name="providerClass" value="<%= PhdGuidanceDocumentTypeProvider.class.getName() %>" />
				<fr:property name="format" value="${localizedName}" />
			</fr:slot>
			<fr:slot name="file" required="true">
				<fr:validator name="<%= FileValidator.class.getName() %>" />
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>
			<fr:slot name="remarks" layout="longText" required="true">
				<fr:property name="rows" value="7" />
				<fr:property name="columns" value="50" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	
		<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=uploadGuidanceDocumentInvalid&processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=manageGuidanceDocuments&processId=" + processId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>

<%--  ### End of Operation Area  ### --%>
