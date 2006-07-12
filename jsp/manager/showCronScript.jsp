<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manage.cron"/>
</h2>
<br/>
<br/>
<logic:present name="cronScriptState">
	<fr:view name="cronScriptState"
			schema="net.sourceforge.fenixedu.domain.system.CronScriptState.FullInformation"
			layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
   	 		<fr:property name="columnClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
	<br/>
	<br/>
	<%  final int lastPage = ((Integer) request.getAttribute("numberOfPages")).intValue();
		final int pageNumber = ((Integer) request.getAttribute("pageNumber")).intValue();
		for (int i = 1; i <= lastPage; i++) {
	       if (i == 1 && i != pageNumber) {
	%>
				<bean:define id="url1" type="java.lang.String">/cron.do?amp;page=0&pageNumber=<%= Integer.toString(pageNumber - 1) %>&cronScriptStateID=<bean:write name="cronScriptState" property="idInternal"/>&method=showScript</bean:define>
				<html:link page="<%= url1 %>"><<</html:link>
	<%         
	       }
	       if (i != pageNumber) {
	%>
				<bean:define id="url" type="java.lang.String">/cron.do?amp;page=0&pageNumber=<%= Integer.toString(i) %>&cronScriptStateID=<bean:write name="cronScriptState" property="idInternal"/>&method=showScript</bean:define>
				<html:link page="<%= url %>"><%= Integer.toString(i) %></html:link>
	<%
	       } else {
	%>
				<%= Integer.toString(i) %>
	<%
		   }
	       if (i == lastPage && i != pageNumber) {
	%>
				<bean:define id="url2" type="java.lang.String">/cron.do?amp;page=0&pageNumber=<%= Integer.toString(pageNumber + 1) %>&cronScriptStateID=<bean:write name="cronScriptState" property="idInternal"/>&method=showScript</bean:define>
				<html:link page="<%= url2 %>">>></html:link>
	<%     }
       }
	%>
	<br/>
	<br/>
	<logic:present name="cronScriptInvocationsPage">
		<fr:view name="cronScriptInvocationsPage"
				schema="net.sourceforge.fenixedu.domain.system.CronScriptInvocation"
				layout="tabular">
			<fr:layout>
		    	<fr:property name="classes" value="style1"/>
   	 			<fr:property name="columnClasses" value="listClasses"/>
				<fr:property name="link(view)" value="/cron.do?method=showScriptInvocationLog&amp;page=0"/>
				<fr:property name="param(view)" value="idInternal/cronScriptInvocationID"/>
				<fr:property name="key(view)" value="link.view.log"/>
				<fr:property name="bundle(view)" value="MANAGER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>