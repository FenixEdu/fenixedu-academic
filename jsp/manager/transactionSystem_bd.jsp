<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.transaction.logs"/></h2>
<br />

<table class="tstyle5">
	<tr>
		<td>				
			<fr:edit name="transactionReport" schema="net.sourceforge.fenixedu.stm.TransactionReport">
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
