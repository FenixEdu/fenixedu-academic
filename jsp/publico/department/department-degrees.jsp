<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/> 
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
    <bean:define id="unitId" name="unit" property="idInternal"/>
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="realName"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="label.degrees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <bean:message key="label.degrees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <bean:message key="of.masculine" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <fr:view name="department" property="realName"/>
</h1>

<logic:notEmpty name="types">
    <logic:iterate id="type" name="types">
        <bean:define id="typeName" name="type" property="name" type="java.lang.String"/>
        <h2 class="mtop2">
            <bean:message key="<%= "DegreeType." + typeName %>" bundle="ENUMERATION_RESOURCES"/>
        </h2>
    
        <ul>
            <logic:iterate id="degree" name="<%= typeName %>">
                <li>
                    <bean:define id="degreeId" name="degree" property="idInternal"/>
                    <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + degreeId %>">
                        <fr:view name="degree" property="nameI18N"/>
                    </html:link>
                </li>
            </logic:iterate>
        </ul>
    </logic:iterate>
</logic:notEmpty>
