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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp$ListDepartmentTeachers" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DepartmentMemberResources"
		var="bundle" />
	<f:loadBundle basename="resources/EnumerationResources"
		var="bundleEnumeration" />
		
	<h:form>
	
		<h:outputText value="<h2>#{bundle['label.teacher.list.title']}</h2>" escape="false" />
		<h:outputText value="<h3>#{viewDepartmentTeachers.department.realName}</h3>" escape="false" />

	
		<h:panelGrid columns="2" styleClass="search">
			<h:outputText value="#{bundle['label.common.executionYear']}:" styleClass="aright"/>
			<fc:selectOneMenu id="dropDownListExecutionYearID" value="#{viewDepartmentTeachers.selectedExecutionYearID}" valueChangeListener="#{viewDepartmentTeachers.onSelectedExecutionYearChanged}" onchange="this.form.submit();">
				<f:selectItems value="#{viewDepartmentTeachers.executionYears}" />
			</fc:selectOneMenu>
		</h:panelGrid>		
	
		<h:outputText value="<br/>" escape="false" />

		<h:dataTable value="#{viewDepartmentTeachers.departmentTeachers}"
			var="teacher" styleClass="table">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.id']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.teacherId}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.name']}"></h:outputText>
				</f:facet>
                <h:outputText value="#{teacher.person.name}" styleClass="aright" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.teacher.category']}"></h:outputText>
				</f:facet>
				<h:outputText value="#{teacher.category.name}" />
			</h:column>
		</h:dataTable>
	</h:form>
</f:view>
