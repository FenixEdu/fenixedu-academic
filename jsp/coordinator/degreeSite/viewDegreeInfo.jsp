<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p><span class="error"><html:errors /></span></p>

<html:form action="/degreeSiteManagement">
	<html:hidden property="method" value="editDegreeInformation" />
	<html:hidden property="page" value="1" />
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />		
	<html:hidden property="degreeCurricularPlanID" value="<%=  degreeCurricularPlanID.toString() %>" />

	<h2><bean:message key="title.coordinator.degreeSite.edit" /> <bean:write name="currentDegreeInfo" property="executionYear.year"/></h2>

	<logic:present name="info">
		<bean:define id="info" name="info" scope="request" />			
	</logic:present>		

	<logic:equal name="info" value="description">
		<fr:edit type="net.sourceforge.fenixedu.domain.DegreeInfo" name="currentDegreeInfo" schema="degree.info.description" nested="true">
		    <fr:layout>
	    	    <fr:property name="classes" value="style1"/>
	        	<fr:property name="columnClasses" value="listClasses"/>
		    </fr:layout>
		</fr:edit>
	</logic:equal>

	<logic:equal name="info" value="acess">
		<fr:edit type="net.sourceforge.fenixedu.domain.DegreeInfo" name="currentDegreeInfo" schema="degree.info.access" nested="true"/>
	</logic:equal>

	<logic:equal name="info" value="professionalStatus">
		<fr:edit type="net.sourceforge.fenixedu.domain.DegreeInfo" name="currentDegreeInfo" schema="degree.info.professional.status" nested="true"/>
	</logic:equal>

	<p style="margin-top: 2em;">
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save" />
	</html:submit>
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear" />
	</html:reset>
	</p>

</html:form>
