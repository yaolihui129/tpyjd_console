package cn.pacificimmi.ucenter.models.view;

public class SystemInfo {
	private Integer sys_id;
	private String sys_name;
	private String sys_domain;
	private String sys_create_time;

	public String getSys_name() {
		return sys_name;
	}
	public void setSys_name(String sys_name) {
		this.sys_name = sys_name;
	}
	public String getSys_domain() {
		return sys_domain;
	}
	public void setSys_domain(String sys_domain) {
		this.sys_domain = sys_domain;
	}
	public String getSys_create_time() {
		return sys_create_time;
	}
	public void setSys_create_time(String sys_create_time) {
		this.sys_create_time = sys_create_time;
	}
	public Integer getSys_id() {
		return sys_id;
	}
	public void setSys_id(Integer sys_id) {
		this.sys_id = sys_id;
	}
	
}
