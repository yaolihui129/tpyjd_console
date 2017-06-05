package cn.pacificimmi.common;

import javax.sql.DataSource;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * Generator
 */
public class GeneratorModel {
	
	public static DataSource getDataSource() {
		Prop p = PropKit.use("debug_config.txt");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
		c3p0Plugin.start();
		return c3p0Plugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "cn.pacificimmi.common.models.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/cn/pacificimmi/common/models/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "cn.pacificimmi.common.models";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		// 创建生成器
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 设置数据库方言
		gernerator.setDialect(new MysqlDialect());
		// 添加不需要生成的表名
		gernerator.addExcludedTable("crm_distribution_type",
				"crm_distribution_main",
				"crm_distribution_scale",
				"crm_distribution_set",
				"crm_project",
				"crm_country",
				"crm_cust_edit_log",
				"crm_cust_follow_records",
				"crm_cust_signinfo",
//				"crm_custinfo",
				"crm_custinfo_intention",
				"crm_custinfo_invoice",
				"crm_custinfo_receive",
				"crm_custinfo_security",
				"crm_custinfo_steward",
				"crm_distribution_main",
				"crm_distribution_scale",
				"crm_distribution_set",
				"crm_distribution_type",
				"crm_letters",
				"crm_project",
				"crm_project_apply_process",
				"crm_project_cost",
				"crm_project_material",
				"crm_signinfo_annex",
				"crm_steward_evaluate",
//				"crm_steward_user",
				"crm_topics",
				"crm_topics_copy",
				"wx_card_access",
				"wx_card_customer",
				"wx_estimate_question",
				"wx_estimate_select",
				"wx_estimate_select_additional",
				"wx_weixin_resource");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("console_","crm_address_","crm_");
	
		// 生成
		gernerator.generate();
	}
}




