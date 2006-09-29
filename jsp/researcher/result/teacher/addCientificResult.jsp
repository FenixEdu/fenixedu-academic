<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<br/>
<h3>
<bean:message key="message.cientificPublications" />
</h3>
<logic:messagesPresent>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
</logic:messagesPresent>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.publications.management" /></p>
<logic:notEmpty name="resultsNotInTeacherSheet">
	<bean:message key="message.publications.explanationTeacherCientific"/>
	<bean:message key="message.publications.insertInTeacher"/>
	<bean:message key="message.publications.cientificContinue"/>
</logic:notEmpty>
<logic:empty  name="resultsNotInTeacherSheet">
	<bean:message key="message.publications.noAuthorpublicationsCientific"/>
	<bean:message key="message.publications.explanationInsertPublication"/>
	<bean:message key="message.publications.cientificContinue"/>
</logic:empty>
<table style="text-align:left" width="100%">

	<logic:iterate id="result" name="resultsNotInTeacherSheet">
	<tr>
		<td class="listClasses" style="text-align:left" width="100%">
			<bean:write name="result" property="resume" />
		</td>
		<td class="listClasses">
			<div class="gen-button">
				<bean:define id="resultId" name="result" property="idInternal"/>
				<html:link page="<%="/resultTeacherManagement.do?method=insertResultTeacher&amp;typeResult=Cientific&amp;resultId="+resultId%>">
					<bean:message key="message.publications.insert"/>
				</html:link>
			</div>
		</td>
	</tr>
	</logic:iterate>
</table>
<table>
	<tr align="center">	
		<td>
		<html:form action="/resultTeacherManagement.do?method=readTeacherResults&amp;typeResult=Cientific">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
				<bean:message key="button.continue"/>
			</html:submit>
		</html:form>
		</td>
	</tr>
</table>
