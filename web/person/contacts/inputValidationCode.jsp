<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />


<em><bean:message key="label.person.main.title" /></em>
<p><html:link page="/visualizePersonalInfo.do"> « Voltar </html:link></p>
 <h2>Validação de Contactos</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="infoop2"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<logic:notPresent name="isPhysicalAddress">
<logic:present name="valid">
	<logic:equal name="valid" value="true">
		<span class="success0">Contacto validado com sucesso. A sua <html:link page="/visualizePersonalInfo.do">informação pessoal</html:link> foi actualizada.</span>
	</logic:equal>
	<logic:equal name="valid" value="false">
		<logic:notEqual name="tries" value="3">
			<p><span class="error0"> Código inválido. Dispõe de <%= request.getAttribute("tries") %> tentativas.</span></p>
		</logic:notEqual>
	</logic:equal>
</logic:present>
<logic:equal name="valid" value="false">
	<logic:greaterThan name="tries" value="0">
		<p class="mbottom2">
			<html:link page="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="partyContactValidation">
				Requisitar código de validação	
			</html:link>
		</p>
		<form action="<%= request.getContextPath() + "/person/partyContacts.do"%>" method="post">
			<input type="hidden" name="method" value="inputValidationCode"/>
			<input type="hidden" name="partyContactValidation" value="<%= request.getAttribute("partyContactValidation") %>"/>
			Código Validação <input name="validationCode" type="text"/>
			<input type="submit" value="Validar">
		</form>
	</logic:greaterThan>
</logic:equal>
</logic:notPresent>

<logic:present name="isPhysicalAddress">
	<div class="mtop2">
	<fr:form action="/partyContacts.do?method=validatePhysicalAddress" encoding="multipart/form-data">
		<fr:edit id="physicalAddressBean" name="physicalAddressBean" schema="contacts.validate.PhysicalAddress">
			<fr:layout name="tabular-editable">
				<fr:property name="columnClasses" value=",,tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit styleId="submitBtn">Submeter Ficheiro</html:submit>
	</fr:form>
	</div>
</logic:present>
