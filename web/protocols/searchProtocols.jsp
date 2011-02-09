<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<div class="infoop2">
	<p class="mvert0"><bean:message key="message.protocol.exampleFiles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></p>
	
	<ul class="list1">
	<%-- 
	<bean:message key="label.thesisMaster" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	<li><html:link href="<%= request.getContextPath()+"/protocols/files/Protocolo-DissertacaoMestrado.doc"%>">Protocolo-DissertacaoMestrado.doc</html:link></li>
	<li><html:link href="<%= request.getContextPath()+"/protocols/files/Anexo-Protocolo-DissertacaoMestrado.doc"%>">Anexo-Protocolo-DissertacaoMestrado.doc</html:link></li>
	<bean:message key="label.company" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	<li><html:link href="<%= request.getContextPath()+"/protocols/files/Protocolo-Empresa.doc"%>" >Protocolo-Empresa.doc</html:link></li>
	<li><html:link href="<%= request.getContextPath()+"/protocols/files/Anexo-Protocolo-Empresa.doc"%>">Anexo-Protocolo-Empresa.doc</html:link></li>
	--%>
	<bean:message key="label.international" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	<li><html:link href="<%= request.getContextPath()+"/protocols/files/Protocolo-Internacional.doc"%>">Protocolo-Internacional.doc</html:link></li>
	<bean:message key="label.polytechnic" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	<li><html:link href="<%= request.getContextPath()+"/protocols/files/Protocolo-Politecnicos.doc"%>">Protocolo-Politecnicos.doc</html:link></li>
	</ul>
</div>
		
<h2><bean:message key="title.protocols.search" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /></h2>

<logic:present name="protocolSearch">
	<html:errors/>
	<fr:form action="/protocols.do?method=searchProtocols">

		<div class="mvert15">
			<p class="mvert0"><span class="error0"><fr:message for="protocolBeginDate"/></span></p>
			<p class="mvert0"><span class="error0"><fr:message for="protocolEndDate"/></span></p>
			<p class="mvert0"><span class="error0"><fr:message for="signedDate"/></span></p>
		</div>

		<fr:edit name="protocolSearch" id="number" schema="edit.protocolSearch.number">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mbottom05"/>
				<fr:property name="columnClasses" value="width100px,width500px,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<logic:present name="showAllNationalityTypes">
			<fr:edit name="protocolSearch" id="protocolSearchNationalityType" schema="edit.protocolSearch.allNationalityType">
				<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 liinline"/>
					<fr:property name="columnClasses" value="width100px,width500px inobullet,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="searchCountry" path="/protocols.do?method=searchProtocols"/>
			</fr:edit>
		</logic:present>
		<logic:notPresent name="showAllNationalityTypes">
			<fr:edit name="protocolSearch" id="protocolSearchNationalityType" schema="edit.protocolSearch.nationalityType">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 liinline"/>
					<fr:property name="columnClasses" value="width100px,width500px inobullet,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="searchCountry" path="/protocols.do?method=searchProtocols"/>
			</fr:edit>
		</logic:notPresent>
		
		<logic:notEmpty name="protocolSearch" property="searchNationalityType">
			<logic:equal name="protocolSearch" property="searchNationalityType" value="<%= net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolSearch.SearchNationalityType.COUNTRY.toString()%>">
				<fr:edit name="protocolSearch" id="protocolSearchCountry" schema="edit.protocolSearch.country">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
						<fr:property name="columnClasses" value="width100px,width500px,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>			
			</logic:equal>
		</logic:notEmpty>
		
		<fr:edit name="protocolSearch" id="protocolSearch" schema="edit.protocolSearch">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value="width100px,width500px,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

		<table class="tstyle5 aright mvert05">
		<tr>
			<td>
				<fr:edit name="protocolSearch" id="protocolBeginDate" schema="edit.protocolSearch.protocolBeginDate" layout="flow">
					<fr:layout name="flow">
						<fr:property name="classes" value=""/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>
				<fr:edit name="protocolSearch" id="protocolEndDate" schema="edit.protocolSearch.protocolEndDate" layout="flow">
					<fr:layout name="flow">
						<fr:property name="classes" value=""/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>
				<fr:edit name="protocolSearch" id="signedDate" schema="edit.protocolSearch.signedDate" layout="flow">
					<fr:layout name="flow">
						<fr:property name="classes" value=""/>
						<fr:property name="labelTerminator" value=""/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		</table>

		<p class="mbottom15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.ok" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.export" property="export">
			<bean:message key="link.export" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</html:submit>
		</p>
	</fr:form>

	<p>
		<em>
	<bean:size id="protocolNumber" name="protocolSearch" property="search"/>
	<bean:message key="message.protocol.number" arg0="<%= protocolNumber.toString()%>" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
		</em>
	</p>
	<logic:notEmpty name="protocolSearch" property="search">
		<fr:view name="protocolSearch" property="search" schema="show.protocol.toList" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="columnClasses" value="acenter,nowrap,,,nowrap"/>
				<fr:property name="link(show)" value="/protocols.do?method=viewProtocolDetails" />
				<fr:property name="key(show)" value="link.scientificCouncil.evaluated.view" />
				<fr:property name="param(show)" value="idInternal" />
				<fr:property name="bundle(show)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

