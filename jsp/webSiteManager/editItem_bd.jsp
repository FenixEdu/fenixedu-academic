<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="section" name="sectionsList">
		<logic:equal name="section" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
			<b><bean:message key="label.edit"/>&nbsp;<bean:write name="section" property="name"/></b>
			<bean:define id="section" name="section"/>
		</logic:equal>
	</logic:iterate> 
</logic:notEmpty>

<span class="error"><html:errors/></span>

<html:form action="/editItemsManagement">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="editItem"/>
	<html:hidden property="objectCode" value="<%=pageContext.findAttribute("objectCode").toString()%>"/>
	<html:hidden property="itemCode" value="<%=pageContext.findAttribute("itemCode").toString()%>"/>
	<table>
	
	<tr><td><bean:message key="label.title"/></td></tr>
	<tr><td><html:text size="66" property="title"/></td></tr>
	
	<tr><td><bean:message key="label.mainEntryText"/>:</td></tr>
	<tr><td><html:textarea rows="7" cols="50" property="mainEntryText"/></td><tr/>
	
	<tr>
		<td>
			<bean:message key="label.excerpt"/>&nbsp;(<bean:message key="label.until"/>&nbsp;
			<bean:write name="section" property="excerptSize"/>&nbsp;<bean:message key="label.words"/>):
		</td>
	</tr>
	<tr><td><html:textarea rows="3" cols="50" property="excerpt"/></td><tr/>
	
	<tr><td><bean:message key="label.keywords"/>:</td></tr>
	<tr><td><html:textarea rows="3" cols="50" property="keywords"/></td><tr/>

	<tr>
		<td>
			<br/>
			<bean:message key="label.itemValidity"/><bean:message key="message.dateFormat"/>:
			<bean:message key="label.from"/><html:text property="itemBeginDay" size="10" maxlength="10"/>
			<bean:message key="label.until"/><html:text property="itemEndDay" size="10" maxlength="10"/>
		</td>
	<tr/>

	<tr>
		<td><br/><bean:message key="label.publish"/>&nbsp;<bean:message key="label.item"/><html:checkbox property="publish"/></td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="label.onlineDay"/><bean:message key="message.dateFormat"/>:
			<bean:message key="label.from"/><html:text property="onlineBeginDay" size="10" maxlength="10"/>
			<bean:message key="label.until"/><html:text property="onlineEndDay" size="10" maxlength="10"/>
		</td>
	<tr/>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
	</html:submit> 
	<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>  
</html:form>
