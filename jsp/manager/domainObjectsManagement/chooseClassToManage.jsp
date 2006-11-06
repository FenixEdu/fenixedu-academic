<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="message">
	<span class="error"><!-- Error messages go here --><bean:write name="message"/></span>
</logic:present>

<h2>
	<bean:write name="UserView" property="person.name"/>, tens a certeza que sabes o que estás a fazer?!? 
</h2>	
<h2>
	Pensa bem antes de fazeres #$%&!...
</h2>
<br/><br/>

<html:form action="/domainObjectManager" >
	
	<html:hidden property="idPos1Index" />
	<html:hidden property="idPos2Index" />
	<html:hidden property="idPos3Index" />

	<bean:message key="documentIdNumber" bundle="MANAGER_RESOURCES"/>:&nbsp;	
	<b><bean:write name="domainObjectManagerForm" property="idPos1Index" />º</b>
	<html:text property="idPos1Value" size="1" /> 
	<b><bean:write name="domainObjectManagerForm" property="idPos2Index" />º</b>
	<html:text property="idPos2Value" size="1" /> 
	<b><bean:write name="domainObjectManagerForm" property="idPos3Index" />º</b>
	<html:text property="idPos3Value" size="1" />

	<br/><br/><br/>	
	
	<bean:message key="label.domainObjectManager.class" bundle="MANAGER_RESOURCES"/>:&nbsp;
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.domainObjectClass" property="classToManage" >
		<html:options collection="domainClasses" property="value" labelProperty="label"/>
	</html:select>
	<bean:message key="label.domainObjectManager.oid" bundle="MANAGER_RESOURCES"/>:&nbsp;
	<html:text property="classToManageId" />

	<br/><br/><br/>		
	<html:radio property="method" value="prepareEditObject">
		<bean:message key="label.domainObjectManagement.editObject" bundle="MANAGER_RESOURCES"/>
	</html:radio>
	<html:radio property="method" value="deleteObject">
		<bean:message key="label.domainObjectManagement.deleteObject" bundle="MANAGER_RESOURCES"/>
	</html:radio>
		
	<br/><br/><br/>	
	<html:submit ><bean:message key="button.submit"/></html:submit>
		
</html:form>