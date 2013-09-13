<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
	
<logic:present name="needToCreatePerson">
	<div class="warning0">
		<strong><bean:message key="label.attention" bundle="LIBRARY_RESOURCES"/>:</strong><br/>
		<bean:message key="message.card.createNewPerson" bundle="LIBRARY_RESOURCES"/>
	</div>
</logic:present>	
<fr:form action="/cardManagement.do?method=createPerson">
	<fr:edit id="createPerson" name="externalPersonBean" schema="library.card.create.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>	
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>	
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.back">
			<bean:message key="button.back" bundle="LIBRARY_RESOURCES"/>
		</html:cancel>
		<logic:present name="needToCreatePerson">
			<html:submit property="createPerson"><bean:message key="button.createExternalPerson" bundle="LIBRARY_RESOURCES"/></html:submit>
		</logic:present>
	</p>
</fr:form>