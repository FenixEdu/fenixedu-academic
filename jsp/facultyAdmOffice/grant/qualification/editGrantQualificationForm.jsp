<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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

	<html:hidden property="method" value="doEdit"/>
	<html:hidden property="page" value="1"/>

	<%-- grant type --%>
	<html:hidden property="idGrantOwner"/>
	<html:hidden property="idPerson"/>
	<html:hidden property="username"/>
	<html:hidden property="grantOwnerNumber"/>
	<html:hidden property="idQualification"/>

	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degree"/>:&nbsp;
			</td>
			<td>
				<html:text property="degree" size="40"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.title"/>:&nbsp;
			</td>
			<td>
				<html:text property="title" size="30"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.school"/>:&nbsp;
			</td>
			<td>
				<html:text property="school" size="40"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.mark"/>:&nbsp;
			</td>
			<td>
				<html:text property="mark" size="10"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.qualificationDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="qualificationDate" size="10"/>
				&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.branch"/>:&nbsp;
			</td>
			<td>
				<html:text property="branch" size="30"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.specializationArea"/>:&nbsp;
			</td>
			<td>
				<html:text property="specializationArea" size="30"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degreeRecognition"/>:&nbsp;
			</td>
			<td>
				<html:text property="degreeRecognition" size="40"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.country"/>:&nbsp;
			</td>
			<td>
				<html:select property="country">
					<html:options collection="countryList" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceSchool"/>:&nbsp;
			</td>
			<td>
				<html:text property="equivalenceSchool" size="40"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="equivalenceDate" size="10"/>&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
	</table>

	<br/>

	<table>
		<tr>
			<td>
				<%-- Save the qualification --%>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.save"/>
				</html:submit>
</html:form>
			</td>
			<td>
				<%-- Cancel edit, Manage qualification --%>
				<html:form action="/manageGrantQualification" style="display:inline">
					<html:hidden property="method" value="prepareManageGrantQualificationForm"/>
					<html:hidden property="idPerson" value='<%= request.getAttribute("idPerson").toString() %>'/>
					<html:hidden property="username" value='<%= request.getAttribute("username").toString() %>'/>
					<html:hidden property="idInternal" value='<%= request.getAttribute("idGrantOwner").toString() %>'/> 
					<html:hidden property="grantOwnerNumber" value='<%= request.getAttribute("grantOwnerNumber").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form> 
			</td>
		</tr>
	</table>