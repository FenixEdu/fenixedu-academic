<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


   
<logic:iterate id="item" name="<%= SessionConstants.INFO_SECTION_ITEMS_LIST %>">
  <logic:equal name="item" property="urgent" value="true"><font color="red"></logic:equal>
	  <h3><bean:write name="item" property="name"/></h3>
  	  <bean:write name="item" property="information"/><br/>
  <logic:equal name="item" property="urgent" value="true"></font></logic:equal>
</logic:iterate>
