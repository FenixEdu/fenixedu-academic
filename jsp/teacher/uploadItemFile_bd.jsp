<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
 
<h2><bean:write name="siteView" property="component.item.name"/></h2>
 
<h3>Inserir Ficheiro</h3>

 <html:form action="/fileUpload.do?method=fileUpload" enctype="multipart/form-data">
 	<bean:define id="itemCode" name="siteView" property="component.item.idInternal"/>
 	<bean:define id="objectCode" name="siteView" property="component.item.infoSection.infoSite.infoExecutionCourse.idInternal"/>
 	<bean:define id="currentSection" name="siteView" property="component.item.infoSection"/>
	<bean:define id="currentSectionCode" name="currentSection" property="idInternal"/>
	<html:hidden property="currentSectionCode" value="<%= currentSectionCode.toString() %>" />
 	<html:hidden  property="itemCode" value="<%= itemCode.toString() %>"/>
 	<html:hidden  property="objectCode" value="<%= objectCode.toString() %>"/>
 
	<strong>Nome que pretende para a visualização do ficheiro:</strong>
	<br/>	
 	<html:text property="displayName" size="55"/>
 	<br/> 	
 	<br/>
 	<strong>Ficheiro:</strong>
 	<br/>
 	<html:file property="theFile" size="40"/>
	<br />
	<br />
	<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
	</html:reset>
 </html:form> 

