package cn.pacificimmi.common.models.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStewardEvaluate<M extends BaseStewardEvaluate<M>> extends Model<M> implements IBean {

	public void setEvaluateId(java.lang.Integer evaluateId) {
		set("evaluate_id", evaluateId);
	}

	public java.lang.Integer getEvaluateId() {
		return get("evaluate_id");
	}

	public void setStewardId(java.lang.Integer stewardId) {
		set("steward_id", stewardId);
	}

	public java.lang.Integer getStewardId() {
		return get("steward_id");
	}

	public void setCustinfoId(java.lang.Integer custinfoId) {
		set("custinfo_id", custinfoId);
	}

	public java.lang.Integer getCustinfoId() {
		return get("custinfo_id");
	}

	public void setStarLevel(java.lang.Double starLevel) {
		set("star_level", starLevel);
	}

	public java.lang.Double getStarLevel() {
		return get("star_level");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}

	public void setServiceAttitude(java.lang.String serviceAttitude) {
		set("service_attitude", serviceAttitude);
	}

	public java.lang.String getServiceAttitude() {
		return get("service_attitude");
	}

	public void setSpecializedKnowledge(java.lang.String specializedKnowledge) {
		set("specialized_knowledge", specializedKnowledge);
	}

	public java.lang.String getSpecializedKnowledge() {
		return get("specialized_knowledge");
	}

	public void setFeedbackEfficiency(java.lang.String feedbackEfficiency) {
		set("feedback_efficiency", feedbackEfficiency);
	}

	public java.lang.String getFeedbackEfficiency() {
		return get("feedback_efficiency");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setDeleteSign(java.lang.Integer deleteSign) {
		set("delete_sign", deleteSign);
	}

	public java.lang.Integer getDeleteSign() {
		return get("delete_sign");
	}

}