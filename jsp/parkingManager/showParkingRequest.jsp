<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.request" /></h2>

<logic:present name="parkingRequest">
	
	<bean:define id="parkingParty" name="parkingRequest" property="parkingParty" />
		<table>
			<tr>
			<%-- tstyle1 thtop thlight printborder --%>
				<th><strong><bean:message key="label.actualState" /></strong></th>
				<th><strong><bean:message key="label.request" /></strong></th>
			</tr>
			<tr>
				<td><fr:view name="parkingParty" property="party" schema="viewPersonInfo">
					<fr:layout name="tabular">
						<fr:property name="classes"
							value="tstyle1 thright thlight mtop025" />
					</fr:layout>
				</fr:view></td>
				<td><fr:view name="parkingRequest" schema="viewRequestPersonInfo">
					<fr:layout name="tabular">
						<fr:property name="classes"
							value="tstyle1 thright thlight mtop025" />
					</fr:layout>
				</fr:view></td>
			</tr>
			<tr>
				<td><fr:view name="parkingParty" schema="viewParkingPartyInfo">
					<fr:layout name="tabular">
						<fr:property name="classes"
							value="tstyle1 thright thlight mtop025" />
					</fr:layout>
				</fr:view></td>
				<td><fr:view name="parkingRequest" schema="viewParkingRequestInfo">
					<fr:layout name="tabular">
						<fr:property name="classes"
							value="tstyle1 thright thlight mtop025" />
					</fr:layout>
				</fr:view></td>
			</tr>
			<tr>
				<td>
				<logic:equal name="parkingParty" property="hasDriverLicense" value="true">
					<fr:view name="parkingParty" schema="parkingParty.driverLicense">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
						</fr:layout>
					</fr:view>
				</logic:equal>
				</td>
				<td>
				<logic:equal name="parkingRequest" property="hasDriverLicense" value="true">
					<fr:view name="parkingRequest" schema="parkingRequest.driverLicense">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
						</fr:layout>
					</fr:view>
				</logic:equal>
				</td>
			</tr>
			
			<tr>
				<td>
				<logic:equal name="parkingParty" property="hasFirstCar" value="true">
					<fr:view name="parkingParty" schema="parkingParty.firstCar">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
						</fr:layout>
					</fr:view>
				</logic:equal>
				</td>
				<td>
				<logic:equal name="parkingRequest" property="hasFirstCar" value="true">
					<fr:view name="parkingRequest" schema="parkingRequest.firstCar">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
						</fr:layout>
					</fr:view>
				</logic:equal>
				</td>
			</tr>
			
			<tr>
				<td>
				<logic:equal name="parkingParty" property="hasSecondCar" value="true">
					<fr:view name="parkingParty" schema="parkingParty.secondCar">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
						</fr:layout>
					</fr:view>
				</logic:equal>
				</td>
				<td>
				<logic:equal name="parkingRequest" property="hasSecondCar" value="true">
					<fr:view name="parkingRequest" schema="parkingRequest.secondCar">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
						</fr:layout>
					</fr:view>
				</logic:equal>
				</td>
			</tr>
		</table>

	<logic:equal name="parkingRequest" property="parkingRequestState" value="PENDING">
		<bean:define id="parkingRequestID" name="parkingRequest" property="idInternal" />
		<html:form action="/parking">
			<html:hidden property="code" value="<%= parkingRequestID.toString()%>"/>
			<html:hidden property="method" value="editParkingParty"/>
		<%--onclick="javascript:document.forms[0].method.value='prepareEditDistributedTest';this.form.page.value=0;"
		--%>
			<p class="mbottom025"><strong><bean:message key="label.cardNumber"/></strong></p>
			<html:text size="10" property="cardNumber"/><span class="error"><!-- Error messages go here --><html:errors /></span>
			
			<p class="mbottom025"><strong><bean:message key="label.group"/></strong></p>
			
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.variationCode" property="group">
				<logic:iterate id="groupIt" name="groups" type="net.sourceforge.fenixedu.domain.parking.ParkingGroup">
					<bean:define id="groupId" name="groupIt" property="idInternal"/>
					<html:option value="<%=groupId.toString()%>">
						<bean:write name="groupIt" property="groupName"/>
					</html:option>
				</logic:iterate>
			</html:select>
			
			
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
