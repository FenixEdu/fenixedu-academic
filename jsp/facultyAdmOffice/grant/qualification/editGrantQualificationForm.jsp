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
	<html:hidden property="idInternal"/>
	<html:hidden property="idPerson"/>
	<html:hidden property="username"/>
	<html:hidden property="idQualification"/>
	<table>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degree"/>:&nbsp;
			</td>
			<td>
				<html:text property="degree"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.title"/>:&nbsp;
			</td>
			<td>
				<html:text property="title"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.school"/>:&nbsp;
			</td>
			<td>
				<html:text property="school"/><bean:message key="label.requiredfield"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.mark"/>:&nbsp;
			</td>
			<td>
				<html:text property="mark"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.qualificationDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="qualificationDate"/><bean:message key="label.requiredfield"/>
				&nbsp;<bean:message key="label.dateformat"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.branch"/>:&nbsp;
			</td>
			<td>
				<html:text property="branch"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.specializationArea"/>:&nbsp;
			</td>
			<td>
				<html:text property="specializationArea"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.degreeRecognition"/>:&nbsp;
			</td>
			<td>
				<html:text property="degreeRecognition"/>
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
				<html:text property="equivalenceSchool"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<bean:message key="label.grant.qualification.equivalenceDate"/>:&nbsp;
			</td>
			<td>
				<html:text property="equivalenceDate"/>&nbsp;<bean:message key="label.dateformat"/>
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
					<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
					<html:hidden property="username" value='<%= request.getAttribute("username").toString() %>'/>
					<html:submit styleClass="inputbutton" style="display:inline">
						<bean:message key="button.cancel"/>
					</html:submit>
				</html:form> 
			</td>
		</tr>
	</table>