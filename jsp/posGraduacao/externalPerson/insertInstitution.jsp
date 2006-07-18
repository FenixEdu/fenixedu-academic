<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.insertInstitution"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>

<html:form action="/insertInstitution.do?method=insert" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	
	<table>
		<tr>
			<td>
				<!-- Name -->
				<bean:message key="label.masterDegree.administrativeOffice.institutionName"/>:
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" />	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<!-- Submit button -->
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
					<bean:message key="button.submit.masterDegree.externalPerson.institution.insertInstitution"/>
				</html:submit>	
			</td>
		</tr>
	</table>
		
</html:form>

</center>