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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<html:xhtml/>


<h2><bean:message key="link.firstYearShifts.export"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<fr:form action="/exportFirstYearShifts.do?method=export">
	<fr:edit name="first_year_shifts_export">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.FirstYearShiftsBean" bundle="APPLICATION_RESOURCES">
		    <fr:slot name="executionYear" layout="menu-select" key="property.executionPeriod"
		        validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		        <fr:property name="providerClass"
		            value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
		        <fr:property name="format" value="${name}" />
		        <fr:property name="nullOptionHidden" value="true" />
		    </fr:slot>
		    <fr:slot name="entryPhase" key="label.firstTimeStudents.phase"
		        validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		    </fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px,,tderror" />
		</fr:layout>
	</fr:edit>

<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="label.choose" />
</html:submit></p>
</fr:form>

