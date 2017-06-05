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

	<link href="/views/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/views/media/css/DT_bootstrap.css" />

	<!-- END GLOBAL MANDATORY STYLES -->

	<!-- BEGIN PAGE LEVEL STYLES --> 
	 <link rel="stylesheet" type="text/css" href="/views/media/css/bootstrap-tree.css" />
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

						<h3 class="page-title">${operation}</h3>

						<ul class="breadcrumb">

							<li>
								<i class="icon-home"></i>
								<a href="/">主页</a> 
								<i class="icon-angle-right"></i>
							</li>

							<li>岗位管理<i class="icon-angle-right"></i>
                            </li>
							
                            <li>
                                <a href="">${operation}</a>
                            </li>
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
                	<div class="portlet-title">
                    	<div class="caption"><i class="icon-list"></i>${operation}</div>
					</div>
              
                    <!-- 添加表单 -->
                    <div class="portlet-body form">

								<!-- BEGIN FORM-->

								<div class="form-horizontal form-bordered form-row-stripped">
								<form id="sform" method="post">
												<div class="control-group">

												  	<div class="controls">岗位名称</div>

													<div class="controls">
														<input must-be="true" data-label="岗位名称" data-length="30" placeholder="" class="m-wrap span6" type="text" name="name" id="name" value="${station.station_name}">
													</div>

												</div>
                                                
                                                
                                                <div class="control-group">

												  <div class="controls">描述</div>

													<div class="controls">

														<input placeholder="" class="m-wrap span6" type="text" name="description" id="description" value="${station.station_description}">
													</div>

												</div>
												
												<div class="control-group">

												  	<div class="controls">所属机构</div>

													<div class="controls portlet box" style="width:450px;height:450px; overflow-y:scroll">
														${tree}
													</div>

												</div>
												<div class="form-actions">

													<a id="subBtn" class="btn blue"><i class="icon-ok"></i> 保存</a>
													<a id="back" class="btn">取消</a>

												</div>
                                                <input type="hidden" id="id" name="id" value="${station.station_id}" />
                                                <input type="hidden" id="org_id" name="org_id" value="${station.orgnization_id}" />
                                                <input id="fromurl" name="fromurl" type="hidden" value="" />
                                                <input id="operation" name="operation" type="hidden" value="${operation}" />
											</form>
											</div>

											<!-- END FORM-->  

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

	<script src="/views/media/js/bootstrap.min.js" type="text/javascript"></script>

	<!--[if lt IE 9]>

	<script src="/views/media/js/excanvas.min.js"></script>

	<script src="/views/media/js/respond.min.js"></script>  

	<![endif]-->   

	<script src="/views/media/js/jquery.slimscroll.min.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.blockui.min.js" type="text/javascript"></script>  

	<script src="/views/media/js/jquery.cookie.min.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.uniform.min.js" type="text/javascript" ></script>

	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->

	<script src="/views/media/js/jquery.vmap.js" type="text/javascript"></script>   

	<script src="/views/media/js/jquery.vmap.russia.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.vmap.world.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.vmap.europe.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.vmap.germany.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.vmap.usa.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.vmap.sampledata.js" type="text/javascript"></script>  

	<script src="/views/media/js/jquery.flot.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.flot.resize.js" type="text/javascript"></script>

	<script src="/views/media/js/jquery.pulsate.min.js" type="text/javascript"></script>

	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script type="text/javascript" src="/views/media/js/jquery.dataTables.js"></script>
	<script src="/views/media/js/app.js" type="text/javascript"></script>
	<script src="/bower_components/lrz/dist/lrz.bundle.js" type="text/javascript"></script>
    <script src="/js/json/json2.js"></script>
	<script src="/views/media/js/form-validate.js"></script>
	<!-- END PAGE LEVEL SCRIPTS -->  

	<script>

		jQuery(document).ready(function() {    

		   App.init(); // initlayout and core plugin

			$("#back").click(function(e) {
                window.history.back();
            });
			
			$("#name").change(function(e){
				if($(this).val()!=''){
			    		$.post("/ucenter/stations/station/checkExist",{"name":$(this).val()},function(data){
			    			if(data.status>0){
			    				alert("当前岗位名已存在");
			    				$("#name").val("");
			    				$("#name").focus();
			    			}
			    		});
				}
		    });	
			//单选处理
			$(".checker input[type='checkbox']").each(function(index, element) {
            	$(this).click(function(e) {
            		$(".checker input[type='checkbox']").prop("checked",false);
					$(".checker span").removeClass("checked");
					$(this).prop("checked",true);
					$(this).parent("span").addClass("checked");
					
					$("#org_id").val($(this).val());
                });
            });
			
			//提交表单
			$("#subBtn").click(function(e) {
				//所属机构必须选择
                if($(".checker input[type='checkbox']:checked").size()==0){
                		alert("请选择所属部门！");
                		return false;
                }
				
				var fromurl = document.referrer;
				$("#fromurl").val(fromurl);
				
				//调用验证插件，进行form表单的验证
				$("#sform").formValidate();
            });
		});
	
	</script>

	<!-- END JAVASCRIPTS -->
<!-- END BODY -->

</html>