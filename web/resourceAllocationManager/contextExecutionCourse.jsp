<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<logic:present name="executionCourse">
	
	<logic:equal name="executionCourse" property="class.name" value="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
		<h3>
			<bean:write name="executionCourse" property="nome"/>
			(<bean:write name="executionCourse" property="sigla"/>)
		</h3>
		<p>
			<bean:write name="executionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/> - 
			<bean:write name="executionCourse" property="infoExecutionPeriod.name"/>
		</p>
	</logic:equal>
	
	<logic:equal name="executionCourse" property="class.name" value="net.sourceforge.fenixedu.domain.ExecutionCourse">
		<h3>
			<bean:write name="executionCourse" property="nome"/>
			(<bean:write name="executionCourse" property="sigla"/>)
		</h3>
		<p>
			<bean:write name="executionCourse" property="executionPeriod.executionYear.year"/> - 
			<bean:write name="executionCourse" property="executionPeriod.name"/>
		</p>		
	</logic:equal>
	
</logic:present>

<logic:notPresent name="executionCourse">	
	<logic:present name="executionPeriod">
		<p>
			<bean:write name="executionPeriod" property="infoExecutionYear.year"/> - 
			<bean:write name="executionPeriod" property="name"/>
		</p>
	</logic:present>	
</logic:notPresent>