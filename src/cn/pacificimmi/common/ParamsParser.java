package cn.pacificimmi.common;

import com.jfinal.core.Controller;

import cn.pacificimmi.common.utils.StringUtil;

public class ParamsParser {
	/**
	 * controller实例对象
	 */
	protected Controller ctr=null;
	
	/***
	 * 传递过来的id参数
	 */
	private String ids="";
	
	/**
	 * 对象唯一id
	 */
	private String id="";
	/***
	 * 选择数据列Sql字符串
	 */
	private String selectStr="";
	
	/***
	 * 查表、条件及排序语句
	 */
	private String fromStr="";
	
	/***
	 * 查询条件Sql字符串
	 */
	private String whereStr="";
	
	/***
	 * 排序条件字符串
	 */
	private String orderStr="";
	
	/***
	 * 默认页码
	 */
	private int pageNum = 1;
	
	/**
	 * 每页条数
	 */
	private int pageSize = 15;

	private String defaultOrderStr="";
	
	
	/**
	 * 解析页码
	 * @param ct
	 */
	public ParamsParser(Controller ct){
		/***
		 * 获取页码
		 */
		ctr= ct;
		String page= ctr.getPara("page");
		if(page!=null && StringUtil.isNumeric(page)){
			this.pageNum=Integer.valueOf(page)>0?Integer.valueOf(page):1;
		}
		if(ctr.getPara("ids")!=null && StringUtil.isNumericList(ctr.getPara("ids")))
			ids = ctr.getPara("ids");
		
		String oid;
		oid = ctr.getPara("id");
		if(oid!=null && !oid.isEmpty() && StringUtil.isNumeric(oid))
			id=oid;
		oid= ctr.getPara(0);
		if(oid!=null && !oid.isEmpty() && StringUtil.isNumeric(oid))
			id=oid;
		
		String field = this.getFieldName("field");
		String way = this.getNormStr("way");
		if(field!=null && way!=null){
			ctr.setAttr("field", field);
			ctr.setAttr("way", way);
			this.addOrderSegment(field, way);
		}
	}
	
	/**
	 * 增加条件片段，简单地and方式连接
	 * @param segment
	 */
	public void addWhereSegmentByAnd(String segment){
		if(this.whereStr.isEmpty()){
			whereStr = " where "+segment+" ";
		}
		else{
			whereStr += " and ";
			whereStr +=segment;
		}
	}
	
	/**
	 * 增加条件片段，简单地or方式连接
	 * @param segment
	 */
	public void addWhereSegmentByOr(String segment){
		if(this.whereStr.isEmpty()){
			whereStr = " where "+segment+" ";
		}
		else{
			whereStr += " or ";
			whereStr +=segment;
		}
	}
	
	/**
	 * 增加排序方式片段
	 * @param field
	 * @param sortway
	 */
	public void addOrderSegment(String field,String sortway){
		if(this.orderStr.isEmpty()){
			orderStr += " order by ";
			orderStr += field;
			orderStr += " ";
			orderStr += sortway;
		}
		else{
			orderStr += " ,";
			orderStr += field;
			orderStr += " ";
			orderStr += sortway;
		}
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getWhereStr() {
		return whereStr;
	}

	/**
	 * 直接赋值where语句
	 * @param whereStr
	 */
	public void setWhereStr(String whereStr) {
		this.whereStr = whereStr;
	}

	public String getOrderStr() {
		return orderStr;
	}

	/**
	 * 直接赋值order语句
	 * @param orderStr
	 */
	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSelectStr() {
		return selectStr;
	}

	public void setSelectStr(String selectStr) {
		this.selectStr = selectStr;
	}

	public String getFromStr() {
		fromStr +=" ";
		fromStr += whereStr;
		fromStr += " ";
		if(orderStr.isEmpty())
			fromStr += defaultOrderStr;
		else
			fromStr += orderStr;
		return fromStr;
	}

	public void setFromStr(String fromStr) {
		this.fromStr = fromStr;
	}

	public String getIds() {
		if(ids.isEmpty())
			return null;
		else
			return ids;
	}
	
	public String getAllStr(String name){
		String value = ctr.getPara(name);
		if(value!=null && !value.isEmpty())
			return value;
		else
			return null;
	}
	
	public String getNormStr(String name){
		String value = ctr.getPara(name);
		if(value!=null && !value.isEmpty() && StringUtil.validateSearchKey(value))
			return value;
		else
			return null;
	}
	
	public String getChinseWords(String name){
		String value = ctr.getPara(name);
		if(value!=null && !value.isEmpty() && StringUtil.validateChinese(value))
			return value;
		else
			return null;
	}
	
	public String[] getIntegers(String name){
		String[] value = ctr.getParaValues(name);
		if(value!=null && value.length>0 && StringUtil.validateIntegerStringArray(value))
			return value;
		else
			return null;
	}
	
	public String getFieldName(String name){
		String value = ctr.getPara(name);
		if(value!=null && !value.isEmpty() && StringUtil.validateFieldName(name))
			return value;
		else
			return null;
	}
	
	public String getInt(String name){
		String value = ctr.getPara(name);
		if(value!=null && !value.isEmpty() && StringUtil.isNumeric(value))
			return value;
		else
			return null;
	}

	public String getDefaultOrderStr() {
		return defaultOrderStr;
	}

	public void setDefaultOrderStr(String defaultOrderStr) {
		this.defaultOrderStr = defaultOrderStr;
	}

	public String getId() {
		if(!id.isEmpty())
			return id;
		else
			return null;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
