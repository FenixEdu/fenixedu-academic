<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.pricesManagement" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet">
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>


	<logic:iterate id="postingRule" name="postingRules">
		<div style="background: #f5f5f5; width: 300px; margin: 1em 1em 0 0; float: left; border: 1px solid #ddd; padding: 0 1em 1em 1em; height: 220px;">
			<h3 style="color: #369;"><bean:message name="postingRule" property="eventType.qualifiedName"	bundle="ENUMERATION_RESOURCES" /></h3>
			<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" /> 
			<fr:view name="postingRule"	schema="<%=postingRuleClassName + ".view" %>">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thlight mtop025"/>
					<fr:property name="rowClasses" value=",,,tdbold,tdbold,tdbold"/>
				</fr:layout>
			</fr:view>
			<bean:define id="postingRuleId" name="postingRule" property="idInternal"/>
			<p class="indent1">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
				<html:link page="<%= "/pricesManagement.do?method=prepareEditPrice&amp;postingRuleId=" + postingRuleId %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="link.pricesManagement.edit"/></html:link>
			</p>
		</div>
	</logic:iterate>

</logic:present>
