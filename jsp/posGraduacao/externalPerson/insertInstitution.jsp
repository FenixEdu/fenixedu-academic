<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.insertInstitution"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>

<html:form action="/insertInstitution.do?method=insert" >
	<html:hidden property="page" value="1" />
	
	<table>
		<tr>
			<td>
				<!-- Name -->
				<bean:message key="label.masterDegree.administrativeOffice.institutionName"/>:
				<html:text property="name" />	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<!-- Submit button -->
				<html:submit styleClass="inputbuttonSmall">
					<bean:message key="button.submit.masterDegree.externalPerson.institution.insertInstitution"/>
				</html:submit>	
			</td>
		</tr>
	</table>
		
</html:form>

</center>