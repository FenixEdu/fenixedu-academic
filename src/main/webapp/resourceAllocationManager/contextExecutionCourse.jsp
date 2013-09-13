<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>

<logic:present name="executionCourse">
	
	<logic:equal name="executionCourse" property="class.name" value="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
		<h3>
			<bean:write name="executionCourse" property="nome"/>
			(<bean:write name="executionCourse" property="sigla"/>)
		</h3>
		<p>
			<bean:write name="executionCourse" property="academicInterval.pathName"/>
		</p>
	</logic:equal>
	
	<logic:equal name="executionCourse" property="class.name" value="net.sourceforge.fenixedu.domain.ExecutionCourse">
		<h3>
			<bean:write name="executionCourse" property="nome"/>
			(<bean:write name="executionCourse" property="sigla"/>)
		</h3>
		<p>
			<bean:write name="executionCourse" property="academicInterval.pathName"/>
		</p>		
	</logic:equal>
	
</logic:present>

<%--
<logic:notPresent name="executionCourse">	
	<logic:present name="executionPeriod">
		<p>
			<bean:write name="executionPeriod" property="infoExecutionYear.year"/> - 
			<bean:write name="executionPeriod" property="name"/>
		</p>
	</logic:present>	
</logic:notPresent>
--%>