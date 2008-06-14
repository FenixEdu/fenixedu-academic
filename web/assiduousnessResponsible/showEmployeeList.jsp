<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousnessResponsible" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
<h2><bean:message key="label.employees" bundle="ASSIDUOUSNESS_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="ASSIDUOUSNESS_RESOURCES">
		<p class="mvert15"><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="yearMonth">
	<div class="mvert1 invisible">
	<fr:form action="/assiduousnessResponsible.do?method=showEmployeeList">
		<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
			<fr:layout>
		        <fr:property name="classes" value="thlight thright mtop1"/>
			</fr:layout>
		</fr:edit>
		<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible" >
			<bean:message key="button.submit" />
		</html:submit>
		</p>
	</fr:form>
	</div>
	
	<div class="toprint">
		<p class="bold mbottom0">
			<bean:define id="month" name="yearMonth" property="month"/>
			<bean:message key="<%=month.toString()%>" bundle="ENUMERATION_RESOURCES"/>
			<bean:define id="year" name="yearMonth" property="year"/>
			<bean:write name="year"/>
		</p>
	</div>


<logic:present name="unitEmployeesList">
	<logic:empty name="unitEmployeesList">
		<p><em><bean:message key="message.assiduousness.noEmployees" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>
	</logic:empty>
	<logic:notEmpty name="unitEmployeesList">
		<logic:iterate id="unitEmployees" name="unitEmployeesList">
			<fr:view name="unitEmployees" property="unit">
				<fr:layout name="link">
					<fr:property name="classes" value="bold"/>
					<fr:property name="linkFormat" value="<%="/assiduousnessResponsible.do?idInternal=${idInternal}&method=showEmployeeList&month="+month.toString()+"&year="+year.toString()%>"/>
					<fr:property name="moduleRelative" value="true"/>
					<fr:property name="contextRelative" value="true"/>
					<fr:property name="subSchema" value="unit.name"/>
					<fr:property name="subLayout" value="values"/>
				</fr:layout>
			</fr:view>
			<br/>
			<logic:present name="unitToShow">
				<bean:define id="unitToShow" name="unitToShow"/>
				<logic:equal name="unitEmployees" property="unit.idInternal" value="<%=unitToShow.toString()%>">
					<fr:view name="unitEmployees" property="employeeList" schema="show.employeePersonalInformation.toList">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle1 printborder" />
							<fr:property name="columnClasses" value="acenter,,"/>
							<fr:property name="link(view)" value="<%="/assiduousnessResponsible.do?method=showEmployeeWorkSheet&month="+month.toString()+"&year="+year.toString()%>"/>
							<fr:property name="key(view)" value="link.workSheet"/>
							<fr:property name="param(view)" value="employeeNumber"/>
							<fr:property name="bundle(view)" value="ASSIDUOUSNESS_RESOURCES"/>
						</fr:layout>
					</fr:view>
				</logic:equal>
			</logic:present>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>
</logic:present>
