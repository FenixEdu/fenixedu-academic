<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manage.cron"/>
</h2>
<br/>
<br/>
<logic:present name="cronRegistry">
	<bean:message bundle="MANAGER_RESOURCES" key="label.current.build.version"/>
	<dt:format pattern="yyyy-MM-dd HH:mm:ss.sss">
		<bean:write name="cronRegistry" property="buildVersion.millis"/>
	</dt:format>
	<br/>
	<br/>
	<logic:present name="activeCronScriptStates">
		<fr:view name="activeCronScriptStates"
				schema="net.sourceforge.fenixedu.domain.system.CronScriptState.ActiveCronScript"
				layout="tabular">
			<fr:layout>
			    <fr:property name="classes" value="style1"/>
    	 		<fr:property name="columnClasses" value="listClasses"/>

				<fr:property name="link(view)" value="/cron.do?method=showScript&amp;page=0"/>
				<fr:property name="param(view)" value="externalId/cronScriptStateID"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="bundle(view)" value="MANAGER_RESOURCES"/>

				<fr:property name="link(runNow)" value="/cron.do?method=runNow&amp;page=0"/>
				<fr:property name="param(runNow)" value="externalId/cronScriptStateID"/>
				<fr:property name="key(runNow)" value="link.runNow"/>
				<fr:property name="bundle(runNow)" value="MANAGER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	<br/>
	<br/>
	<logic:present name="inActiveCronScriptStates">
		<fr:view name="inActiveCronScriptStates"
				schema="net.sourceforge.fenixedu.domain.system.CronScriptState.InActiveCronScript"
				layout="tabular">
			<fr:layout>
			    <fr:property name="classes" value="style1"/>
    	 		<fr:property name="columnClasses" value="listClasses"/>
				<fr:property name="link(view)" value="/cron.do?method=showScript&amp;page=0"/>
				<fr:property name="param(view)" value="externalId/cronScriptStateID"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="bundle(view)" value="MANAGER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:present>
</logic:present>