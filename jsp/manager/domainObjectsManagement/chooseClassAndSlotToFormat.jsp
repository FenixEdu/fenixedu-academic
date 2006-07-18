<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2>Formata��o de Propriedades</h2>
<br />
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><bean:write name="message"/></span>
</html:messages>


<html:form action="/domainObjectStringPropertyFormatter" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="formatProperty" />

	<html:select bundle="HTMLALT_RESOURCES" altKey="select.domainObjectClass" property="domainObjectClass" onchange="this.form.method.value='chooseClass';this.form.submit();">
		<html:options collection="domainClasses" property="value" labelProperty="label"/>
	</html:select>

	<br/><br/>
		
	<logic:present name="classSlots" >
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.slotName" property="slotName" >
			<html:options collection="classSlots" property="value" labelProperty="label"/>
		</html:select>	
		
		<br/><br/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			Formatar
		</html:submit>
		
	</logic:present>

		
</html:form>