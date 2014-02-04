<%@ page language="java"%>
<%@ page import="net.sourceforge.fenixedu.domain.ScientificCouncilSite"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<em><bean:message key="scientificCouncil" /></em>
<h2><bean:message key="label.authorize.teacher" /></h2>

<p>
	<html:link action="teacherAuthorization.do?method=pre">+ <bean:message key="label.create.authorization" /></html:link>
	|
	<html:link action="teacherAuthorization.do?method=prepareUpload">+ <bean:message key="label.upload.authorizations" /></html:link>
	|
	<a href="http://fenix-ashes.ist.utl.pt/professoresexternos/"><bean:message key="label.manage" /></a>
</p>
<logic:present name="uploadMessages">
	<logic:iterate id="uploadMessage" name="uploadMessages">
		<p class="error0">
			<bean:write name="uploadMessage"/>
		</p>
	</logic:iterate>
</logic:present>
<table class="tstyle4 thlight">
<tr>
	<th><bean:message key="label.istid" /></th>
	<th><bean:message key="label.authorizedPerson"/></th>
	<th><bean:message key="label.semester"/></th>
	<th><bean:message key="label.state"/></th>
	<th><bean:message key="label.equalTo"/></th>
	<th><bean:message key="department"/></th>
	<th><bean:message key="label.hours"/></th>
	<th><bean:message key="label.park"/></th>
	<th><bean:message key="label.authorized.by"/></th>
</tr>
<logic:iterate id="obj" name="auths">
<bean:define id="auth" name="obj" type="net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization" />
<tr>
<td>
	<bean:write name="auth" property="teacher.person.user.username"/>
</td>
<td>
	<bean:write name="auth" property="teacher.person.name"/>
</td>
<td>
	<bean:write name="auth" property="executionSemester.executionYear.year"/> - <bean:write name="auth" property="executionSemester.semester"/>º semestre 
</td>
<td>
<logic:equal name="auth" property="active" value="true">
	<bean:message key="label.authorized" />
</logic:equal>
<logic:equal name="auth" property="active" value="false">
	<bean:message key="label.revoked" />
</logic:equal>
</td>
<td>
	<bean:write name="auth" property="professionalCategory.name"/>
</td>
<td>
	<logic:present name="auth" property="department">
		<bean:write name="auth" property="department.realName"/>
	</logic:present> 
</td>
<td>
	<bean:write name="auth" property="lessonHours"/>
</td>
<td>
	<logic:equal name="auth" property="canPark" value="true">
		Sim
	</logic:equal>
	<logic:equal name="auth" property="canPark" value="false">
		Não
	</logic:equal>
	
</td>
<td>
	<bean:write name="auth" property="authorizer.nickname"/> 
</td>

<td>
<logic:equal name="auth" property="active" value="true">
	<html:link action="<%= "teacherAuthorization.do?method=revoke&oid=" + auth.getExternalId() %>"><bean:message key="label.revoke" /></html:link>
</logic:equal>
<logic:equal name="auth" property="active" value="false">
</logic:equal>
</td>
</tr>	
</logic:iterate>
</table>