<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="siteActionName" value="/manageDepartmentSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>

<h2><bean:message key="label.WebSiteManagement"/></h2>

<div class="mbottom05">
    <strong>
        <fr:view name="site" property="unit.nameWithAcronym"/>
    </strong>
</div>