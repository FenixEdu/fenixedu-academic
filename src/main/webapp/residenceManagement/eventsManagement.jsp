<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.debtManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>


	<p class="mtop15 mbottom025">
		<bean:message key="label.currentYear.prices" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>:
	</p>
	
	<table class="tstyle1 thright thlight mvert05">
	<tr>
		<th>
			<bean:message key="label.roomValues" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</th>
		<td>
			<logic:present name="currentResidence" property="currentSingleRoomValue">
				<fr:view name="currentResidence" property="currentSingleRoomValue"/> 
				<bean:message key="label.currency.symbol" bundle="GLOBAL_RESOURCES"/> | 
			</logic:present>
			<logic:present name="currentResidence" property="currentDoubleRoomValue">
				<fr:view name="currentResidence" property="currentDoubleRoomValue"/> <bean:message key="label.currency.symbol" bundle="GLOBAL_RESOURCES"/>
			</logic:present> 
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.paymentDate" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</th>
		<td>
			<fr:view name="currentResidence" property="currentPaymentLimitDay" type="java.lang.Integer">
				<fr:layout name="null-as-label">
					<property name="subLayout" value="values"/>
					<property name="label" value="-"/>			
				</fr:layout>
			</fr:view>
		</td>
	</tr>
	</table>


<fr:form action="/residenceEventManagement.do">
	<fr:edit id="searchEventMonth" name="searchBean" schema="edit.import.residence.bean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight"/>
				<fr:property name="columnClasses" value=",,dnone"/>
			</fr:layout>
			<fr:destination name="postback" path="/residenceEventManagement.do?method=manageResidenceEvents"/>
	</fr:edit>
</fr:form>


<logic:present name="searchBean">
	<logic:present name="searchBean" property="residenceMonth">
		<bean:define id="monthOID" name="searchBean" property="residenceMonth.OID"/>

		<p class="mtop05">
			<logic:equal name="currentResidence" property="residencePriceTableConfigured" value="true">
				<html:link page="<%=  "/residenceManagement.do?method=importData&monthOID=" + monthOID + (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "")%>">
					<bean:message key="label.importFile" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
				</html:link>
				|
				<html:link page="<%=  "/residenceManagement.do?method=importCurrentDebts&monthOID=" + monthOID +  (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "") %>">
					Actualizar dividas (Excel)
				</html:link>
			</logic:equal>
			|
			<html:link page="<%=  "/residenceManagement.do?method=editPaymentLimitDay&monthOID=" + monthOID %>">
				<bean:message key="label.modify.limitPaymentDay" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
			</html:link>
			|
			<html:link page="<%=  "/residenceManagement.do?method=editRoomValues&monthOID=" + monthOID %>">
				<bean:message key="label.modify.roomValues" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
			</html:link>
			<logic:present role="role(MANAGER)">
			|
			<html:link page="<%= "/residenceEventManagement.do?method=generatePaymentCodes&monthOID=" + monthOID %>">
				<bean:message key="label.residence.events.generate.payment.codes" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
			</html:link>
			</logic:present>
		</p>

		<logic:notEmpty name="searchBean" property="residenceMonth.events">		
			<p class="mtop15 mbottom05"><bean:message key="label.residents.with.paymentCodes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></p>
		
			<logic:notEmpty name="searchBean" property="residenceMonth.eventsWithPaymentCodes">
				<fr:view name="searchBean" property="residenceMonth.eventsWithPaymentCodes" schema="show.residenceEvent.with.dates">	
						<fr:layout name="tabular-sortable">
                                <fr:property name="classes" value="tstyle1 tdcenter thlight mtop05"/>
                                <fr:property name="columnClasses" value=",,aleft,,,,,"/>
                                <fr:property name="sortParameter" value="sortBy"/>
                                <fr:property name="sortUrl" value="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>" />
                                <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "person.student.number=asc" : request.getParameter("sortBy") %>"/>
                                <fr:property name="sortableSlots" value="person.student.number, person.socialSecurityNumber, person.name, roomValue, room, paymentDate" />
                        </fr:layout>

						<fr:destination name="personLink" path="<%= "/residenceEventManagement.do?method=viewPersonResidenceEvents&person=${person.OID}&monthOID=${residenceMonth.OID}" + (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "") %>"/>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="searchBean" property="residenceMonth.eventsWithPaymentCodes">
				<p class="mtop05"><em><bean:message key="message.month.no.payments.with.codes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em></p>
			</logic:empty>
			
			<p class="mtop2 mbottom05"><bean:message key="label.residents.with.no.paymentCodes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></p>
			<logic:notEmpty name="searchBean" property="residenceMonth.eventsWithoutPaymentCodes">
				<fr:view name="searchBean" property="residenceMonth.eventsWithoutPaymentCodes" schema="show.residenceEvent.with.dates">	
						<fr:layout name="tabular-sortable">
							 <fr:property name="classes" value="tstyle1 tdcenter thlight mtop05"/>
                                <fr:property name="columnClasses" value=",,aleft,,,,,"/>
                                <fr:property name="sortParameter" value="sortBy"/>
                                <fr:property name="sortUrl" value="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>" />
                                <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "person.student.number=asc" : request.getParameter("sortBy") %>"/>
                                <fr:property name="sortableSlots" value="person.student.number, person.socialSecurityNumber, person.name, roomValue, room, paymentDate" />
                     
						</fr:layout>
						<fr:destination name="personLink" path="<%= "/residenceEventManagement.do?method=viewPersonResidenceEvents&person=${person.OID}&monthOID=${residenceMonth.OID}" + (request.getParameter("sortBy") != null ? "&sortBy=" + request.getParameter("sortBy") : "") %>"/>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="searchBean" property="residenceMonth.eventsWithoutPaymentCodes">
				<p class="mtop05"><em><bean:message key="message.month.no.payments.without.codes" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em></p>
			</logic:empty>
			
		</logic:notEmpty>
		
		<logic:empty name="searchBean" property="residenceMonth.events">
			<p><em><bean:message key="message.month.empty.events" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>.</em></p>
		</logic:empty>
		
		
		
		
	</logic:present>
</logic:present>

