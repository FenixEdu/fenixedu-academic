<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.pricesManagement" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet">
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<logic:empty name="postingRules">
		There is no Posting Rules.
	</logic:empty>
	<logic:iterate id="postingRule" name="postingRules">
		<div style="background: #f5f5f5; width: 350px; margin: 1em 1em 0 0; float: left; border: 1px solid #ddd; padding: 0 1em 1em 1em; height: 230px;">
			<h4 style="color: #369;"><bean:message name="postingRule" property="eventType.qualifiedName"	bundle="ENUMERATION_RESOURCES" /></h4>
			<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" /> 
			<fr:view name="postingRule"	schema="<%=postingRuleClassName + ".view" %>">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thright thlight mtop025"/>
					<fr:property name="rowClasses" value=",,,tdbold,tdbold,tdbold"/>
				</fr:layout>
			</fr:view>
			<bean:define id="postingRuleId" name="postingRule" property="externalId"/>
			<p class="indent1">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
				<html:link page="<%= "/pricesManagement.do?method=prepareEditPrice&amp;postingRuleId=" + postingRuleId %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="link.pricesManagement.edit"/></html:link>
			</p>
		</div>
	</logic:iterate>
