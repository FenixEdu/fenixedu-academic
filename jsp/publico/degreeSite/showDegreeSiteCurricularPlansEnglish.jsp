<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.util.DegreeCurricularPlanState" %>

<p><span class="error"><html:errors/></span></p>

<logic:present name="infoDegreeCurricularPlan">

<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>
<div class="breadcumbs"><a href="<%= institutionUrl %>"><bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/></a> >
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		<logic:equal name="degreeType" value="<%= DegreeType.MASTER_DEGREE.toString() %>">
			 <html:link page="<%= "/showDegrees.do?method=master&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Mestrados</html:link>
		</logic:equal>
		<logic:equal name="degreeType" value="<%= DegreeType.DEGREE.toString() %>">
			<html:link page="<%= "/showDegrees.do?method=nonMaster&executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >Ensino Licenciaturas</html:link>		
		</logic:equal>
		&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString() + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		 &gt;&nbsp;<bean:message key="label.curricularPlan.en"/>
	</div>		
		
	<%-- P�GINA EM PORTUG�S --%>
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" >
				<bean:message key="label.version.portuguese" />
			</html:link>
			<img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: Portuguese version!" width="16" height="12" />
		</span>	
	</div>
	<div class="clear"></div> 
	
	<h1><bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />&nbsp;<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" /></h1>

	<h2>
	<span class="greytxt">
	<bean:message key="label.curricularPlan.en"/>
	of
	<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
		<bean:define id="initialDate" name="infoDegreeCurricularPlan" property="initialDate" />		
		<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		<logic:notEmpty name="infoDegreeCurricularPlan" property="endDate">
			<bean:define id="endDate" name="infoDegreeCurricularPlan" property="endDate" />	
			-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
		</logic:notEmpty>
	</logic:notEmpty>
	</span>
	</h2>
	<br />

	<div class="col_right">
	  <table class="box" cellspacing="0">
		<tr>
			<td class="box_header"><strong><bean:message key="label.courses.en" /></strong></td>
		</tr>
		<tr>
			<td class="box_cell">
				<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
				<p><html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;inEnglish=true&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)  + "&amp;executionDegreeID=" +  request.getAttribute("executionDegreeID")%>" ><bean:message key="link.curricularPlan.en" /></html:link>
					<bean:message key="text.curricularPlan.en" />
				<br /><br />
				</p>
			</td>
		</tr>
		
		
		<!-- Meu -->
	<tr>
		<td class="box_header"><strong><bean:message key="label.exams.en" /></strong></td>
	</tr>
	<tr>
		<td class="box_cell">
			<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
			<html:hidden property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />	
			<p><html:link page="<%= "/chooseExamsMapContextDA.do?executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;page=1&amp;method=choose&amp;index=18&amp;selectAllCurricularYears=on" %>"><bean:message key="link.exams.en" /></html:link>
		<!--	<bean:message key="text.curricularPlan" /> -->
			In this area it will find the information relative the dates of evaluation (1� and 2� time).

			
			<br /><br />
			</p>
		</td>
	</tr>
	<tr>
		<td class="box_header"><strong><bean:message key="label.turmas.en" /></strong></td>
	</tr>
	<tr>
		<td class="box_cell">
			<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
			<p><html:link page="<%= "/showDegreeCurricularPlan.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;executionDegreeID="  +  request.getAttribute("executionDegreeID") %>" ><bean:message key="link.turmas.en" /></html:link>
			<!-- <bean:message key="text.curricularPlan" /> -->
			<br /><br />
			</p>
		</td>
	</tr>
<!-- FimMeu -->
	
		<logic:present name="infoDegreeCurricularPlanList">
		<logic:notEmpty name="infoDegreeCurricularPlanList">
			<bean:size id="listSize" name="infoDegreeCurricularPlanList" />
			<%-- LISTA DOS OUTROS PLANOSCURRICULARES --%>
			<logic:greaterThan name="listSize" value="1">
				<!-- verify if the others plans are actives -->
				<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
					<logic:notEqual name="index" value="0">
					<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" >
						<bean:define id="plansActives" value="true" />											
					</logic:equal>
					</logic:notEqual>
				</logic:iterate>
				
				<!-- links for the others degree curricular plan if they are actives -->
				<logic:present name="plansActives">		
				<tr>
					<td class="box_header"><strong><bean:message key="label.allCurricularPlans.en" /></strong></td>
				</tr>
				<tr>
					<td class="box_cell">
					<ul>		
						<logic:iterate id="infoDegreeCurricularPlanElem" name="infoDegreeCurricularPlanList" indexId="index"> 
							<bean:define id="otherDegreeCurricularPlanID" name="infoDegreeCurricularPlanElem" property="idInternal" />						
		  					<logic:equal name="infoDegreeCurricularPlanElem" property="state" value="<%= DegreeCurricularPlanState.ACTIVE_OBJ.toString() %>" > <!-- If is active -->
		  						<li><html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("otherDegreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" >
									<logic:notEmpty name="infoDegreeCurricularPlan" property="initialDate">
									<bean:define id="initialDate" name="infoDegreeCurricularPlanElem" property="initialDate" />		
									<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")) %>
									<logic:notEmpty name="infoDegreeCurricularPlanElem" property="endDate">
										<bean:define id="endDate" name="infoDegreeCurricularPlanElem" property="endDate" />	
										-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
									</logic:notEmpty>
									</logic:notEmpty>
								</html:link></li>
							</logic:equal>																				
						</logic:iterate>
					</ul>
				</tr>
				</logic:present>
			</logic:greaterThan>	
		</logic:notEmpty>  
		</logic:present>		
	  </table> 
	</div>
			
	<%-- DESCRI��O DO PLANO CURRICULAR(activo e o mais recente) --%>
	<logic:notEmpty name="infoDegreeCurricularPlan" property="descriptionEn">
		<p><bean:write name="infoDegreeCurricularPlan" property="descriptionEn" filter="false" /></p>
	</logic:notEmpty>	
	
	<br />
	<br />
	<br /> 
	<bean:message key="label.information.responsability.information.degree.en" />			 
	<br />
	<br />
</logic:present>	
