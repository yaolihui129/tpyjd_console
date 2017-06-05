package cn.pacificimmi.ucenter.models.view;

import java.util.Date;

import cn.pacificimmi.common.ComplexModel;

public class StationInfo extends ComplexModel<StationInfo>{
	private Integer station_id;
	private String station_name="";
	private String orgnization_name="";
	private Integer orgnization_id;
	private String orgnization_code="";
	private String station_description="";
	private Date station_create_time=new Date();
		
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public String getOrgnization_name() {
		return orgnization_name;
	}
	public void setOrgnization_name(String orgnization_name) {
		this.orgnization_name = orgnization_name;
	}
	public Date getStation_create_time() {
		return station_create_time;
	}
	public void setStation_create_time(Date station_create_time) {
		this.station_create_time = station_create_time;
	}
	public String getOrgnization_code() {
		return orgnization_code;
	}
	public void setOrgnization_code(String orgnization_code) {
		this.orgnization_code = orgnization_code;
	}
	public String getStation_description() {
		return station_description;
	}
	public void setStation_description(String station_description) {
		this.station_description = station_description;
	}
	public Integer getStation_id() {
		return station_id;
	}
	public void setStation_id(Integer station_id) {
		this.station_id = station_id;
	}
	public Integer getOrgnization_id() {
		return orgnization_id;
	}
	public void setOrgnization_id(Integer orgnization_id) {
		this.orgnization_id = orgnization_id;
	}
		
	
}
