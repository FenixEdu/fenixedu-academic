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
    <fr:view name="department" property="realName"/>
</div>

<h1>
    <fr:view name="department" property="realName"/>
</h1>

<logic:present name="announcements">
    <logic:notEmpty name="announcements">
        <table class="box" cellspacing="0" style="float: right;">
        <tr>
            <td class="box_header">
                <strong><bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="public.department.information.label.latestAnnouncements"/></strong>
            </td>
        </tr>
        <tr>
            <td class="box_cell">
                <bean:define id="unitId" name="unit" property="idInternal"/>
                <logic:iterate id="announcement" name="announcements">
                    <p style="padding-bottom: 0.9em;">
                        <fr:view name="announcement" property="lastModification" layout="no-time"/><br/> 
                        
                        <bean:define id="announcementId" name="announcement" property="idInternal"/>
                        <html:link page="<%= String.format("/department/announcements.do?method=viewAnnouncement&amp;selectedDepartmentUnitID=%s&amp;announcementId=%s", unitId, announcementId) %>">
                            <fr:view name="announcement" property="subject"/>
                        </html:link>
                    </p>
                </logic:iterate>
                <p class="aright">
                    <html:link page="<%= String.format("/department/announcements.do?method=viewAnnouncements&amp;selectedDepartmentUnitID=%s", unitId) %>">
                        <bean:message bundle="PUBLIC_DEPARTMENT_RESOURCES" key="public.department.information.label.latestAnnouncements.showAll"/>
                    </html:link>
                </p>
            </td>
        </tr>
        </table>
    </logic:notEmpty>
</logic:present>

<logic:present name="site">
    <logic:notEmpty name="site" property="alternativeSite">
        <p class="mtop15">
            <bean:message key="label.department.alternativePage" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>:
            <fr:view name="site" property="alternativeSite">
                <fr:layout>
                    <fr:property name="link" value="true"/>
                </fr:layout>
            </fr:view>
        </p>
    </logic:notEmpty>
    
    <div class="mtop15">
        <logic:notPresent name="noDescription"> 
            <fr:view name="site" property="description" layout="html"/>
        </logic:notPresent>
    </div>
</logic:present>

<logic:present name="noDescription">
    <bean:message key="label.department.no.description" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
</logic:present>

<p class="mtop25" style="color: #888;">
    <em><bean:message key="label.department.info.resposibility" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></em>
</p>
