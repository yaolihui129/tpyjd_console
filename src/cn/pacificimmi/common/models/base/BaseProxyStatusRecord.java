package cn.pacificimmi.common.models.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseProxyStatusRecord<M extends BaseProxyStatusRecord<M>> extends Model<M> implements IBean {

	public void setProxyStatusRecordId(java.lang.Integer proxyStatusRecordId) {
		set("proxy_status_record_id", proxyStatusRecordId);
	}

	public java.lang.Integer getProxyStatusRecordId() {
		return get("proxy_status_record_id");
	}

	public void setCustinfoId(java.lang.Integer custinfoId) {
		set("custinfo_id", custinfoId);
	}

	public java.lang.Integer getCustinfoId() {
		return get("custinfo_id");
	}

	public void setReason(java.lang.String reason) {
		set("reason", reason);
	}

	public java.lang.String getReason() {
		return get("reason");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("update_time");
	}

}