<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>




 <tr><td>&nbsp;</td></tr>
 <tr><td>&nbsp;</td></tr>
<tr>
  <td align="right" valign="top">
  Chefe de Secção
  </td>
 </tr>
 
 <tr><td>&nbsp;</td></tr>
 
 <tr>
   <td align="right" valign="top">
  (Josefina Miranda)
  </td>
 </tr>
 
 <tr>
    <td>
	 <bean:write name="<%= SessionConstants.DATE %>" />			
    </td>
 </tr>