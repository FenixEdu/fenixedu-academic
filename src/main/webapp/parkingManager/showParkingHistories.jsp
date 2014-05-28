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


<logic:present name="parkingParty">
	<h2><bean:message key="label.actualState" /></h2>
	<fr:view name="parkingParty" schema="show.parkingParty">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 ulnomargin" />
			<fr:property name="columnClasses" value="acenter,,,," />
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="parkingPartyHistories">
	<h2><bean:message key="label.history" /></h2>
	<fr:view name="parkingPartyHistories" schema="show.parkingPartyHistory">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 ulnomargin" />
			<fr:property name="columnClasses" value="acenter,,,," />
		</fr:layout>
	</fr:view>
</logic:present>
