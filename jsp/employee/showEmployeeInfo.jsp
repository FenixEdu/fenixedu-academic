<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="label.schedule" /></h2>
<br />
<br />
<logic:present name="schedule">
	<fr:view name="schedule" schema="show.schedule" layout="tabular" />
</logic:present>
<logic:notPresent name="schedule">
	<bean:message key="message.employee.noInfo" />
</logic:notPresent>
