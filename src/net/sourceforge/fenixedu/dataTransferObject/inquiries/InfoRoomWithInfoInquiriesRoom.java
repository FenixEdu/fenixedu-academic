/*
 * Created on 29/Abr/2005 - 12:04:24
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.IRoom;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoRoomWithInfoInquiriesRoom extends InfoRoom {
	
//	private boolean alreadyEvaluatedFlag = false;
	private InfoInquiriesRoom inquiriesRoom;

	/**
	 * @return Returns the alreadyEvaluatedFlag.
	 */
//	public boolean isAlreadyEvaluatedFlag() {
//		return alreadyEvaluatedFlag;
//	}
	

	/**
	 * @param alreadyEvaluatedFlag The alreadyEvaluatedFlag to set.
	 */
//	public void setAlreadyEvaluatedFlag(boolean alreadyEvaluatedFlag) {
//		this.alreadyEvaluatedFlag = alreadyEvaluatedFlag;
//	}
	
	
	
	
	public void copyFromDomain(IRoom room) {
		super.copyFromDomain(room);
	}
	
    public static InfoRoomWithInfoInquiriesRoom newInfoFromDomain(IRoom room) {
		InfoRoomWithInfoInquiriesRoom infoRoom = null;
        if (room != null) {
            infoRoom = new InfoRoomWithInfoInquiriesRoom();
            infoRoom.copyFromDomain(room);
        }
        return infoRoom;
    }
	public InfoRoomWithInfoInquiriesRoom() {
		
	}
	
	public InfoRoomWithInfoInquiriesRoom(InfoRoom ir) {
		this.setIdInternal(ir.getIdInternal());
	    this.setNome(ir.getNome());
		this.setEdificio(ir.getEdificio());
		this.setPiso(ir.getPiso());
		this.setCapacidadeNormal(ir.getCapacidadeNormal());
		this.setCapacidadeExame(ir.getCapacidadeExame());
		this.setTipo(ir.getTipo());
	}

	/**
	 * @return Returns the inquiriesRoom.
	 */
	public InfoInquiriesRoom getInquiriesRoom() {
		return inquiriesRoom;
	}
	

	/**
	 * @param inquiriesRoom The inquiriesRoom to set.
	 */
	public void setInquiriesRoom(InfoInquiriesRoom inquiriesRoom) {
		this.inquiriesRoom = inquiriesRoom;
	}
	


}
