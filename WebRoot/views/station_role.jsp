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

							<li>角色分配<i class="icon-angle-right"></i>
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
								<form id="sform" method="get">
								<div class="form-horizontal form-bordered form-row-stripped">
									<div class="control-group">
										<div class="controls">岗位名称：${station.station_name}</div>
									</div>
                                                
                                          
                                    <div class="control-group">
										<div class="controls">描述：${station.station_description}</div>
									</div>
												
									<div class="control-group">
										<div class="controls">所属机构：${station.orgnization_name}</div>
									</div>
                                    <div class="control-group">
                                        <div class="controls">分配角色：</div>
                                        <div class="controls row-fluid">
                                        	<div class="span4" style="text-align:center;">
                                            <p>待分配角色</p>
                                           	<select multiple="multiple" id="from" name="from" style="width:300px;height:300px;overflow-y:scroll">${options_from}
                                            </select>
                                            </div>
                                            <div class="span4" style="text-align:center;">
                                            <p>已分配角色</p>
                                            <select multiple="multiple" id="target" name="target" style="width:300px;height:300px; overflow-y:scroll">
                                            ${options_target}
                                            </select>
                                            </div>
                                            <div class="span4">
                                            </div>
                                        </div>
                                    </div>
								</div>
                                
                                <div class="form-actions">
									<button id="subBtn" type="btn" class="btn blue"><i class="icon-ok"></i> 保存</button>
									<button id="back" type="button" class="btn">取消</button>
                                    <input id="fromurl" name="fromurl" type="hidden" value="" />
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
			
			$("#from").change(function(e) {
               var option = $("#from").find("option:selected");
			   var optgroup = option.parent();
			   var groupLabel = optgroup.attr("label");
			   
			   //判断目标select中是否存在此分组
			   var targetGroup = $("#target").children("optgroup[label='"+groupLabel+"']");
			   
			   if(targetGroup.length==0){
				   $("#target").append("<optgroup label='"+groupLabel+"'><option value='"+option.val()+"'>"+option.text()+"</option></optgroup>");
				   
				   //从from中移除
				   if(option.siblings().size()==0)
				   		option.parent().remove();
				   else
				   		option.remove();
				}
				else{
				   targetGroup.append("<option value='"+option.val()+"'>"+option.text()+"</option>");
					//从from中移除
				   if(option.siblings().size()==0)
				   		option.parent().remove();
				   else
				   		option.remove();
				}
            });
			
			$("#target").change(function(e) {
               var option = $("#target").find("option:selected");
			   var optgroup = option.parent();
			   var groupLabel = optgroup.attr("label");
			   
			   //判断目标select中是否存在此分组
			   var targetGroup = $("#from").children("optgroup[label='"+groupLabel+"']");
			   
			   if(targetGroup.length==0){
				   $("#from").append("<optgroup label='"+groupLabel+"'><option value='"+option.val()+"'>"+option.text()+"</option></optgroup>");
				   
				   //从from中移除
				   if(option.siblings().size()==0)
				   		option.parent().remove();
				   else
				   		option.remove();
				}
				else{
				   targetGroup.append("<option value='"+option.val()+"'>"+option.text()+"</option>");
					//从from中移除
				   if(option.siblings().size()==0)
				   		option.parent().remove();
				   else
				   		option.remove();
				}
            });
			
			//提交表单
			$("#subBtn").click(function(e) {
				
				$("#target").find("option").each(function(index, element) {
                    $(this).prop("selected",true);
                });
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