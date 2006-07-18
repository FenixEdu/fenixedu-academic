<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3><bean:message bundle="CMS_RESOURCES" key="cms.websiteTypeManagement.label" /></h3>

<logic:present name="websiteTypes">
    <fr:view name="websiteTypes" layout="tabular" schema="websiteType.basic">
        <fr:layout>
            <fr:property name="headerClasses" value="listClasses-header"/>
            <fr:property name="columnClasses" value="listClasses"/>
            <fr:property name="style" value="width: 100%"/>

            <fr:property name="link(details)" value="/websiteTypeManagement.do?method=details"/>
            <fr:property name="param(details)" value="idInternal/oid"/>
            <fr:property name="key(details)" value="cms.websiteTypeManagement.websiteType.details"/>
            <fr:property name="bundle(details)" value="CMS_RESOURCES"/>
            <fr:property name="order(details)" value="0"/>
            
            <fr:property name="link(edit)" value="/websiteTypeManagement.do?method=edit"/>
            <fr:property name="param(edit)" value="idInternal/oid"/>
            <fr:property name="key(edit)" value="cms.websiteTypeManagement.websiteType.edit"/>
            <fr:property name="bundle(edit)" value="CMS_RESOURCES"/>
            <fr:property name="order(edit)" value="1"/>
            
            <fr:property name="link(delete)" value="/websiteTypeManagement.do?method=delete"/>
            <fr:property name="param(delete)" value="idInternal/oid"/>
            <fr:property name="key(delete)" value="cms.websiteTypeManagement.websiteType.delete"/>
            <fr:property name="bundle(delete)" value="CMS_RESOURCES"/>
            <fr:property name="order(delete)" value="2"/>
        </fr:layout>
    </fr:view>
</logic:present>

<html:form action="/websiteTypeManagement" method="get">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
    <html:submit><bean:message key="cms.websiteTypeManagement.create" bundle="CMS_RESOURCES"/></html:submit>
</html:form>