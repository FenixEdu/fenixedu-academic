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
	<fr:view name="cronScriptState"
			property="cronScriptInvocationsSetSortedByInvocationStartTime"
			schema="net.sourceforge.fenixedu.domain.system.CronScriptInvocation"
			layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
   	 		<fr:property name="columnClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
</logic:present>