<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/externalActivity">

<h3>
	<logic:present name="infoExternalActivity">
	<bean:message key="message.externalActivities.edit" />
	</logic:present>
	<logic:notPresent name="infoExternalActivity">
	<bean:message key="message.externalActivities.insertActivity" />
	</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
<bean:message key="message.externalActivities.managementEdit" /></p>
	<p>
		<span class="error"><!-- Error messages go here -->
			<html:errors/>
		</span>
	</p>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoTeacher#idInternal" property="infoTeacher#idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	
<table class="tstyle5 thtop mtop1 thlight">
	<tr>
		<th><bean:message key="message.externalActivities.activity" />:</th>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.activity" bundle="HTMLALT_RESOURCES" property="activity" cols="90%" rows="4"/></td>
	</tr>
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>
</p>

</html:form>
