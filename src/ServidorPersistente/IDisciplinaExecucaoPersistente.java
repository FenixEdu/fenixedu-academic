package ServidorPersistente;

import java.util.ArrayList;
import java.util.List;

import Dominio.IDisciplinaExecucao;

public interface IDisciplinaExecucaoPersistente extends IPersistentObject {
    public boolean apagarTodasAsDisciplinasExecucao();
    public boolean escreverDisciplinaExecucao(IDisciplinaExecucao disciplinaExecucao);
    public IDisciplinaExecucao lerDisciplinaExecucao(int chaveLicenciaturaExecucao, String sigla);
    public boolean apagarDisciplinaExecucao(int chaveLicenciaturaExecucao, String sigla);
    public ArrayList lerTodasDisciplinaExecucao();

    public IDisciplinaExecucao readBySiglaAndAnoLectivAndSiglaLicenciatura(String sigla, String anoLectivo, String siglaLicenciatura) throws ExcepcaoPersistencia;
  	public List readByAnoCurricularAndAnoLectivoAndSiglaLicenciatura(Integer anoCurricular,String anoLectivo, Integer semestre, String siglaLicenciatura) throws ExcepcaoPersistencia;
}