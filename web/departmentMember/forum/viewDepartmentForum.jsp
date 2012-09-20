<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="title.departmentForum"/></h2>

<table>
	<logic:iterate id="forum" name="foruns" type="net.sourceforge.fenixedu.domain.messaging.DepartmentForum">
		
		<bean:size id="threadsCount" name="forum" property="conversationThreads"/>
	
		<tr>
			<td style="padding: 16px 0px 4px 0px;"><strong><bean:write name="forum" property="name"/></strong></td>
			<td></td>
		</tr>
		
		<tr>
			<td>
				<html:link action="<%=String.format("/departmentForum.do?method=viewForum&forumId=%s",forum.getIdInternal())%>">
					<bean:write name="forum" property="name"/>
				</html:link>
			</td>
			<td>
				<span class="color888">Tópicos: <bean:write name="threadsCount"/></span>
			</td>
		</tr>
	</logic:iterate>
</table>