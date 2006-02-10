<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3><bean:message bundle="CMS_RESOURCES" key="cms.websiteTypeManagement.label" /></h3>

<logic:present name="websiteType">
    <fr:edit action="/websiteTypeManagement.do?method=start"
             name="websiteType" layout="tabular" schema="websiteType.edit"/>
</logic:present>
