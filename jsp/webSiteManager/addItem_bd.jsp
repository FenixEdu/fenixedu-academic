<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="sectionElem" name="sectionsList">
		<logic:equal name="sectionElem" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
			<h2><bean:message key="label.add"/>&nbsp;<bean:write name="sectionElem" property="name"/></h2>
			<bean:define id="section" name="sectionElem"/>
		</logic:equal>
	</logic:iterate> 
</logic:notEmpty>

<table>
	<tr><td class="infoop"><bean:message key="message.addInstructions"/></td></tr>
	<tr><td><span class="error"><html:errors/></span><br /></td></tr>
</table>
<html:form action="/itemsManagement">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="addItem"/>
	<html:hidden property="objectCode" value="<%=pageContext.findAttribute("objectCode").toString()%>"/>
	<logic:notEqual name="section" property="whatToSort" value="CREATION_DATE">
		<html:hidden property="creationDate"/>
	</logic:notEqual>
	<table>
		<tr><td colspan="2"><bean:message key="label.title"/></td></tr>
		<tr><td colspan="2"><html:text size="86" property="title"/></td></tr>
		
		<tr><td colspan="2"><bean:message key="label.mainEntryText"/>:</td></tr>
		<tr><td colspan="2"><html:textarea rows="6" cols="70" property="mainEntryText"/></td><tr/>
		<tr><td colspan="2" class="px9"><bean:message key="message.html.allowed"/><br /></td><tr/>
		
		<tr>
			<td colspan="2">
				<bean:message key="label.excerpt"/>&nbsp;(<bean:message key="label.until"/>&nbsp;
				<bean:write name="section" property="excerptSize"/>&nbsp;<bean:message key="label.words"/>):
			</td>
		</tr>
		<tr><td colspan="2"><html:textarea rows="3" cols="70" property="excerpt"/></td><tr/>
		
		<tr><td colspan="2"><bean:message key="label.keywords"/>&nbsp;(<bean:message key="message.keyword.help" />):</td></tr>
		<tr><td colspan="2"><html:textarea rows="3" cols="70" property="keywords"/></td><tr/>
	
		<tr>
			<td><bean:message key="label.authorName"/>:</td>
			<td><bean:message key="label.authorEmail"/>:</td>
		</tr>
		<tr>
			<td><html:text size="42" property="authorName"/></td>
			<td><html:text size="42" property="authorEmail"/></td>
		</tr>
	
		<tr>
			<td colspan="2">
				<br />
				<logic:equal name="section" property="whatToSort" value="CREATION_DATE">
					<bean:message key="label.creation.date"/><bean:message key="message.dateFormat"/>:
					<html:text property="creationDate" size="10" maxlength="10"/>
				</logic:equal>
				<logic:equal name="section" property="whatToSort" value="ITEM_BEGIN_DAY">
					<bean:message key="label.itemValidity"/><bean:message key="message.dateFormat"/>:
					<bean:message key="label.from"/><html:text property="itemBeginDay" size="10" maxlength="10"/>
					<bean:message key="label.until"/><html:text property="itemEndDay" size="10" maxlength="10"/>
				</logic:equal>
				<logic:equal name="section" property="whatToSort" value="ITEM_END_DAY">
					<bean:message key="label.itemValidity"/><bean:message key="message.dateFormat"/>:
					<bean:message key="label.from"/><html:text property="itemBeginDay" size="10" maxlength="10"/>
					<bean:message key="label.until"/><html:text property="itemEndDay" size="10" maxlength="10"/>
				</logic:equal>
				<br /><br />
			</td>
		<tr/>
	
	<%--	<tr>
			<td><br/><bean:message key="label.publish"/>&nbsp;<bean:message key="label.item"/><html:checkbox property="publish"/></td>
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
				<bean:message key="label.from"/><html:text property="onlineBeginDay" size="10" maxlength="10"/>
				<bean:message key="label.until"/><html:text property="onlineEndDay" size="10" maxlength="10"/>
			</td>
		</tr>
		<tr>
			<td colspan="2"><hr></hr></td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
	</html:submit> 
	<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>  
</html:form>
