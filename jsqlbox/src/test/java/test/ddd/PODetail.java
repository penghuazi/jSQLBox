package test.ddd;

import com.github.drinkjava2.jsqlbox.Dao;
import com.github.drinkjava2.jsqlbox.IEntity;
import com.github.drinkjava2.jsqlbox.SqlHelper;
import com.github.drinkjava2.jsqlbox.id.UUIDGenerator;

public class PODetail implements IEntity {
	public static final String CREATE_SQL = "create table podetail("//
			+ "id varchar(32),"//
			+ "po varchar(32),"//
			+ "partID varchar(32),"//
			+ "poQTY int,"//
			+ "received int,"//
			+ "backOrder int)"//
	;
	String id;
	String po;
	String partID;
	Integer poQTY;
	Integer received;
	Integer backOrder;
	{
		this.box().configIdGenerator("id", UUIDGenerator.INSTANCE);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getPartID() {
		return partID;
	}

	public void setPartID(String partID) {
		this.partID = partID;
	}

	public Integer getPoQTY() {
		return poQTY;
	}

	public void setPoQTY(Integer poQTY) {
		this.poQTY = poQTY;
	}

	public Integer getReceived() {
		return received;
	}

	public void setReceived(Integer received) {
		this.received = received;
	}

	public Integer getBackOrder() {
		return backOrder;
	}

	public void setBackOrder(Integer backOrder) {
		this.backOrder = backOrder;
	}

	public static PODetail create(String po, Part part, Integer poQTY) {
		PODetail poDetail = new PODetail();
		poDetail.po = po;
		poDetail.partID = part.getPartID();
		poDetail.poQTY = poQTY;
		poDetail.received = 0;
		poDetail.backOrder = poQTY;
		poDetail.insert();
		return poDetail;
	}

	public void trigger_calculateBackorder() {
		this.backOrder = poQTY - received;
		Part part = Dao.load(Part.class, this.getPartID());
	}

	public static void onChange_backorder(PODetail poDetail) {
		Part part = Dao.load(Part.class, poDetail.getPartID());
		part.setPendingPOs(Dao.queryForInteger(
				"select sum(backOrder) from podetail where partID= " + SqlHelper.q(poDetail.getPartID())));
	}

}