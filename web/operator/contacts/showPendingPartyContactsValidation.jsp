<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.contacts.validate.address" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<logic:present role="OPERATOR">
	 
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