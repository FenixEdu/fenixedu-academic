<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<h2><bean:message
	key="label.pricesManagement.edit" /></h2>

<hr/>
<br />

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<fr:hasMessages>
	<ul>
	<fr:messages>
		<li><span class="error0"><fr:message/></span></li>
	</fr:messages>
	</ul>
</fr:hasMessages>

<h3><bean:message name="postingRuleDTO" property="postingRule.eventType.name" bundle="ENUMERATION_RESOURCES"/></h3>

<fr:edit name="postingRuleDTO" 
		 schema="certificateRequestPRDTO.edit"
		 action="/pricesManagement.do?method=editPrice">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
	<fr:destination name="cancel" path="/pricesManagement.do?method=viewPrices"/>
</fr:edit>

</logic:present>
