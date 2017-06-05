package cn.pacificimmi.common.models.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseProjectExpands<M extends BaseProjectExpands<M>> extends Model<M> implements IBean {

	public void setProjectExpandsId(java.lang.Integer projectExpandsId) {
		set("project_expands_id", projectExpandsId);
	}

	public java.lang.Integer getProjectExpandsId() {
		return get("project_expands_id");
	}

	public void setProjectId(java.lang.Integer projectId) {
		set("project_id", projectId);
	}

	public java.lang.Integer getProjectId() {
		return get("project_id");
	}

	public void setDictCode(java.lang.String dictCode) {
		set("dict_code", dictCode);
	}

	public java.lang.String getDictCode() {
		return get("dict_code");
	}

	public void setDeleteFlag(java.lang.Integer deleteFlag) {
		set("delete_flag", deleteFlag);
	}

	public java.lang.Integer getDeleteFlag() {
		return get("delete_flag");
	}

}
