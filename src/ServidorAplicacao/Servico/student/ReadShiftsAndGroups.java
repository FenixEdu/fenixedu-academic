/*
 * Created on 28/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.ISiteComponent;
import DataBeans.InfoShift;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteShift;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.InfoStudentGroup;
import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class ReadShiftsAndGroups implements IServico {

    private static ReadShiftsAndGroups service = new ReadShiftsAndGroups();

    /**
     * The singleton access method of this class.
     */
    public static ReadShiftsAndGroups getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private ReadShiftsAndGroups() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "ReadShiftsAndGroups";
    }

    /**
     * Executes the service.
     */
    public ISiteComponent run(Integer groupPropertiesCode) throws FenixServiceException {

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();
        List infoSiteShiftsAndGroupsList = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();

            IGroupProperties groupProperties = (IGroupProperties) sp.getIPersistentGroupProperties()
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            List allShifts = persistentShift.readByExecutionCourseAndType(groupProperties
                    .getExecutionCourse(), groupProperties.getShiftType().getTipo());

            List allStudentsGroup = sp.getIPersistentStudentGroup()
                    .readAllStudentGroupByGroupProperties(groupProperties);

            if (allStudentsGroup.size() != 0) {

                Iterator iterator = allStudentsGroup.iterator();
                while (iterator.hasNext()) {
                    ITurno shift = ((IStudentGroup) iterator.next()).getShift();
                    if (!allShifts.contains(shift)) {
                        allShifts.add(shift);

                    }
                }
            }

            if (allShifts.size() != 0) {
                Iterator iter = allShifts.iterator();
                infoSiteShiftsAndGroupsList = new ArrayList();
                InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                InfoSiteShift infoSiteShift = null;

                while (iter.hasNext()) {

                    ITurno shift = (ITurno) iter.next();
                    List allStudentGroups = persistentStudentGroup
                            .readAllStudentGroupByGroupPropertiesAndShift(groupProperties, shift);

                    infoSiteShift = new InfoSiteShift();
                    infoSiteShift.setInfoShift((InfoShift) Cloner.get(shift));

                    if (groupProperties.getGroupMaximumNumber() != null) {

                        int vagas = groupProperties.getGroupMaximumNumber().intValue()
                                - allStudentGroups.size();
                        //if (vagas >= 0)
                        infoSiteShift.setNrOfGroups(new Integer(vagas));
                        //else
                        //	infoSiteShift.setNrOfGroups(new Integer(0));
                    } else
                        infoSiteShift.setNrOfGroups("Sem limite");

                    infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                    infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

                    List infoSiteStudentGroupsList = new ArrayList();
                    if (allStudentGroups.size() != 0) {
                        infoSiteStudentGroupsList = new ArrayList();
                        Iterator iterGroups = allStudentGroups.iterator();

                        while (iterGroups.hasNext()) {
                            InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                            InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                            infoStudentGroup = Cloner
                                    .copyIStudentGroup2InfoStudentGroup((IStudentGroup) iterGroups
                                            .next());
                            infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                            infoSiteStudentGroupsList.add(infoSiteStudentGroup);

                            //#####################################
                            //WHO FORGOT THIS?
                            //ass: BlueBus

                            IPersistentStudentGroupAttend persistentStudentGroupAttend = sp
                                    .getIPersistentStudentGroupAttend();
                            List groupAttends = persistentStudentGroupAttend
                                    .readByStudentGroupId(infoSiteStudentGroup.getInfoStudentGroup()
                                            .getIdInternal());

                            List studentInformation = new ArrayList();
                            for (Iterator attendIterator = groupAttends.iterator(); attendIterator
                                    .hasNext();) {
                                IStudentGroupAttend element = (IStudentGroupAttend) attendIterator
                                        .next();
                                IStudent student = element.getAttend().getAluno();

                                InfoSiteStudentInformation infoSiteStudentInformation = new InfoSiteStudentInformation();
                                infoSiteStudentInformation.setEmail(student.getPerson().getEmail());
                                infoSiteStudentInformation.setName(student.getPerson().getNome());
                                infoSiteStudentInformation.setNumber(student.getNumber());
                                infoSiteStudentInformation
                                        .setUsername(student.getPerson().getUsername());
                                studentInformation.add(infoSiteStudentInformation);

                            }

                            infoSiteStudentGroup.setInfoSiteStudentInformationList(studentInformation);

                            //#####################################

                        }
                        Collections.sort(infoSiteStudentGroupsList, new BeanComparator(
                                "infoStudentGroup.groupNumber"));

                    }

                    infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                    infoSiteShiftsAndGroupsList.add(infoSiteGroupsByShift);
                }
                /* Sort the list of shifts */

                ComparatorChain chainComparator = new ComparatorChain();
                chainComparator.addComparator(new BeanComparator("infoSiteShift.infoShift.tipo"));
                chainComparator.addComparator(new BeanComparator("infoSiteShift.infoShift.nome"));

                Collections.sort(infoSiteShiftsAndGroupsList, chainComparator);

            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadShiftsAndGroups");
        }
        infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroupsList);

        return infoSiteShiftsAndGroups;
    }
}