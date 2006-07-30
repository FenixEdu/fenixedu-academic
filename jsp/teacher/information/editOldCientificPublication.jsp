<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/oldCientificPublication">
<br/>
<h3>
	<logic:present name="infoOldPublication">
		<bean:message key="message.publications.insertPublication" />
	</logic:present>
	<logic:notPresent name="infoOldPublication">
		<bean:message key="message.publications.editPublication" />
	</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
	<logic:present name="infoOldPublication">
		<bean:message key="message.publications.managementInsert" />
	</logic:present>
	<logic:notPresent name="infoOldPublication">
		<bean:message key="message.publications.managementEdit" />
	</logic:notPresent></p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoTeacher#idInternal" property="infoTeacher#idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldPublicationTypeString" property="oldPublicationTypeString" value="Cientific"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
<table>
	<tr>
		<td><bean:message key="message.publications.publication" /></td>
	</tr>
	<tr>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.publication" property="publication" cols="90%" rows="4"/></td>
	<tr/>
</table>
<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
