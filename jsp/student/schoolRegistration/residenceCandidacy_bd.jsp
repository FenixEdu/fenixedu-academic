<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/enrollStudent?method=enrollStudent">
<html:hidden property="page" value="5"/>
 <strong>Página 4 de 6</strong>
 <h3 align="center"><bean:message key="schoolRegistration.Header.residenceTitle"/></h3>
	<p align="center"><span class="error"><html:errors property="residenceCandidate"/></span></p>	
	<p align="center"><span class="error"><html:errors property="dislocated"/></span></p>
	<p align="center"><span class="error"><html:errors property="observations"/></span></p>
	<table>
		<tr>
			<td><bean:message key="label.schoolRegistration.residenceCandidate"/></td>
			<td>Sim<html:radio property="residenceCandidate" value="true" /></td>
			<td>Não<html:radio property="residenceCandidate" value="false" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.schoolRegistration.dislocated"/></td>
			<td>Sim<html:radio property="dislocated" value="true" /></td>
			<td>Não<html:radio property="dislocated" value="false" /></td>
		</tr>
	</table>
	<table>
		<tr><td><bean:message key="label.schoolRegistration.observations"/></td></tr>
		<tr>
			<td><html:textarea cols="80" rows="3" property="observations" 
					onkeyup="document.schoolRegistrationForm.charCount.value=240-document.schoolRegistrationForm.observations.value.length;"/>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<bean:message key="label.person.remainingChars" bundle="DEFAULT"/>:
			</td>
			<td>
				<html:text property="charCount" size="4" maxlength="3" readonly="true" value="240" />
			</td>
		</tr>
	</table>

	<p align="center"><html:submit value="Continuar" styleClass="inputbutton"/></p>
</html:form>