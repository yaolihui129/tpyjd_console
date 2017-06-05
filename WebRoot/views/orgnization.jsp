<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->

<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->

<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->

<!-- BEGIN HEAD -->

<head>

	<meta charset="utf-8" />

	<title>太平洋加达出国－后台管理系统 v1.0</title>

	<meta content="width=device-width, initial-scale=1.0" name="viewport" />

	<meta content="" name="description" />

	<meta content="" name="author" />

	<!-- BEGIN GLOBAL MANDATORY STYLES -->

	<link href="/views/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/style-metro.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/style.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link rel="stylesheet" type="text/css" href="/views/media/css/bootstrap-tree.css" />
	<!-- END GLOBAL MANDATORY STYLES -->

	<!-- BEGIN PAGE LEVEL STYLES --> 
	<!-- END PAGE LEVEL STYLES -->

</head>

<!-- END HEAD -->

<!-- BEGIN BODY -->

<body class="page-header-fixed">

	<!-- BEGIN HEADER -->

	<%@ include  file="navi.jsp"%>

	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->

	<div class="page-container">

		<!-- BEGIN SIDEBAR -->

		<%@ include  file="menus.jsp"%>

		<!-- END SIDEBAR -->

		<!-- BEGIN PAGE -->

		<div class="page-content">

			<!-- BEGIN PAGE CONTAINER-->

			<div class="container-fluid">

				<!-- BEGIN PAGE HEADER-->

				<div class="row-fluid">

					<div class="span12">

						<!-- BEGIN PAGE TITLE & BREADCRUMB-->

						<h3 class="page-title">组织机构管理</h3>

						<ul class="breadcrumb">

							<li>

								<i class="icon-home"></i>

								<a href="/console/">主页</a> 

								<i class="icon-angle-right"></i>

							</li>

							<li>组织机构管理</li>
							
							<li class="pull-right no-text-shadow">

								<div id="dashboard-report-range" class="dashboard-date-range tooltips no-tooltip-on-touch-device responsive" data-tablet="" data-desktop="tooltips" data-placement="top" data-original-title="Change dashboard date range">

									<i class="icon-calendar"></i>

									<span></span>

									<i class="icon-angle-down"></i>

								</div>

							</li>

						</ul>

						<!-- END PAGE TITLE & BREADCRUMB-->

					</div>
					
				</div>

				<!-- END PAGE HEADER-->
				<div class="row-fluid">
					<div class="span4 portlet box" style="border:#efefef 1px solid;">
							<div class="portlet-title">
								<div class="caption"><i class="icon-sitemap"></i></div>
								<div class="actions">
									<a href="javascript:;" id="tree_collapse" class="btn green"> 收缩</a>
									<a href="javascript:;" id="tree_expand" class="btn yellow"> 展开</a>
								</div>
							</div>

							<div class="portlet-body fuelux">${orgTree}</div>
					</div>
					<div class="span8 portlet form">
							<div class="portlet-title">
								<div class="caption"><i class="icon-reorder"></i>${opt_title}机构</div>
							</div>
 							
                            <form id="boxForm" action="/ucenter/orgnizations/opt">
							<div class="portlet-body fuelux form-horizontal form-bordered form-row-stripped">
                                    <div class="control-group">
                                        <label class="control-label">机构名称：</label>
                                        <div class="controls">
                                            <input data-label="机构名称" must-be="true" data-length="30" name="name" id="name" class="m-wrap" data-tabindex="1" value="${name}" placeholder="请输入机构名称..">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">上级机构：</label>
                                        <div class="controls">
                                        	${parent}
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">机构类型：</label>
                                        <div class="controls">
                                        	${org_type}
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">U8编码：</label>
                                        <div class="controls">
                                        	<input data-label="U8编码" must-be="true" data-length="50" name="u8_code" id="u8_code" class="m-wrap" data-tabindex="1" value="${u8_code}" placeholder="财务系统U8编码..">
                                        </div>
                                    </div>
                                    <input type="hidden" id="code" name="code" value="${org_code}" />
                                    <input type="hidden" id="opt_type" name="opt_type" value="${opt_type}" />
									<div class="form-actions">
										<a id="subBtn" class="btn blue"><i class="icon-ok"></i> 保存</a>
										<a id="back"  class="btn">取消</a>
									</div>      
                            </div>
                            </form>
					</div>
                </div>

			</div>

			<!-- END PAGE CONTAINER-->    

		</div>

		<!-- END PAGE -->

	</div>

	<!-- END CONTAINER -->

	<!-- BEGIN FOOTER -->

	<div class="footer">

		<div class="footer-inner">

			2016 &copy; copyright pacificimmi.

		</div>

		<div class="footer-tools">

			<span class="go-top">

			<i class="icon-angle-up"></i>

			</span>

		</div>

	</div>
	<!-- END FOOTER -->

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

	<!-- BEGIN CORE PLUGINS -->

	<script src="/views/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

	<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

	<script src="/views/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
    <script src="/views/media/js/jquery.uniform.min.js" type="text/javascript" ></script>
	<script src="/views/media/js/bootstrap.min.js" type="text/javascript"></script>

	<!--[if lt IE 9]>

	<script src="/views/media/js/excanvas.min.js"></script>

	<script src="/views/media/js/respond.min.js"></script>  

	<![endif]--> 


	<!-- BEGIN PAGE LEVEL SCRIPTS -->

	<script src="/views/media/js/app.js" type="text/javascript"></script>     
	<script src="/views/media/js/form-validate.js"></script>
	<!-- END PAGE LEVEL SCRIPTS -->  

	<script>

		jQuery(document).ready(function() {    

		   App.init(); // initlayout and core plugins
		   
		   $("i.node").each(function(index) {
           		$(this).on("click",function(){
					//var nodeName = $(this).parent().children("span").text();
					var nodeCode = $(this).parent().attr("data-value");
					var optType = $(this).attr("opt-type");
					
					if(optType=='add'){
						window.location.href='?code='+nodeCode+'&opt_type=add';
					}
					else if(optType=='edit'){
						window.location.href='?code='+nodeCode+'&opt_type=edit';
					}
					else if(optType=='del'){
						$.post("/ucenter/orgnizations/chkusers",{"code":nodeCode},function(data){
							if(data.status==0){
								window.location.href='/ucenter/orgnizations/opt?code='+nodeCode+'&opt_type=del';
							}
							else{
								alert("请将当前部门下员工转移后再进行删除操作！");
							}
						});
					}
					return false;
           		});
		   });
		   
		 	//收缩
           $('#tree_collapse').click(function () {
               $('.tree-toggle', $('#orgTree > li > ul')).addClass("closed");
               $('.branch', $('#orgTree > li > ul')).removeClass("in");
           });
		   
			//展开
           $('#tree_expand').click(function () {
               $('.tree-toggle', $('#orgTree > li > ul')).removeClass("closed");
               $('.branch', $('#orgTree > li > ul')).addClass("in");
           });
			
			$('.tree-toggle').each(function(i, element) {
               $(this).click(function(){
					if($(this).hasClass("closed")){
						$(this).removeClass("closed");
						$(this).parent().children(".branch").addClass("in");
					}
					else{
						$(this).addClass("closed");
						$(this).parent().children(".branch").removeClass("in");
					}
				});
           });
		
			$(".nodetitle").each(function(i,element){
				 $(this).click(function(){
					 //在此处可以添加节点文字点击时触发的操作代码
					 return false;
				 });
			});
			
		   //提交表单
			$("#subBtn").click(function(e) {
                
				//调用验证插件，进行form表单的验证
				$("#boxForm").formValidate();
            });
		});

	</script>

	<!-- END JAVASCRIPTS -->
<!-- END BODY -->

</html>