<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="title.unitSite.manage.sites" bundle="MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
	<p class="error0">
		<bean:write name="messages" />
	</p>
	</html:messages>
</logic:messagesPresent>

<bean:define id="siteOid" name="siteOid"></bean:define>
<fr:form action="<%="/unitSiteManagement.do?method=createEntryPoint&oid="+siteOid%>"> 
	
	<fr:edit id="multiLanguageStringBean" name="multiLanguageStringBean"> 
	 	<fr:schema bundle="SITE_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean"> 
			<fr:slot name="MLString" key="label.name"></fr:slot>
	 	</fr:schema>
	</fr:edit>
	
	<html:submit> 
		<bean:message key="label.submit"/>
	</html:submit>
</fr:form>
