<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
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
