package cn.pacificimmi.ucenter.models.view;

import java.util.Date;

import cn.pacificimmi.common.ComplexModel;

public class RoleInfo extends ComplexModel<RoleInfo> {
	/**
	 * 角色名称
	 */
	private String name="";
	/***
	 * 角色id
	 */
	private Integer role_id;
	/***
	 * 所属平台id
	 */
	private Integer sys_id;
	/**
	 * 所属平台名称
	 */
	private String sys_name="";
	/**
	 * 角色描述
	 */
	private String description="";
	
	/**
	 * 创建时间
	 */
	private Date create_time= new Date();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSys_name() {
		return sys_name;
	}
	public void setSys_name(String sys_name) {
		this.sys_name = sys_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public Integer getSys_id() {
		return sys_id;
	}
	public void setSys_id(Integer sys_id) {
		this.sys_id = sys_id;
	}
	
}
