/*
 * ISalaPersistente.java
 * 
 * Created on 17 de Outubro de 2002, 15:09
 */

package ServidorPersistente;

/**
 * @author tfc130
 */
import java.util.List;

import Dominio.IExam;
import Dominio.ISala;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface ISalaPersistente extends IPersistentObject
{
    public ISala readByName(String nome) throws ExcepcaoPersistencia;
    public void lockWrite(ISala sala) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void delete(ISala sala) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;
    public List readSalas(
        String nome,
        String edificio,
        Integer piso,
        Integer tipo,
        Integer capacidadeNormal,
        Integer capacidadeExame)
        throws ExcepcaoPersistencia;
    public List readAvailableRooms(IExam exam) throws ExcepcaoPersistencia;
    public List readForRoomReservation() throws ExcepcaoPersistencia;
    public List readByPavillion(String string) throws ExcepcaoPersistencia;
	public List readAllBuildings() throws ExcepcaoPersistencia;}
