<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson" %>


<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.view"/></h2>
<center>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	
	<br/>
	
	<bean:define id="externalPerson" name="<%= SessionConstants.EXTERNAL_PERSON %>" />
	<table>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.name"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.nome"/>
			</td>
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.person.sex"/>&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.sexo"/>
			</td>
		</tr>		
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoInstitution.name"/>
			</td>
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.address"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.morada"/>
			</td>
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.phone"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.telefone"/>
			</td>
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.mobile"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.telemovel"/>
			</td>
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.homepage"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.enderecoWeb"/>
			</td>
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.email"/>:&nbsp;
			</th>
			<td align="left">
				<bean:write name="externalPerson" property="infoPerson.email"/>
			</td>
		</tr>					
		
	</table>

</center>