<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:iterate id="infoDCP" name="<%= SessionConstants.INFO_DEGREE_CURRICULAR_PLANS_LIST %>">			
	<bean:write name="infoDCP" property="idInternal"/>
	<html:link page="/readDegreeCurricularPlan.do" paramId="idInternal" paramName="infoDCP" paramProperty="idInternal"><bean:write name="infoDCP" property="name"/></html:link>
</logic:iterate>