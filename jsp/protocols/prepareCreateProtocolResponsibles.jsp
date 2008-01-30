<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.create" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><bean:message key="label.protocol.create.step1" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> > 
	<span class="actual"><bean:message key="label.protocol.create.step2" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span> > 
	<span><bean:message key="label.protocol.create.step3" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span>
</p>


<fr:form action="/createProtocol.do">
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="method" value="removeISTResponsible"/>
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="responsibleID"/>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<!-- IST Responsibles -->
<%--
<strong><bean:message key="label.protocol.ist" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
--%>

<p class="mtop15 mbottom05"><strong><bean:message key="label.protocol.internalResponsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="responsibles">
<table class="tstyle1 mvert05">
	<tr>
		<th><bean:message key="label.person.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.unit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="responsible" name="protocolFactory" property="responsibles" type="net.sourceforge.fenixedu.domain.Person">
	<tr>
		<td><bean:write name="responsible" property="name"/></td>
		<td><bean:write name="responsible" property="unitText"/></td>
		<td>
			<html:submit onclick="<%= "this.form.responsibleID.value=" + responsible.getIdInternal().toString()%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="responsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>


<!-- IST Responsibles Functions-->
<p class="mtop15 mbottom05"><strong><bean:message key="label.protocol.internalFunctionResponsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="responsibleFunctions">
<table class="tstyle1 mvert05">
	<tr>
		<th><bean:message key="label.person.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.unit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="responsibleFunction" name="protocolFactory" property="responsibleFunctions" type="net.sourceforge.fenixedu.domain.organizationalStructure.Function">
	<tr>
		<td><bean:write name="responsibleFunction" property="name"/></td>
		<td><bean:write name="responsibleFunction" property="unit.name"/></td>
		<td>
			<html:submit onclick="<%= "this.form.responsibleID.value=" + responsibleFunction.getIdInternal().toString()+ ";this.form.method.value='removeISTResponsibleFunction'"%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="responsibleFunctions">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>


<!-- Partner Responsibles -->
<%--
<strong><bean:message key="label.protocol.partner" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong><br/>
--%>
<p class="mtop15 mbottom05"><strong><bean:message key="label.protocol.externalResponsibles" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="partnerResponsibles">
<table class="tstyle1 mvert05">
	<tr>
		<th><bean:message key="label.person.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.unit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th></th>				
	</tr>
	<logic:iterate id="partnerResponsible" name="protocolFactory" property="partnerResponsibles" type="net.sourceforge.fenixedu.domain.Person">
	<tr>
		<td><bean:write name="partnerResponsible" property="name"/></td>
		<td><bean:write name="partnerResponsible" property="unitText"/></td>
		<td>
			<html:submit onclick="<%= "this.form.responsibleID.value=" + partnerResponsible.getIdInternal().toString() + ";this.form.method.value='removePartnerResponsible'"%>">
				<bean:message key="button.remove" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="partnerResponsibles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>.</em></p>
</logic:empty>
</fr:form>


<!-- Add responsible -->
<logic:notPresent name="createExternalPerson">
<logic:notPresent name="createExternalUnit">
<fr:form action="/createProtocol.do?method=insertResponsible">
	<div class="mtop15 mbottom1">
		<span class="error0">
			<html:errors bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			<html:messages id="message" name="errorMessage" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
				<bean:write name="message" />
				<br />
			</html:messages>
		</span>
	</div>
<logic:present name="needToCreatePerson">
	<p class="warning0 mbottom1" style="padding: 0.25em 0.5em;">
		<strong><bean:message key="label.attention" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</strong><br/>
		<bean:message key="message.protocol.createNewPerson" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</p>
</logic:present>

<fieldset class="fieldset1 mtop15">
<legend><bean:message key="label.protocol.insertResponsible" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></legend>
<logic:equal name="protocolFactory" property="istResponsible" value="true">
	<logic:equal name="protocolFactory" property="istResponsibleIsPerson" value="true">
		<fr:edit id="istResponsible" name="protocolFactory" schema="search.istResponsible">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright"/>
		        <fr:property name="columnClasses" value="width10em,,tderror1"/>
			</fr:layout>
			<fr:destination name="changePersonType" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
			<fr:destination name="changePersonorFunction" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
		</fr:edit>
	</logic:equal>
	<logic:equal name="protocolFactory" property="istResponsibleIsPerson" value="false">
		<fr:edit id="istResponsible2" name="protocolFactory" schema="search.istResponsibleFunction">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright"/>
		        <fr:property name="columnClasses" value="width10em,,tderror1"/>
			</fr:layout>
			<fr:destination name="changePersonType" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
			<fr:destination name="changePersonorFunction" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
			<fr:destination name="changeFunctionByPerson" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
		</fr:edit>

		<logic:equal name="protocolFactory" property="functionByPerson" value="true">
			<fr:edit id="istResponsible3" name="protocolFactory" schema="search.istResponsibleFunction.byPerson">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright mbottom025"/>
			        <fr:property name="columnClasses" value="width10em,,tderror1"/>
				</fr:layout>
			</fr:edit>
			<p class="mtop05 mbottom1">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.researchActivity.choose" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
				</html:submit>
			</p>
			<logic:notEmpty name="protocolFactory" property="responsible">
				<fr:edit id="istResponsible4" name="protocolFactory" schema="search.istResponsibleFunction.unitFunctions">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright"/>
			        <fr:property name="columnClasses" value="width10em,,tderror1"/>
			        <fr:destination name="changePersonType" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
					<fr:destination name="changePersonorFunction" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
				</fr:layout>
				</fr:edit>
			</logic:notEmpty>
		</logic:equal>
		<logic:equal name="protocolFactory" property="functionByPerson" value="false">
			<fr:edit id="istResponsible5" name="protocolFactory" schema="search.istResponsibleFunction.byUnit">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright mbottom025"/>
			        <fr:property name="columnClasses" value="width10em,,tderror1"/>
				</fr:layout>
			</fr:edit>
			<p class="mtop05 mbottom1">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.researchActivity.choose" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
				</html:submit>
			</p>
			<logic:notEmpty name="protocolFactory" property="responsibleFunctionUnit">
				<fr:edit id="istResponsible6" name="protocolFactory" schema="search.istResponsibleFunction.unitFunctions">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright"/>
			        <fr:property name="columnClasses" value="width10em,,tderror1"/>
			        <fr:destination name="changePersonType" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
					<fr:destination name="changePersonorFunction" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
				</fr:layout>
				</fr:edit>
			</logic:notEmpty>
		</logic:equal>
	</logic:equal>
</logic:equal>

<logic:equal name="protocolFactory" property="istResponsible" value="false">
<fr:edit id="partnerResponsible" name="protocolFactory" schema="search.partnerResponsible">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mvert05 thmiddle thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
	<fr:destination name="changePersonType" path="/createProtocol.do?method=prepareCreateProtocolResponsibles"/>
</fr:edit>
</logic:equal>

<p class="mtop05">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" />
	</html:submit>
	<logic:present name="needToCreatePerson">
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalPerson" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:submit>
	</logic:present>
</p>

</fieldset>


<p class="mtop15">
	<logic:notPresent name="needToCreatePerson">
		<html:cancel bundle="HTMLALT_RESOURCES" property="back">
			<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>
		<html:cancel bundle="HTMLALT_RESOURCES" property="next">
			<bean:message key="button.next" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>	
	</logic:notPresent>
	<logic:present name="needToCreatePerson">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
			<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" /> <%-- Changed from cancel to back --%>
		</html:cancel>	
	</logic:present>
</p>
</fr:form>
</logic:notPresent>
</logic:notPresent>

<!-- Create External Person -->
<logic:present name="createExternalPerson">
<fr:form action="/createProtocol.do?method=createExternalResponsible">
<logic:present name="needToCreateUnit">
	<div class="warning0 mbottom1" style="padding: 0.25em 0.5em;">
		<strong><bean:message key="label.attention"/>:</strong><br/>
		<bean:message key="message.protocol.createNewUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
	</div>
</logic:present>

<fieldset class="fieldset1">
<legend><bean:message key="label.protocol.insertNewExternalPerson"/></legend>
<fr:edit id="responsible" name="protocolFactory" schema="partnerResponsible.fullCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mtop05 thmiddle thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
</fr:edit>
<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
	<logic:present name="needToCreateUnit">
		<html:submit bundle="HTMLALT_RESOURCES" property="createNew">
			<bean:message key="button.insertNewExternalUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:submit>
	</logic:present>	
</p>
</fieldset>
</fr:form>
</logic:present>

<!-- Create External Person and Unit -->
<logic:present name="createExternalUnit">
<fieldset class="fieldset1">
<fr:form action="/createProtocol.do?method=createExternalPersonAndUnit">
<legend><bean:message key="label.protocol.insertNewExternalPersonUnit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></legend>
<fr:view name="protocolFactory" schema="partnerUnit.fullCreation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mtop05 thmiddle thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
</fr:view>

<fr:edit id="country" name="protocolFactory" schema="partnerUnit.country">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle9 thlight mtop0 dinline thright"/>
        <fr:property name="columnClasses" value="width10em,,tderror1"/>
	</fr:layout>
</fr:edit>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
		<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
</p>
</fr:form>
</fieldset>
</logic:present>