<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<h2><bean:write name="<%=SessionConstants.INFO_SECTION %>" property="name"/></h2>
<table	width="50%" cellpadding="0" border="0">
	<tr> 
	
		<td class="listClasses"><html:link page="/editSection.do?method=prepareEdit">
			<bean:message key="button.editSection"/>
			</html:link>
		</td>

		<td class="listClasses"><html:link page="/deleteSection.do">
			<bean:message key="button.deleteSection"/>
			</html:link>
		</td>

</tr>
<tr>
	<td class="listClasses"><html:link page="/prepareInsertSection.do">
			<bean:message key="button.insertSubSection"/>
			</html:link>
		</td>

		<td class="listClasses"><html:link page="/createItem.jsp">
							<bean:message key="button.insertItem"/>
							</html:link>
		</td>
</tr>
</table>

<br>   
<br>





<logic:iterate id="item" name="<%= SessionConstants.INFO_SECTION_ITEMS_LIST %>">
   <h2><bean:write name="item" property="name"/></h2>
<table>
	<tr>
		<td>
			 <logic:equal name="item" property="urgent" value="true"><font color="red"></logic:equal>
	 		
  			  <bean:write name="item" property="information"/>
 			 <logic:equal name="item" property="urgent" value="true"></font></logic:equal>
		</td>
		
		<td class="listClasses"><html:link page="/deleteItem.do" indexed="true">
				<bean:message key="button.deleteItem"/>
				</html:link>
		</td>
		<td class="listClasses"><html:link page="/editItem.do?method=prepareEdit">
						<bean:message key="button.editItem"/>
						</html:link>
		</td>
	</tr>
</table>  
  



<br> 

</logic:iterate>
