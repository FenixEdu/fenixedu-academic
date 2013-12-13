<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="role(STUDENT)">

	<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
	<h2><bean:message key="label.prices" /></h2>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>


		<h3 class="mtop15 mbottom025"><bean:message name="insurancePostingRule" property="eventType.qualifiedName" bundle="ENUMERATION_RESOURCES" /></h3>
		<bean:define id="insurancePostingRuleClassName" name="insurancePostingRule" property="class.simpleName" />
		<fr:view name="insurancePostingRule" schema="<%= insurancePostingRuleClassName + ".view-student" %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thright thlight mtop025" />
			</fr:layout>
		</fr:view>

		<logic:iterate id="entry" name="postingRulesByAdminOfficeType">
			<bean:define id="adminOfficeType" name="entry" property="key" />
			<bean:define id="postingRules" name="entry" property="value" />
			
			<div style="background: #f5f5f5; width: 300px; margin: 1em 1em 0 0; float: left; border: 1px solid #ddd; padding: 0 1em 1em 1em;">
			<h3 style="color: #369;"><bean:message name="adminOfficeType" property="qualifiedName" bundle="ENUMERATION_RESOURCES" /></h3>
				<logic:iterate id="postingRule" name="postingRules">
					<p class="mtop2 mbottom025"><strong><bean:message name="postingRule" property="eventType.qualifiedName" bundle="ENUMERATION_RESOURCES" /></strong></p>
					<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" />
						<fr:view name="postingRule" schema="<%=postingRuleClassName + ".view-student" %>">
							<fr:layout name="tabular">
								<fr:property name="classes" value="thlight thleft mtop025 width100"/>
								<fr:property name="columnClasses" value=",aright,"/>
							</fr:layout>
						</fr:view>
				</logic:iterate>	
			</div>
		</logic:iterate>
		
		<div class="cboth"></div>

	
</logic:present>