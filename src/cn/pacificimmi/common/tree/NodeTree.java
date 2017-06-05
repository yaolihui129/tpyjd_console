package cn.pacificimmi.common.tree;

import java.util.ArrayList;
import java.util.List;

public class NodeTree {
	/**
	 * 树的根节点
	 */
	private Node root;
	
	/***
	 * 树的展开深度
	 */
	private int limitDepth=1;
	
	/***
	 * 树中被选中的节点编码
	 */
	private List<String> selectedCodes=new ArrayList<String>();
	
	/***
	 * 树在html页面中的id
	 */
	private String treeId="";
	
	/***
	 * 开启选择模式
	 */
	private boolean selectMode=false;
	
	/**
	 * 是否允许编辑
	 */
	private boolean allownEdit=true;
	
	
	public NodeTree(String treeid,String name,int limit){
		root = new Node();
		root.setName(name);
		root.setOpen(true);
		treeId=treeid;
		limitDepth=limit;
	}
	
	
	public String getTreeId() {
		return treeId;
	}


	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}


	public int getLimitDepth() {
		return limitDepth;
	}


	public void setLimitDepth(int limitDepth) {
		this.limitDepth = limitDepth;
	}


	public List<String> getSelectedCodes() {
		return selectedCodes;
	}


	public void setSelectedCodes(List<String> selectedCodes) {
		this.selectedCodes = selectedCodes;
	}


	public boolean isSelectMode() {
		return selectMode;
	}


	public void setSelectMode(boolean selectMode) {
		this.selectMode = selectMode;
	}


	//增加节点
	public void addNode(Node node){
		for(String sc:selectedCodes){
			if(sc.equals(node.getCode()) ||sc.equals(node.getId()))
				node.setSelected(true);
			if(sc.startsWith(node.getCode()))
				node.setOpen(true);
		}
		node.setAllownCheck(selectMode);
		node.setAllownEdit(allownEdit);
		Node prtNode = findParentNode(node);
		
		if(prtNode!=null){
			//node.setDepth(prtNode.getDepth()+1);
			prtNode.addSubNode(node);
			if(prtNode.getDepth()<=limitDepth){
				prtNode.setOpen(true);
			}
		}
	}
	
	/**
	 * 找到当前节点的父节点
	 * @param node
	 * @return
	 */
	private Node findParentNode(Node node){
		Node prtNode=root;
		ArrayList<String> tips = node.getCodeTips();
		if(tips.size()>1){
			for(int i=0;i<tips.size()-1;i++){
				if(prtNode!=null)
					prtNode=prtNode.getSubNode(tips.get(i));	
			}
		}
		
		return prtNode;
	}
	
	/**
	 * 设定当前选定的路径
	 * @param code
	 */
//	public void setSelectedNode(String code){
//		Node node = root;
//		String tip="";
//		for(int i=0;i<code.length();i++){
//			if(i>0 && (i+1)%4==0){
//				if(!(node.getSubNodes().size()==0))
//					node.setOpen(true);
//				else
//					node.setSelected(true);
//				node = node.getSubNode(tip);
//			}
//			else
//				tip += code.charAt(i);
//		}
//	}
	
	public Node getRootNode(){
		return root;
	}
	
	public String buildTree(){
		if(selectedCodes.size()==0)
			root.setSelected(true);
		StringBuffer bf = new StringBuffer();
		String tree_head="<ul class=\"tree\" id=\""+treeId+"\">",tree_footer="</ul>";
		bf.append(tree_head)
		.append(root.getNodeString(root))
		.append(tree_footer);
		return bf.toString();
	}


	public boolean isAllownEdit() {
		return allownEdit;
	}


	public void setAllownEdit(boolean allownEdit) {
		this.allownEdit = allownEdit;
		root.setAllownEdit(allownEdit);
	}
	
	
}
