<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<tiles:importAttribute />
<bean:size id="rowSize" name="rows" />	
	<bean:define id="emptyListMessage" >
		<tiles:getAsString name="emptyRows" />
	</bean:define>		
	<logic:equal name="rowSize" value="0">
		<bean:message name="emptyListMessage" />
	</logic:equal>	
	<logic:notEqual name="rowSize" value="0">
<bean:size id="headerSize" name="headers" />	
	<h3 class="centerContent">
	<bean:define id="title" >
	  	<tiles:getAsString name="title" />
	</bean:define>	
	<bean:message name="title" />
	</h3>	
<table class="verb-table" width="<tiles:getAsString name="width" />" cellspacing="0"> 	
	<tr>
		<logic:iterate id="header" name="headers" indexId="headerIndex">
				<bean:define id="classHeader" toScope="request">verb-hdr</bean:define>
			<logic:equal name="headerIndex" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<%-- ultima coluna --%>			
				<bean:define id="classHeader" toScope="request">verb-hrd-last</bean:define>
			</logic:equal>
			<td class="<bean:write name="classHeader" />"><bean:write name="header" /></td>	
		</logic:iterate>
	</tr>
	<logic:iterate id="row" name="rows" indexId="rowIndex">
		<bean:define id="mod">
			<%= String.valueOf(rowIndex.intValue() % headerSize.intValue()) %>
		</bean:define>
		<bean:define id="startTr" value="" />
		<bean:define id="closeTr" value="" />
		<logic:equal name="mod" value="0">
			<bean:define id="startTr">
				<%= "<tr>" %>
			</bean:define>
		</logic:equal>
		<logic:equal name="mod" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<logic:notEqual name="rowIndex" value="0">
				<bean:define id="closeTr">
					<%= "</tr>" %>						
				</bean:define>
			</logic:notEqual>
		</logic:equal>
		<bean:write name="startTr" filter="false"></bean:write>
			<logic:equal name="mod" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<%-- ultima coluna --%>
		<bean:define id="classTd">verb-td-last</bean:define>
			<logic:greaterEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - headerSize.intValue()) %>">
				<logic:lessEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - 1) %>">
				<%-- ultima coluna e ultima linha --%>	
		<bean:define id="classTd">verb-finalrow-last</bean:define>			
				</logic:lessEqual>	
			</logic:greaterEqual>
			</logic:equal>
			<logic:notEqual name="mod" value="<%= String.valueOf(headerSize.intValue() - 1) %>">
			<%-- nao e a ultima coluna --%>
		<bean:define id="classTd">verb-td</bean:define>
			<logic:greaterEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - headerSize.intValue() - 1) %>">
				<logic:lessEqual name="rowIndex" value="<%= String.valueOf(rowSize.intValue() - 1) %>">
					<%-- nao e a ultima coluna e e' a ultima linha --%>
					<bean:define id="classTd">verb-finalrow</bean:define>	
				</logic:lessEqual>	
			</logic:greaterEqual>
		</logic:notEqual>
			<td class="<bean:write name="classTd"/>" nowrap="nowrap"><bean:write name="row" filter="false"/></td>
		<bean:write name="closeTr" filter="false"></bean:write>
	</logic:iterate>
</table> 
</logic:notEqual>