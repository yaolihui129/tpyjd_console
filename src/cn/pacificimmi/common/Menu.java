package cn.pacificimmi.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Menu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 548463495038003000L;
	private String icon="";
	private String href="";
	private String title="";
	private String id="";
	private String resType="";
	
	private ArrayList<Menu> subMenu = new ArrayList<Menu>();

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Menu> getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(ArrayList<Menu> subMenu) {
		this.subMenu = subMenu;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	/**
	 * 返回menu页面html
	 * @return
	 */
	public String getMenuHtml(String path){
		StringBuffer bf = new StringBuffer();
		if(href.equals("/")){
			if(path.equals("/")){
				bf.append("<li class=\"active\" data-code=\""+id+"\">");
				bf.append("<a href=\""+href+"\">");
				bf.append("<i class=\""+icon+"\"></i>");
				bf.append("<span class=\"title\">"+title+"</span>");
				bf.append("<span class=\"arrow \"></span>");
				bf.append("<span class=\"selected \"></span>");
				bf.append("</a>");
			}
			else{
				bf.append("<li data-code=\""+id+"\">");
				bf.append("<a href=\""+href+"\">");
				bf.append("<i class=\""+icon+"\"></i>");
				bf.append("<span class=\"title\">"+title+"</span>");
				bf.append("<span class=\"arrow \"></span>");
				bf.append("</a>");
			}
		}
		else{
			if(path.startsWith(href)){
				bf.append("<li class=\"active\" data-code=\""+id+"\">");
				bf.append("<a href=\""+href+"\">");
				bf.append("<i class=\""+icon+"\"></i>");
				bf.append("<span class=\"title\">"+title+"</span>");
				bf.append("<span class=\"arrow \"></span>");
				bf.append("<span class=\"selected \"></span>");
				bf.append("</a>");
			}else{
				bf.append("<li data-code=\""+id+"\">");
				bf.append("<a href=\""+href+"\">");
				bf.append("<i class=\""+icon+"\"></i>");
				bf.append("<span class=\"title\">"+title+"</span>");
				bf.append("<span class=\"arrow \"></span>");
				bf.append("</a>");
			}
		}
		
		if(subMenu.size()>0){
			bf.append("<ul class=\"sub-menu\">");
			for(Menu menu:subMenu){
				bf.append(menu.getMenuHtml(path));
			}
			bf.append("</ul>");
		}
		
		bf.append("</li>");
		return bf.toString();
	}
	
	public static Menu loadFromJson(JSONObject mjson){
//		"id": "00010004",
//        "title": "角色管理",
//        "icon": "icon-user",
//        "href": "/ucenter/roles",
//        "subMenu": []
		Menu menu = new Menu();
		String id = mjson.getString("id");
		menu.setId(id);
		String title = mjson.getString("title");
		menu.setTitle(title);
		String icon = mjson.getString("icon");
		menu.setIcon(icon);
		String href = mjson.getString("href");
		menu.setHref(href);
		JSONArray submenu = mjson.getJSONArray("subMenu");
		ArrayList<Menu> subMenu = new ArrayList<Menu>();
		for(int i=0;i<submenu.length();i++){
			JSONObject mj = submenu.getJSONObject(i);
			subMenu.add(loadFromJson(mj));
		}
		menu.setSubMenu(subMenu);
		return menu;
	}
	
	public String toJson(){
		return toJsonMenu(this);
	}
	
	private String toJsonMenu(Menu menu){
		StringBuffer bf = new StringBuffer();
		bf.append("{");
		bf.append("\"").append("id").append("\"").append(":").append("\"").append(menu.getId()).append("\"").append(",");
		bf.append("\"").append("title").append("\"").append(":").append("\"").append(menu.getTitle()).append("\"").append(",");
		bf.append("\"").append("icon").append("\"").append(":").append("\"").append(menu.getIcon()).append("\"").append(",");
		bf.append("\"").append("href").append("\"").append(":").append("\"").append(menu.getHref()).append("\"").append(",");
		bf.append("\"").append("subMenu").append("\"").append(":").append(getJsonMenus(menu.getSubMenu()));
		bf.append("}");
		return bf.toString();
	}
	
	private String getJsonMenus(List<Menu> menus){
		StringBuffer bu = new StringBuffer();
		bu.append("[");
		boolean isfirst = true;
		for(Menu m:menus){
			if(isfirst){
				bu.append(toJsonMenu(m));
				isfirst=false;
			}
			else
				bu.append(",").append(toJsonMenu(m));
		}
		bu.append("]");
	return bu.toString();
	}
}
