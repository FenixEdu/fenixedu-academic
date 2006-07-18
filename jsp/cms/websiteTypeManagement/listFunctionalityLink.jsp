<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3><bean:message bundle="CMS_RESOURCES" key="cms.functionalityLinkManagement.label" /></h3>

<logic:present name="functionalityLinks">
    <fr:view name="functionalityLinks" layout="tabular" schema="cms.content.basic">
        <fr:layout>
            <fr:property name="headerClasses" value="listClasses-header"/>
            <fr:property name="columnClasses" value="listClasses"/>
            <fr:property name="style" value="width: 100%"/>

            <fr:property name="link(details)" value="/functionalityLinkManagement.do?method=details"/>
            <fr:property name="param(details)" value="idInternal/oid"/>
            <fr:property name="key(details)" value="cms.functionalityLinkManagement.functionalityLink.details"/>
            <fr:property name="bundle(details)" value="CMS_RESOURCES"/>
            <fr:property name="order(details)" value="0"/>
            
            <fr:property name="link(edit)" value="/functionalityLinkManagement.do?method=edit"/>
            <fr:property name="param(edit)" value="idInternal/oid"/>
            <fr:property name="key(edit)" value="cms.functionalityLinkManagement.functionalityLink.edit"/>
            <fr:property name="bundle(edit)" value="CMS_RESOURCES"/>
            <fr:property name="order(edit)" value="1"/>
            
            <fr:property name="link(delete)" value="/functionalityLinkManagement.do?method=delete"/>
            <fr:property name="param(delete)" value="idInternal/oid"/>
            <fr:property name="key(delete)" value="cms.functionalityLinkManagement.functionalityLink.delete"/>
            <fr:property name="bundle(delete)" value="CMS_RESOURCES"/>
            <fr:property name="order(delete)" value="2"/>
        </fr:layout>
    </fr:view>
</logic:present>

<html:form action="/functionalityLinkManagement" method="get">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
    <html:submit><bean:message key="cms.functionalityLinkManagement.create" bundle="CMS_RESOURCES"/></html:submit>
</html:form>