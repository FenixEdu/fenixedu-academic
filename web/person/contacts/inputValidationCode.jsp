<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<%-- <bean:define id="partyContactClass" scope="request" name="partyContactClass" /><!--  -->  --%>

<em><bean:message key="label.person.main.title" /></em>
<p><html:link page="/visualizePersonalInfo.do"> « Voltar </html:link></p>
<%--  <h2><bean:message key="<%= "label.partyContacts.add" +  partyContactClass %>" /></h2> --%>
 <h2>Validação de Contactos</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="success0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>





<logic:notPresent name="isPhysicalAddress">
<logic:present name="valid">
	<logic:equal name="valid" value="true">
		Contacto validado com sucesso!
	</logic:equal>
	<logic:equal name="valid" value="false">
		<logic:notEqual name="tries" value="3">
			<span class="error0"> Código inválido. Dispõe de <%= request.getAttribute("tries") %> tentativas.</span>
		</logic:notEqual>
	</logic:equal>
</logic:present>
<logic:equal name="valid" value="false">
	<logic:greaterThan name="tries" value="0">
		<html:link page="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="partyContactValidation">
			Requisitar código de validação	
		</html:link>
		<form action="partyContacts.do" method="get">
			<input type="hidden" name="method" value="inputValidationCode"/>
			<input type="hidden" name="partyContactValidation" value="<%= request.getAttribute("partyContactValidation") %>"/>
			Código Validação <input name="validationCode" type="text"/>
			<input type="submit" value="Validar">
		</form>
	</logic:greaterThan>
</logic:equal>
</logic:notPresent>

<logic:present name="isPhysicalAddress">
	<fr:form action="/partyContacts.do?method=validatePhysicalAddress" encoding="multipart/form-data">
		<fr:edit id="physicalAddressBean" name="physicalAddressBean" schema="contacts.validate.PhysicalAddress" layout="tabular-editable"/>
		<html:submit styleId="submitBtn">Submeter Ficheiro</html:submit>
	</fr:form>
</logic:present>
