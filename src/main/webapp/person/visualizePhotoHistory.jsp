<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<logic:present role="PERSON">
<bean:define id="person" name="LOGGED_USER_ATTRIBUTE" property="person"/>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.person.title.photoHistory"/></h2>

<p>
    <span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<p class="mtop2">
	<html:link page="/photoHistory.do?method=backToShowInformation">
		<bean:message key="link.back" bundle="COMMON_RESOURCES"/>
	</html:link>
</p>

<logic:present name="history">

	<logic:empty name="history">
		<div class="infoop6">
			<bean:message key="message.person.photoHistory.empty" bundle="APPLICATION_RESOURCES"/>
		</div>
		<p>
			<span class="mtop1 mbottom0">
				<html:link page="/uploadPhoto.do?method=prepare">
					<bean:message key="link.person.upload.photo" bundle="APPLICATION_RESOURCES" />
				</html:link>
			</span>
		</p>
    </logic:empty>
    
	<logic:notEmpty name="history">
		
		<fr:view name="history">
			<fr:schema type="net.sourceforge.fenixedu.domain.Photograph" bundle="APPLICATION_RESOURCES">
				<fr:slot name="externalId" key="label.photo" layout="view-as-image">
			    	<fr:property name="classes" value="nobullet noindent"/>
					<fr:property name="useParent" value="true" />
					<fr:property name="moduleRelative" value="false" />
					<fr:property name="contextRelative" value="true" />
			    	<fr:property name="imageFormat"
							value="/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=${externalId}" />
		    	</fr:slot>
				<fr:slot name="submission" key="label.date">
					<fr:property name="classes" value="nobullet noindent"/>   
			    </fr:slot>
			    <fr:slot name="state" key="label.state">
			    	<fr:property name="classes" value="nobullet noindent"/>   
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 tstyle9 mtop05" />
				<fr:property name="columnClasses" value="acenter ,acenter ,acenter" />
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>
	
</logic:present>

</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>