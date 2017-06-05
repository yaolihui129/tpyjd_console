package cn.pacificimmi.common.models.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePortalInvitationCode<M extends BasePortalInvitationCode<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setStewardId(java.lang.Integer stewardId) {
		set("steward_id", stewardId);
	}

	public java.lang.Integer getStewardId() {
		return get("steward_id");
	}

	public void setLiveId(java.lang.Integer liveId) {
		set("live_id", liveId);
	}

	public java.lang.Integer getLiveId() {
		return get("live_id");
	}

	public void setCode(java.lang.String code) {
		set("code", code);
	}

	public java.lang.String getCode() {
		return get("code");
	}

	public void setUsed(java.lang.Integer used) {
		set("used", used);
	}

	public java.lang.Integer getUsed() {
		return get("used");
	}

}