package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Dominio.CurricularCourse;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionCourse;
import Dominio.Enrolment;
import Dominio.Frequenta;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import Dominio.ShiftStudent;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import Dominio.Turno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

public class LoadCourseEnrolments extends DataFileLoader
{

    // Lista de inscrições de alunos em disciplinas curriculares.
    private static ArrayList listaInscricoes = new ArrayList();
    private static ArrayList listaInscricoesFinal = new ArrayList();
    private LoadCourseEnrolments() throws IOException
    {
        super();
    }

    public static void main(String[] args)
    {

        try
        {
            new LoadCourseEnrolments();
        } catch (IOException e)
        {
            e.printStackTrace(System.out);
            System.out.println("Erro a carregar as propriedades");
        }

        try
        {
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

            // Obter interfaces de acesso ao suporte persistente.
            IPersistentStudent iAlunoPersistente = sp.getIPersistentStudent();
            //			IPersistentCurricularCourse iDisciplinaCurricularPersistente =
            //				sp.getIPersistentCurricularCourse();
            IPersistentExecutionCourse iDisciplinaExecucaoPersistente =
                sp.getIPersistentExecutionCourse();
            IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
            IStudentCurricularPlanPersistente studentCurricularPlanPersistente =
                sp.getIStudentCurricularPlanPersistente();
            IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();

            ITurnoAlunoPersistente shiftStudentDAO = sp.getITurnoAlunoPersistente();

            // Ler o ficheiro de entrada.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getDataFilePath()));
            String line = bufferedReader.readLine();
            while (line != null)
            {
                carregarInscricao(line);
                line = bufferedReader.readLine();
            }

            sp.iniciarTransaccao();

            // Percorrer a lista que vem da secretaria.
            // Vamos inscrever todos os alunos.
            listaInscricoesFinal = new ArrayList();
            for (int i = 0; i < listaInscricoes.size(); i++)
            {
                // Para cada inscricao na secretaria
                AlunoDisciplinaCurricular alunoDisciplinaCurricular =
                    (AlunoDisciplinaCurricular) listaInscricoes.get(i);

                // Ler o aluno
                IStudent studentCriteria = new Student();
                studentCriteria.setNumber(alunoDisciplinaCurricular.getNumeroAluno());
                studentCriteria.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
                IStudent student =
                    (IStudent) iAlunoPersistente.readDomainObjectByCriteria(studentCriteria);
                // Ler o plano curricular do aluno
                IStudentCurricularPlan studentCurricularPlanCriteria = new StudentCurricularPlan();
                studentCurricularPlanCriteria.setStudent(studentCriteria);
                studentCurricularPlanCriteria.setCurrentState(
                    new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
                IStudentCurricularPlan studentCurricularPlan =
                    (
                        IStudentCurricularPlan) studentCurricularPlanPersistente
                            .readDomainObjectByCriteria(
                        studentCurricularPlanCriteria);

                // Ler a disciplina em que o aluno se está a inscreveer
                ICurricularCourse curricularCourseCriteria = new CurricularCourse();
                //curricularCourseCriteria.setName(alunoDisciplinaCurricular.getNomeDisciplinaCurricular());
                curricularCourseCriteria.setCode(
                    alunoDisciplinaCurricular.getSiglaDisciplinaCurricular());
                //				ICurricularCourse curricularCourse =
                //					(
                //						ICurricularCourse) iDisciplinaCurricularPersistente
                //							.readDomainObjectByCriteria(
                //						curricularCourseCriteria);

                // Ver se a inscricao do aluno na disciplina está na base de dados do CIAPL
                IEnrolment enrolmentCriteria = new Enrolment();
                //				enrolmentCriteria.setCurricularCourse(curricularCourseCriteria);
                enrolmentCriteria.setStudentCurricularPlan(studentCurricularPlanCriteria);
                IEnrolment enrolment =
                    (IEnrolment) persistentEnrolment.readDomainObjectByCriteria(enrolmentCriteria);
                if (enrolment == null)
                {
                    // A inscricao nao esta na base de dados do CIAPL
                    // Adicionar a inscricao do aluno a base de dados CIAPL
                    enrolment = new Enrolment();
                    //					enrolment.setCurricularCourse(curricularCourse);
                    enrolment.setStudentCurricularPlan(studentCurricularPlan);
                    persistentEnrolment.lockWrite(enrolment);

                    // Ver se existe o frequenta
                    IFrequenta frequentaCriteria = new Frequenta();
                    frequentaCriteria.setAluno(studentCriteria);
                    IExecutionCourse disciplinaExecucaoCriteria = new ExecutionCourse();
                    disciplinaExecucaoCriteria.setNome(
                        alunoDisciplinaCurricular.getNomeDisciplinaCurricular());
                    frequentaCriteria.setDisciplinaExecucao(disciplinaExecucaoCriteria);
                    IFrequenta frequenta =
                        (IFrequenta) frequentaPersistente.readDomainObjectByCriteria(frequentaCriteria);

                    // Se o frequenta ainda nao esta na base de dados
                    if (frequenta == null)
                    {
                        //Adicionar o frequenta equivalente a inscricao
                        IExecutionCourse disciplinaExecucao =
                            (
                                IExecutionCourse) iDisciplinaExecucaoPersistente
                                    .readDomainObjectByCriteria(
                                disciplinaExecucaoCriteria);
                        if (disciplinaExecucao != null)
                        {
                            frequenta.setDisciplinaExecucao(disciplinaExecucao);
                            frequenta.setAluno(student);
                            frequentaPersistente.lockWrite(frequenta);
                        } else
                        {
                            //							System.out.println(
                            //								"Nao foi encontrado a disciplina execucao equivalente da disciplina curricular: "
                            //									+ alunoDisciplinaCurricular
                            //										.getNomeDisciplinaCurricular());
                        }
                    }

                } else
                {
                    // A inscricao ja esta na base de dados do CIAPL
                    // Nao se faz nada
                }

                // Construcao da lista de inscricoes com objectos do dominio
                listaInscricoesFinal.add(enrolment);

            } // for listaInscricoes secretaria

            sp.confirmarTransaccao();

            sp.iniciarTransaccao();

            // Ler a lista de inscricoes da base de dados CIAPL
            IEnrolment enrolmentCriteria = new Enrolment();
            List listEnrolmentsCiapl = persistentEnrolment.readByCriteria(enrolmentCriteria);

            sp.confirmarTransaccao();

            sp.iniciarTransaccao();

            // Percorrer a lista
            for (int j = 0; j < listEnrolmentsCiapl.size(); j++)
            {
                // Para cada inscricao
                IEnrolment enrolment = (IEnrolment) listEnrolmentsCiapl.get(j);

                if (!listaInscricoesFinal.contains(enrolment))
                {
                    // Se a inscricao nao estiver na lista final e preciso remove-la da base de dados.

                    // Ler o Frequenta equivalente à inscricao
                    IFrequenta attendCriteria = new Frequenta();

                    ICurricularCourse curricularCourse =
                        enrolment.getCurricularCourse();

                    IExecutionCourse executionCourse =
                        iDisciplinaExecucaoPersistente.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
                            curricularCourse.getCode(),
                            "2002/2003",
                            curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
                    if (executionCourse != null)
                    {
                        attendCriteria.setAluno(enrolment.getStudentCurricularPlan().getStudent());

                        attendCriteria.setDisciplinaExecucao(executionCourse);

                        IFrequenta persistentAttend =
                            (IFrequenta) frequentaPersistente.readDomainObjectByCriteria(attendCriteria);

                        // Ler os TurnoAlunos associados ao frequenta
                        ITurnoAluno shiftStudentCriteria = new ShiftStudent();

                        ITurno shiftCriteria = new Turno();
                        shiftCriteria.setDisciplinaExecucao(executionCourse);

                        shiftStudentCriteria.setStudent(
                            enrolment.getStudentCurricularPlan().getStudent());
                        shiftStudentCriteria.setShift(shiftCriteria);

                        // Apagar os TurnoAlunos equivalentes
                        shiftStudentDAO.deleteByCriteria(shiftStudentCriteria);

                        // Apagar o Frequenta equivalente
                        frequentaPersistente.delete(persistentAttend);

                        // Apagar a Inscricao
                        persistentEnrolment.delete(enrolment);
                    } else
                    {
                        //						System.out.println(
                        //							"A disciplina "
                        //								+ curricularCourse.getCode()
                        //								+ "-"
                        //								+ curricularCourse.getName()
                        //								+ " na licenciatura "
                        //								+ curricularCourse
                        //									.getDegreeCurricularPlan()
                        //									.getCurso()
                        //									.getSigla()
                        //								+ " não tem execução!");

                    }
                }

            }
            sp.confirmarTransaccao();

        } catch (Exception e)
        {
            System.out.println("ATENCAO: LoadCourseEnrolments: " + e.toString());

            try
            {
                SuportePersistenteOJB.getInstance().cancelarTransaccao();
            } catch (Exception x)
            {
                System.out.println(
                    "ATENCAO: LoadCourseEnrolments: cancelarTransacccao: " + e.toString());
            }

        }
    }

    private static void carregarInscricao(String line) throws ExcepcaoPersistencia
    {
        StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());
        // Obter os dados da disciplina.
        String numeroAluno = stringTokenizer.nextToken();
        //ano curricular
        stringTokenizer.nextToken();
        //semestre
        stringTokenizer.nextToken();
        String codigoDisciplinaCurricular = stringTokenizer.nextToken();
        stringTokenizer.nextToken(); // ignora campo
        String chaveLicenciatura = stringTokenizer.nextToken();
        // ramo
        stringTokenizer.nextToken();
        // Obter interfaces de acesso ao suporte persistente.

        SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

        IPersistentCurricularCourse iDisciplinaCurricularPersistente =
            sp.getIPersistentCurricularCourse();

        // Verifica o ramo porque pode vir a null.
        //        int intChaveRamo;
        //        try {
        //            intChaveRamo = Integer.parseInt(chaveRamo.trim());
        //        }
        //        catch(Exception e) {
        //            intChaveRamo = 0;
        //        }
        //CurricularCourse disciplinaCurricular = iDisciplinaCurricularPersistente.lerDisciplinaCurricular(codigoDisciplinaCurricular.trim(), Integer.parseInt(anoCurricular.trim()), Integer.parseInt(semestre.trim()), Integer.parseInt(chaveLicenciatura.trim()), intChaveRamo);
        ICurricularCourse disciplinaCurricularTemp = new CurricularCourse();
        //		disciplinaCurricularTemp.setCurricularYear(
        //			new Integer(anoCurricular.trim()));
        //		disciplinaCurricularTemp.setSemester(new Integer(semestre.trim()));
        disciplinaCurricularTemp.setCode(codigoDisciplinaCurricular.trim());
        IDegreeCurricularPlan planoCurricularCursoTemp = new DegreeCurricularPlan();
        ICurso curso = new Curso();
        curso.setSigla(chaveLicenciatura.trim());
        planoCurricularCursoTemp.setDegree(curso);
        disciplinaCurricularTemp.setDegreeCurricularPlan(planoCurricularCursoTemp);
        sp.iniciarTransaccao();
        ICurricularCourse disciplinaCurricular =
            (ICurricularCourse) iDisciplinaCurricularPersistente.readDomainObjectByCriteria(
                disciplinaCurricularTemp);
        sp.confirmarTransaccao();
        if (disciplinaCurricular != null)
        {
            listaInscricoes.add(
                new AlunoDisciplinaCurricular(
                    new Integer(numeroAluno.trim()),
                    disciplinaCurricular.getCode(),
                    disciplinaCurricular.getName()));
            //disciplinaCurricular.codigoInterno()));
        } else
        {
            // No caso de não encontrar a disciplina curricular, então inscreve o aluno na primeira disciplina curricular que tenha o codigo igual ao recebido.
            //ArrayList listaDisciplinasCurriculares = iDisciplinaCurricularPersistente.lerDisciplinasCurricularesPorCodigo(codigoDisciplinaCurricular.trim());
            disciplinaCurricularTemp = new CurricularCourse();
            disciplinaCurricularTemp.setCode(codigoDisciplinaCurricular.trim());
            sp.iniciarTransaccao();
            List listaDisciplinasCurriculares =
                iDisciplinaCurricularPersistente.readByCriteria(disciplinaCurricularTemp);
            sp.confirmarTransaccao();
            if (listaDisciplinasCurriculares != null && !listaDisciplinasCurriculares.isEmpty())
            {
                disciplinaCurricular = (CurricularCourse) listaDisciplinasCurriculares.get(0);
                listaInscricoes.add(
                    new AlunoDisciplinaCurricular(
                        new Integer(numeroAluno.trim()),
                        disciplinaCurricular.getCode(),
                        disciplinaCurricular.getName()));
            } else
            {
                // Caso em que a disciplina não existe.
                //System.out.println("-> " + codigoDisciplinaCurricular);
            }
        }
    }
}