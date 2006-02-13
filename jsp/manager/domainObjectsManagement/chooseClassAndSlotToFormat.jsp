<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2>Formatação de Propriedades</h2>
<br />
<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
	<span class="error"><bean:write name="message"/></span>
</html:messages>


<html:form action="/domainObjectStringPropertyFormatter" >
	<html:hidden property="method" value="formatProperty" />

	<html:select property="domainObjectClass" onchange="this.form.method.value='chooseClass';this.form.submit();">
		<html:options collection="domainClasses" property="value" labelProperty="label"/>
	</html:select>

	<br/><br/>
		
	<logic:present name="classSlots" >
		<html:select property="slotName" >
			<html:options collection="classSlots" property="value" labelProperty="label"/>
		</html:select>	
		
		<br/><br/>
		
		<html:submit styleClass="inputbutton">
			Formatar
		</html:submit>
		
	</logic:present>

		
</html:form>