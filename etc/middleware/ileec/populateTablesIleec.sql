LOAD DATA INFILE '@load.data.infile.rootIleec@ACientificasIleecTAB.txt' into table mw_AREAS_CIENTIFICAS_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@AEspecializacaoIleecTAB.txt' into table mw_AREAS_ESPECIALIZACAO_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@ASecundariaTAB.txt' into table mw_AREAS_SECUNDARIAS_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@DisciplinaGrupoTAB.txt' into table mw_DISCIPLINA_GRUPO_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@disciplinasIleecTAB.txt' into table mw_DISCIPLINAS_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@equivalenciasIleecTAB.txt' into table mw_EQUIVALENCIAS_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@gruposIleecTAB.txt' into table mw_GRUPOS_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@precedenciasDisciplinaDisciplinaIleecTAB.txt' into table mw_PRECEDENCIAS_DISCIPLINA_DISCIPLINA_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@precedenciasXCadeirasIleecTAB.txt' into table mw_PRECEDENCIAS_NUMERO_DISCIPLINAS_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@TiposEquivalenciaTAB.txt' into table mw_TIPOS_EQUIVALENCIA_ILEEC IGNORE 2 LINES;
LOAD DATA INFILE '@load.data.infile.rootIleec@tiposPrecedenciaIleecTAB.txt' into table mw_TIPOS_PRECENDENCIA_ILEEC IGNORE 2 LINES;

