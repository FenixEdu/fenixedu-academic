<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="DataBeans.InfoWebSiteItem, java.sql.Timestamp" %>
<span class="error"><html:errors/></span>

<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="section" name="sectionsList">
		<logic:equal name="section" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
			<b><bean:message key="label.list"/>&nbsp;<bean:write name="section" property="name"/></b>
			<bean:define id="sectionName" name="section" property="name"/>
			<bean:size id="itemsSize" name="section" property="infoItemsList"/>
		</logic:equal>
	</logic:iterate> 
</logic:notEmpty>

<bean:define id="sectionName" name="sectionName"/>
<bean:define id="itemsSize" name="itemsSize"/>

<logic:equal name="itemsSize" value="0">
	<p><bean:message key="message.nonExisting" arg0="<%= sectionName.toString() %>"/></p>
</logic:equal>
<logic:notEqual name="itemsSize" value="0">
	<html:form action="listItemsManagement">
		<html:hidden property="method" value="deleteItems"/>
		<html:hidden property="objectCode" value="<%=pageContext.findAttribute("objectCode").toString()%>"/>
		<table>
			<tr><td colspan="5" class="infoop"><bean:message key="message.listInstructions"/></td></tr>
			<tr><td><br/></td></tr>
			<tr>
				<td class="listClasses-header"><bean:message key="message.delete"/></td>
				<td class="listClasses-header"><bean:message key="message.item.title"/></td>
				<td class="listClasses-header"><bean:message key="label.author"/></td>
				<td class="listClasses-header"><bean:message key="label.status"/></td>
				<td class="listClasses-header"><bean:message key="label.date"/></td>
			</tr>
			<logic:notEmpty name="sectionsList" >
				<logic:iterate id="section" name="sectionsList">
					<logic:equal name="section" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
						<html:hidden property="itemsListSize" value="<%= itemsSize.toString() %>"/>
						
						<logic:iterate id="item" name="section" property="infoItemsList" indexId="itemId" type="DataBeans.InfoWebSiteItem">
							<tr>
								<bean:define id="itemCode" name="item" property="idInternal"/>
								<td class="listClasses">
									<html:checkbox name="item" property="toDelete" indexed="true"/>
									<html:hidden name="item" property="itemCode" value="<%= itemCode.toString() %>" indexed="true" />
								</td>
								<td class="listClasses">
									<html:link page="<%="/editItemsManagement.do?method=prepareEditItem&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;itemCode=" + itemCode %>">
										<bean:write name="item" property="title"/>
									</html:link>
								</td>
								<td class="listClasses">
									<bean:write name="item" property="authorName"/>
								</td>
								<logic:equal name="item" property="published" value="true">
									<td class="listClasses"><bean:message key="message.published"/></td>
								</logic:equal>
								<logic:equal name="item" property="published" value="false">
									<td class="listClasses"><bean:message key="message.notPublished"/></td>
								</logic:equal>
								<td class="listClasses">
									<bean:define id="creationDate" name="item" property="creationDate" type="java.sql.Timestamp"/>
									<%=creationDate.toString().substring(0, creationDate.toString().indexOf("."))%>
								</td>
							</tr>
						</logic:iterate>
					</logic:equal>
				</logic:iterate> 
			</logic:notEmpty>
		</table>
		<br/>
		<html:submit styleClass="inputbutton"><bean:message key="button.delete"/>                    		         	
		</html:submit> 
	</html:form>
</logic:notEqual>