<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">

<h2><bean:message key="label.curricularplan" /></h2>

<hr/><br/>

<html:form action="/studentTimeTable.do" target="_blank">
	<html:hidden property="method" value="showTimeTable"/>
	<html:hidden property="page" value="1"/>
	<table>
	  <tr>
	    <td><bean:message  key="label.studentCurricularPlan"/></td>
	    <td>	
	    	<html:select property="registrationId">
				<html:options collection="registrations" property="idInternal" labelProperty="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/>
			</html:select>
		</td>
	  </tr>
	</table>
	
	<html:submit styleClass="inputbutton"><bean:message key="button.submit" /></html:submit>
	
</html:form>
</logic:present>

