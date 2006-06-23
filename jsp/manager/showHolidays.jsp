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
<table class="style1">
	<tr>
		<th><bean:message bundle="MANAGER_RESOURCES" key="label.date"/></th>
		<th><bean:message bundle="MANAGER_RESOURCES" key="label.locality"/></th>
		<th></th>
	</tr>
	<logic:iterate id="holiday" name="holidays">
		<tr>
			<td class="listClasses">
				<fr:view name="holiday" property="date"/>
			</td>
			<td class="listClasses">
				<logic:present name="holiday" property="locality">
					<bean:write name="holiday" property="locality.name"/>
				</logic:present>
			</td>
			<td class="listClasses">
				<html:link action="/manageHolidays.do?method=create&amp;page=0"
						paramId="holidayID" paramName="holiday" paramProperty="idInternal">
					<bean:message bundle="MANAGER_RESOURCES" key="link.delete"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
<%--
This has a bug... locality may be null, and when this happens the getName provokes a NPE.
<br/>
<br/>
<logic:present name="holidays">
	<fr:view name="holidays" schema="Holiday" layout="tabular">
		<fr:layout>
		    <fr:property name="classes" value="style1"/>
     		<fr:property name="columnClasses" value="listClasses"/>
			<fr:property name="link(delete)" value="/manageHolidays.do?method=delete"/>
			<fr:property name="param(delete)" value="idInternal/holidayID"/>
			<fr:property name="key(delete)" value="link.delete"/>
			<fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>
--%>