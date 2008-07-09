<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.debtManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/residenceManagement.do?method=createYear"><bean:message key="label.createNewYear" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></html:link>
	</li>
	<li>
		<html:link page="/residenceManagement.do?method=configurePaymentLimits"><bean:message key="label.configurePaymentLimits" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></html:link>
	</li>
</ul>

<fr:form action="/residenceManagement.do?method=postBack">
	<fr:edit id="editBean" name="importFileBean" schema="edit.import.residence.bean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight"/>
			</fr:layout>
			<fr:destination name="postback" path="/residenceManagement.do?method=postBack"/>
	</fr:edit>
</fr:form>

<logic:present name="importFileBean" property="residenceMonth">

	<fr:form action="/residenceManagement.do?method=importData" encoding="multipart/form-data">
		<fr:edit id="importFile" name="importFileBean" visible="false"/>
		
		<table class="tstyle5 thleft thlight">
		<tr>
			<td>
				<bean:message key="label.importFile" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
			</td>
			<td>
				<fr:edit id="file" name="importFileBean" slot="file"/>
			</td>
			<td>
				<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
			</td>
		</tr>
		</table>	
	</fr:form>

	<logic:present name="importList">
		Status OK
		<fr:view name="importList" property="successfulEvents">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
				</fr:layout>
			</fr:view>
			
			Status NOT OK
			<fr:view name="importList" property="unsuccessfulEvents">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
				</fr:layout>
			</fr:view>
	</logic:present>
	
	
</logic:present>
