<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="org.fenixedu.academic.domain.organizationalStructure.UnitUtils"%>
<%@page import="org.fenixedu.academic.domain.organizationalStructure.Unit"%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/drag-drop-folder-tree/js/ajax.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/drag-drop-folder-tree/js/drag-drop-folder-tree.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/drag-drop-folder-tree/js/tree-renderer.js"></script>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mvert2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<h3><bean:message key="label.student.enrollment.choose.externalUnit" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<bean:define id="registrationId" name="registration" property="externalId" />
<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />
<logic:notEmpty name="parameters">
	<bean:define id="parameters">&amp;<bean:write name="parameters"/></bean:define>
</logic:notEmpty>


<p>&nbsp;</p>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.0/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.0/js/select2.full.min.js"></script>
<select id="countries">
	<option value=""><bean:message key="label.selected" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="label.externalUnits.country" bundle="ACADEMIC_OFFICE_RESOURCES"/></option>
<% for(Unit country : UnitUtils.readEarthUnit().getSortedExternalChilds()) { %>
	<option value="<%= country.getExternalId() %>"><%= country.getName() %></option>
<% } %>
</select>
<script type="text/javascript">
$("#countries").select2();
$("#countries").on("change", function(){
	id = $("#countries").val()
	$("#countryUnitField").val(id);
   	if(id != ''){
        //$("#selectCountryButton").removeAttr('disabled');
        $("#countrySelectionForm").submit();
    }	
});
</script>
<fr:form action="<%= contextInformation.toString() + "method=chooseExternalUnit" + parameters %>" id="countrySelectionForm">
	<html:hidden property="registrationId" value="<%= registrationId.toString() %>" />
	<html:hidden property="unitId" value="" styleId="countryUnitField" />
<%-- 	<html:submit disabled="true" styleId="selectCountryButton"> --%>
<%-- 		<bean:message key="label.selected" bundle="ACADEMIC_OFFICE_RESOURCES"/> --%>
<%-- 		<bean:message key="label.externalUnits.country" bundle="ACADEMIC_OFFICE_RESOURCES"/> --%>
<%-- 	</html:submit> --%>
</fr:form>
<br/>

<logic:notEmpty name="unit">
	<h2><bean:write name="unit" property="name"/></h2>
	<fr:view name="unit" property="sortedExternalChilds">
	    <fr:layout name="tree">
	        <fr:property name="eachLayout" value="values"/>
	        <fr:property name="schemaFor(Unit)" value="Unit.name.tree.view"/>
<%-- 	        <fr:property name="childrenFor(Unit)" value="sortedExternalChilds"/> --%>
	        <fr:property name="expandable" value="true"/>
	    </fr:layout>
	    <fr:destination name="choose.ExternalCurricularCourses" path="<%= contextInformation.toString() + "method=chooseExternalCurricularCourses&amp;registrationId=" + registrationId + parameters + "&amp;externalUnitId=${externalId}" %>"/>
	</fr:view>
</logic:notEmpty>

<fr:form action="<%= contextInformation.toString() + "method=backToMainPage" + parameters %>">
	<html:hidden property="registrationId" value="<%= registrationId.toString() %>"/>
	<br/>
	<html:cancel><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
