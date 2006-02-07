<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<fr:create type="net.sourceforge.fenixedu.domain.cms.website.WebsiteType" layout="tabular"
           schema="websiteType.create"
           action="/websiteTypeManagement.do?method=start"/>
