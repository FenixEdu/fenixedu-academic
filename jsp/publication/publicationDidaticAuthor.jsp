<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<br/>
<h3>
<bean:message key="message.authorPublications" />
</h3>
<logic:messagesPresent>
		<span class="error"><html:errors/></span>
</logic:messagesPresent>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.publications.management" /></p>
<logic:notEmpty name="infoSitePublications" property="infoPublications">
	<bean:message key="message.publications.explanationTeacherDidatic"/>
	<bean:message key="message.publications.insertInTeacher"/>
	<bean:message key="message.publications.cientificContinue"/>
</logic:notEmpty>
<logic:empty  name="infoSitePublications" property="infoPublications">
	<bean:message key="message.publications.noAuthorpublicationsDidatic"/>
	<bean:message key="message.publications.explanationInsertPublication"/>
	<bean:message key="message.publications.cientificContinue"/>
</logic:empty>
<table style="text-align:left" width="100%">

	<logic:iterate id="infoPublication" name="infoSitePublications" property="infoPublications">
	<tr>
		<td class="listClasses" style="text-align:left" width="100%">
			<bean:write name="infoPublication" property="publicationString" />
		</td>
		<td class="listClasses">
			<div class="gen-button">
				<bean:define id="infoTeacher" name="infoSitePublications" property="infoTeacher"/>
				<bean:define id="idTeacher" name="infoTeacher" property="idInternal"/>
					<html:link page='<%= "/insertPublicationTeacher.do?method=insertPublicationTeacher&amp;typePublication=Didatic&amp;page=0&amp;teacherId=" + idTeacher%>' 
					   paramId="idPublication" 
					   paramName="infoPublication" 
					   paramProperty="idInternal">
					<bean:message key="message.publications.insert" />
				</html:link>
			</div>
		</td>
	</tr>
	</logic:iterate>
</table>
<table>
	<tr align="center">	
		<td>
		<html:form action="/readPublications.do?typePublication=Didatic&amp;page=0">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
				<bean:message key="button.continue"/>
			</html:submit>
		</html:form>
		</td>
	</tr>
</table>
