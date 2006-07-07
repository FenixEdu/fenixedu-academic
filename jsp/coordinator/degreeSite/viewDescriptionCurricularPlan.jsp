<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2 class="mbottom2"><bean:message key="title.coordinator.degreeSite.edit"/></h2>

<p>
	<span class="error">
		<html:errors/>
	</span>
</p>

<logic:present name="degreeCurricularPlan" >

	<html:form action="/degreeSiteManagement">
		<html:hidden property="method" value="editDescriptionDegreeCurricularPlan" />
		<html:hidden property="page" value="1" />
        <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
		<html:hidden property="degreeCurricularPlanID" value="<%=  degreeCurricularPlanID.toString() %>" />	
		
		<fr:edit name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" schema="degree.curricular.plan.description" nested="true">
		    <fr:layout>
	    	    <fr:property name="classes" value="thtop width8em"/>
	        	<fr:property name="columnClasses" value=",pbottom1,valigntop"/>
		    </fr:layout>
		</fr:edit>
	
		<p style="margin-top: 2em;">
			<html:submit styleClass="inputbutton">
				<bean:message key="button.save"/>                    		         	
			</html:submit>       
			<html:reset styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>  
		</p>

	</html:form>
	
</logic:present>
