<%@ page language="java"%>
<%@ page import="net.sourceforge.fenixedu.domain.ScientificCouncilSite"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="scientificCouncil" /></em>
<h2><bean:message key="label.authorize.teacher" /></h2>

<ul>
	<li class="navheader">
		<html:link action="teacherAuthorization.do?method=pre"><bean:message key="label.create.authorization" /></html:link>
	</li>
</ul>
<table class="tstyle4 mtop05">
<tr>
	<th><bean:message key="label.istid" /></th>
	<th><bean:message key="label.authorizedPerson"/></th>
	<th><bean:message key="label.semester"/></th>
	<th><bean:message key="label.state"/></th>
	<th><bean:message key="label.equalTo"/></th>
	<th><bean:message key="label.hours"/></th>
	<th><bean:message key="label.park"/></th>
	<th><bean:message key="label.card"/></th>
	<th><bean:message key="label.authorized.by"/></th>
</tr>
<logic:iterate id="obj" name="auths">
<bean:define id="auth" name="obj" type="net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization" />
<tr>
<td>
	<bean:write name="auth" property="teacher.person.user.userUId"/>
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
	<logic:equal name="auth" property="canHaveCard" value="true">
		Sim
	</logic:equal>
	<logic:equal name="auth" property="canHaveCard" value="false">
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