<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>

<logic:messagesPresent>
		<span class="error"><html:errors/></span>
</logic:messagesPresent>
<logic:present name="siteView"> 
	<bean:define id="infoSitePublications" name="siteView" property="component"/>
	<br/>
	<h3>
	<bean:message key="message.cientificPublications" />
	</h3>
	<p class="infoop"><span class="emphasis-box">1</span>
	<logic:notEmpty name="infoSitePublications" property="infoPublications">
		<bean:message key="message.publications.management" /></p>
		<bean:message key="message.publications.managementCleanPublicationTeacher" />
		<bean:message key="message.publications.managementInsertPublicationTeacher" />
		<bean:message key="message.publications.managementContinue" />
	</logic:notEmpty>
	<logic:empty name="infoSitePublications" property="infoPublications">
		<bean:message key="message.publications.management" /></p>
		<bean:message key="message.publications.managementNoPublications"/><br/>
		<bean:message key="message.publications.managementInsertPublicationTeacher" />
		<bean:message key="message.publications.managementContinue" />
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
					<html:link page='<%= "/deletePublicationTeacher.do?method=deletePublicationTeacher&amp;typePublication=Cientific&amp;page=0&amp;teacherId=" + idTeacher %>' 
							   paramId="idPublication" 
							   paramName="infoPublication" 
							   paramProperty="idInternal">
						<bean:message key="link.publication.remove" />
					</html:link>
				</div>
			</td>
		</tr>
		</logic:iterate>
	</table>
	<br />
	<bean:define id="infoPublications" name="infoSitePublications" property="infoPublications" type="java.util.List"/>
	<bean:size id="infoPublicationsSize"  name="infoPublications"/>
	<logic:lessThan name="infoPublicationsSize"  value="5">
		<div class="gen-button">
			<html:link page="/readPublicationsAuthor.do?typePublication=Cientific&amp;page=0&amp;method=readPublicationsAuthor">
				<bean:message key="link.publication.add" />
			</html:link>
		</div>
	</logic:lessThan>
	<logic:greaterEqual name="infoPublicationsSize" value="5">
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
</logic:present>
