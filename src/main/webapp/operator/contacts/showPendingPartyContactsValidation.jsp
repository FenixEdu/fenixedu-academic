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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.contacts.validate.address" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<logic:present role="role(OPERATOR)">
	 
	<logic:notEmpty name="partyContacts">
		<p class="mtop15 mbottom3"><b><bean:message key="label.contacts.validate.pending.address" bundle="ACADEMIC_OFFICE_RESOURCES"/></b></p>		
		<fr:view name="partyContacts" schema="contacts.PhysicalAddressValidation.list">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				<fr:property name="renderCompliantTable" value="true"/>
				
				<fr:property name="linkFormat(viewValidation)" value="<%= "/validate.do?method=viewPartyContactValidation&partyContactValidation=${externalId}" %>" />
				<fr:property name="key(viewValidation)" value="label.view"/>
				<fr:property name="bundle(viewValidation)" value="APPLICATION_RESOURCES"/>
 				<fr:property name="visibleIfNot(viewValidation)" value="valid" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="partyContacts">
		Não existem pedidos pendentes de validação de moradas.
	</logic:empty>
	
</logic:present>