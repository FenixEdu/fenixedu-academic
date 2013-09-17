<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>

<html:form action="/oldDidacticPublication">

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
	
	<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	</p>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoTeacher#externalId" property="infoTeacher#externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldPublicationTypeString" property="oldPublicationTypeString" value="Didactic"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>

<table class="tstyle5 thlight thtop" width="90%">
	<tr>
		<th><bean:message key="message.publications.publication" />:</th>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.publication" property="publication" cols="90%" rows="4"/></td>
	<tr/>
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
	</html:submit> 
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>
</p>

</html:form>