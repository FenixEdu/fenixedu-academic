<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>

<logic:present name="cronScriptInvocation">
	<fr:view name="cronScriptInvocation" property="cronScriptState"
			schema="net.sourceforge.fenixedu.domain.system.CronScriptState.FullInformation"
			layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
  	 		<fr:property name="columnClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
	<br/>
	<fr:view name="cronScriptInvocation"
			schema="net.sourceforge.fenixedu.domain.system.CronScriptInvocation"
			layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
   	 		<fr:property name="columnClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
	<br/>
	<logic:present name="cronScriptInvocation" property="log">
		<str:replace replace="
" with="<br/>">
			<bean:write name="cronScriptInvocation" property="log" filter="false"/>
		</str:replace>
	</logic:present>
	<logic:notPresent name="cronScriptInvocation" property="log">
		<bean:message bundle="MANAGER_RESOURCES" key="message.log.not.present"/>
	</logic:notPresent>
</logic:present>
<logic:notPresent name="cronScriptInvocation">
	<bean:message bundle="MANAGER_RESOURCES" key="message.log.not.present"/>
</logic:notPresent>