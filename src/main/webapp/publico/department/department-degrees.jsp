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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<html:xhtml/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link href="<%= institutionUrl + structureUrl %>">
        <bean:message key="structure" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="/department/showDepartments.faces">
        <bean:message key="academic.units" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="unitId" name="unit" property="externalId"/>
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="nameI18n"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="label.degrees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <bean:message key="label.degrees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <bean:message key="of.masculine" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <fr:view name="department" property="nameI18n"/>
</h1>

<logic:notEmpty name="active-types">
    <logic:iterate id="type" name="active-types">
        <bean:define id="typeName" name="type" property="name" type="java.lang.String"/>
        <h2 class="mtop2">
            <bean:message key="<%= "DegreeType." + typeName %>" bundle="ENUMERATION_RESOURCES"/>
        </h2>
    
        <ul>
            <logic:iterate id="degree" name="<%= typeName %>">
                <logic:equal name="degree" property="active" value="true">
                <li>                
                	<app:contentLink name="degree" property="site">
                		<fr:view name="degree" property="nameI18N"/>
                	</app:contentLink>                    
                </li>
                </logic:equal>
            </logic:iterate>
        </ul>
    </logic:iterate>
</logic:notEmpty>

<logic:notEmpty name="inactive-types">

	<h3 class="mtop3 mbottom1" ><bean:message key="label.inactive.degrees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></h3>
    <logic:iterate id="type" name="inactive-types">
        <bean:define id="typeName" name="type" property="name" type="java.lang.String"/>
        <h2 class="mtop2 greytxt">
            <bean:message key="<%= "DegreeType." + typeName %>" bundle="ENUMERATION_RESOURCES"/>
        </h2>
    
        <ul>
            <logic:iterate id="degree" name="<%= typeName %>">
                <logic:equal name="degree" property="active" value="false">
                <li>                
                	<app:contentLink name="degree" property="site">
                		<fr:view name="degree" property="nameI18N"/>
                	</app:contentLink>                    
                </li>
                </logic:equal>
            </logic:iterate>
        </ul>
    </logic:iterate>
</logic:notEmpty>
