<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>



<logic:present name="<%= SessionConstants.DOCUMENT_REASON_LIST%>">
	<tr>
  	<td align="left" valign="top">Esta declaração destina-se a fins comprovativos de:</td>
  	</tr>
 	<tr>
  	<td align="left" valign="top">
		<logic:iterate id="item" name="<%= SessionConstants.DOCUMENT_REASON_LIST%>" >
           <tr> 
           <td>               			
				<bean:write name="item" />
		   </td>
	       </tr>
		</logic:iterate>
	</td>
  	</tr>
</logic:present>

<logic:notPresent name="<%=SessionConstants.DOCUMENT_REASON_LIST%>">
    <tr>
      <td align="left" valign="top">
      Esta declaração destina-se a fins comprovativos.
      </td>
    </tr>
</logic:notPresent>

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
 
</table>
</td>
<td>&nbsp;</td>
</tr>

<%--
<tr>
  <td align="left" valign="top">
  Esta declaração destina-se a fins comprovativos.
  </td>
 </tr>



--%>