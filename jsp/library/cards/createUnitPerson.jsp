<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.create.person"/>
</h2>
	
<p>	
	<span class="error0">
		<html:errors/>
		<html:messages id="error" property="error" message="true">
			<bean:write name="error" />
		</html:messages>
	</span>
</p>
	
<logic:present name="needToCreateUnit">
	<div class="warning0">
		<strong><bean:message key="label.attention" bundle="LIBRARY_RESOURCES"/>:</strong><br/>
		<bean:message key="message.card.createNewUnit" bundle="LIBRARY_RESOURCES"/>
	</div>
</logic:present>

<fr:form action="/cardManagement.do?method=createUnitPerson">
	<fr:edit id="createUnitPerson" name="externalPersonBean" schema="library.card.create.unitPerson">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>	
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>
		<html:submit property="cancel" bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:submit>
		<logic:present name="needToCreateUnit">
			<html:submit property="createUnit"><bean:message key="button.createExternalUnit" bundle="LIBRARY_RESOURCES"/></html:submit>
		</logic:present>
	</p>
</fr:form>