package cn.pacificimmi.common.models.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseOrgnization<M extends BaseOrgnization<M>> extends Model<M> implements IBean {

	public void setOrgId(java.lang.Integer orgId) {
		set("org_id", orgId);
	}

	public java.lang.Integer getOrgId() {
		return get("org_id");
	}

	public void setOrgCode(java.lang.String orgCode) {
		set("org_code", orgCode);
	}

	public java.lang.String getOrgCode() {
		return get("org_code");
	}

	public void setOrgPcode(java.lang.String orgPcode) {
		set("org_pcode", orgPcode);
	}

	public java.lang.String getOrgPcode() {
		return get("org_pcode");
	}

	public void setOrgType(java.lang.String orgType) {
		set("org_type", orgType);
	}

	public java.lang.String getOrgType() {
		return get("org_type");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setShortName(java.lang.String shortName) {
		set("short_name", shortName);
	}

	public java.lang.String getShortName() {
		return get("short_name");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public java.lang.String getDescription() {
		return get("description");
	}

	public void setManagerId(java.lang.String managerId) {
		set("manager_id", managerId);
	}

	public java.lang.String getManagerId() {
		return get("manager_id");
	}

	public void setFoundTime(java.util.Date foundTime) {
		set("found_time", foundTime);
	}

	public java.util.Date getFoundTime() {
		return get("found_time");
	}

	public void setSort(java.lang.Integer sort) {
		set("sort", sort);
	}

	public java.lang.Integer getSort() {
		return get("sort");
	}

	public void setDeleteFlag(java.lang.Integer deleteFlag) {
		set("delete_flag", deleteFlag);
	}

	public java.lang.Integer getDeleteFlag() {
		return get("delete_flag");
	}

	public void setCreateUser(java.lang.String createUser) {
		set("create_user", createUser);
	}

	public java.lang.String getCreateUser() {
		return get("create_user");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setUpdateUser(java.lang.String updateUser) {
		set("update_user", updateUser);
	}

	public java.lang.String getUpdateUser() {
		return get("update_user");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("update_time");
	}

	public void setU8Code(java.lang.String u8Code) {
		set("u8_code", u8Code);
	}

	public java.lang.String getU8Code() {
		return get("u8_code");
	}

}
