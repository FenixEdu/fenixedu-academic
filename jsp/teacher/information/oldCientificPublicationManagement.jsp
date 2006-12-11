<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<logic:present name="siteView"> 
<bean:define id="infoSiteOldPublications" name="siteView" property="component"/>
<br/>
<h3>
<bean:message key="message.cientificPublications" />
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.publications.management" /></p>
<bean:message key="message.publications.managementInsertExplanation" />
<bean:message key="message.publications.managementCleanExplanation" />
<bean:message key="message.publications.managementInsertPubExplanation" />
<bean:message key="message.publications.managementSeeExplanation" />
<table border="0" style="margin-top:10px" cellspacing="1" cellpadding="5" width="100%">
<logic:iterate id="infoOldPublication" name="infoSiteOldPublications" property="infoOldPublications">
<tr>
	<td class="listClasses" style="text-align:left" width="60%">
		<span><bean:write name="infoOldPublication" property="publication" /></span>
	</td>
	<td class="listClasses"> 
		<div class="gen-button">
			<html:link page="/oldCientificPublication.do?method=prepareEdit&amp;page=0" 
						paramId="idInternal"
					   paramName="infoOldPublication" 
					   paramProperty="idInternal">
				<bean:message key="label.edit" />
			</html:link>
		</div>
	</td>
	<td class="listClasses">
		<div class="gen-button">
			<html:link page="/oldCientificPublication.do?method=delete&amp;page=0" 
					   paramId="idInternal" 
					   paramName="infoOldPublication" 
					   paramProperty="idInternal">
				<bean:message key="label.delete" />
			</html:link>
		</div>
	</td>
</tr>
</logic:iterate>
</table>
<br />
<logic:lessThan name="infoSiteOldPublications" property="numberOldPublications" value="5">	
	<div class="gen-button">
		<html:link page="/oldCientificPublication.do?method=prepareEdit&amp;page=0&amp;oldPublicationType=Cientific" 
				   paramId="infoTeacher#idInternal" 
				   paramName="infoSiteOldPublications" 
				   paramProperty="infoTeacher.idInternal" >
			<bean:message key="message.publications.insert" />
		</html:link>
	</div>
</logic:lessThan>
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
