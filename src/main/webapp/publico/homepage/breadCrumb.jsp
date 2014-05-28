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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<html:xhtml/>

<bean:define id="homepage" name="actual$site" toScope="request"/>

<logic:present name="homepage">
    <logic:present name="homepage" property="person.employee.currentDepartmentWorkingPlace">
        <logic:present name="homepage" property="person.teacher">
            <bean:define id="institutionUrl" type="java.lang.String">
                <%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>
            </bean:define>
            <bean:define id="institutionUrlStructure" type="java.lang.String">
                <%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
            </bean:define>
            <bean:define id="departmentUnitID" type="java.lang.String">
                <bean:write name="homepage" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.externalId"/>
            </bean:define>
        
        	
            <div class="breadcumbs mvert0">
                <html:link href="<%= institutionUrl %>">
                    <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>
                </html:link>
                &nbsp;&gt;&nbsp;
                <html:link href="<%=institutionUrlStructure%>">
                    <bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="structure"/>
                </html:link>
                &nbsp;&gt;&nbsp;
                <html:link page="/publico/department/showDepartments.faces" module="">
                    <bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="academic.units"/>
                </html:link>
                &nbsp;&gt;&nbsp;            
                <bean:define id="currentDepartment" name="homepage" property="person.employee.currentDepartmentWorkingPlace"/>
				<app:contentLink name="currentDepartment" property="departmentUnit.site">
                    <bean:write name="currentDepartment" property="realName"/>
				</app:contentLink>				
                &nbsp;&gt;&nbsp;
                <bean:write name="homepage" property="ownersName"/>
            </div>
        </logic:present>
    </logic:present>
    
    <bean:define id="site" name="homepage" toScope="request"/>
    <bean:define id="siteActionName" value="/viewHomepage.do" toScope="request"/>
    <bean:define id="siteContextParam" value="homepageID" toScope="request"/>
    <bean:define id="siteContextParamValue" name="homepage" property="externalId" toScope="request"/>
    
</logic:present>