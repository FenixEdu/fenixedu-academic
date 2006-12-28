<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="sectionElem" name="sectionsList">
		<logic:equal name="sectionElem" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
			<h2><bean:message key="label.edit"/>&nbsp;<bean:write name="sectionElem" property="name"/></h2>
			<bean:define id="section" name="sectionElem"/>
		</logic:equal>
	</logic:iterate> 
</logic:notEmpty>

<span class="error"><html:errors/></span>

<html:form action="/editItemsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editItem"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=pageContext.findAttribute("objectCode").toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.itemCode" property="itemCode" value="<%=pageContext.findAttribute("itemCode").toString()%>"/>
	<logic:notEqual name="section" property="whatToSort" value="CREATION_DATE">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.creationDate" property="creationDate"/>
	</logic:notEqual>
	<table>
		<tr><td colspan="2"><bean:message key="label.title"/></td></tr>
		<tr><td colspan="2"><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="86" property="title"/></td></tr>
		
		<tr><td colspan="2"><bean:message key="label.mainEntryText"/>:</td></tr>
		<tr><td colspan="2"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.mainEntryText" rows="6" cols="70" property="mainEntryText"/></td><tr/>
		<tr><td colspan="2" class="px9"><bean:message key="message.html.allowed"/><br /></td><tr/>
		
		<tr>
			<td colspan="2">
				<bean:message key="label.excerpt"/>&nbsp;(<bean:message key="label.until"/>&nbsp;
				<bean:write name="section" property="excerptSize"/>&nbsp;<bean:message key="label.words"/>):
			</td>
		</tr>
		<tr><td colspan="2"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.excerpt" rows="3" cols="70" property="excerpt"/></td><tr/>
		
		<tr><td colspan="2"><bean:message key="label.keywords"/>&nbsp;(<bean:message key="message.keyword.help" />):</td></tr>
		<tr><td colspan="2"><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.keywords" rows="3" cols="70" property="keywords"/></td><tr/>
	
		<tr>
			<td><bean:message key="label.authorName"/>:</td>
			<td><bean:message key="label.authorEmail"/>:</td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.authorName" size="42" property="authorName"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.authorEmail" size="42" property="authorEmail"/></td>
		</tr>
	
		<tr>
			<td colspan="2">
				<br />
				<logic:equal name="section" property="whatToSort" value="CREATION_DATE">
					<bean:message key="label.creation.date"/><bean:message key="message.dateFormat"/>:
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.creationDate" property="creationDate" size="10" maxlength="10"/>
				</logic:equal>
				<logic:equal name="section" property="whatToSort" value="ITEM_BEGIN_DAY">
					<bean:message key="label.itemValidity"/><bean:message key="message.dateFormat"/>:
					<bean:message key="label.from"/><html:text bundle="HTMLALT_RESOURCES" altKey="text.itemBeginDay" property="itemBeginDay" size="10" maxlength="10"/>
					<bean:message key="label.until"/><html:text bundle="HTMLALT_RESOURCES" altKey="text.itemEndDay" property="itemEndDay" size="10" maxlength="10"/>
				</logic:equal>
				<logic:equal name="section" property="whatToSort" value="ITEM_END_DAY">
					<bean:message key="label.itemValidity"/><bean:message key="message.dateFormat"/>:
					<bean:message key="label.from"/><html:text bundle="HTMLALT_RESOURCES" altKey="text.itemBeginDay" property="itemBeginDay" size="10" maxlength="10"/>
					<bean:message key="label.until"/><html:text bundle="HTMLALT_RESOURCES" altKey="text.itemEndDay" property="itemEndDay" size="10" maxlength="10"/>
				</logic:equal>
				<br /><br />
			</td>
		<tr/>
	
	<%--	<tr>
			<td><br/><bean:message key="label.publish"/>&nbsp;<bean:message key="label.item"/><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.publish" property="publish"/></td>
		</tr>--%>

		<tr>
			<td colspan="2"><hr><i><b><bean:message key="label.publishment" /></b></i></hr> - <bean:message key="message.publish.help" /><br /><br /></td>
		</tr>

		<tr>
			<td colspan="2">
				<bean:message key="label.publish.state"/>:
				<html:select property="publish">
					<html:option key="message.published" value="true"/>
					<html:option key="message.notPublished" value=""/>
				</html:select>
				&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;
				<bean:message key="label.onlineDay"/><bean:message key="message.dateFormat"/>:
				<bean:message key="label.from"/><html:text bundle="HTMLALT_RESOURCES" altKey="text.onlineBeginDay" property="onlineBeginDay" size="10" maxlength="10"/>
				<bean:message key="label.until"/><html:text bundle="HTMLALT_RESOURCES" altKey="text.onlineEndDay" property="onlineEndDay" size="10" maxlength="10"/>
			</td>
		</tr>
		<tr>
			<td colspan="2"><hr></hr></td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
	</html:submit> 
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>  
</html:form>
