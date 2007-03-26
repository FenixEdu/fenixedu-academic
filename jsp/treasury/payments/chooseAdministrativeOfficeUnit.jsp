<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="TREASURY">
	<h2><strong><bean:message key="label.payments.chooseAdministrativeOfficeUnit" bundle="TREASURY_RESOURCES"/></strong></h2>
	
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
	<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
	<fr:view name="unitsForAdministrativeOffice" schema="Unit.view-nameWithAcronym-and-costCenterCode">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
			<fr:property name="linkFormat(view)" value="<%= "/payments.do?method=showOperations&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId +"&amp;administrativeOfficeUnitId=${idInternal}"%>" />
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<fr:property name="contextRelative(view)" value="true"/>	
			<fr:property name="sortBy" value="nameWithAcronym=asc"/>
		</fr:layout>
	</fr:view>

	
</logic:present>


