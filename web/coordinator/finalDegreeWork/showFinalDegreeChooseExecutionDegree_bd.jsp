<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="degreeCurricularPlanOID" name="degreeCurricularPlanID"/>

<h2><bean:message key="link.coordinator.managefinalDegreeWorks"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<p class="mtop2 mbottom05">
	<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
</p>
<p class="infoop2">
	Se desejar criar o processo de candidaturas a dissertações utilize a opção criar no ano correspondente.
	<br> <b>Para iniciar o processo de candidaturas a dissertações juntamente com outro curso contacte o CIIST.</b> 
</p>
<ul class="mtop05">
	<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
		<bean:define id="executionDegreeOID" name="infoExecutionDegree" property="externalId" />
		<li>
			<logic:empty name="infoExecutionDegree" property="executionDegree.scheduling">
				<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
				<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfoWithNewScheduling&amp;degreeCurricularPlanID="
				    + degreeCurricularPlanOID
					+ "&amp;executionDegreeOID="
					+ executionDegreeOID
					%>">(criar)
				</html:link>
			</logic:empty>
			<logic:notEmpty name="infoExecutionDegree" property="executionDegree.scheduling">
				<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&amp;degreeCurricularPlanID="
						+ degreeCurricularPlanOID
						+ "&amp;executionDegreeOID="
						+ executionDegreeOID
						%>">
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
				</html:link>
			</logic:notEmpty>
		</li>
	</logic:iterate>
</ul>

