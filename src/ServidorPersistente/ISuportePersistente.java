/*
 * Interface.java
 *
 * Created on 19 de Agosto de 2002, 1:09
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */
public interface ISuportePersistente {
    public void iniciarTransaccao() throws ExcepcaoPersistencia;
    public void confirmarTransaccao() throws ExcepcaoPersistencia;
    public void cancelarTransaccao() throws ExcepcaoPersistencia;

    public IItemPersistente getIItemPersistente();
    public ISeccaoPersistente getISeccaoPersistente();
    public ISitioPersistente getISitioPersistente();
    //public IPessoaPersistente getIPessoaPersistente();

    public IAulaPersistente getIAulaPersistente();
    public ISalaPersistente getISalaPersistente();
    public ITurmaPersistente getITurmaPersistente();
    public ITurnoPersistente getITurnoPersistente();
    
    public IFrequentaPersistente getIFrequentaPersistente();
    public IPersistentEnrolment getIInscricaoPersistente();
    public ITurmaTurnoPersistente getITurmaTurnoPersistente();    
    public ITurnoAlunoPersistente getITurnoAlunoPersistente();
    public ITurnoAulaPersistente getITurnoAulaPersistente();
    
    public IPersistentCurricularCourse getIPersistentCurricularCourse();
    public IDisciplinaExecucaoPersistente getIDisciplinaExecucaoPersistente();
    public IPessoaPersistente getIPessoaPersistente();
    public IPersistentCountry getIPersistentCountry();

    public ICursoPersistente getICursoPersistente();
    public ICursoExecucaoPersistente getICursoExecucaoPersistente();
    
    public IPersistentStudent getIPersistentStudent();
    
    public IDepartamentoPersistente getIDepartamentoPersistente();
    public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente();
    public IPlanoCurricularCursoPersistente getIPlanoCurricularCursoPersistente();
    public IStudentCurricularPlanPersistente getIStudentCurricularPlanPersistente();

	public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate();
	public IPersistentCandidateSituation getIPersistentCandidateSituation();
}
