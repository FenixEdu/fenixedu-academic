<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:output method="xml" indent="yes"/>
 <xsl:template match="/"> 
	<xsl:apply-templates select="list"/> 
</xsl:template> 

<xsl:template match="list">
	<info-aluno>
		<xsl:apply-templates select="net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoStudentExternalInformation"/>
	</info-aluno>
</xsl:template>

<xsl:template match="net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoStudentExternalInformation">
		<numero>
			<xsl:value-of select="./number"/>
		</numero>
		<ano-academico>
			<xsl:value-of select="./curricularYear"/>
		</ano-academico>
		<media-curso>
			<xsl:value-of select="./average"/>
		</media-curso>
		<xsl:apply-templates select="degree"/>
		<xsl:apply-templates select="person"/>
		<disciplinas>		
			<xsl:apply-templates select="courses"/>
			<xsl:apply-templates select="availableRemainingCourses/net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCurricularCourseInfo"/>	
		</disciplinas>		
						
</xsl:template> 

<xsl:template match="courses">
			<xsl:apply-templates select="net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalEnrollmentInfo"/>	
</xsl:template> 

<xsl:template match="net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalEnrollmentInfo">
	<disciplina>
			<nome>
				<xsl:value-of select="./course/name"/>
			</nome>
		<codigo>
			<xsl:value-of select="./course/code"/>
		</codigo>
			<creditos>
				<xsl:value-of select="./course/credits"/>
			</creditos>
			<creditos-ECTS>
				<xsl:value-of select="./course/ECTSCredits"/>
			</creditos-ECTS>
			<peso>
				<xsl:value-of select="./course/weigth"/>
			</peso>
			<ano>
				<xsl:value-of select="./course/curricularYear"/>
			</ano>	
		<nota>
			<xsl:value-of select="./finalGrade"/>
		</nota>
	</disciplina>						
</xsl:template> 

<xsl:template match="degree">
				<curso>
					<codigo>
						<xsl:value-of select="./code"/>
					</codigo>
					<nome>
						<xsl:value-of select="./name"/>
					</nome>
					<ramo>
						<xsl:apply-templates select="branch"/>
					</ramo>
				</curso>
</xsl:template> 

<xsl:template match="net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCurricularCourseInfo">
	<disciplina>
			<nome>
				<xsl:value-of select="./name"/>
			</nome>
			<codigo>
				<xsl:value-of select="./code"/>
			</codigo>
			<creditos>
				<xsl:value-of select="./credits"/>
			</creditos>
			<creditos-ECTS>
				<xsl:value-of select="./ECTSCredits"/>
			</creditos-ECTS>
			<peso>
				<xsl:value-of select="./weigth"/>
			</peso>
			<ano>
				<xsl:value-of select="./curricularYear"/>
			</ano>
			<nota/>
	</disciplina>			
</xsl:template>


<xsl:template match="branch">
					<codigo>
						<xsl:value-of select="./code"/>
					</codigo>
					<nome>
						<xsl:value-of select="./name"/>
					</nome>
</xsl:template> 

<xsl:template match="person">
				<dados-pessoais>
					<username>
						<xsl:value-of select="./username"/>
					</username>
					<nome>
						<xsl:value-of select="./name"/>
					</nome>
					<sexo>
						<xsl:value-of select="./sex"/>
					</sexo>
					<data-nascimento>
						<xsl:value-of select="./birthday"/>
					</data-nascimento>					
					<nacionalidade>
						<xsl:value-of select="./nationality"/>
					</nacionalidade>		
					<naturalidade>								
						<xsl:apply-templates select="citizenship"/>
					</naturalidade>
					<morada>
						<xsl:apply-templates select="address"/>
					</morada>
					<telefone>
						<xsl:value-of select="./phone"/>
					</telefone>
					<telemovel>
						<xsl:value-of select="./celularPhone"/>
					</telemovel>
					<e-mail>
						<xsl:value-of select="./email"/>
					</e-mail>
					<identificacao>
						<xsl:apply-templates select="identification"/>
					</identificacao>
					<nif>
						<xsl:value-of select="./fiscalNumber"/>
					</nif>
				</dados-pessoais>
</xsl:template>


<xsl:template match="address">
				<rua>
					<xsl:value-of select="./street"/>
				</rua>
				<codigo-postal>
					<xsl:value-of select="./postalCode"/>
				</codigo-postal>
				<localidade>
					<xsl:value-of select="./town"/>
				</localidade>				
</xsl:template> 

<xsl:template match="citizenship">
				<freguesia>
					<xsl:value-of select="./area"/>
				</freguesia>
				<concelho>
					<xsl:value-of select="./county"/>
				</concelho>
</xsl:template> 

<xsl:template match="identification">
				<numero>
					<xsl:value-of select="./number"/>
				</numero>
				<data-validade>
					<xsl:value-of select="./expiryDate"/>
				</data-validade>
				<data-emissao>
					<xsl:value-of select="./emitionDate"/>				
				</data-emissao>
				<local-emissao>
					<xsl:value-of select="./emitionLocal"/>				
				</local-emissao>				
</xsl:template> 


</xsl:stylesheet>
