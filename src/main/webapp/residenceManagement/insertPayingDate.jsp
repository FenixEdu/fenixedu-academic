<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="label.resident" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<bean:define id="monthOID" name="month" property="OID"/>
<bean:define id="eventOID" name="residenceEvent" property="OID"/>
<bean:define id="personOID"  name="residenceEvent" property="person.OID"/>

<fr:view name="residenceEvent" property="person" schema="show.residence.person">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft" />
	</fr:layout>
</fr:view>

<div class="dinline forminline">
<fr:form action="<%= "/residenceEventManagement.do?method=payResidenceEvent&monthOID=" + monthOID  + "&event=" + eventOID + "&person="  + personOID %>" >
	<table class="tstyle5">
		<tr>
			<td>
				<bean:message key="label.paymentDate" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>:
			</td>
			<td>
				<fr:edit id="date" name="bean" slot="yearMonthDay">
					<fr:layout name="default">
						<fr:property name="size" value="10"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>
	
	<html:submit><bean:message key="label.pay.residence.event" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></html:submit>
</fr:form>

<fr:form action="<%= "/residenceEventManagement.do?method=viewPersonResidenceEvents&monthOID=" + monthOID + "&person=" + personOID %>">
	<html:submit><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:submit>
</fr:form> 
</div>

