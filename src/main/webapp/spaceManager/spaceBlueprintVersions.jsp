<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="blueprint" property="validUntil">
	<bean:message bundle="SPACE_RESOURCES" key="label.from"/>
	<bean:write name="blueprint" property="validFrom"/>
	<bean:message bundle="SPACE_RESOURCES" key="label.until"/>
	<bean:write name="blueprint" property="validUntil"/>
</logic:present>
<logic:notPresent name="blueprint" property="validUntil">
	<bean:message bundle="SPACE_RESOURCES" key="label.as.of"/>
	<bean:write name="blueprint" property="validFrom"/>
</logic:notPresent>