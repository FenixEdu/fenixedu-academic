<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:xhtml/>

<h2><bean:message key="title.teacherInformation"/></h2>
<logic:messagesPresent>
	<span class="error"><!-- Error messages go here -->
		<html:errors />
	</span>
</logic:messagesPresent>

	<br/>
	<bean:define id="teacherResults" name="teacherResults" type="java.util.List"/>
	<h3><bean:message key="message.cientificPublications" /></h3>
	<p class="infoop"><span class="emphasis-box">1</span>
	<logic:notEmpty name="teacherResults">
		<bean:message key="message.publications.management" /></p>
		<bean:message key="message.publications.managementCleanPublicationTeacher" />
		<bean:message key="message.publications.managementInsertPublicationTeacher" />
		<bean:message key="message.publications.managementContinue" />
	</logic:notEmpty>
	<logic:empty name="teacherResults">
		<bean:message key="message.publications.management" /></p>
		<bean:message key="message.publications.managementNoPublications"/><br/>
		<bean:message key="message.publications.managementInsertPublicationTeacher" />
		<bean:message key="message.publications.managementContinue" />
	</logic:empty>
	<table style="text-align:left" width="100%">
	
		<logic:iterate id="result" name="teacherResults">
		<tr>
			<td class="listClasses" style="text-align:left" width="100%">
				<bean:write name="result" property="resume"/>
			</td>
			<td class="listClasses">
				<div class="gen-button">
					<bean:define id="resultId" name="result" property="idInternal"/>
					<html:link page="<%="/resultTeacherManagement.do?method=deleteResultTeacher&amp;typeResult=Cientific&amp;resultId="+resultId%>">
						<bean:message key="link.publication.remove"/>
					</html:link>
				</div>
			</td>
		</tr>
		</logic:iterate>
	</table>
	<br />
	<bean:size id="teacherResultsSize"  name="teacherResults"/>
	<logic:lessThan name="teacherResultsSize" value="5">
		<div class="gen-button">
			<html:link page="/resultTeacherManagement.do?method=readResultsParticipation&amp;typeResult=Cientific">
				<bean:message key="link.publication.add" />
			</html:link>
		</div>
	</logic:lessThan>
	<logic:greaterEqual name="teacherResultsSize" value="5">
		<bean:message key="message.publications.more5"/>
	</logic:greaterEqual>
	<br />
	<h3>
	<table>
		<tr align="center">	
			<td>
			<html:form action="/voidAction">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
					<bean:message key="button.continue"/>
				</html:submit>
			</html:form>
			</td>
		</tr>
	</table>
	</h3>
