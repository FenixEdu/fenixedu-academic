<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3><bean:message bundle="CMS_RESOURCES" key="cms.functionalityLinkManagement.label" /></h3>

<fr:create type="net.sourceforge.fenixedu.domain.cms.FunctionalityLink" layout="tabular"
           schema="functionalityLink.edit"
           action="/functionalityLinkManagement.do?method=start">
    <fr:hidden slot="cms" name="cms"/>
</fr:create>
