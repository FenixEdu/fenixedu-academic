<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="message">
	<span class="error"><!-- Error messages go here --><bean:write name="message"/></span>
</logic:present>

<h2>
	<bean:write name="UserView" property="person.name"/>, tens a certeza que sabes o que est�s a fazer?!? 
</h2>	
<h2>
	Pensa bem antes de fazeres #$%&!...
</h2>
<br/><br/>

<html:form action="/domainObjectManager" >
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idPos1Index" property="idPos1Index" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idPos2Index" property="idPos2Index" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idPos3Index" property="idPos3Index" />

	<bean:message key="documentIdNumber" bundle="MANAGER_RESOURCES"/>:&nbsp;	
	<b><bean:write name="domainObjectManagerForm" property="idPos1Index" />�</b>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.idPos1Value" property="idPos1Value" size="1" /> 
	<b><bean:write name="domainObjectManagerForm" property="idPos2Index" />�</b>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.idPos2Value" property="idPos2Value" size="1" /> 
	<b><bean:write name="domainObjectManagerForm" property="idPos3Index" />�</b>
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.idPos3Value" property="idPos3Value" size="1" />

	<br/><br/><br/>	
	
	<bean:message key="label.domainObjectManager.class" bundle="MANAGER_RESOURCES"/>:&nbsp;
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.domainObjectClass" property="classToManage" >
		<html:options collection="domainClasses" property="value" labelProperty="label"/>
	</html:select>
	<bean:message key="label.domainObjectManager.oid" bundle="MANAGER_RESOURCES"/>:&nbsp;
	<html:text bundle="HTMLALT_RESOURCES" altKey="text.classToManageId" property="classToManageId" />

	<br/><br/><br/>		
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.method" property="method" value="prepareEditObject">
		<bean:message key="label.domainObjectManagement.editObject" bundle="MANAGER_RESOURCES"/>
	</html:radio>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.method" property="method" value="deleteObject">
		<bean:message key="label.domainObjectManagement.deleteObject" bundle="MANAGER_RESOURCES"/>
	</html:radio>
		
	<br/><br/><br/>	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message key="button.submit"/></html:submit>
		
</html:form>