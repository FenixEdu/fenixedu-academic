/*
 * ITurnoPersistente.java
 *
 * Created on 17 de Outubro de 2002, 19:32
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.ITurno;

public interface ITurnoPersistente extends IPersistentObject{
    public ITurno readByNome(String nome) throws ExcepcaoPersistencia;
	public ITurno readByNameAndExecutionCourse(String nome, IDisciplinaExecucao IDE) throws ExcepcaoPersistencia;
    public void lockWrite(ITurno turno) throws ExcepcaoPersistencia;
    public void delete(ITurno turno) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
    public Integer querie2(String nome) throws ExcepcaoPersistencia;    
    
    
    public ArrayList readByDisciplinaExecucao(String sigla, String anoLectivo, String siglaLicenciatura) throws ExcepcaoPersistencia;
	
	
	public ArrayList readByDisciplinaExecucaoAndType(String sigla, String anoLectivo, String siglaLicenciatura,Integer type) throws ExcepcaoPersistencia;
	
	public List readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
}
