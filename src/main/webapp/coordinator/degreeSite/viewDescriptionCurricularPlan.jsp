<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2 class="mbottom15"><bean:message key="title.coordinator.degreeSite.edit"/></h2>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<logic:present name="degreeCurricularPlan" >

	<html:form action="/degreeSiteManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editDescriptionDegreeCurricularPlan" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
        <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%=  degreeCurricularPlanID.toString() %>" />	
		
		<fr:edit name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" schema="degree.curricular.plan.description" nested="true">
		    <fr:layout>
	    	    <fr:property name="classes" value="tstyle2 thlight thright thtop mbottom2"/>
	        	<fr:property name="columnClasses" value="width10em,pbottom1,tdclear tderror1 valigntop"/>
		    </fr:layout>
		</fr:edit>
	
		<p style="margin-top: 2em;">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.save"/>                    		         	
			</html:submit>       
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>  
		</p>

	</html:form>
	
</logic:present>
