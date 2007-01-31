<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="degreeId" name="degree" property="idInternal"/>

<div class="breadcumbs mvert0">
    <a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a>
    <bean:define id="institutionUrlTeaching" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
    &nbsp;&gt;&nbsp;
    <a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
        
    &nbsp;&gt;&nbsp;
    <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + degreeId %>">
        <bean:write name="degree" property="sigla"/>
    </html:link>

    <logic:present name="sectionCrumbs">
        <logic:iterate id="crumbSection" name="sectionCrumbs">
            &nbsp;&gt;&nbsp;
            <bean:define id="crumbSectionId" name="crumbSection" property="idInternal"/>
            <html:link page="<%= String.format("/showDegreeSiteContent.do?method=section&amp;degreeID=%s&amp;sectionID=%s", degreeId, crumbSectionId) %>">
                <fr:view name="crumbSection" property="name"/>
            </html:link>
        </logic:iterate>

        <logic:notPresent name="item">
            &nbsp;&gt;&nbsp;
            <fr:view name="section" property="name"/>
        </logic:notPresent>
    </logic:present>
    
    <logic:present name="item">
        &nbsp;&gt;&nbsp;
        <fr:view name="item" property="name"/>
    </logic:present>
</div>

<!-- COURSE NAME -->
<h1 class="mbottom15">
    <logic:equal name="degree" property="bolonhaDegree" value="true">
        <bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="bolonhaDegreeType.name"/>
    </logic:equal>
    <logic:equal name="degree" property="bolonhaDegree" value="false">
        <bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="tipoCurso.name"/>
    </logic:equal>
    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in"/>
    <logic:present name="inEnglish">
        <logic:equal name="inEnglish" value="false">
            <bean:write name="degree" property="nome"/>
        </logic:equal>
        <logic:equal name="inEnglish" value="true">
            <bean:write name="degree" property="nameEn"/>
        </logic:equal>
    </logic:present>
</h1>

<bean:define id="site" name="degree" property="site" toScope="request"/>
<bean:define id="siteActionName" value="/showDegreeSiteContent.do" toScope="request"/>
<bean:define id="siteContextParam" value="degreeID" toScope="request"/>
<bean:define id="siteContextParamValue" name="degree" property="idInternal" toScope="request"/>
