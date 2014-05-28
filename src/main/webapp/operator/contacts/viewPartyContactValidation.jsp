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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.contacts.validate.address" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<logic:present role="role(OPERATOR)">

		<h3 class="mtop2 mbottom05">Informação do Pedido</h3>
		<fr:view name="physicalAddressValidation" schema="contacts.PhysicalAddressValidation.view">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
		<h3 class="mtop2 mbottom05"><bean:message key="label.professionalData" bundle="CONTRACTS_RESOURCES"/></h3>
		<logic:notEmpty name="person" property="personProfessionalData">
			<bean:define id="changeAddressIRSFormURL">
				<a href="<%= request.getContextPath() %>/templates/Decl_CIRS_ART99.pdf">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.ADIST.form"/>
				</a>
			</bean:define>
				<p>
					<span class="infoop2">
						<bean:message key="label.contact.validation.message.ADIST" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= changeAddressIRSFormURL %>" />
					</span>
    			</p>
    		<bean:define id="professionalData" name="person" property="personProfessionalData"/>
			<logic:iterate id="giafProfessionalData" name="professionalData" property="giafProfessionalDatas">
				<fr:view name="giafProfessionalData" schema="view.person.personProfessionalData">
					<fr:layout name="matrix">
						<fr:property name="classes" value="tstyle1 thlight mtop025" />
						<fr:property name="slot(professionalContractType)" value="professionalContractType"/>
						<fr:property name="row(professionalContractType)" value="0"/>
						<fr:property name="column(professionalContractType)" value="0"/>
						<fr:property name="columnSpan(professionalContractType)" value="3"/>
						
						<fr:property name="slot(giafPersonIdentification)" value="giafPersonIdentification"/>
						<fr:property name="row(giafPersonIdentification)" value="1"/>
						<fr:property name="column(giafPersonIdentification)" value="0"/>
						<fr:property name="columnSpan(giafPersonIdentification)" value="3"/>
						
						<fr:property name="slot(institutionEntryDate)" value="institutionEntryDate"/>
						<fr:property name="row(institutionEntryDate)" value="2"/>
						<fr:property name="column(institutionEntryDate)" value="0"/>
						<fr:property name="columnSpan(institutionEntryDate)" value="3"/>
						
						
						<fr:property name="slot(contractSituation)" value="contractSituation"/>
						<fr:property name="row(contractSituation)" value="3"/>
						<fr:property name="column(contractSituation)" value="0"/>
						
						<fr:property name="slot(contractSituationDate)" value="contractSituationDate"/>
						<fr:property name="labelHidden(contractSituationDate)" value="true"/>
						<fr:property name="row(contractSituationDate)" value="3"/>
						<fr:property name="column(contractSituationDate)" value="1"/>
						
						<fr:property name="slot(terminationSituationDate)" value="terminationSituationDate"/>
						<fr:property name="labelHidden(terminationSituationDate)" value="true"/>
						<fr:property name="row(terminationSituationDate)" value="3"/>
						<fr:property name="column(terminationSituationDate)" value="2"/>
						
						<fr:property name="slot(professionalRelation)" value="professionalRelation"/>
						<fr:property name="row(professionalRelation)" value="4"/>
						<fr:property name="column(professionalRelation)" value="0"/>
						
						<fr:property name="slot(professionalRelationDate)" value="professionalRelationDate"/>
						<fr:property name="labelHidden(professionalRelationDate)" value="true"/>
						<fr:property name="row(professionalRelationDate)" value="4"/>
						<fr:property name="column(professionalRelationDate)" value="1"/>
						<fr:property name="columnSpan(professionalRelationDate)" value="2"/>
						
						<fr:property name="slot(professionalCategory)" value="professionalCategory"/>
						<fr:property name="row(professionalCategory)" value="5"/>
						<fr:property name="column(professionalCategory)" value="0"/>
						
						<fr:property name="slot(professionalCategoryDate)" value="professionalCategoryDate"/>
						<fr:property name="labelHidden(professionalCategoryDate)" value="true"/>
						<fr:property name="row(professionalCategoryDate)" value="5"/>
						<fr:property name="column(professionalCategoryDate)" value="1"/>
						<fr:property name="columnSpan(professionalCategoryDate)" value="2"/>
						
						<fr:property name="slot(professionalRegime)" value="professionalRegime"/>
						<fr:property name="row(professionalRegime)" value="6"/>
						<fr:property name="column(professionalRegime)" value="0"/>
						
						<fr:property name="slot(professionalRegimeDate)" value="professionalRegimeDate"/>
						<fr:property name="labelHidden(professionalRegimeDate)" value="true"/>
						<fr:property name="row(professionalRegimeDate)" value="6"/>
						<fr:property name="column(professionalRegimeDate)" value="1"/>
						<fr:property name="columnSpan(professionalRegimeDate)" value="2"/>
					</fr:layout>
				</fr:view>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="person" property="personProfessionalData">
			<bean:message key="label.contact.validation.message.no.professional.data" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</logic:empty>
		
		
		<h3 class="mtop2 mbottom05">Morada</h3>
		<fr:view name="physicalAddressValidation" property="partyContact" schema="contacts.PhysicalAddress.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
		
		<h3 class="mtop2 mbottom05">Validação</h3>
		<fr:form id="physicalAddressValidation" action="/validate.do?method=validate">
			<fr:edit id="physicalAddressValidation" name="physicalAddressValidation">
				<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.contacts.PhysicalAddressValidation">
					<fr:slot name="description" key="label.description" bundle="APPLICATION_RESOURCES" layout="longText">
						<fr:property name="rows" value="5"/>
						<fr:property name="columns" value="40"/>
					</fr:slot>
					<fr:slot name="state">
						<fr:property name="excludedValues" value="INVALID"/>
					</fr:slot>
				</fr:schema>
			</fr:edit>
			<html:submit value="Submeter"></html:submit>
		</fr:form>
</logic:present>