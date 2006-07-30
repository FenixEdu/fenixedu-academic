<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.publications.Management"/></h2>
<logic:present name="infoPublication"> 
		<logic:messagesPresent>
		<span class="error"><!-- Error messages go here -->
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
									<logic:notEmpty name="infoAuthor" property="author">
										<bean:write name="infoAuthor" property="author" />
									</logic:notEmpty>
									<logic:empty name="infoAuthor" property="author">
										<logic:notEmpty name="infoAuthor" property="infoPessoa.nome">
											<bean:write name="infoAuthor" property="infoPessoa.nome" />
										</logic:notEmpty>
										<logic:empty name="infoAuthor" property="infoPessoa.nome">
											Designacao de autor nao especificada
										</logic:empty>
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
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value="<%= pageContext.findAttribute("id1").toString() %>" />
						--%>
						
						<%--
						<bean:define id="idInternal" name="infoPublication" property="idInternal"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" />
						--%>
			
						<bean:define id="idInternal" name="infoPublication" property="idInternal"/>						
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value="<%= idInternal.toString() %>" />
			
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" />
						
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='delete' " %>'>
							<bean:message key="button.confirm"/>	
						</html:submit>

						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= "this.form.method.value='cancel' " %>'>
							<bean:message key="button.cancel"/>
						</html:submit>

					</html:form>
				</td>
			</tr>
		</table>
	
		
</logic:present>