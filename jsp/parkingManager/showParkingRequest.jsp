<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.request" /></h2>

<logic:present name="parkingRequest">
	
	<bean:define id="parkingParty" name="parkingRequest" property="parkingParty" />
	<bean:define id="personID" name="parkingParty" property="party.idInternal" />
	
	<h3><bean:message key="label.parkUserInfo" /></h3>
	<p><html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
	<logic:iterate id="occupation" name="parkingParty" property="occupations">
		<p><bean:write name="occupation" filter="false"/></p>
	</logic:iterate>
	<br/>		
	<h3><bean:message key="label.actualState" /></h3>
	<p><fr:view name="parkingParty" property="party" schema="viewPersonInfo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
		</fr:layout>
	</fr:view></p>
	<p><fr:view name="parkingParty" schema="viewParkingPartyInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
	</fr:view></p>	
	<logic:equal name="parkingParty" property="hasDriverLicense" value="true">
		<p><fr:view name="parkingParty" schema="parkingParty.driverLicense">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
			</fr:layout>
		</fr:view></p>
	</logic:equal>
	<logic:equal name="parkingParty" property="hasFirstCar" value="true">
		<p><fr:view name="parkingParty" schema="parkingParty.firstCar">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
			</fr:layout>
		</fr:view></p>
	</logic:equal>
	<logic:equal name="parkingParty" property="hasSecondCar" value="true">
		<p><fr:view name="parkingParty" schema="parkingParty.secondCar">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
			</fr:layout>
		</fr:view></p>
	</logic:equal>
	
	
	
	<h3><bean:message key="label.request" /></h3>
	<p><fr:view name="parkingRequest" schema="viewRequestPersonInfo">
			<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
	</fr:view></p>
	<p><fr:view name="parkingRequest" schema="viewParkingRequestInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
	</fr:view>
	<logic:equal name="parkingRequest" property="hasDriverLicense" value="true">
		<p><fr:view name="parkingRequest" schema="parkingRequest.driverLicense">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
			</fr:layout>
		</fr:view></p>
	</logic:equal>
	<logic:equal name="parkingRequest" property="hasFirstCar" value="true">
		<p><fr:view name="parkingRequest" schema="parkingRequest.firstCar">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
			</fr:layout>
		</fr:view></p>
	</logic:equal>
	<logic:equal name="parkingRequest" property="hasSecondCar" value="true">
		<p><fr:view name="parkingRequest" schema="parkingRequest.secondCar">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
			</fr:layout>
		</fr:view></p>
	</logic:equal>
	
	<logic:equal name="parkingRequest" property="parkingRequestState" value="PENDING">
		<bean:define id="parkingRequestID" name="parkingRequest" property="idInternal" />
		<html:form action="/parking">
			<html:hidden property="code" value="<%= parkingRequestID.toString()%>"/>
			<html:hidden property="method" value="editParkingParty"/>
			<html:hidden property="parkingRequestState" value="<%= pageContext.findAttribute("parkingRequestState").toString() %>"/>
			<html:hidden property="parkingPartyClassification" value="<%= pageContext.findAttribute("parkingPartyClassification").toString() %>"/>
			<html:hidden property="personName" value="<%= pageContext.findAttribute("personName").toString() %>"/>
			<html:hidden property="carPlateNumber" value="<%= pageContext.findAttribute("carPlateNumber").toString() %>"/>
			
		<%--onclick="javascript:document.forms[0].method.value='prepareEditDistributedTest';this.form.page.value=0;"
		--%>
			<p class="mbottom025"><strong><bean:message key="label.cardNumber"/></strong></p>
			<html:text size="10" property="cardNumber"/><span class="error"><!-- Error messages go here --><html:errors /></span>
			<span class="error0 mtop0"><html:messages id="message" property="cardNumber" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			
			<p class="mbottom025"><strong><bean:message key="label.group"/></strong></p>
			
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.variationCode" property="group">
				<html:option value="0">
					<bean:message key="label.choose" />
				</html:option>
				<logic:iterate id="groupIt" name="groups" type="net.sourceforge.fenixedu.domain.parking.ParkingGroup">
					<bean:define id="groupId" name="groupIt" property="idInternal"/>
					<html:option value="<%=groupId.toString()%>">
						<bean:write name="groupIt" property="groupName"/>
					</html:option>
				</logic:iterate>
			</html:select>
			<span class="error0 mtop0"><html:messages id="message" property="group" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			
			
			<p class="mbottom025"><strong><bean:message key="label.note"/></strong></p>
			<html:textarea rows="7" cols="45" property="note"/>
			<p class="mtop2">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="accept"><bean:message key="button.accept"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="notify"><bean:message key="button.notify"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="reject"><bean:message key="button.reject"/></html:submit>
			</p>
		</html:form>
	</logic:equal>
</logic:present>
