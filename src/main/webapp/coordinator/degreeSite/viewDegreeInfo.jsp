<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:form action="/degreeSiteManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editDegreeInformation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%=  degreeCurricularPlanID.toString() %>" />

	<h2 class="mbottom15"><bean:message key="title.coordinator.degreeSite.edit" /> <bean:write name="currentDegreeInfo" property="executionYear.year"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

	<logic:present name="info">
		<bean:define id="info" name="info" scope="request" />			
	</logic:present>		

	<logic:equal name="info" value="description">
		<fr:edit type="net.sourceforge.fenixedu.domain.DegreeInfo" name="currentDegreeInfo" schema="degree.info.description" nested="true">
		    <fr:layout>
	    	    <fr:property name="classes" value="tstyle2 thlight thright thtop mbottom2"/>
	        	<fr:property name="columnClasses" value="width10em,pbottom1,tdclear tderror1 valigntop"/>
		    </fr:layout>
		</fr:edit>
	</logic:equal>

	<logic:equal name="info" value="acess">
		<fr:edit type="net.sourceforge.fenixedu.domain.DegreeInfo" name="currentDegreeInfo" schema="degree.info.access" nested="true">
			<fr:layout>
	    	    <fr:property name="classes" value="tstyle2 thlight thright thtop mbottom2"/>
	        	<fr:property name="columnClasses" value="width10em,pbottom1,tdclear tderror1 valigntop"/>
		    </fr:layout>
		</fr:edit>
	</logic:equal>

	<logic:equal name="info" value="professionalStatus">
		<fr:edit type="net.sourceforge.fenixedu.domain.DegreeInfo" name="currentDegreeInfo" schema="degree.info.professional.status" nested="true">
			<fr:layout>
	    	    <fr:property name="classes" value="tstyle2 thlight thright thtop mbottom2"/>
	        	<fr:property name="columnClasses" value="width10em,pbottom1,tdclear tderror1 valigntop"/>
		    </fr:layout>
		</fr:edit>
	</logic:equal>

	<p style="margin-top: 2em;">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save" />
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear" />
	</html:reset>
	</p>

</html:form>
