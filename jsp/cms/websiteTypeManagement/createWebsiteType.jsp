<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h3><bean:message bundle="CMS_RESOURCES" key="cms.websiteTypeManagement.label" /></h3>

<fr:create type="net.sourceforge.fenixedu.domain.cms.website.WebsiteType" layout="tabular"
           schema="websiteType.create"
           action="/websiteTypeManagement.do?method=start"/>
