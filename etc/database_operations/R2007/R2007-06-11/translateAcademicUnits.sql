UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Chemical and Biological Engineering (DEQB)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Química e Biológica (DEQB)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Civil Engineering and Architecture (DECivil)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Civil e Arquitectura (DECivil)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Mechanical Engineering Department (DEM)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia Mecânica (DEM)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Departamento de Física (DF)'
	WHERE `REAL_NAME`  = 'Department of Physics (DF)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Mining and Earth Resources (DEMG)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia de Minas e Georrecursos (DEMG)';
UPDATE `DEPARTMENT`
	SET `REAL_NAME_EN` = 'Department of Materials Engineering (DEMAT)'
	WHERE `REAL_NAME`  = 'Departamento de Engenharia de Materiais (DEMAT)';

ALTER TABLE `PARTY` ADD COLUMN `NAME_EN` VARCHAR(255) DEFAULT NULL AFTER `NAME`;

UPDATE PARTY 
	SET NAME_EN = 'Hydraulics and Water and Environmental Resources Section'
	WHERE NAME  = 'Secção de Hidraulica e Recursos Hídricos e Ambientais';
UPDATE PARTY 
	SET NAME_EN = 'Structural Mechanics and Structures Section'
	WHERE NAME  = 'Secção de Mecânica Estrutural e Estruturas';
UPDATE PARTY 
	SET NAME_EN = 'Systems and Aided Design Section'
	WHERE NAME  = 'Secção de Sistemas de Apoio ao Projecto';
UPDATE PARTY
	SET NAME_EN = 'Urban Planning, Transportation and Systems Section'
	WHERE NAME  = 'Secção de Urbanismo, Transportes, Vias e Sistemas';
UPDATE PARTY
	SET NAME_EN = 'Architecture Section'
	WHERE NAME  = 'Seccao de Arquitectura';
UPDATE PARTY
	SET NAME_EN = 'Construction Section' 
	WHERE NAME  = 'Seccao de Construcao';
UPDATE PARTY
	SET NAME_EN = 'Geotechnics Section' 
	WHERE NAME  = 'Seccao de Geotecnia';
UPDATE PARTY
	SET NAME_EN = 'Computers Scientific Area' 
	WHERE NAME  = 'Área Científica de Computadores';
UPDATE PARTY
	SET NAME_EN = 'Electronics Scientific Area' 
	WHERE NAME  = 'Área Científica de Electrónica';
UPDATE PARTY
	SET NAME_EN = 'Energy Scientific Area' 
	WHERE NAME  = 'Área Científica de Energia';
UPDATE PARTY
	SET NAME_EN = 'Systems, Decision and Control Scientific Area' 
	WHERE NAME  = 'Área Científica de Sistemas, Decisão e Controlo';
UPDATE PARTY
	SET NAME_EN = 'Telecommunications Scientific Area' 
	WHERE NAME  = 'Área Científica de Telecomunicações';
UPDATE PARTY
	SET NAME_EN = 'Mineralogy and Petrology Laboratory' 
	WHERE NAME  = 'Laboratório de Mineralogia e Petrologia';
UPDATE PARTY
	SET NAME_EN = 'Mining Exploitation Section' 
	WHERE NAME  = 'Secção de Exploração de Minas';
UPDATE PARTY
	SET NAME_EN = 'Minerallurgy and Mining Planning Section' 
	WHERE NAME  = 'Laboratório de Mineralurgia e Planeamento Mineiro';
UPDATE PARTY 
	SET NAME_EN = 'Applied Geology Laboratory' 
	WHERE NAME  = 'Laboratório de Geologia Aplicada';
UPDATE PARTY 
	SET NAME_EN = 'Mechanical Design Section' 
	WHERE NAME  = 'Secção de Projecto Mecânico';
UPDATE PARTY 
	SET NAME_EN = 'Systems Section' 
	WHERE NAME  = 'Secção de Sistemas';
UPDATE PARTY 
	SET NAME_EN = 'Mechanics Technology Section' 
	WHERE NAME  = 'Secção de Tecnologia Mecânica';
UPDATE PARTY 
	SET NAME_EN = 'Thermofluids and Energy Section' 
	WHERE NAME  = 'Secção de Termofluidos e Energia';
UPDATE PARTY 
	SET NAME_EN = 'Environment and Energy Section' 
	WHERE NAME  = 'Secção de Ambiente e Energia';
UPDATE PARTY 
	SET NAME_EN = 'Aerospace Mechanics Section' 
	WHERE NAME  = 'Secção de Mecânica Aeroespacial';
UPDATE PARTY 
	SET NAME_EN = 'Algebra and Mathematical Analysis Section' 
	WHERE NAME  = 'Secção de Álgebra e Análise';
UPDATE PARTY
	SET NAME_EN = 'Computation Science Section'
	WHERE NAME  = 'Secção de Ciência da Computação';
UPDATE PARTY
	SET NAME_EN = 'Statistics and Applications Section'
	WHERE NAME  = 'Secção de Estatística e Aplicações';
UPDATE PARTY
	SET NAME_EN = 'Applied Mathematics and Numerical Analysis Section'
	WHERE NAME  = 'Secção de Matemática Aplicada e Análise Numérica';

