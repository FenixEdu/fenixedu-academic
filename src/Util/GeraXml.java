/*
 * Created on 3/Fev/2004
 */
package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import Dominio.DegreeCurricularPlan;
import Dominio.Enrolment;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.SmartDataSetGeneratorForLEECTestBattery;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class GeraXml
{

    private static final String CC_FILE_PATH = "CurricularCoursesFilePath";

    private static final String BACKUP_DATASET_PROPERTY = "BackupDataSetFilePath";

    private static final String STUDENT_NUMBER_PROPERTY = "StudentNumber";

    private static final String DCP_ID_PROPERTY = "DegreeCurricularPlanID";

    private static final String DATASET_CONFIG = "config/DataSetGeneratorLEEC.properties";

    private static final String PROPERTIES_FILE_PATH = "config/gera.properties";

    private static final String TEST_FILE_PATH = "test.properties";

    private static final String DELIMITADOR = "\t";

    public static void main(String[] args)
    {
        Hashtable dadosConfiguracao = leFicheiroPropriedades();
        String number = args[0];
        String file = args[1];

        System.out.println(number + "-" + file);
        String filePath = (String) dadosConfiguracao.get(CC_FILE_PATH);

        String backupDataSetFilePath = (String) dadosConfiguracao
                .get(BACKUP_DATASET_PROPERTY);
        backupDataSetFilePath = filePath + "/" + number + "/"
                + backupDataSetFilePath;
        String testFileFullPath = filePath + "/" + number + "/"
                + TEST_FILE_PATH;
        Integer studentNumber = null;
        try
        {
            ResourceBundle testProperties = new PropertyResourceBundle(
                    new FileInputStream(testFileFullPath));
            studentNumber = new Integer(testProperties
                    .getString(STUDENT_NUMBER_PROPERTY));
            dadosConfiguracao.put(STUDENT_NUMBER_PROPERTY, studentNumber);

        }
        catch (FileNotFoundException e1)
        {

        }
        catch (IOException e1)
        {

        }

        filePath = filePath + "/" + number + "/" + file + ".txt";

        Integer degreeCurricularPlanID = new Integer((String) dadosConfiguracao
                .get(DCP_ID_PROPERTY));

        ArrayList enrolments = new ArrayList();

        backUpEnrolmentContents(studentNumber, backupDataSetFilePath);
        apagaStudentCurricularPlanEnrolments(studentNumber);
        System.out.println(filePath);
        List curricularCourses = leFicheiro(filePath);
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext())
        {
            String curricularCourseData[] = (String[]) iter.next();
            System.out.println("A Lista tem: " + curricularCourseData[0]);

            IEnrolment enrolment = criaEnrolment(curricularCourseData,
                    studentNumber, degreeCurricularPlanID);
            enrolments.add(enrolment);
        }

        try
        {
            String newFilePath = filePath.replaceFirst(".txt", ".xml");
            SmartDataSetGeneratorForLEECTestBattery dataSetGen = new SmartDataSetGeneratorForLEECTestBattery(
                    DATASET_CONFIG, newFilePath, studentNumber.toString());
            dataSetGen.writeDataSet();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {

            e.printStackTrace();
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        System.out.println("Ja gerei o DataSet");

        apagaEnrolments(enrolments);

        System.out.println("Ja criei os enrolments");

        loadEnrolments(backupDataSetFilePath);

        System.out.println("Ja repus a BD");

    }

    /**
     * @param enrolments
     */
    private static void apagaEnrolments(ArrayList enrolments)
    {
        try
        {
            ISuportePersistente suportePersistente = SuportePersistenteOJB
                    .getInstance();
            IPersistentEnrolment persistentEnrolment = suportePersistente
                    .getIPersistentEnrolment();

            suportePersistente.iniciarTransaccao();

            Iterator iter = enrolments.iterator();
            while (iter.hasNext())
                persistentEnrolment.delete((IEnrolment) iter.next());

            suportePersistente.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param object
     */
    private static IEnrolment criaEnrolment(String[] curricularCourseData,
            Integer studentNumber, Integer degreeCurricularPlanID)
    {
        String curricularCourseName = curricularCourseData[0].toUpperCase();
        IDegreeCurricularPlan degreeCurricularPlan2Read = new DegreeCurricularPlan();
        degreeCurricularPlan2Read.setIdInternal(degreeCurricularPlanID);
        EnrolmentState enrolmentState = EnrolmentState.getEnum("msg."
                + curricularCourseData[1]);
        EnrolmentEvaluationType enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;

        IEnrolment enrolment = null;
        try
        {
            ISuportePersistente suportePersistente = SuportePersistenteOJB
                    .getInstance();

            IPersistentCurricularCourse persistentCurricularCourse = suportePersistente
                    .getIPersistentCurricularCourse();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = suportePersistente
                    .getIPersistentDegreeCurricularPlan();
            IPersistentEnrolment persistentEnrolment = suportePersistente
                    .getIPersistentEnrolment();
            IStudentCurricularPlanPersistente studentCurricularPlanPersistente = suportePersistente
                    .getIStudentCurricularPlanPersistente();
            IPersistentExecutionPeriod persistentExecutionPeriod = suportePersistente
                    .getIPersistentExecutionPeriod();

            suportePersistente.iniciarTransaccao();

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOId(degreeCurricularPlan2Read, false);

            List curricularCourses = persistentCurricularCourse
                    .readbyCourseNameAndDegreeCurricularPlan(
                            curricularCourseName, degreeCurricularPlan);

            ICurricularCourse curricularCourse = null;
            try
            {
                curricularCourse = (ICurricularCourse) curricularCourses.get(0);

                IStudentCurricularPlan studentCurricularPlan = studentCurricularPlanPersistente
                        .readActiveByStudentNumberAndDegreeType(studentNumber,
                                TipoCurso.LICENCIATURA_OBJ);

                IExecutionPeriod executionPeriod = persistentExecutionPeriod
                        .readActualExecutionPeriod();

                enrolment = new Enrolment();
                persistentEnrolment.lockWrite(enrolment);
                enrolment.setCurricularCourse(curricularCourse);
                enrolment.setStudentCurricularPlan(studentCurricularPlan);
                enrolment.setExecutionPeriod(executionPeriod);
                enrolment.setEnrolmentState(enrolmentState);
                enrolment.setEnrolmentEvaluationType(enrolmentEvaluationType);
            }
            catch (RuntimeException e1)
            {
                System.out
                        .println("Disciplina não encontrada no planos curricular: "
                                + curricularCourseName);
            }
            suportePersistente.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
        }

        return enrolment;
    }

    private static void apagaStudentCurricularPlanEnrolments(
            Integer studentNumber)
    {
        try
        {
            ISuportePersistente suportePersistente = SuportePersistenteOJB
                    .getInstance();
            IPersistentEnrolment persistentEnrolment = suportePersistente
                    .getIPersistentEnrolment();
            IStudentCurricularPlanPersistente studentCurricularPlanPersistente = suportePersistente
                    .getIStudentCurricularPlanPersistente();

            suportePersistente.iniciarTransaccao();

            IStudentCurricularPlan studentCurricularPlan = studentCurricularPlanPersistente
                    .readActiveByStudentNumberAndDegreeType(studentNumber,
                            TipoCurso.LICENCIATURA_OBJ);

            List enrolments = persistentEnrolment
                    .readAllByStudentCurricularPlan(studentCurricularPlan);
            Iterator iterador = enrolments.iterator();

            while (iterador.hasNext())
                persistentEnrolment.delete((IEnrolment) iterador.next());

            suportePersistente.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
        }
    }

    private static List leFicheiro(String filePath)
    {
        ArrayList lista = new ArrayList();
        BufferedReader leitura = null;
        String linhaFicheiro = null;

        try
        {
            File file = new File(filePath);
            InputStream ficheiro = new FileInputStream(file);
            leitura = new BufferedReader(new InputStreamReader(ficheiro,
                    "8859_1"), new Long(file.length()).intValue());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        do
        {
            try
            {
                linhaFicheiro = leitura.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            if (linhaFicheiro != null)
            {
                String[] linha = decomporLinha(linhaFicheiro);
                lista.add(linha);
            }
        }
        while ((linhaFicheiro != null));

        return lista;
    }

    /**
     * @param linhaFicheiro
     * @return
     */
    private static String[] decomporLinha(String linhaFicheiro)
    {
        StringTokenizer st = new StringTokenizer(linhaFicheiro, DELIMITADOR);
        String linha[] = {st.nextToken(), st.nextToken()};
        return linha;
    }

    /**
     * @return
     */
    private static Hashtable leFicheiroPropriedades()
    {
        Hashtable dadosConfiguracao = new Hashtable();

        try
        {
            ResourceBundle bundle = new PropertyResourceBundle(
                    new FileInputStream(PROPERTIES_FILE_PATH));

            dadosConfiguracao.put(CC_FILE_PATH, bundle.getString(CC_FILE_PATH));
            dadosConfiguracao.put(BACKUP_DATASET_PROPERTY, bundle
                    .getString(BACKUP_DATASET_PROPERTY));
            dadosConfiguracao.put(DCP_ID_PROPERTY, bundle
                    .getString(DCP_ID_PROPERTY));
        }
        catch (MissingResourceException ex)
        {
            ex.printStackTrace();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File " + PROPERTIES_FILE_PATH + " not found.");
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            System.out.println("IOException reading file "
                    + PROPERTIES_FILE_PATH + ex);
            ex.printStackTrace();
        }

        return dadosConfiguracao;
    }

    private static IDatabaseConnection getConnection() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost/ciapl", "root", "");
        return new DatabaseConnection(jdbcConnection);
    }

    private static void backUpEnrolmentContents(
            Integer studentCurricularPlanID, String backupDataSetFilePath)
    {
        try
        {
            QueryDataSet partialDataSet = new QueryDataSet(getConnection());
            partialDataSet.addTable("ENROLMENT",
                    "SELECT * FROM ENROLMENT WHERE KEY_STUDENT_CURRICULAR_PLAN="
                            + studentCurricularPlanID.intValue());

            FileWriter fileWriter = new FileWriter(new File(
                    backupDataSetFilePath));
            FlatXmlDataSet.write(partialDataSet, fileWriter, "ISO-8859-1");
            fileWriter.close();
        }
        catch (SQLException sqle)
        {
            System.out.println("ERRO: No acesso à Base de Dados");
            sqle.printStackTrace();
        }
        catch (IOException ioe)
        {
            System.out.println("ERRO: A Escrever o ficheiro: "
                    + backupDataSetFilePath);
            ioe.printStackTrace();
        }
        catch (DataSetException dte)
        {
            System.out.println("ERRO: A criar o DataSet");
            dte.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void loadEnrolments(String backupDataSetFilePath)
    {
        try
        {
            FileReader fileReader = new FileReader(new File(
                    backupDataSetFilePath));
            IDataSet dataSet = new FlatXmlDataSet(fileReader);
            DatabaseOperation.DELETE.execute(getConnection(), dataSet);
            DatabaseOperation.INSERT.execute(getConnection(), dataSet);
            fileReader.close();
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println("ERRO: O ficheiro: " + backupDataSetFilePath
                    + " nao foi encontrado");
            fnfe.printStackTrace();
        }

        catch (DataSetException dte)
        {
            System.out.println("ERRO: A criar o DataSet");
            dte.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (DatabaseUnitException due)
        {
            due.printStackTrace();
        }
        catch (SQLException sqle)
        {
            System.out.println("ERRO: No acesso à Base de Dados");
            sqle.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}