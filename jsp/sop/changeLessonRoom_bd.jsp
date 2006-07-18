<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoselected">
			<p>
				O curso seleccionado &eacute;:
			</p>
			<strong>
				<jsp:include page="context.jsp"/>
			</strong>
       	</td>
   	</tr>
</table>
<br />
<h2>
	<bean:message key="title.editAula"/>
</h2>
<span class="error">
	<html:errors/>
</span>
<html:form action="/editarAulaForm" focus="nomeSala">
<table> 
	<tr>
			<td class="formTD">
				<bean:message key="property.aula.weekDay"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.diaSemana" property="diaSemana"/>
						<bean:write name="weekDayString"/>
					</b>
				</span>
			</td>
   	</tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.time.begining"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaInicio" property="horaInicio" write="true"/>
						:
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosInicio" property="minutosInicio" write="true"/>
					</b>
      		</span>
      	</td>
  	</tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.time.end"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaFim" property="horaFim" write="true"/>
						:
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosFim" property="minutosFim" write="true"/>
					</b>
       		</span>
       	</td>
    </tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.type"/>
				:
			</td>
			<td>
				<span class="grey-txt">
					<b>
						<html:select bundle="HTMLALT_RESOURCES" altKey="select.tipoAula" property="tipoAula">
             	<html:options collection="tiposAula" property="value" labelProperty="label"/>
						</html:select>
					</b>
           	</span>
      	</td>
   	</tr>
    <tr>
			<td class="formTD">
				<bean:message key="property.aula.sala"/>
				:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.nomeSala" property="nomeSala" size="1" >
            	<html:options collection="listaSalas" property="value" labelProperty="label"/>
           	</html:select>
     	</td>
  	</tr>
</table>
<br />
<bean:define id="infoAula_oid"
			 name="infoAula"
			 property="idInternal"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoAula_oid" property="infoAula_oid"
			 value="<%= pageContext.findAttribute("infoAula_oid").toString() %>"/>

<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton">
		<bean:message key="label.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>