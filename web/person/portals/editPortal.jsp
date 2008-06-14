<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:messagesPresent message="true">
			<ul class="nobullet list6">
				<html:messages id="messages" message="true"
					bundle="CONTENT_RESOURCES">
					<li><span class="error0"><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
			<br/>
</logic:messagesPresent>

<fr:edit id="editPortal" name="bean" schema="edit.portal.using.bean"
action="/portalManagement.do?method=editPortal">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/> 
	</fr:layout>	
	<fr:destination name="cancel" path="/portalManagement.do?method=prepare"/>
</fr:edit>

<bean:message key="label.pool.contents" bundle="CONTENT_RESOURCES"/>:

<bean:define id="portalId" name="bean" property="portal.idInternal"/>

<fr:view name="bean" property="portal.pool" schema="content.in.tree"> 
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES"/>
		<fr:property name="key(delete)" value="label.delete"/>
		<fr:property name="linkFormat(delete)" value="<%= "/portalManagement.do?method=deleteFromPool&elementId=${idInternal}&pid=" + portalId %>"/>
	</fr:layout>
</fr:view>

<html:link page="<%= "/portalManagement.do?method=prepareAddToPool&pid=" + portalId%>">
	<bean:message key="label.add.content.to.pool" bundle="CONTENT_RESOURCES"/>
</html:link>