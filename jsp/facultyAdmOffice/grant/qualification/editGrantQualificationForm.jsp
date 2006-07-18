<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.qualification.edition"/></p></strong><br/>

<html:form action="/editGrantQualification" style="display:inline">
	<%-- Presenting errors --%>
	<logic:messagesPresent>
	<span class="error">
		<html:errors/>
	</span><br/>
	</logic:messagesPresent>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="doEdit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<%-- grant type --%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idGrantOwner" property="idGrantOwner"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idPerson" property="idPerson"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.username" property="username"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantOwnerNumber" property="grantOwnerNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idQualification" property="idQualification"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degree"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.degree" property="degree" size="40"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.title"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.title" property="title" size="30"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.school"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.school" property="school" size="40"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.mark"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.mark" property="mark" size="10"/>
				&nbsp;<bean:message key="label.integer"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.qualificationDate"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.qualificationDate" property="qualificationDate" size="10"/>
				&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.branch"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.branch" property="branch" size="30"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.specializationArea"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.specializationArea" property="specializationArea" size="30"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degreeRecognition"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.degreeRecognition" property="degreeRecognition" size="40"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.country"/>:&nbsp;
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.country" property="country">
					<html:options collection="countryList" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceSchool"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.equivalenceSchool" property="equivalenceSchool" size="40"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceDate"/>:&nbsp;
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.equivalenceDate" property="equivalenceDate" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Save the qualification --%>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<%-- Cancel edit, Manage qualification --%>
				<html:form action="/manageGrantQualification" style="display:inline">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareManageGrantQualificationForm"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idPerson" property="idPerson" value='<%= request.getAttribute("idPerson").toString() %>'/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.username" property="username" value='<%= request.getAttribute("username").toString() %>'/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/> 
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.grantOwnerNumber" property="grantOwnerNumber" value='<%= request.getAttribute("grantOwnerNumber").toString() %>'/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form> 
			</td>
		</tr>
	</table>