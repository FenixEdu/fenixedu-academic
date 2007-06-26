<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>

<logic:messagesPresent>
	<p>
		<span class="error0"><!-- Error messages go here --><html:errors /></span>
	</p>
</logic:messagesPresent>

<logic:present name="siteView"> 
	<bean:define id="infoSitePublications" name="siteView" property="component"/>

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
	
	<table class="tstyle4" width="100%">
	
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

	<bean:define id="infoPublications" name="infoSitePublications" property="infoPublications" type="java.util.List"/>
	<bean:size id="infoPublicationsSize"  name="infoPublications"/>
	<logic:lessThan name="infoPublicationsSize"  value="5">
		<p class="mtop05">
			<html:link page="/readPublicationsAuthor.do?typePublication=Cientific&amp;page=0&amp;method=readPublicationsAuthor">
				<bean:message key="link.publication.add" />
			</html:link>
		</p>
	</logic:lessThan>
	
	<logic:greaterEqual name="infoPublicationsSize" value="5">
		<p class="mtop05 mbottom15">
			<em><bean:message key="message.publications.more5"/></em>
		</p>
	</logic:greaterEqual>

	<html:form action="/voidAction">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
			<bean:message key="button.continue"/>
		</html:submit>
	</html:form>

</logic:present>
