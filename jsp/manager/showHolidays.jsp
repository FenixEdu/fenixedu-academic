<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message bundle="MANAGER_RESOURCES" key="label.manage.holidays"/>
</h2>
<bean:message bundle="MANAGER_RESOURCES" key="message.manage.holidays"/>
<br/>
<br/>
<fr:create type="net.sourceforge.fenixedu.domain.Holiday$HolidayFactoryCreator"
		schema="HolidayFactoryCreatory"
		action="/manageHolidays.do?method=create&amp;page=0">
</fr:create>
<br/>
<br/>
<logic:present name="holidays">
	<fr:view name="holidays" layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
     		<fr:property name="columnClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
</logic:present>