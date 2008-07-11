<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<html:xhtml />

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></em>
<h2><bean:message key="label.debtManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></h2>

<ul>
	<li>
		<html:link page="/residenceManagement.do?method=createYear">
			<bean:message key="label.createNewYear"	bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
		</html:link>
	</li>
	<li>
		<html:link page="/residenceManagement.do?method=configurePaymentLimits">
			<bean:message key="label.configurePaymentLimits" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
		</html:link>
	</li>
</ul>

<fr:form action="/residenceManagement.do?method=postBack">
	<fr:edit id="editBean" name="importFileBean" schema="edit.import.residence.bean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thleft thlight" />
		</fr:layout>
		<fr:destination name="postback" path="/residenceManagement.do?method=postBack" />
	</fr:edit>
</fr:form>

<logic:present name="importFileBean" property="residenceMonth">

	<logic:messagesPresent message="true">
		<p>
			<html:messages id="messages" message="true" bundle="RESIDENCE_MANAGEMENT_RESOURCES">
				<span class="error0"><bean:write name="messages"/></span>
			</html:messages>
		</p>
	</logic:messagesPresent>
	
	<logic:notEmpty name="availableSpreadsheets">
		<bean:message key="label.availableSpreadsheets.are" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>:
		<ul>
			<logic:iterate id="spreadsheetName" name="availableSpreadsheets">
				<li>
					<bean:write name="spreadsheetName"/>
				</li>
			</logic:iterate>
		</ul>
	</logic:notEmpty>
	
	<fr:form action="/residenceManagement.do?method=importData" encoding="multipart/form-data">
		<fr:edit id="importFile" name="importFileBean" visible="false" />

		<table class="tstyle5 thleft thlight">
			<tr>
				<td>
				 	<bean:message key="label.spreadsheetName" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></td>
				<td>
					<fr:edit id="sheetName" name="importFileBean" slot="spreadsheetName">
						<fr:layout>
									<fr:property name="size" value="30"/>
								</fr:layout>	
						</fr:edit>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.importFile" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
				</td>
				<td>
					<fr:edit id="file" name="importFileBean" slot="file" >
								<fr:layout>
									<fr:property name="size" value="30"/>
								</fr:layout>
						</fr:edit>
				</td>
				<td>
					<html:submit>
						<bean:message key="label.submit" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</td>
			</tr>
		</table>
	</fr:form>

	<logic:present name="importList">
		<div id="warn">
			<bean:define id="totalImports" name="importList" property="numberOfImports" /> 
			<bean:message key="label.total.imports" arg0="<%= totalImports.toString() %>" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /> 
			
			<logic:notEmpty name="importList" property="unsuccessfulEvents">
				<bean:size id="numberOfErrors" name="importList"	property="unsuccessfulEvents" />
				<p class="error">
					<bean:message key="label.errors.in.import" arg0="<%= numberOfErrors.toString() %>" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />. 
					<%=ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#statusNotOk"> 
						<bean:message key="label.details" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
					</a>
				</p>
			</logic:notEmpty>
		</div>


		<logic:notEmpty name="importList" property="successfulEvents">
			<bean:message key="label.debts.to.create" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
			<fr:view name="importList" property="successfulEvents" schema="show.residenceEventBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1" />
					<fr:property name="sortBy" value="userName"/>
				</fr:layout>
			</fr:view>
			<fr:form action="/residenceManagement.do?method=generateDebts">
				<fr:edit id="dateBean" name="importFileBean" visible="false"/>
				<fr:edit id="importList" name="importList" visible="false" />
				<html:submit>
					<bean:message key="label.generate.debts" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
				</html:submit>
			</fr:form>
		</logic:notEmpty>

		<logic:notEmpty name="importList" property="unsuccessfulEvents">
			<div id="statusNotOk">
				<bean:message	key="label.unable.to.create" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
				<fr:view name="importList" property="unsuccessfulEvents" schema="show.residenceEventBean">
					<fr:layout name="tabular">	
						<fr:property name="classes" value="tstyle1" />
					</fr:layout>
				</fr:view>
			</div>
		</logic:notEmpty>
	</logic:present>


</logic:present>
