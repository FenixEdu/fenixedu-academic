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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilancyPoints"/></h2>

<bean:define id="groupID" name="group" property="externalId"/>
<ul>
	<li>
		<html:link page="/vigilancy/vigilantGroupManagement.do?method=selectPreviousPointsSchema" paramId="oid" paramName="groupID">
			<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.selectPreviousPointsSchema" />
		</html:link>
	</li>
</ul>

<fr:edit id="group" name="group" schema="edit.vigilantGroup.points">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="success" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
</fr:edit>