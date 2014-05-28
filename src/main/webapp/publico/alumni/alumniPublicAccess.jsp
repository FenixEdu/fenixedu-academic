<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniPublicAccess.jsp -->

<h1><bean:message key="label.alumni.registration" bundle="ALUMNI_RESOURCES" /></h1>

<h2><bean:message key="label.alumni.registration.form" bundle="ALUMNI_RESOURCES" /> <span class="color777 fwnormal"><bean:message key="label.step.1.3" bundle="ALUMNI_RESOURCES" /></span></h2>

<div class="alumnilogo">
<logic:present name="alumniPublicAccessMessage">
	<span class="error0"><bean:write name="alumniPublicAccessMessage" scope="request" /></span><br/>
</logic:present>

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	<logic:present name="showReportError">
		<bean:define id="documentIdNumber" name="alumniBean" property="documentIdNumber"/>
		<bean:define id="email" name="alumniBean" property="email"/>
		<bean:define id="errorMessage" name="errorMessage"/>
		<bean:define id="studentNumber" name="alumniBean" property="studentNumber"/>
		<html:link action="<%= "alumni.do?method=prepareSendEmailReportingError&amp;documentIdNumber=" + documentIdNumber + 
								"&amp;email=" + email + "&amp;studentNumber=" + studentNumber + "&amp;errorMessage=" + errorMessage %>">
			<bean:message key="label.public.report.error" bundle="ALUMNI_RESOURCES"/>
		</html:link>
	</logic:present>
</html:messages>

<div class="reg_form">	

	<fr:form action="/alumni.do?method=validateFenixAcessData">

		<fieldset style="display: block;">
			<h3>Identificação <%-- <bean:message key="label.alumni.form" bundle="ALUMNI_RESOURCES" /> --%></h3>
			<p>
				<bean:message key="label.alumni.registration.process" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ALUMNI_RESOURCES" />				
			</p>
		
			<fr:edit id="alumniBean" name="alumniBean" visible="false" />

			<label for="student_number" class="student_number">
				<bean:message key="label.student.number" bundle="ALUMNI_RESOURCES" />:
			</label>
			<fr:edit id="studentNumber-validated" name="alumniBean" slot="studentNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
				<fr:destination name="invalid" path="/alumni.do?method=initFenixPublicAccess&showForm=true"/>
				<fr:layout>
					<fr:property name="size" value="30"/>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<span class="error0"><fr:message for="studentNumber-validated" /></span>
			<html:link href="<%= request.getContextPath() + "/publico/alumni.do?method=requestIdentityCheck"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.request.identity.check"/></html:link>						
					
			<label for="bi_number" class="bi_number">
				<bean:message key="label.document.id.number" bundle="ALUMNI_RESOURCES" />:
			</label>
			<fr:edit id="documentIdNumber-validated" name="alumniBean" slot="documentIdNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:destination name="invalid" path="/alumni.do?method=initFenixPublicAccess&showForm=true"/>
				<fr:layout>
					<fr:property name="size" value="30"/>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<span class="error0"><fr:message for="documentIdNumber-validated" /></span>
			
			<label for="email">
				<bean:message key="label.email" bundle="ALUMNI_RESOURCES" />:
			</label>
			<fr:edit id="email-validated" name="alumniBean" slot="email" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredEmailValidator">
				<fr:destination name="invalid" path="/alumni.do?method=initFenixPublicAccess&showForm=true"/>
				<fr:layout>
					<fr:property name="size" value="40"/>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<span class="error0"><fr:message for="email-validated" /></span>

			<label for="captcha">
				<bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:
			</label>
			<div class="mbottom05"><img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>"/><br/></div>
			<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
			<input type="text" name="j_captcha_response" size="30" style="margin-bottom: 1em;"/>
			
			<logic:present name="captcha.unknown.error">
				<p style="margin: 0;">
					<span class="error0">
						<bean:message key="captcha.unknown.error" bundle="ALUMNI_RESOURCES" />
					</span>
				</p>
			</logic:present>

			<br/>

			<fr:edit id="privacyPolicy-validated" name="alumniBean" slot="privacyPolicy" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="style" value="display: inline;"/>
				</fr:layout>
			</fr:edit>
			<label style="display: inline;">
				<bean:message key="label.privacy.policy.a" bundle="ALUMNI_RESOURCES" />
				<html:link href="#" onclick="document.getElementById('policyPrivacy').style.display='block'" >
					<bean:message key="label.privacy.policy.b" bundle="ALUMNI_RESOURCES" />
				</html:link>
			</label>

			<div id="policyPrivacy" class="switchInline mtop1">
				<bean:message key="label.privacy.policy.text" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ALUMNI_RESOURCES" />
			</div>
						
			<logic:present name="privacyPolicyPublicAccessMessage">
				<span class="error0">
					<bean:message key="privacy.policy.acceptance" bundle="ALUMNI_RESOURCES" />
				</span>
			</logic:present>
			
			<p class="comment"><bean:message key="label.all.required.fields" bundle="ALUMNI_RESOURCES" /></p>

			<html:submit>
				<bean:message key="label.submit" bundle="ALUMNI_RESOURCES" />
			</html:submit>
		</fieldset>
	</fr:form>
</div>
<div class="alumni-faq color777">
		<h3>FAQ</h3>
		<ol>
			<li>
				<h4>Como recuperar a minha IST-ID?</h4>
				<p>Poderá contactar a Direção de Serviços de Informática (ci@ist.utl.pt), fornecendo o seu número de identificação (BI, Cartão do Cidadão...).</p>
			</li>
			<li>
				<h4>Como recuperar a password?</h4>
				<p>Existem várias possibilidades de recuperação, que podem ser consultadas no seguinte endereço: <a href="https://id.ist.utl.pt/password/recover.php?language=pt" title="Recuperar password">https://id.ist.utl.pt/password/recover.php?language=pt</a>.</p>
			</li>
			<li>
				<h4>Como alterar a IST-ID?</h4>
				<p>Não é possível alterar a IST-ID, uma vez que é um número de identificação gerado uma única vez, utilizado para o acesso aos serviços informáticos do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, correspondendo na maior parte dos casos ao número de Aluno/Docente (ex: Nºde Aluno 55000 corresponde ao IST-ID ist155000).</p>
			</li>
			<li>
				<h4>Tive mais do que um número de aluno. Qual o número que deverei facultar?</h4>
				<p>Poderá facultar qualquer número de Aluno (Licenciatura, Mestrado, Doutoramento), uma vez que a IST-ID agrupa todas as Matriculas que teve enquanto Aluno.</p>
			</li>
			<li>
				<h4>Como alterar o número de telemóvel?</h4>
				<p>Terá de solicitar ao Núcleo correspondente ao seu Curso a atualização dos seus dados Pessoais (neste caso, telemóvel).<br>
				Núcleo de Graduação - <a href="mailto:nucleo.graduacao@ist.utl.pt" title="Enviar email">nucleo.graduacao@ist.utl.pt</a><br>
				Núcleo de Pós-Graduação e Formação Contínua - <a href="mailto:npfc@ist.utl.pt" title="Enviar email">npfc@ist.utl.pt</a></p>
			</li>
		</ol>
	</div>
</div>

<!-- END CONTENTS -->
</div>