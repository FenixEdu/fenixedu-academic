package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoClass;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 */
public class SelectClasses implements IServico {

	private static SelectClasses _servico = new SelectClasses();

	/**
	  * The actor of this class.
	  **/

	private SelectClasses() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "SelectClasses";
	}

	/**
	 * Returns the _servico.
	 * @return SelectClasses
	 */
	public static SelectClasses getService() {
		return _servico;
	}

	public Object run(InfoClass infoClass) {
		
		List classes = new ArrayList();
		List infoClasses = new ArrayList();
		Turma turma = new Turma();
		Curso lic = new Curso();
		if (infoClass.getInfoLicenciatura()!=null) {
			lic.setSigla(infoClass.getInfoLicenciatura().getSigla());
			lic.setNome(infoClass.getInfoLicenciatura().getNome());
			turma.setLicenciatura(lic);
		}
		
		turma.setNome(infoClass.getNome());
		turma.setSemestre(infoClass.getSemestre());
		turma.setAnoCurricular(infoClass.getAnoCurricular());
		
		
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			classes = sp.getITurmaPersistente().readByCriteria(turma);
          
			for (int i = 0; i < classes.size(); i++) {
				ITurma taux = (ITurma) classes.get(i);
				infoClasses.add(Cloner.copyClass2InfoClass(taux));
			}

		} catch (ExcepcaoPersistencia e) {
			
			e.printStackTrace();
		}

		return infoClasses;

	}

}
