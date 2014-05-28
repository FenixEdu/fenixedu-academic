<%--

    Copyright © 2014 Instituto Superior Técnico

    This file is part of Fenix Parking.

    Fenix Parking is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Fenix Parking is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.manageRequestsPeriods" /></h2>

<p class="mtop15 mbottom05">	
	<span class="error0">
		<html:messages id="message" message="true" bundle="PARKING_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>
	
<logic:present name="parkingRequestPeriodToEdit">
	<p class="mbottom025"><strong><bean:message key="label.editPeriod"/></strong></p>
	<fr:edit id="parkingRequestPeriodToEdit" name="parkingRequestPeriodToEdit" schema="show.parkingRequestPeriod" action="/manageParkingPeriods.do?method=prepareManageRequestsPeriods" type="org.fenixedu.parking.domain.ParkingRequestPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop025" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	</fr:edit>
</logic:present>
<logic:notPresent name="parkingRequestPeriodToEdit">
	<p class="mbottom025"><strong><bean:message key="label.insertNewPeriod"/></strong></p>
	<fr:form action="/manageParkingPeriods.do?method=prepareManageRequestsPeriods">
		<fr:create id="aa" schema="show.parkingRequestPeriod" type="org.fenixedu.parking.domain.ParkingRequestPeriod">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mvert025" />
				<fr:property name="columnClasses" value=",,tderror1 tdclear" />
			</fr:layout>
		</fr:create>
		<p class="mtop05">
			<html:submit>
				<bean:message key="button.submit" />
			</html:submit>
		</p>
	</fr:form>
</logic:notPresent>




<logic:present name="parkingRequestPeriods">
	<bean:size id="size" name="parkingRequestPeriods"/>
	<logic:equal name="size" value="0">
		<p class="mtop15">
			<em><bean:message key="label.periodNotSpecified"/>.</em>
		</p>
	</logic:equal>
	<logic:notEqual name="size" value="0">
		<fr:view name="parkingRequestPeriods" schema="show.parkingRequestPeriod">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="columnClasses" value="bgcolor3 acenter,acenter,aleft" />
				<fr:property name="headerClasses" value="acenter" />
				
				<fr:property name="link(edit)" value="/manageParkingPeriods.do?method=editRequestPeriod" />
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="param(edit)" value="externalId" />
				<fr:property name="bundle(edit)" value="PARKING_RESOURCES" />
				<fr:property name="link(delete)" value="/manageParkingPeriods.do?method=deleteRequestPeriod" />
				<fr:property name="key(delete)" value="label.delete" />
				<fr:property name="param(delete)" value="externalId" />
				<fr:property name="bundle(delete)" value="PARKING_RESOURCES" />
			</fr:layout>
		</fr:view>	
	</logic:notEqual>
</logic:present>
