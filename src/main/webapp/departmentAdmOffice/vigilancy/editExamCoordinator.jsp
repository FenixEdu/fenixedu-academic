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


<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editExamCoordinator"/></h2><br>


<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>

<fr:edit id="editCoordinators" name="bean" property="examCoordinators" schema="editCoordinatorPreviledges">
	<fr:destination name="cancel" path="<%= "/vigilancy/examCoordinatorManagement.do?method=prepareAddExamCoordinatorWithState&unitId=" + bean.getSelectedUnit().getExternalId() + "&deparmentId=" + bean.getSelectedDepartment().getExternalId() %>"/>
	<fr:destination name="success" path="<%= "/vigilancy/examCoordinatorManagement.do?method=prepareAddExamCoordinatorWithState&unitId=" + bean.getSelectedUnit().getExternalId() + "&deparmentId=" + bean.getSelectedDepartment().getExternalId() %>"/>
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5"/>
		<fr:property name="rowClasses" value="trhighlight1, "/>
	</fr:layout>
</fr:edit>