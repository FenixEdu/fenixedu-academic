<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="unitId" name="site" property="unit.externalId"/>
<em><bean:message key="label.websiteManagement" bundle="MANAGER_RESOURCES"/> | <fr:view name="site" property="unit.nameWithAcronym"/></em>
