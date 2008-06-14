<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal"/>
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.viewProject.useCasetitle"/></h2>
  	
	<%-- PARTICIPATIONS --%>	
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants"/></strong></p>
	<fr:view name="participations" layout="tabular" schema="projectParticipation.summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/projects/editProject.do?method=prepareEditParticipants&projectId="+projectId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editParticipants" />
			</html:link>
		</li>
	</ul>
	
	<%-- DATA --%>		
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.data"/></strong></p>
	<fr:view name="selectedProject" schema="project.view-defaults">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright thtop width600px"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/projects/editProject.do?method=prepareEditData&projectId="+projectId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editData" />
			</html:link>
		</li>
	</ul>
 	
	<%-- EVENTS --%>		
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.events"/></strong></p>
	<fr:view name="selectedProject" property="associatedEvents" schema="projectEventAssociation.event-summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 mtop05"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/projects/editProject.do?method=prepareEditEventAssociations&projectId="+projectId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editAssociatedEvents" />
			</html:link>
		</li>
	</ul>

	
	<%-- UNITS --%>		
	<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.units"/></strong></p>
	<fr:view name="unitParticipations" layout="tabular" schema="projectUnitParticipation.summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 mtop05"/>
        	<fr:property name="columnClasses" value=",,"/>
	    </fr:layout>
	</fr:view>
	<ul class="mtop0 list5">
		<li>
			<html:link page="<%="/projects/editProject.do?method=prepareEditParticipantUnits&projectId="+projectId%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editAssociatedUnits" />
			</html:link>
		</li>
	</ul>

</logic:present>
