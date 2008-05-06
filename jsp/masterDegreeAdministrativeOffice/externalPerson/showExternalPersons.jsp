<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>

<h2 align="center"><bean:message key="label.masterDegree.administrativeOffice.searchResults"/></h2>

<center>

	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<br/>

	<logic:present name="<%= SessionConstants.EXTERNAL_PERSONS_LIST %>" scope="request">
		<bean:define id="externalPersonsList" name="<%= SessionConstants.EXTERNAL_PERSONS_LIST %>" type="java.util.List"/>
		<table>
			<tr align="center">
				<td><strong><bean:message key="label.masterDegree.administrativeOffice.externalPersonName" /></strong></td>
				<td><strong><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution" /></strong></td>
				<th align="left">&nbsp;</th>
				<th align="left">&nbsp;</th>									
			</tr>		
			<tr align="center">
				<th align="left">&nbsp;</th>
				<th align="left">&nbsp;</th>
				<th align="left">&nbsp;</th>
				<th align="left">&nbsp;</th>									
			</tr>			
			<logic:iterate id="externalPerson" name="externalPersonsList">

				<bean:define id="linkViewExternalPerson">
					/viewExternalPerson.do?method=view&id=<bean:write name="externalPerson" property="idInternal"/>
				</bean:define>				
				<bean:define id="linkEditExternalPerson">
					/editExternalPerson.do?method=prepare&id=<bean:write name="externalPerson" property="idInternal"/>
				</bean:define>
				
				<tr>					
					<td align="left"><bean:write name="externalPerson" property="infoPerson.nome"/>&nbsp;</td>	
					<td align="left"><bean:write name="externalPerson" property="infoInstitution.name"/>&nbsp;</td>	
					<td align="center">
						<html:link page="<%= linkViewExternalPerson %>" ><bean:message key="link.masterDegree.administrativeOffice.viewDetails"/></html:link>&nbsp;
					</td>
					<td align="center">
						<html:link page="<%= linkEditExternalPerson %>" ><bean:message key="link.masterDegree.administrativeOffice.edit"/></html:link>
					</td>
				</tr>				
			</logic:iterate>			
		</table>
	</logic:present>	

</center>