<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html:link page="/deleteSection.do">
	<bean:message key="button.delete"/>
</html:link>

<html:link page="/prepareInsertSection.do">
	<bean:message key="button.insert"/>
</html:link>

<html:link page="/editSection.do?method=prepareEdit">
	<bean:message key="button.edit"/>
</html:link>
<br>   
<br>


<html:link page="/createItem.jsp">
	<bean:message key="button.insertItem"/>
</html:link>

<logic:iterate id="item" name="<%= SessionConstants.INFO_SECTION_ITEMS_LIST %>">
  <logic:equal name="item" property="urgent" value="true"><font color="red"></logic:equal>
	  <h2><bean:write name="item" property="name"/></h2>
  	  <bean:write name="item" property="information"/>
  <logic:equal name="item" property="urgent" value="true"></font></logic:equal>
  
<br>
  
  <html:link page="/deleteItem.do">
	<bean:message key="button.deleteItem"/>
</html:link>


<html:link page="/editItem.do?method=prepareEdit">
	<bean:message key="button.editItem"/>
</html:link>
<br> 


</logic:iterate>
