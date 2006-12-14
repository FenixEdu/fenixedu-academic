<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.pricesManagement.edit" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet">
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:hasMessages>
	<ul class="nobullet">
	<fr:messages>
		<li><span class="error0"><fr:message/></span></li>
	</fr:messages>
	</ul>
</fr:hasMessages>


<p class="mtop2 mbottom05"><strong><bean:message name="postingRule" property="eventType.qualifiedName" bundle="ENUMERATION_RESOURCES"/></strong></p>
<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" />
<fr:edit name="postingRule" 
		 schema="<%=postingRuleClassName + ".edit"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05" />
		<fr:property name="columnClasses" value=",,tdclear" />
	</fr:layout>
	<fr:destination name="cancel" path="/pricesManagement.do?method=viewPrices"/>
	<fr:destination name="success" path="/pricesManagement.do?method=viewPrices"/>
</fr:edit>

</logic:present>
