<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.util.EnrollmentState" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>
<%@ page import="java.util.Date" %>



  <span class="error"><html:errors/></span>

  <logic:present name="studentCurricularPlan">
	  <table>
	  	<tr>
			<td>
				<bean:message key="label.student"/>
			</td>
			<td>
				<bean:write name="studentCurricularPlan" property="infoStudent.number"/>
			</td>
	  	</tr>
	  	<tr>
			<td>
				<bean:message key="property.name"/>
			</td>
			<td>
				<bean:write name="studentCurricularPlan" property="infoStudent.infoPerson.nome"/>
			</td>
	  	</tr>
	  	<tr>
			<td>
				<bean:message key="label.degree"/>
			</td>
			<td>
				<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
			</td>
	  	</tr>
	  	<tr>
			<td>
				<bean:message key="label.specialization"/>
			</td>
			<td>
				<bean:write name="studentCurricularPlan" property="specialization"/>
			</td>
	  	</tr>
	  </table>
  </logic:present>

  <br />
  <br />
  <logic:present name="gratuityInformation">
	  <h2><bean:message key="label.masterDegree.administrativeOffice.gratuityActualSituation" /></h2>
	  <table>
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.situation"/>
			</td>
			<td>
				<bean:message name="gratuityInformation" property="gratuityState.name" bundle="ENUMERATION_RESOURCES" />
			</td>
	  	</tr>
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.date"/>
			</td>
			<td>
	  			<logic:present name="gratuityInformation" property="date" >
			        <bean:define id="date" name="gratuityInformation" property="date" />
					<%= Data.format2DayMonthYear((Date) date) %>
				</logic:present>
			</td>
	  	</tr>
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.remarks"/>
			</td>
			<td>
				<bean:write name="gratuityInformation" property="remarks"/>
			</td>
	  	</tr>
	  </table>
  </logic:present>  
  
  
  <logic:present name="gratuityInformationFromGuide">
	<h2><bean:message key="label.masterDegree.administrativeOffice.gratuityInformation"/></h2>
	<table>
		<tr>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.description"/>
			</td>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.value"/>
			</td>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.guideNumber"/>
			</td>
			<td>
				<bean:message key="label.masterDegree.administrativeOffice.guideYear"/>
			</td>
	  	</tr>
	  	<logic:iterate id="guideEntry" name ="gratuityInformationFromGuide" >
	  		<tr>
	  			<td>
		  			<bean:write name="guideEntry" property="description" />
	  			</td>
	  			<td>
		  			<%= NumberUtils.formatNumber(new Double(((InfoGuideEntry) guideEntry).getPrice().floatValue() * ((InfoGuideEntry) guideEntry).getQuantity().intValue()), 2) %>
	  			</td>
	  			<td align="center">
		  			<bean:write name="guideEntry" property="infoGuide.number" />
	  			</td>
	  			<td align="center">
		  			<bean:write name="guideEntry" property="infoGuide.year" />
	  			</td>
	  		</tr>
	  	</logic:iterate>
	</table>
  </logic:present>


  <logic:notPresent name="gratuityInformation">
		<h2><bean:message key="label.masterDegree.AdministrativeOffice.gratuityInformationNotDefined"/></h2>
  </logic:notPresent>
  
  
  <html:form action="/gratuityChange.do?method=changeGratuityStatus">
  	<html:hidden property="studentCPID"/>
  	<html:hidden property="page" value="1"/>
  	
  	<table>
  		<tr>
  			<td>
  				<bean:message key="label.masterDegree.administrativeOffice.newGratuitySituation"/>
  			</td>
  			<td>
				<tiles:insert page="/enumList.jsp" flush="true">
				  <tiles:put name="property" value="situation" />
				  <tiles:put name="enumList" beanName="situationList" />
				</tiles:insert>  			
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.observation"/>
			</td>
			<td>
	        	<html:textarea property="othersRemarks" cols="30" rows="5" />
			</td>
		</tr>
  	</table>
     <html:submit property="Alterar">Confirmar</html:submit>
  </html:form>
  
