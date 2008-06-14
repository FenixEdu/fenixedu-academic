<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<script language="Javascript" type="text/javascript">
<!--
function addElement(addElement){
	if(addElement == 1){
		addElement = "responsible";
	} else if(addElement == 2){
		addElement = "partnerResponsible";
	}else if(addElement == 3){
		addElement = "unit";
	} else if(addElement == 4){
		addElement = "partnerUnit";
	} else {
		addElement = "file";
	}
	document.forms[0].method.value='prepareCreateProtocol';
	document.forms[0].addElement.value=addElement;	
	document.forms[0].submit();
	return true;
}
// -->
</script>

<html:xhtml/>
<h2><bean:message key="title.protocols"/></h2>
<br/>
<h3><bean:message key="title.protocols.insert"/></h3>
<br/>
<%
int addResponsible = 1;
int addPartnerResponsible = 2;
int addUnit = 3;
int addPartnerUnit = 4;
int addFile = 5;
%>

<fr:form action="/protocols.do" encoding="multipart/form-data">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="protocolsForm" property="method" value="createProtocol"/>
	<html:hidden name="protocolsForm" property="addElement"/>

<logic:iterate id="responsible" name="protocolFactory" property="responsibles" indexId="iter">
	<bean:define id="ID">responsible<bean:write name="iter"/></bean:define>
	<fr:edit id="<%= ID %>" name="responsible" schema="search.istResponsible"/>
</logic:iterate>
<ul>
	<li>		
		<html:link href="<%= "javascript:addElement("+addResponsible+")"%>"><bean:message key="link.protocol.addResponsible" /></html:link>
	</li>
</ul>
<br/>

<logic:iterate id="partnerResponsible" name="protocolFactory" property="partnerResponsibles" indexId="iter">
	<bean:define id="ID">partnerResponsible<bean:write name="iter"/></bean:define>
	<fr:edit id="<%= ID %>" name="partnerResponsible" schema="search.partnerResponsible"/>
</logic:iterate>
<ul>
	<li>		
		<html:link href="<%= "javascript:addElement("+addPartnerResponsible+")"%>"><bean:message key="link.protocol.addPartnerResponsible" /></html:link>
	</li>
</ul>
<br/>

<logic:iterate id="unit" name="protocolFactory" property="units" indexId="iter">
	<bean:define id="ID">unit<bean:write name="iter"/></bean:define>
	<fr:edit id="<%= ID %>" name="unit" schema="search.unit"/>
</logic:iterate>
<ul>
	<li>		
		<html:link href="<%= "javascript:addElement("+addUnit+")"%>"><bean:message key="link.protocol.addUnit" /></html:link>
	</li>
</ul>
<br/>

<logic:iterate id="partnerUnit" name="protocolFactory" property="partnerUnits" indexId="iter">
	<bean:define id="ID">partnerUnit<bean:write name="iter"/></bean:define>
	<fr:edit id="<%= ID %>" name="partnerUnit" schema="search.partnerUnit"/>	
</logic:iterate>
<ul>
	<li>		
		<html:link href="<%= "javascript:addElement("+addPartnerUnit+")"%>"><bean:message key="link.protocol.addUnitPartner" /></html:link>
	</li>
</ul>
<br/>
<fr:edit id="protocolFactory" name="protocolFactory" schema="create.protocol"/>

<br/>
<logic:iterate id="file" name="protocolFactory" property="files" indexId="iter">
	<bean:define id="ID">file<bean:write name="iter"/></bean:define>
	<fr:edit id="<%= ID %>" name="file" schema="edit.protocolFile"/>
</logic:iterate>
<ul>
	<li>		
		<html:link href="<%= "javascript:addElement("+addFile+")"%>"><bean:message key="link.protocol.addFile" /></html:link>
	</li>
</ul>
<br/>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit" />
	</html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='showProtocols';this.form.submit();">
		<bean:message key="button.cancel" />
	</html:submit>
</p>
</fr:form>