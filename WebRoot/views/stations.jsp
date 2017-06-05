<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

	<link href="/views/media/css/uniform.default.css" rel="styles
	<meta content="" name="author" />

	<link href="/views/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/style-metro.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/style.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>

	<link href="/views/media/css/default.css" rel="stylesheet" type="text/css" id="style_color"/>
heet" type="text/css"/>

	<!-- END GLOBAL MANDATORY STYLES -->

	<!-- BEGIN PAGE LEVEL STYLES -->

	<link rel="stylesheet" type="text/css" href="/views/media/css/select2_metro.css" />
    <link rel="stylesheet" type="text/css" href="/views/media/css/bootstrap-tree.css" />
	<link rel="stylesheet" type="text/css" href="/views/media/css/DT_bootstrap.css" />
    <style type="text/css">
	th{
		cursor:pointer;
	}
	th div{
		float:left;
	}
	.tree a.tree-toggle{
		padding-left:20px;
	}
	</style>

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

						<h3 class="page-title">岗位管理</h3>

						<ul class="breadcrumb">

							<li>
								<i class="icon-home"></i>

								<a href="/">主页</a> 

								<i class="icon-angle-right"></i>

							</li>

							<li>
                                <a href="/ucenter/stations">岗位管理</a>
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
                    <div class="span12">
                    <div class="portlet box grey">

							<div class="portlet-title">

								<div class="caption"><i class="icon-list"></i></div>

								<div class="actions">

									<a href="/ucenter/stations/station/add" class="btn blue addnew"><i class="icon-pencil"></i> 新增</a>

									<div class="btn-group">

										<a class="btn green" href="#" data-toggle="dropdown">

										<i class="icon-cogs"></i> 批量操作

										<i class="icon-angle-down"></i>

										</a>

										<ul class="dropdown-menu pull-right">
											<li><a id="batchDel"><i class="icon-trash"></i> 批量删除</a></li>
										</ul>

									</div>

								</div>

							</div>

					  <div class="portlet-body  no-more-tables">
                      <form id="searchForm" method="get">
                       	<div class="row-fluid">
                                	<div class="span6">
                                    <div class="btn-group">

										<a class="btn green" href="#" data-toggle="dropdown">
                                            <span>${org_name}</span>
                                            <i class="icon-angle-down"></i>
										</a>

										<ul class="dropdown-menu pull-left">
                                        	<div style="width:450px;height:500px; overflow-y:scroll">
											${tree}
                                            </div>
										</ul>
									</div>
                           	  	  </div>
                           	  <div class="span6"><div id="sample_2_filter" class="dataTables_filter"><label><input id="name" class="m-wrap medium" placeholder="输入岗位名称.." name="name" type="text"><a id="searchBtn" class="btn blue"><i class="icon-search"></i></a></label></div></div></div>
								
                          <table class="table">
							  <thead>
								  <tr>
									  <th width="10%" id="all"><input type="checkbox" /><span>全选</span></th>
                                      <c:choose>
                                      	  <c:when test="${field=='station.name' && way=='asc'}">
                                              <th width="30%" class="sorting_asc" data-field="station.name">岗位名称</th>
                                          </c:when>
                                          <c:when test="${field=='station.name' && way=='desc'}">
                                              <th width="30%" class="sorting_desc" data-field="station.name">岗位名称</th>
                                          </c:when>
                                          <c:otherwise>
                                         	 <th width="30%" class="sorting" data-field="station.name">岗位名称</th>
                                          </c:otherwise>
                                      </c:choose>
                                      <c:choose>
                                      	  <c:when test="${field=='orgnization.name' && way=='asc'}">
                                              <th width="" class="sorting_asc" data-field="orgnization.name">所属部门</th>
                                          </c:when>
                                          <c:when test="${field=='orgnization.name' && way=='desc'}">
                                              <th width="" class="sorting_desc" data-field="orgnization.name">所属部门</th>
                                          </c:when>
                                          <c:otherwise>
                                         	 <th width="" class="sorting" data-field="orgnization.name">所属部门</th>
                                          </c:otherwise>
                                      </c:choose>
									  <c:choose>
                                      	  <c:when test="${field=='station.create_time' && way=='asc'}">
                                              <th width="" class="sorting_asc"  style="text-align:center" data-field="station.create_time">创建时间</th>
                                          </c:when>
                                          <c:when test="${field=='station.create_time' && way=='desc'}">
                                              <th width="" class="sorting_desc"  style="text-align:center" data-field="station.create_time">创建时间</th>
                                          </c:when>
                                          <c:otherwise>
                                         	 <th width="" class="sorting"  style="text-align:center" data-field="station.create_time">创建时间</th>
                                          </c:otherwise>
                                      </c:choose>
									  
                                      <th width="20%">快捷操作&nbsp;&nbsp;</th>
								  </tr>

							  </thead>

							  <tbody id="list">
								  <c:forEach items="${list}" var="item">
									<tr>
										<td><input type="checkbox" class="checkboxes" value="${item.station_id}" /></td>
										<td data-title="系统名称">
                                            ${item.station_name}
                                        </td>
										<td>${item.orgnization_name}</td>
										<td style="text-align:center">
                                           	<p>${item.station_create_time}</p>
                                        </td>
										<td data-title="操作">
                                           		<div data-id="${item.station_id}">
                                           			<i class="icon-pencil">编辑</i>&nbsp;&nbsp;
                                                    <i class="icon-trash">删除</i>&nbsp;&nbsp;
                                                    <i class="icon-user">角色</i>
                                                </div>
                                        </td>
								    </tr>
                                  </c:forEach>
							  </tbody>

						  </table>
                          		<input id="field" name="field" type="hidden" /> 
                                <input id="way" name="way" type="hidden" /> 
                                <input id="org_id" name="org_id" type="hidden" value="${org_id}"/>
                                <input id="org_name" name="org_name" type="hidden" value="${org_name}"/>
                                <div id="pageBar">
                                	<input type="hidden" id="page" name="page" value="1" />
                                    ${pageBar}
                                </div>
						</form>
						</div>
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

			2016 &copy; copyright xiuhexuan.

		</div>

		<div class="footer-tools">

			<span class="go-top">

			<i class="icon-angle-up"></i>

			</span>

		</div>

	</div>

	<!-- FOOTER -->

	<!-- BEGIN J2.1.1RIPTS(Lll reduce page load time) -->

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

	<script type="text/javascript" src="/views/media/js/select2.min.js"></script>

	<script type="text/javascript" src="/views/media/js/jquery.dataTables.js"></script>

	<!--<script type="text/javascript" src="/views/media/js/DT_bootstrap.js"></script>-->

	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->

	<script src="/views/media/js/app.js"></script>
	<script src="/js/json/json2.js"></script>
	<script src="/views/media/js/form-validate.js"></script>     
	<script>
		jQuery(document).ready(function() {    

		   App.init(); // initlayout and core plugins
		   $("#orgTree").find("li>a").each(function(index, element) {
			   $(this).click(function(e) {
                 var id =$(this).attr("data-info");
					 $("#org_id").val(id);
					 $("#org_name").val($(this).text());
					 $(this).parents("div.btn-group").find("a>span").text($(this).text());
					 doRequest();
				});
			});
		   //全选事件处理
				$("#all").click(function(e) {
                   if($(this).children(":last").text()=="全选"){
					   $(this).children(":last").text("反选");
					   $("input:checkbox").prop("checked",function(i,v){
							return true;   
						});
						$("div.checker>span").addClass("checked");
					}
					else{
						$(this).children(":last").text("全选");
						$("input:checkbox").prop("checked",function(i,v){
							return false;   
						});
						$("div.checker>span").removeClass("checked");
					}
                });
				//搜索触发
				$("#searchBtn").click(function(e) {
                    doRequest();
                });
				
				//排序事件处理
				$(".sorting,.sorting_asc,.sorting_desc").each(function(index, element) {
					$(this).click(function(e) {
						var field = $(this).attr("data-field");
						$("#field").val(field);
						
						$(this).siblings(".sorting,.sorting_asc,.sorting_desc").attr("class","sorting");
                        if($(this).attr("class")=="sorting"){
							$(this).attr("class","sorting_asc");
							$("#way").val("asc");
							//do something
							doRequest();
						}
						else if($(this).attr("class")=="sorting_asc"){
							$(this).attr("class","sorting_desc");
							$("#way").val("desc");
							//do something
							doRequest();
						}
						else{
							$(this).attr("class","sorting_asc");
							$("#way").val("asc");
							//do something
							doRequest();
						}
                    });
                });
				
				/**
				批量处理触发
				**/
				//批量删除
				$("#batchDel").click(function(e) {
                   var checks = $("input.checkboxes:checked");
					if(checks.size()>0){
						if(confirm("确定删除吗?")){
							var ids="";
							checks.each(function(index, element) {
								if(ids=="")
									ids=$(this).val();
								else{
									ids = ids+","+$(this).val();
								}
							});
							//dosomething
							$.post("/ucenter/stations/remove",{"ids":ids},function(data){
								if(data.status==0){
									checks.each(function(index, element) {
										$(this).parents("#list>tr").remove();
									});
								}
								else{
									alert("删除失败");
								}
							});
						}
					}
					else{
						alert("请选择其中一行");	
					}
                });
			
			//绑定内容编辑
				$("i.icon-pencil").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						location.href="/ucenter/stations/station/update/"+id;
					});		
                });
			//绑定内容编辑
				$("i.icon-user").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						location.href="/ucenter/stations/station/roles/"+id;
					});		
                });	
			//绑定删除按钮
				$("i.icon-trash").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						if(confirm("确定删除吗?")){
							$.post("/ucenter/stations/remove",{"ids":id},function(data){
								if(data.status==0){
									$(element).parents("#list>tr").remove();
								}
								else
									alert("删除失败");
							});
						}
					});		
                });
				
			//绑定翻页
				$('li.pages').each(function(index, element) {
					if($(element).attr("data-page")!=undefined){
						$(this).click(function(e) {
							$("#page").val($(element).attr("data-page"));
							doRequest();
						});
					}
                });
			
			//提交查询请求
			function doRequest(){
				//$("#searchForm").submit();
				$("#searchForm").formValidate();
			}
			
		});

	</script>

	<!-- END JAVASCRIPTS -->
<!-- END BODY -->

</html>