<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem, java.sql.Timestamp" %>
<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<logic:notEmpty name="sectionsList" >
	<logic:iterate id="section" name="sectionsList">
		<logic:equal name="section" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
			<h2><bean:message key="label.list"/>&nbsp;<bean:write name="section" property="name"/></h2>
			<bean:define id="sectionName" name="section" property="name"/>
			<bean:size id="itemsSize" name="section" property="infoItemsList"/>
		</logic:equal>
	</logic:iterate> 
</logic:notEmpty>

<span class="error"><html:errors/></span>

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
			<tr><td colspan="5"><br/></td></tr>
<%-- Mostrar quando se fizer a ordenação de itens
			<tr>
				<td colspan="5">
					<html:select property="publish">
						<html:option key="message.all" value=""/>
						<html:option key="message.ten" value=""/>
						<html:option key="message.twentyFive" value=""/>
						<html:option key="message.fifty" value=""/>
					</html:select>
					<bean:message key="message.orderItems" />
					<html:select property="publish">
						<html:option key="message.item.title" value=""/>
						<html:option key="label.author" value=""/>
						<html:option key="label.status" value=""/>
						<html:option key="label.date" value=""/>
					</html:select>
					<bean:message key="message.way" />
					<html:select property="publish">
						<html:option key="message.ascendent" value=""/>
						<html:option key="message.descendent" value=""/>
					</html:select>
					<html:submit styleClass="inputbutton">
						<bean:message key="button.website.list"/>                    		         	
					</html:submit> 			
				</td>
			</tr>
			<tr><td><br/></td></tr>
--%>
			<tr>
				<td class="listClasses-header"><bean:message key="message.delete"/></td>
				<td class="listClasses-header">
					<html:link page="<%="/listItemsManagement.do?method=listItems&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;orderBy=title" %>">			
						<bean:message key="message.item.title"/>
					</html:link>
				</td>
				<td class="listClasses-header">
					<html:link page="<%="/listItemsManagement.do?method=listItems&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;orderBy=authorName" %>">			
						<bean:message key="label.author"/>
					</html:link>
				</td>
				<td class="listClasses-header">
					<html:link page="<%="/listItemsManagement.do?method=listItems&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;orderBy=published" %>">			
						<bean:message key="label.status"/>
					</html:link>
				</td>
				<td class="listClasses-header">
					<html:link page="<%="/listItemsManagement.do?method=listItems&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;orderBy=creationDate" %>">			
						<bean:message key="label.date"/>
					</html:link>
				</td>
			</tr>
			<logic:notEmpty name="sectionsList" >
				<logic:iterate id="section" name="sectionsList">
					<logic:equal name="section" property="idInternal" value="<%=pageContext.findAttribute("objectCode").toString()%>">
						<html:hidden property="itemsListSize" value="<%= itemsSize.toString() %>"/>
						
						<logic:iterate id="item" name="section" property="infoItemsList" indexId="itemId" type="net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem">
							<bean:define id="isEven">
								<%= String.valueOf(itemId.intValue() % 2) %>
							</bean:define>
							<logic:equal name="isEven" value="0"> <%-- Linhas impares --%>
								<tr>
									<bean:define id="itemCode" name="item" property="idInternal"/>
									<td class="listClasses">
										<html:checkbox name="item" property="toDelete" indexed="true"/>
										<html:hidden name="item" property="itemCode" value="<%= itemCode.toString() %>" indexed="true" />
									</td>
									<td class="listClasses" style="text-align: left;">
										<html:link page="<%="/editItemsManagement.do?method=prepareEditItem&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;itemCode=" + itemCode %>">
											<bean:write name="item" property="title"/>
										</html:link>
									</td>
									<td class="listClasses" style="text-align: left;">
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
							</logic:equal>			
							<logic:equal name="isEven" value="1"> <%-- Linhas pares  --%>
								<tr>
									<bean:define id="itemCode" name="item" property="idInternal"/>
									<td style="text-align: center;">
										<html:checkbox name="item" property="toDelete" indexed="true"/>
										<html:hidden name="item" property="itemCode" value="<%= itemCode.toString() %>" indexed="true" />
									</td>
									<td style="text-align: left;">
										<html:link page="<%="/editItemsManagement.do?method=prepareEditItem&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;itemCode=" + itemCode %>">
											<bean:write name="item" property="title"/>
										</html:link>
									</td>
									<td style="text-align: left;">
										<bean:write name="item" property="authorName"/>
									</td>
									<logic:equal name="item" property="published" value="true">
										<td style="text-align: center;"><bean:message key="message.published"/></td>
									</logic:equal>
									<logic:equal name="item" property="published" value="false">
										<td style="text-align: center;"><bean:message key="message.notPublished"/></td>
									</logic:equal>
									<td style="text-align: center;">
										<bean:define id="creationDate" name="item" property="creationDate" type="java.sql.Timestamp"/>
										<%=creationDate.toString().substring(0, creationDate.toString().indexOf("."))%>
									</td>
								</tr>
							</logic:equal>							
						</logic:iterate>
					</logic:equal>
				</logic:iterate> 
			</logic:notEmpty>
		</table>
		<br/>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.delete"/>                    		         	
		</html:submit> 
	</html:form>
</logic:notEqual>