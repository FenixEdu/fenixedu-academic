<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">
	
	<h2><bean:message key="label.payments.postingRules.category"
		bundle="MANAGER_RESOURCES" /></h2>
		
		<br/>

	<html:link
		action="/postingRules.do?method=chooseDFADegreeCurricularPlan">
		<bean:message
			key="<%=DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA.getQualifiedName()%>"
			bundle="ENUMERATION_RESOURCES" />
	</html:link>
</logic:present>
