<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.publications.Management"/></h2>
<logic:present name="infoPublication"> 
		<logic:messagesPresent>
		<span class="error">
			<html:errors/>
		</span>
		</logic:messagesPresent>
		<br/>
		<bean:message key="message.publications.managementConfirmDelete"/>	
		<br />
		<br />
		<table style="text-align:left" width="75%">
			<tr>
				<td class="listClasses" style="text-align:left"> <bean:message key="message.publicationAttribute.title" /> </td>
				<td class="listClasses" style="text-align:left"> <bean:write name="infoPublication" property="title" /> </td>
			</tr>
			<tr>
				<td class="listClasses" style="text-align:left"><bean:message key="message.publications.publicationType" /></td>
				<td class="listClasses" style="text-align:left"><bean:write name="infoPublication" property="infoPublicationType.publicationType" /></td>
			</tr>
			<tr>
				<td valign="top" class="listClasses" style="text-align:left"> <bean:message key="message.publicationAttribute.authors" /> </td>
				<td class="listClasses" style="text-align:left">
					<table>
						<logic:iterate id="infoAuthor" name="infoPublication" property="infoPublicationAuthors">
							<tr>
								<td>
									<bean:write name="infoAuthor" property="author" />
									<logic:empty name="infoAuthor" property="author">
										Designacao de autor nao especificada
									</logic:empty>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</td>
			</tr>
			<tr>
				<td class="listClasses" style="text-align:left"><bean:message key="message.publicationAttribute.year" /></td>
				<td class="listClasses" style="text-align:left"><bean:write name="infoPublication" property="year" /></td>
			</tr>

		</table>
		<br />			

		<table>
			<tr>
				<td>
					<html:form action="/deletePublication">
						<%--
						<bean:define id="id1" name="infoPublication" property="idInternal"/>
						<html:hidden property="idInternal" value="<%= pageContext.findAttribute("id1").toString() %>" />
						--%>
						
						<%--
						<bean:define id="idInternal" name="infoPublication" property="idInternal"/>
						<html:hidden property="idInternal" />
						--%>
			
						<bean:define id="idInternal" name="infoPublication" property="idInternal"/>						
						<html:hidden property="idInternal" value="<%= idInternal.toString() %>" />
						
						<html:submit styleClass="inputbutton" property="confirm">
							<bean:message key="button.confirm"/>	
						</html:submit>
					</html:form>
				</td>
				
				<td>
					<html:form action="/publicationManagement">
						<html:submit styleClass="inputbutton" property="confirm">
							<bean:message key="button.cancel"/>
						</html:submit>
					</html:form>
				</td>	
			</tr>
		</table>
	
		
</logic:present>