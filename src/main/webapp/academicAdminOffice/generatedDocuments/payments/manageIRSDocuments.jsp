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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.docs.IRSCustomDeclaration.IRSDeclarationDTO"%>

<html:xhtml/>

<h2><strong><bean:message key="label.documents.anualIRS" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<p>
	<html:link page="/payments.do?method=showOperations" paramId="personId" paramName="person" paramProperty="externalId">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>

<logic:messagesPresent message="true" property="success">
	<div class="success5 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="success">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.person" /></strong></p>
<fr:view name="person" 	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<br/>

<logic:empty name="declarationDTO">
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documents" /></strong></p>
	<ul>
		<li>
			<html:link action="/generatedDocuments.do?method=prepareGenerateNewIRSDeclaration" paramId="personId" paramName="person" paramProperty="externalId">
				<bean:message key="label.new.irs.annual.document.create" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>

	<logic:empty name="annualIRSDocuments">
		<bean:message  key="label.documents.noDocumentFound" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="annualIRSDocuments">	
		<fr:view name="annualIRSDocuments" schema="AnnualIRSDeclarationDocument.view">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
				<fr:property name="columnClasses" value=",,nowrap,nowrap," />
			
				<fr:link name="generate" label="label.new.irs.annual.document.generate,ACADEMIC_OFFICE_RESOURCES" 
					link="/generatedDocuments.do?method=generateAgainAnnualIRSDeclarationDocument&amp;annualIRSDocumentOid=${externalId}"  />

				<fr:property name="sortBy" value="year=desc" />
			</fr:layout>
			
		</fr:view>
	</logic:notEmpty>
</logic:empty>

<logic:notEmpty name="declarationDTO">

	<bean:define id="personId" name="person" property="externalId" />

	<fr:edit id="declarationDTO" name="declarationDTO"
		action="<%= "/generatedDocuments.do?method=generateNewIRSDeclaration&personId=" + personId %>">
		
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="<%= IRSDeclarationDTO.class.getName()  %>">
			<fr:slot name="civilYear" key="label.new.irs.annual.civil.year" required="true" />
		</fr:schema>
					
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/generatedDocuments.do?method=generateNewIRSDeclarationInvalid&personId=" + personId %>"/>
		<fr:destination name="cancel" path="<%= "/generatedDocuments.do?method=showAnnualIRSDocumentsInPayments&personId=" + personId %>"/>
	</fr:edit>
</logic:notEmpty>
