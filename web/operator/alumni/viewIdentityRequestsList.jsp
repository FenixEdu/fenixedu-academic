<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<!-- viewIdentityRequestsList.jsp -->

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
<h2><bean:message key="alumni.identity.requests" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="OPERATOR">

	<div class="infoop2">
		Esta lista consiste nos pedidos feitos por alumni com o objectivo de:
		<ul>
			<li>obter o número de aluno para efectuar registo online</li>
			<li>ou consultar o Curriculum do Aluno no Portal do Alumni.</li>
		</ul>
	</div>

	<p class="mtop1 mbottom15">
		<html:link page="/alumni.do?method=viewClosedRequests">
			<bean:message key="link.closed.requests" bundle="MANAGER_RESOURCES"/>
		</html:link>
	</p>
	
	<logic:empty name="identityRequestsList">
		<p><em><bean:message key="alumni.no.identity.requests" bundle="MANAGER_RESOURCES" /></em></p>
	</logic:empty>

	<logic:notEmpty name="identityRequestsList">
		<p class="mbottom05">
			<bean:message key="alumni.received.requests" bundle="MANAGER_RESOURCES"/>:</p>
		<fr:view name="identityRequestsList" layout="tabular" schema="alumni.identity.request.list" >
			<fr:layout>
				<fr:property name="classes" value="tstyle1 tdcenter mtop05"/>
				<fr:property name="columnClasses" value="nowrap acenter,acenter,acenter,acenter,acenter"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="creationDateTime,fullName"/>
	           	<fr:property name="sortBy" value="creationDateTime=desc" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>


</logic:present>




 
