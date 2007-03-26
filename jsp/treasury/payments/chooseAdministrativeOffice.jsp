<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="TREASURY">
	<h2><strong><bean:message key="label.payments.chooseAdministrativeOffice" bundle="TREASURY_RESOURCES"/></strong></h2>
	
	<logic:messagesPresent message="true">
		<p>
			<ul class="nobullet list6">
				<html:messages id="messages" message="true" bundle="TREASURY_RESOURCES">
					<li><span class="error0"><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
		</p>
	</logic:messagesPresent>

	<bean:define id="personId" name="person" property="idInternal" />		
	<fr:view name="administrativeOffices" schema="AdministrativeOffice.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
			<fr:property name="linkFormat(view)" value="<%= "/payments.do?method=prepareChooseAdministrativeOfficeUnit&amp;personId=" + personId + "&amp;administrativeOfficeId=${idInternal}"%>" />
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="contextRelative(view)" value="true"/>	
			<fr:property name="sortBy" value="unit.nameWithAcronym=asc"/>
		</fr:layout>
	</fr:view>

	
</logic:present>


