<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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
				<html:link action="<%=String.format("/departmentForum.do?method=viewForum&forumId=%s",forum.getExternalId())%>">
					<bean:write name="forum" property="name"/>
				</html:link>
			</td>
			<td>
				<span class="color888">Tópicos: <bean:write name="threadsCount"/></span>
			</td>
		</tr>
	</logic:iterate>
</table>