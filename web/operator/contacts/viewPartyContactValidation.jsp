<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.contacts.validate.address" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<logic:present role="OPERATOR">

		<h3 class="mtop2 mbottom05">Informação do Pedido</h3>
		<fr:view name="physicalAddressValidation" schema="contacts.PhysicalAddressValidation.view">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
		
		<h3 class="mtop2 mbottom05">Morada</h3>
		<fr:view name="physicalAddressValidation" property="partyContact" schema="contacts.PhysicalAddress.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
		
		<h3 class="mtop2 mbottom05">Validação</h3>
		<fr:form id="physicalAddressValidation" action="/validate.do?method=prepare">
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