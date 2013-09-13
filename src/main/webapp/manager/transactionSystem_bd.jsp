<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.transaction.logs"/></h2>
<br />

<table class="tstyle5">
	<tr>
		<td>				
			<fr:edit name="transactionReport" schema="pt.ist.fenixframework.pstm.TransactionReport">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstylenone vamiddle thlight" />
				</fr:layout>		
			</fr:edit>			
		</td>		
	</tr>
</table>
<br/>

<bean:define id="startOfReportArg" type="java.lang.String">&amp;startOfReport=<bean:write name="transactionReport" property="startOfReport"/></bean:define>
<bean:define id="endOfReportArg" type="java.lang.String">&amp;endOfReport=<bean:write name="transactionReport" property="endOfReport"/></bean:define>
<bean:define id="actionArg" type="java.lang.String">&amp;action=<bean:write name="transactionReport" property="transactionAction"/></bean:define>
<bean:define id="serverArg" type="java.lang.String">&amp;server=<bean:write name="transactionReport" property="server"/></bean:define>
<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/manager/transactionSystem.do?method=viewChart<%= startOfReportArg %><%= endOfReportArg %><%= actionArg %><%= serverArg %></bean:define>
<html:img align="middle" src="<%= url %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />
