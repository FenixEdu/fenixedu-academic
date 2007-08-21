<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType" %>
<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<ul class="list5">
	<li>
		<html:link page="/createProtocol.do?method=prepareCreateProtocolData"><bean:message key="link.protocols.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></html:link>
	</li>
</ul>

<fr:form action="/protocols.do?method=showProtocols">
	<fr:edit id="active" name="protocolSearch" schema="edit.protocolSearch.active">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
		<fr:destination name="postBack" path="/protocols.do?method=showProtocols"/>
	</fr:edit>	
</fr:form>

<logic:notEmpty name="protocols">
<e:labelValues id="actionTypes" enumeration="net.sourceforge.fenixedu.domain.protocols.util.ProtocolActionType" bundle="ENUMERATION_RESOURCES" />		
	<logic:iterate id="protocol" name="protocols" type="net.sourceforge.fenixedu.domain.protocols.Protocol">

	<table class="tdtop mvert0" style=" padding: 0; margin: 0;">
		<tr>
			<td style="width: 390px;">
				<fr:view name="protocol" schema="show.protocol.toList">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 fleft thlight thright mvert0"/>
						<fr:property name="columnClasses" value="nowrap,width225px"/>
					</fr:layout>
				</fr:view>
			</td>
			<td>
				<table class="tstyle1 thlight" style="padding: 0; margin: 0;">
					<tr><th><bean:message key="label.protocol.actionTypes" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th></tr>
					<tr>
						<td>
							<logic:iterate id="actionType" name="actionTypes" type="org.apache.struts.util.LabelValueBean">
								<bean:define id="isPresent" value="false" type="java.lang.String"/>
								<logic:notEmpty name="protocol" property="protocolAction">
									<bean:define id="isPresent" type="java.lang.String">
										<%= protocol.getProtocolAction().contains(ProtocolActionType.valueOf(actionType.getValue())) %>
									</bean:define>
								</logic:notEmpty>
								<logic:equal name="isPresent" value="true">
									<strong><bean:message name="actionType" property="value" bundle="ENUMERATION_RESOURCES"/></strong>
								</logic:equal>
								<logic:equal name="isPresent" value="false">
									<span class="color888"><bean:message name="actionType" property="value" bundle="ENUMERATION_RESOURCES"/></span>
								</logic:equal>
								<br/>
							</logic:iterate>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<html:link page="/editProtocol.do?method=viewProtocol" paramId="idInternal" paramName="protocol" paramProperty="idInternal">
					<bean:message key="link.edit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
				</html:link>
			</td>
		</tr>
	</table>
	
	
	</logic:iterate>
</logic:notEmpty>