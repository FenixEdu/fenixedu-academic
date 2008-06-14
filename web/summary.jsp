<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<tiles:importAttribute />


<bean:define id="headers" name="headers" />      	
<bean:define id="list" name="body" type="java.util.List"/>	
	
<h3 style="text-align: center;">
	<bean:define id="title" >
	  	<tiles:getAsString name="title" />
	</bean:define>	
	<bean:message name="title" />
</h3>	

<table style="border: 1px solid #000;" cellspacing="0"> 	
	<tr>
		<logic:iterate id="elem" name="headers" indexId="headersIndex">
			<td>
				<bean:write name="elem" />:&nbsp
				<%= (String)(list.get(headersIndex.intValue())) %>
			</td>			
		</logic:iterate>
	</tr>
</table>	
