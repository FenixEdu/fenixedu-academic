<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="title.alumni.statistics" bundle="GEP_RESOURCES" /></h2>


<table class="tstyle2">
	<tr>
		<td class="aleft"><bean:message key="label.alumni.total.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics1" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.new.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics2" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.registered.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics3" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.job.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics4" /></td>
	</tr>
	<tr>
		<td class="aleft"><bean:message key="label.alumni.formation.alumni" bundle="GEP_RESOURCES" /></td>
		<td class="aright"><bean:write name="statistics5" /></td>
	</tr>
</table>


<p>Gerar lista de alumni's em formato Excel:</p>
<ul>
	<li>
		<html:link page="/alumni.do?method=generateAlumniPartialReport">
			<bean:message key="link.alumni.partial.reports" bundle="GEP_RESOURCES"/>
		</html:link>
		<br/> Lista de alumni que (pelo menos) concluiram o passo inicial do processo público de registo.
	</li>
	
	<li class="mtop1">
		<html:link page="/alumni.do?method=generateAlumniFullReport">
			<bean:message key="link.alumni.full.reports" bundle="GEP_RESOURCES"/>
		</html:link>
		<br/>Lista de todos os alumni que na parte pública ou privada iniciaram a actualização da informação pessoal e/ou informação profissional e/ou formação pós-graduada.
	</li>
</ul>
