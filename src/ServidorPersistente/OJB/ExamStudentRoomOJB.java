/*
 * Created on 30/Mai/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExamStudentRoom;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.ISala;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamStudentRoom;

/**
 * @author João Mota
 *
 */
public class ExamStudentRoomOJB extends ObjectFenixOJB implements IPersistentExamStudentRoom{

	public List readBy(IExam exam) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExam", exam.getIdInternal());
		return queryList(ExamStudentRoom.class, criteria);

	}

	public List readBy(IStudent student) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(ExamStudentRoom.class, criteria);
	}

	public List readBy(ISala room) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyRoom", room.getIdInternal());
		return queryList(ExamStudentRoom.class, criteria);
	}

	public List readBy(IExam exam, ISala room) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExam", exam.getIdInternal());
		criteria.addEqualTo("keyRoom", room.getIdInternal());
		return queryList(ExamStudentRoom.class, criteria);
	}

	public List readBy(IExam exam, IStudent student)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExam", exam.getIdInternal());
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(ExamStudentRoom.class, criteria);
	}

	public List readBy(IStudent student, ISala room)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyRoom", room.getIdInternal());
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return queryList(ExamStudentRoom.class, criteria);
	}

	public ExamStudentRoom readBy(IExam exam, IStudent student, ISala room)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExam", exam.getIdInternal());
		criteria.addEqualTo("keyRoom", room.getIdInternal());
		criteria.addEqualTo("keyStudent", student.getIdInternal());
		return (ExamStudentRoom) queryObject(ExamStudentRoom.class, criteria);
	}

	public void delete(IExamStudentRoom examStudentRoom)
		throws ExcepcaoPersistencia {
		super.delete(examStudentRoom);
	}

	public void deleteByCriteria(IExamStudentRoom examStudentRoom)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		if (examStudentRoom.getExam() != null) {
			criteria.addEqualTo(
				"keyExam",
				examStudentRoom.getExam().getIdInternal());
		}
		if (examStudentRoom.getRoom() != null) {
			criteria.addEqualTo(
				"keyRoom",
				examStudentRoom.getRoom().getIdInternal());
		}
		if (examStudentRoom.getStudent() != null) {
			criteria.addEqualTo(
				"keyStudent",
				examStudentRoom.getStudent().getIdInternal());
		}
		List toDeleteList = queryList(ExamStudentRoom.class, criteria);
		Iterator iter = toDeleteList.iterator();
		while (iter.hasNext()) {
			super.delete(iter.next());
		}
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + ExamStudentRoom.class.getName();
		super.deleteAll(oqlQuery);
	}

	
}
