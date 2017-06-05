package cn.pacificimmi.common.tree;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class Node {
	
	/***
	 * 节点id
	 */
	private String id="000000000000000000";
	
	/***
	 * 节点名称
	 */
	private String name="";
	
	/***
	 * 节点编码
	 */
	private String code="0000";
	
	/***
	 * 是否展开
	 */
	private boolean isOpen=false;
	
	/***
	 * 当前节点是否被选中
	 */
	private boolean isSelected=false;
	
	/**
	 * 当前节点深度
	 */
	private int depth=0;
	
	/***
	 * 是否显示树图编辑按钮
	 */
	private boolean allownEdit=true;
	
	/***
	 * 是否显示check按钮
	 */
	private boolean allownCheck=false;
	
	/**
	 * 额外扩充信息
	 */
	private String extInfo="";
	
	/**
	 * 是否显示增加按钮
	 */
	private boolean allownAdd=true;
	
	/**
	 * 是否显示修改按钮
	 */
	private boolean allownUpdate=true;
	
	/**
	 * 是否显示删除按钮
	 */
	private boolean allownDel=true;
	
	/**
	 * checkbox name
	 */
	private String checkboxName="chks";
	
	private Hashtable<String,Node> subNodes = new Hashtable<String,Node>();
	
	
	public boolean isAllownEdit() {
		return allownEdit;
	}

	public void setAllownEdit(boolean allownEdit) {
		this.allownEdit = allownEdit;
	}

	public void addSubNode(Node node){
		subNodes.put(node.getCode(), node);
	}

	public Node getSubNode(String code){
		return subNodes.get(code);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		this.depth = getCodeTips().size();
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Hashtable<String, Node> getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(Hashtable<String, Node> subNodes) {
		this.subNodes = subNodes;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAllownCheck() {
		return allownCheck;
	}

	public void setAllownCheck(boolean allownCheck) {
		this.allownCheck = allownCheck;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	
	public boolean isAllownAdd() {
		return allownAdd;
	}

	public void setAllownAdd(boolean allownAdd) {
		this.allownAdd = allownAdd;
	}

	public boolean isAllownUpdate() {
		return allownUpdate;
	}

	public void setAllownUpdate(boolean allownUpdate) {
		this.allownUpdate = allownUpdate;
	}

	public boolean isAllownDel() {
		return allownDel;
	}

	public void setAllownDel(boolean allownDel) {
		this.allownDel = allownDel;
	}

	public ArrayList<String> getCodeTips(){
		ArrayList<String> tips = new ArrayList<String>();
		String tip="";
		for(int i=0;i<code.length();i++){
			if(i>0 && (i+1)%4==0){
				tip += code.charAt(i);
				tips.add(tip);
			}
			else
				tip += code.charAt(i);
		}
		
		return tips;
	}
	
	private String getEditCode(boolean isLeaf,Node node){
		StringBuffer bf = new StringBuffer();
		if(isLeaf){
			if(node.isAllownAdd())
				bf.append("<i class=\"icon-plus hor-left  popovers node\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"在当前节点下新增子节点!\" data-original-title=\"新增\" opt-type=\"add\" ></i>\n");
			if(node.isAllownUpdate())
				bf.append("<i class=\"icon-pencil  hor-left  popovers node\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"修改当前节点!\" data-original-title=\"编辑\" opt-type=\"edit\" ></i>\n");
			if(node.isAllownDel())
				bf.append("<i class=\"icon-remove  hor-left  popovers node\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"删除当前节点!\" data-original-title=\"删除\" opt-type=\"del\" ></i>\n");
		}
		else{
			if(node.isAllownAdd())
				bf.append("<i class=\"icon-plus hor-left  popovers node\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"在当前节点下新增子节点!\" data-original-title=\"新增\" opt-type=\"add\" ></i>\n");
			if(node.isAllownUpdate())
				bf.append("<i class=\"icon-pencil  hor-left  popovers node\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"修改当前节点!\" data-original-title=\"编辑\" opt-type=\"edit\" ></i>\n");
		}
		return bf.toString();
	}
	
	public String getNodeString(Node node){
		String checkboxShow = "";
		if(node.isAllownCheck()){
			if(node.isSelected())
				checkboxShow = "<input name=\""+node.getCheckboxName()+"\" type=\"checkbox\" value=\""+node.getId()+"\" checked />\n";
			else
				checkboxShow = "<input name=\""+node.getCheckboxName()+"\" type=\"checkbox\" value=\""+node.getId()+"\" />\n";
		}
		StringBuffer bf = new StringBuffer();
		bf.append("<li>");
		if(node.subNodes.size()==0){
			String nodeName=node.getName();
			if(node.isSelected())
				nodeName = "<b><font color=\"#ff0000\">"+nodeName+"</font></b>\n";
			String title="<a data-role=\"leaf\" data-info=\""+node.getExtInfo()+"\" data-value=\""+node.getCode()+"\">"+checkboxShow+"<span class=\"split-right\"><i class=\"icon-chevron-right\"></i>"+nodeName+"</span>\n";
			if(node.isAllownEdit())
				bf.append(title).append(getEditCode(true,node));
			else
				bf.append(title);
			bf.append("</a>\n");
		}
		else{
			String closed="";
			if(!node.isOpen())
				closed="closed";
			String nodeName=node.getName();
			if(node.isSelected())
				nodeName = "<b><font color=\"#ff0000\">"+nodeName+"</font></b>\n";
			
			String title="<a class=\"tree-toggle "+closed+"\"  data-info=\""+node.getExtInfo()+"\" data-toggle=\"branch\" data-value=\""+node.getCode()+"\">"+checkboxShow+"<span class=\"split-right\">"+nodeName+"</span>\n";
			if(node.isAllownEdit())
				bf.append(title).append(getEditCode(false,node));
			else
				bf.append(title);
			bf.append("</a>\n");
			
			if(node.isOpen())
				bf.append("<ul class=\"branch in\">\n");
			else
				bf.append("<ul class=\"branch\">\n");
			Hashtable<String,Node> subNodes = node.getSubNodes();
			Iterator<String> iter = subNodes.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				Node sn = subNodes.get(key);
				bf.append(getNodeString(sn));
			}
			bf.append("</ul>\n");
		}
		bf.append("</li>\n");
		return bf.toString();
	}

	public String getCheckboxName() {
		return checkboxName;
	}

	public void setCheckboxName(String checkboxName) {
		this.checkboxName = checkboxName;
	}
	
	
}
