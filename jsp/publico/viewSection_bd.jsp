<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:iterate id="item" name="itemList">
  <logic:equal name="item" property="urgent" value="true"><font color="red"></logic:equal>
	  <h3><bean:write name="item" property="name"/></h3>
  	  <bean:write name="item" property="information" filter="false"/><br/>
  <logic:equal name="item" property="urgent" value="true"></font></logic:equal>
</logic:iterate>
