<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<br />
<logic:present name="message">
	<span class="error"><!-- Error messages go here --><bean:write name="message"/></span>
</logic:present>
<br />
<h2>
	<bean:write name="UserView" property="person.name"/>, tens a certeza que sabes o que estás a fazer?!? 
</h2>	
<h2>
	Pensa bem antes de fazeres #$%&!...
</h2>
<br/><br/>

<html:form action="/domainObjectManager" >
	<html:hidden property="method" value="deleteObject" />
	<html:hidden property="idPos1Index" />
	<html:hidden property="idPos2Index" />
	<html:hidden property="idPos3Index" />

	<bean:write name="domainObjectManagerForm" property="idPos1Index" />º
	<html:text property="idPos1Value" size="1" /> 
	<bean:write name="domainObjectManagerForm" property="idPos2Index" />º
	<html:text property="idPos2Value" size="1" /> 
	<bean:write name="domainObjectManagerForm" property="idPos3Index" />º
	<html:text property="idPos3Value" size="1" />

	<br/><br/>
	
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.domainObjectClass" property="classToManage" >
		<html:options collection="domainClasses" property="value" labelProperty="label"/>
	</html:select>
	<html:text property="classToManageId" />

	<br/><br/>
		
	<html:submit ><bean:message key="button.submit"/></html:submit>
		
</html:form>