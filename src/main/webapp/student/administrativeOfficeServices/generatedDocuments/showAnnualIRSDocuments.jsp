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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<logic:present role="role(STUDENT)">
	<h2><strong><bean:message key="label.documents.anualIRS" bundle="STUDENT_RESOURCES"/></strong></h2>
		
	<logic:empty name="annualIRSDocuments">
		<bean:message  key="label.documents.noDocumentFound" bundle="STUDENT_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="annualIRSDocuments">	
		<fr:view name="annualIRSDocuments" schema="AnnualIRSDeclarationDocument.view">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
				<fr:property name="columnClasses" value=",,nowrap,nowrap," />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
</logic:present>


