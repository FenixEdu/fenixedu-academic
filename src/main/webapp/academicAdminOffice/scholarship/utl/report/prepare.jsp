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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.academicAdminOffice.scholarship.utl.report" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>	

<fr:form action="/reportStudentsUTLCandidates.do?method=showReport" encoding="multipart/form-data" >
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-selection-year" name="bean" >
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report.ReportStudentsUTLCandidatesBean" 
			bundle="ACADEMIC_OFFICE_RESOURCES">
			
			<fr:slot name="executionYear" required="true" layout="menu-select" >
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
				<fr:property name="sortBy" value="year=desc" />
			</fr:slot>
			<fr:slot name="forFirstYear" />
			<fr:slot name="xlsFile" >
				<property name="fileNameSlot" value="fileName" />
				<property name="fileSizeSlot" value="fileSize" />
			</fr:slot>
			
		</fr:schema>
	</fr:edit>

	<p>
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>	
</fr:form>

<div>
	<html:link action="/reportStudentsUTLCandidates.do?method=prepareForOneStudent">Relatório por aluno</html:link>
</div>
