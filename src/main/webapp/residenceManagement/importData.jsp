<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml />

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></em>
<h2><bean:message key="label.import.debt" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></h2>

<bean:define id="monthOID" name="importFileBean" property="residenceMonth.OID"/>

<p>
	<html:link page="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID="  + monthOID +  (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "") %>"> 
		&laquo; <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

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
	

	<div class="infoop2">
		<bean:message key="label.information.xls.format" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		<p>Nº do Quarto <span class="colorbbb">|</span> ISTxxxx <span class="colorbbb">|</span> NIF <span class="colorbbb">|</span> Nome <span class="colorbbb">|</span> Valor do Quarto</p>
	</div>


	<logic:present name="createdDebts">
		<p class="mtop15 mbottom05">
			<span class="success0">
				<bean:message key="label.debts.created" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.
			</span>
		</p>
	</logic:present>

	
	<fr:form action="<%= "/residenceManagement.do?method=importData&monthOID=" + monthOID %>" encoding="multipart/form-data">
		<fr:edit id="importFile" name="importFileBean" visible="false" />

		<table class="tstyle5 thleft thlight">
			<tr>
				<td>
					<bean:message key="label.importFile" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
				</td>
				<td>
					<fr:edit id="file" name="importFileBean" slot="file" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:layout>
							<fr:property name="size" value="30"/>
						</fr:layout>
						<fr:destination name="invalid" path="/residenceManagement.do?method=invalid"/>
					</fr:edit>
				</td>
				<td>
					<html:submit>
						<bean:message key="label.submit" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</td>
				<td class="tdclear">		
					<fr:hasMessages for="file">
						<p class="mtop15"><span class="error0 mbottom0"><fr:message for="file" show="message"/></span></p>
					</fr:hasMessages>
				</td>
			</tr>
		</table>
	</fr:form>

	<logic:present name="importList">
		<div id="warn">
			<bean:define id="totalImports" name="importList" property="numberOfImports" /> 
			<p><em><bean:message key="label.total.imports" arg0="<%= totalImports.toString() %>" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></em></p> 
			
			<logic:notEmpty name="importList" property="unsuccessfulEvents">
				<bean:size id="numberOfErrors" name="importList"	property="unsuccessfulEvents" />
				<p>
					<span class="error" style="padding: 3px 6px;">
						<bean:message key="label.errors.in.import" arg0="<%= numberOfErrors.toString() %>" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />. 
						<%=pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX%><a href="#statusNotOk"> 
							<bean:message key="label.details" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
						</a>
					</span>
				</p>
			</logic:notEmpty>
		</div>


		<logic:notEmpty name="importList" property="successfulEvents">
			<p class="mtop2 mbottom05"><bean:message key="label.valid.residents" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />:</p>
			<fr:view name="importList" property="successfulEvents" schema="show.residenceEventBean">
				<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05" />
						<fr:property name="columnClasses" value=",aleft,,,," />
					<fr:property name="sortBy" value="userName"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<logic:notEmpty name="importList" property="unsuccessfulEvents">
			<div id="statusNotOk">
				<p class="mtop2 mbottom05"><bean:message key="label.invalid.residents" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />:</p>
				<fr:view name="importList" property="unsuccessfulEvents" schema="show.residenceEventBean.with.status">
					<fr:layout name="tabular">	
						<fr:property name="classes" value="tstyle1 tdcenter thlight mtop05" />
						<fr:property name="columnClasses" value=",aleft,,,,highlight6 aleft" />
						<fr:property name="sortBy" value="userName"/>
					</fr:layout>
				</fr:view>
			</div>
			<p>
			<bean:size id="invalidNumber" name="importList" property="unsuccessfulEvents"/>
			<strong><bean:message key="label.request.confirmation.due.to.invalids" arg0="<%=  invalidNumber.toString()%>" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></strong>
			</p>
			
		</logic:notEmpty>
		
		<logic:notEmpty name="importList" property="successfulEvents">
		   <fr:form action="<%= "/residenceManagement.do?method=generateDebts&monthOID=" + monthOID %>">
				<fr:edit id="dateBean" name="importFileBean" visible="false"/>
				<fr:edit id="importList" name="importList" visible="false" />
				<html:submit>
					<bean:message key="label.generate.debts" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
				</html:submit>
			</fr:form>
		</logic:notEmpty>
	</logic:present>


</logic:present>
