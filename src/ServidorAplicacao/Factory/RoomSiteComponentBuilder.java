/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoLesson;
import DataBeans.InfoSiteRoomTimeTable;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class RoomSiteComponentBuilder {

	private static RoomSiteComponentBuilder instance = null;

	public RoomSiteComponentBuilder() {
	}

	public static RoomSiteComponentBuilder getInstance() {
		if (instance == null) {
			instance = new RoomSiteComponentBuilder();
		}
		return instance;
	}

	public ISiteComponent getComponent(
		ISiteComponent component,
		IExecutionPeriod executionPeriod,
		ISala room) {

		if (component instanceof InfoSiteRoomTimeTable) {
			return getInfoSiteRoomTimeTable(
				(InfoSiteRoomTimeTable) component,
				executionPeriod,
				room);
		}

		return null;

	}

	private ISiteComponent getInfoSiteRoomTimeTable(
		InfoSiteRoomTimeTable component,
		IExecutionPeriod executionPeriod,
		ISala room) {
		
			List infoAulas = null;

			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();

				IAulaPersistente lessonDAO = sp.getIAulaPersistente();
				List lessonList =
					lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);

				Iterator iterator = lessonList.iterator();
				infoAulas = new ArrayList();
				while (iterator.hasNext()) {
					IAula elem = (IAula) iterator.next();
					InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
					infoAulas.add(infoLesson);
				}
			} catch (ExcepcaoPersistencia ex) {
				ex.printStackTrace();
			}

		component.setInfoLessons(infoAulas);
		component.setInfoRoom(Cloner.copyRoom2InfoRoom(room));
		
		
		return component;
	}

}
