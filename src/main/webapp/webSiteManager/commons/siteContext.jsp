<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="unitId" name="site" property="unit.externalId"/>
<em><bean:message key="label.websiteManagement" bundle="MANAGER_RESOURCES"/> | <fr:view name="site" property="unit.nameWithAcronym"/></em>
