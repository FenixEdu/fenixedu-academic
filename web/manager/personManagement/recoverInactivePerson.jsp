<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="link.recover.inactive.person"/>
</h2>
<bean:message bundle="MANAGER_RESOURCES" key="message.activate.person"/>
<br/>
<br/>
<logic:notPresent name="findPersonFactory">
	<fr:create type="net.sourceforge.fenixedu.domain.Person$FindPersonFactory"
			schema="FindPersonFactory"
			action="/recoverInactivePerson.do?method=search">
	</fr:create>
</logic:notPresent>
<logic:present name="findPersonFactory">
	<fr:edit name="findPersonFactory"
			type="net.sourceforge.fenixedu.domain.Person$FindPersonFactory"
			schema="FindPersonFactory"
			action="/recoverInactivePerson.do?method=search">
	</fr:edit>
	<br/>
	<br/>
	<fr:view name="findPersonFactory" property="people" schema="FindPersonFactoryResult" layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
     		<fr:property name="columnClasses" value="listClasses"/>
			<fr:property name="link(activate)" value="/recoverInactivePerson.do?method=activate"/>
			<fr:property name="param(activate)" value="idInternal/personID"/>
			<fr:property name="key(activate)" value="link.activate.person"/>
			<fr:property name="bundle(activate)" value="MANAGER_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>