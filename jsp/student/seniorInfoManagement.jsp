<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/html2xhtml.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/richtext.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/htmleditor.js"></script>

<h2><bean:message key="label.title.seniorInfo"/></h2>
<logic:equal name="seniorInfoForm" property="name" value="">
	<html:errors/>
</logic:equal>
<logic:notEqual name="seniorInfoForm" property="name" value="">
  <html:form action="/seniorInformation?method=change">
	
	<html:hidden property="seniorIDInternal"/>
	<html:hidden property="name" name="seniorInfoForm"/>
	<html:hidden property="address" name="seniorInfoForm"/>	
	<html:hidden property="areaCode" name="seniorInfoForm"/>	
	<html:hidden property="areaCodeArea" name="seniorInfoForm"/>	
	<html:hidden property="phone" name="seniorInfoForm"/>	
	<html:hidden property="mobilePhone" name="seniorInfoForm"/>			
	<html:hidden property="email" name="seniorInfoForm"/>
	<html:hidden property="availablePhoto" name="seniorInfoForm"/>	
	<html:hidden property="personID" name="seniorInfoForm"/>		
	<html:hidden property="page" value="1"/>
	
	<html:hidden property="specialtyField"/>
	<html:hidden property="professionalInterests"/>
	<html:hidden property="languageSkills"/>
	<html:hidden property="informaticsSkills"/>
	<html:hidden property="extracurricularActivities"/>
	<html:hidden property="professionalExperience"/>

	
    <table width="100%" cellspacing="0">
		<tr>
			<tr>
				<td class="infoop"><span class="emphasis-box">info</span>
     			<td class="infoop"><bean:message key="label.senior.personalInfoWarning"/></td>
     	</tr>
    </table>
	<br/>
	<table border="0" width="100%" cellspacing="9">
          <tr><td colspan="2">
          	<table border="0" width="100%" height="100%" cellspacing="4">
				<tr>
        		    <td align="right"><bean:message key="label.person.name"/>:</td>
		        	<td colspan="5"><bean:write name="seniorInfoForm" property="name"/></td>
					<td rowspan="3" valign="center" height="12px" width="5px" align="center">
						<logic:equal name="seniorInfoForm" property="availablePhoto" value="true">
							<bean:define id="personID" name="seniorInfoForm" property="personID"/>
      						<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
						</logic:equal>
						<logic:equal name="seniorInfoForm" property="availablePhoto" value="false">
							<html:img src="<%= request.getContextPath() +"/images/photoPlaceHolder.jpg"%>" altKey="<bean:message key="photoPlaceHolder" bundle="IMAGE_RESOURCES" />"/>
						</logic:equal>
					</td>
		        </tr>
          		<tr>
		            <td align="right"><bean:message key="label.person.address" bundle="APPLICATION_RESOURCES"/></td>
		            <td colspan="5"><bean:write name="seniorInfoForm" property="address" filter="false"/></td>
		        </tr>
          		<tr>
		            <td align="right"><bean:message key="label.person.postCode" bundle="APPLICATION_RESOURCES"/></td>
		            <td><bean:write name="seniorInfoForm" property="areaCode" filter="false"/></td>
		            <td align="right" colspan="2"><bean:message key="label.person.areaOfPostCode" bundle="APPLICATION_RESOURCES"/></td>
		            <td><bean:write name="seniorInfoForm" property="areaCodeArea" filter="false"/></td>
		        </tr>
				<tr>	
					<td align="right" width="14%"><bean:message key="label.person.telephone" bundle="APPLICATION_RESOURCES"/></td>
            		<td width="8%"><bean:write name="seniorInfoForm" property="phone"/></td>
            		<td align="right"width="16%" colspan="2"><bean:message key="label.person.mobilePhone" bundle="APPLICATION_RESOURCES"/></td>
            		<td width="16%"><bean:write name="seniorInfoForm" property="mobilePhone"/></td>
            		<td align="right" width="16%"><bean:message key="label.person.email" bundle="APPLICATION_RESOURCES"/></td>
	        		<td width="16%"><bean:write name="seniorInfoForm" property="email"/></td>
				</tr>
	        </table>				
	      </td></tr>
		  <tr>
			<td colspan="2">&nbsp;
				<span class="error"><html:errors/></span>
			</td>
		  </tr>
		  <tr>
            <td width="20%"><bean:message key="label.senior.expectedDegreeTermination"/></td>
            <td>
				<html:text maxlength="2" size="2" property="expectedDegreeTerminationDay"/> / <html:text maxlength="2" size="2" property="expectedDegreeTerminationMonth"/> / <html:text maxlength="4" size="4" property="expectedDegreeTerminationYear"/>
				<bean:message key="label.senior.expectedDegreeTerminationHelp"/>
			</td>
          </tr>
		  <tr>
            <td><bean:message key="label.senior.expectedDegreeAverageGrade"/>:</td>
            <td><html:text maxlength="2" size="2" property="expectedDegreeAverageGrade"/></td>
          </tr>
    </table>
   	<table border="0" width="100%" cellspacing="9">
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>		  
          <tr>
	          <td valign="top"><bean:message key="label.senior.specialtyField_"/></td>	          
          </tr>		  
		  <tr>
            <!--<td><html:textarea rows="5" cols="65" property="specialtyField"/></td>-->           
            <td>		        
				<script language="JavaScript" type="text/javascript"> 
					<!--
					initEditor();		
					//-->
					</script>
					
					<noscript>JavaScript must be enable to use this form <br> </noscript>
					
					<script language="JavaScript" type="text/javascript"> 
					<!--
					writeMultipleTextEditor('rte1' ,100, 130, document.forms[0].specialtyField.value);		
					//-->
				</script>		
			</td>
            <td valign="middle" align="center"><bean:message key="label.senior.specialtyFieldFinalWork"/></td>
          </tr>
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>	
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>		  
		  <tr>		  	 
		  	<td valign="top" ><bean:message key="label.senior.professionalInterests_"/></td>		 
		  </tr>		  
		  <tr>
			<!--<td valign="top"><bean:message key="label.senior.professionalInterests"/></td>-->
            <!--<td><html:textarea rows="5" cols="65" property="professionalInterests"/></td>-->
            <td>	
				<script language="JavaScript" type="text/javascript"> 
					<!--
					initEditor();		
					//-->
					</script>
					
					<noscript>JavaScript must be enable to use this form <br> </noscript>
					
					<script language="JavaScript" type="text/javascript"> 
					<!--
					writeMultipleTextEditor('rte2', 100, 130, document.forms[0].professionalInterests.value);		
					//-->
				</script>		
			</td>
          </tr>
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>	
		  <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>		  
		   <tr>		 
	          <td valign="top"><bean:message key="label.senior.languageSkills_"/></td>	          
          </tr>		  
		  <tr>
            <!--<td valign="top"><bean:message key="label.senior.languageSkills"/></td>-->
            <!--<td><html:textarea rows="5" cols="65" property="languageSkills"/></td>-->
            <td>	
				<script language="JavaScript" type="text/javascript"> 
					<!--
					initEditor();		
					//-->
					</script>
					
					<noscript>JavaScript must be enable to use this form <br> </noscript>
					
					<script language="JavaScript" type="text/javascript"> 
					<!--
					writeMultipleTextEditor('rte3', 100, 130, document.forms[0].languageSkills.value);		
					//-->
				</script>		
			</td>
          </tr>
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>
		  <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>	
		  <tr>		   	
	          <td valign="top" ><bean:message key="label.senior.informaticsSkills_"/></td>
          </tr>		  
		  <tr>
            <!--<td valign="top"><bean:message key="label.senior.informaticsSkills"/></td>-->
            <!--<td><html:textarea rows="5" cols="65" property="informaticsSkills"/></td>-->
            <td>	
				<script language="JavaScript" type="text/javascript"> 
					<!--
					initEditor();		
					//-->
					</script>
					
					<noscript>JavaScript must be enable to use this form <br> </noscript>
					
					<script language="JavaScript" type="text/javascript"> 
					<!--
					writeMultipleTextEditor('rte4' ,100, 130, document.forms[0].informaticsSkills.value);		
					//-->
				</script>		
			</td>
          </tr>
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>	
		  <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>		  
		  <tr>
	          <td valign="top" ><bean:message key="label.senior.extracurricularActivities_"/></td>
          </tr>		  
		  <tr>
            <!--<td valign="top"><bean:message key="label.senior.extracurricularActivities"/></td>-->
            <!--<td><html:textarea rows="5" cols="65" property="extracurricularActivities"/></td>-->            
            <td>	
				<script language="JavaScript" type="text/javascript"> 
					<!--
					initEditor();		
					//-->
					</script>
					
					<noscript>JavaScript must be enable to use this form <br> </noscript>
					
					<script language="JavaScript" type="text/javascript"> 
					<!--
					writeMultipleTextEditor('rte5', 100, 130, document.forms[0].extracurricularActivities.value);		
					//-->
				</script>		
			</td>
          </tr>
          <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>		 
		  <tr>
			<td colspan='2'>&nbsp;</td>
		  </tr>	 
		  <tr>		  
	          <td valign="top" ><bean:message key="label.senior.professionalExperience_"/></td>
          </tr>		  
  		  <tr>
            <!--<td valign="top"><bean:message key="label.senior.professionalExperience"/></td>-->
            <!--<td><html:textarea rows="5" cols="65" property="professionalExperience"/></td>-->            
          	<td>	
				<script language="JavaScript" type="text/javascript"> 
					<!--
					initEditor();		
					//-->
					</script>
					
					<noscript>JavaScript must be enable to use this form <br> </noscript>
					
					<script language="JavaScript" type="text/javascript"> 
					<!--
					writeMultipleTextEditor('rte6', 100, 130, document.forms[0].professionalExperience.value);		
					//-->
				</script>		
			</td>
          </tr>		  		  
		  <tr>
			<td>&nbsp;</td>
		  </tr>
  		  <tr>
            <td valign="bottom" align="right" colspan="3">
				<bean:message key="label.senior.lastModificationDate"/>
				<bean:define id="formatDate">
					<dt:format pattern="dd/MM/yyyy">
						<bean:write name="seniorInfoForm" property="lastModificationDate.time"/>
					</dt:format>
				</bean:define>
				<br/><bean:write name="formatDate"/>
			</td>
          </tr>
   	</table>
<br /><br />
<html:submit styleClass="inputbutton" property="Alterar" onclick="this.form.specialtyField.value=update1();this.form.professionalInterests.value=update2();this.form.languageSkills.value=update3();this.form.informaticsSkills.value=update4();this.form.extracurricularActivities.value=update5(); this.form.professionalExperience.value=update6()"><bean:message key="button.change"/></html:submit>
<html:reset property="Reset" styleClass="inputbutton"><bean:message key="button.reset"/></html:reset>
</html:form>

</logic:notEqual>
