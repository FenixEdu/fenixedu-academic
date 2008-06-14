<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<script type='text/javascript' src='<%= request.getContextPath() + "/dwr/engine.js" %>'></script>
<script type='text/javascript' src='<%= request.getContextPath() + "/dwr/util.js" %>'></script>
<script type='text/javascript' src='<%= request.getContextPath() + "/dwr/interface/CurricularCourseStatisticsStatusBridge.js" %>'></script>

<html:form action="/competenceCoursesStatistics">

	<SCRIPT LANGUAGE="Javascript">
	<!-- 
		
		var x = 10;		
		var processingDCPs = 1;
		var toProcessDCPs = 1;
		
		function startClock(){ 
			x = x - 1;			
			if(processingDCPs > 0 || toProcessDCPs > 0){
				setTimeout("startClock()", 1000);
			}						
			if(x < -1){ 
				CurricularCourseStatisticsStatusBridge.readProcessedDegreeCurricularPlans(fillProcessedDCPs);
				CurricularCourseStatisticsStatusBridge.readProcessingDegreeCurricularPlans(fillProcessingDCPs);
				CurricularCourseStatisticsStatusBridge.readToProcessDegreeCurricularPlans(fillToProcessDCPs);
				x=10; 
			} 
		} 
	
		var getName = function(name) { return name };
		
		function fillProcessedDCPs(dcps){
			fillDCPs(dcps, "processedDCPs");
		}			
		function fillProcessingDCPs(dcps){
			processingDCPs = dcps.length;
			fillDCPs(dcps, "processingDCPs");
		}			
		function fillToProcessDCPs(dcps){
			toProcessDCPs = dcps.length;
			fillDCPs(dcps, "toProcessDCPs");
		}			
		
		function fillDCPs(dcps, componentID){
			DWRUtil.removeAllRows(componentID);
		    DWRUtil.addRows(componentID, dcps, [ getName ]);
		}	

	-->	
	</SCRIPT>


	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionYear"/>

	<logic:present name="executionYears" >		
		<bean:message key="label.gep.chooseExecutionYear" bundle="GEP_RESOURCES" />: 
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID">
			<html:options collection="executionYears" property="idInternal"
				labelProperty="year" />
		</html:select>	
		<br/>
		<bean:message key="label.gep.registrationAgreement" bundle="GEP_RESOURCES" />: 
		<e:labelValues id="agreementValues"
			enumeration="net.sourceforge.fenixedu.domain.student.RegistrationAgreement"
			bundle="ENUMERATION_RESOURCES" /> 
		<html:select property="registrationAgreement">
			<html:option key="dropDown.Default" value="" />
			<html:options collection="agreementValues" property="value" labelProperty="label" />
		</html:select>		
		<br/><br/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="startClock()">Ok</html:submit>		
		<br/><br/>
	</logic:present>
	
	<table>
		<tbody id="processedDCPs" style="color:green"/>
		<tbody id="processingDCPs" style="color:blue"/>
		<tbody id="toProcessDCPs" style="color:red"/>
	</table>		
	
</html:form>