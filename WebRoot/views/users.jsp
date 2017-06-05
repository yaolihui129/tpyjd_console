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

	<link href="/views/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>
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

						<h3 class="page-title">用户管理</h3>

						<ul class="breadcrumb">

							<li>
								<i class="icon-home"></i>

								<a href="/console">主页</a> 

								<i class="icon-angle-right"></i>

							</li>

							<li>
                                <a href="/console/users/">用户管理</a>
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

									<a href="/ucenter/users/user/add" class="btn blue addnew"><i class="icon-pencil"></i> 新增</a>

									<div class="btn-group">

										<a class="btn green" href="#" data-toggle="dropdown">

										<i class="icon-cogs"></i> 批量操作

										<i class="icon-angle-down"></i>

										</a>

										<ul class="dropdown-menu pull-right">
                                        
                                           <li><a id="batchInit"><i class="icon-bolt"></i> 批量初始化</a></li>
                                           <li><a id="batchExport" href="/ucenter/users/exportUsers" target="_blank"><i class="icon-download-alt"></i> 批量导出</a></li>
                                           <li><a id="batchImport" href="#importBox" data-toggle="modal"><i class="icon-upload-alt"></i> 批量导入</a></li> 
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
                           	  <div class="span6"><div id="sample_2_filter" class="dataTables_filter"><label><input id="name" class="m-wrap medium" placeholder="输入登录名或姓名.." value="${name}" name="name" type="text"><a id="searchBtn" class="btn blue"><i class="icon-search"></i></a></label></div></div></div>
								
                          <table class="table">
							  <thead>
								  <tr>
									  <th width="10%" id="all"><input type="checkbox" /><span>全选</span></th>
                                      <c:choose>
                                      	  <c:when test="${field=='u.login_name' && way=='asc'}">
                                              <th  class="sorting_asc" data-field="u.login_name">登录名</th>
                                          </c:when>
                                          <c:when test="${field=='u.login_name' && way=='desc'}">
                                              <th  class="sorting_desc" data-field="u.login_name">登录名</th>
                                          </c:when>
                                          <c:otherwise>
                                         	 <th  class="sorting" data-field="u.login_name">登录名</th>
                                          </c:otherwise>
                                      </c:choose>
                                      <th>姓名</th>
                                      <c:choose>
                                      	  <c:when test="${field=='dep_name' && way=='asc'}">
                                              <th width="" class="sorting_asc" data-field="dep_name">所属部门</th>
                                          </c:when>
                                          <c:when test="${field=='dep_name' && way=='desc'}">
                                              <th width="" class="sorting_desc" data-field="dep_name">所属部门</th>
                                          </c:when>
                                          <c:otherwise>
                                         	 <th width="" class="sorting" data-field="dep_name">所属部门</th>
                                          </c:otherwise>
                                      </c:choose>
									  <c:choose>
                                      	  <c:when test="${field=='u.create_time' && way=='asc'}">
                                              <th width="" class="sorting_asc"  style="text-align:center" data-field="u.create_time">创建时间</th>
                                          </c:when>
                                          <c:when test="${field=='u.create_time' && way=='desc'}">
                                              <th width="" class="sorting_desc"  style="text-align:center" data-field="u.create_time">创建时间</th>
                                          </c:when>
                                          <c:otherwise>
                                         	 <th width="" class="sorting"  style="text-align:center" data-field="u.create_time">创建时间</th>
                                          </c:otherwise>
                                      </c:choose>
									  
                                      <th width="25%">快捷操作&nbsp;&nbsp;</th>
								  </tr>

							  </thead>

							  <tbody id="list">
								  <c:forEach items="${list}" var="item">
									<tr>
										<td>
										<c:if test="${item.login_name!='admin'}"> 
											<input type="checkbox" class="checkboxes" value="${item.user_id}" /></td>
										</c:if>
										<td>
                                            ${item.login_name}
                                        </td>
                                        <td>
                                        ${item.user_name}
                                        </td>
										<td>${item.dep_name}</td>
										<td style="text-align:center">
                                           	<p>${item.create_time}</p>
                                        </td>
										<td data-title="操作">
                                           		<div data-id="${item.user_id}">
                                           		   <c:if test="${item.login_name!='admin'}"> 
                                           			<i class="icon-pencil">编辑</i>&nbsp;&nbsp;
                                           			</c:if>	
                                                    
                                                    	<c:if test="${item.login_name!='admin'}">                                        
                                                        	<i class="icon-trash">删除</i>&nbsp;&nbsp;
                                                     </c:if>
                                                    
                                                    <c:if test="${item.login_name!='admin'}"> 
	                                                    <i class="icon-user-md">岗位</i>
	                                                    <c:if test="${item.frozen=='0'}">
	                                                    <i class="icon-unlock">冻结</i>
	                                                    </c:if>
	                                                    <c:if test="${item.frozen=='1'}">
	                                                    <i class="icon-lock">解冻</i>
	                                                    </c:if>
	                                                    <c:choose>
	                                                    	<c:when test="${empty item.crm_id}">
	                                                    		<i class="icon-check-empty">开通crm</i>
	                                                    	</c:when>
	                                                    	<c:otherwise>
	                                                    		<i class="icon-check">已开通crm</i>
	                                                    	</c:otherwise>
	                                                    </c:choose>
                                                    </c:if>
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
	<div id="importBox" class="modal hide fade" >
			<div class="modal-header">
				<h3 id="boxTitle">数据导入</h3>
			</div>
            
			<div class="modal-body">
                <iframe id="importFM" frameborder="0" width="470" height="350" src="/views/import.jsp">
                
                </iframe>
   			</div>

			<div class="modal-footer">
				<button type="button" data-dismiss="modal" class="btn">关闭</button>
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
				/**$("#batchDel").click(function(e) {
                   var checks = $("input.checkboxes:checked");
					if(checks.size()>0){
						var ids="";
						checks.each(function(index, element) {
                            if(ids=="")
								ids=$(this).val();
							else{
								ids = ids+","+$(this).val();
							}
                        });
						//dosomething
						if(confirm("确定删除吗")){
							$.post("/ucenter/users/remove",{"ids":ids},function(data){
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
                });**/
				//批量初始化
				$("#batchInit").click(function(e) {
                   var checks = $("input.checkboxes:checked");
					if(checks.size()>0){
						var ids="";
						checks.each(function(index, element) {
                            if(ids=="")
								ids=$(this).val();
							else{
								ids = ids+","+$(this).val();
							}
                        });
						//dosomething
						$.post("/ucenter/users/init",{"ids":ids},function(data){
							if(data.status==0){
								alert(data.msg);
							}
							else{
								alert(data.msg);
							}
						});
					}
					else{
						alert("请选择其中一行");	
					}
                });
			//绑定内容编辑
				$("i.icon-pencil").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						location.href="/ucenter/users/user/update/"+id;
					});		
                });
			//绑定删除按钮
				$("i.icon-trash").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						if(confirm("确定删除吗")){
							$.post("/ucenter/users/remove",{"id":id},function(data){
								if(data.status==0){
									$(element).parents("#list>tr").remove();
								}
								else
									alert(data.msg);
							}); 
					      }else{
					         //取消 
					      }
					});		
                });
			//绑定岗位按钮
				$("i.icon-user-md").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						var url = "/ucenter/users/user_stations/"+id;
						
						window.location.href=url;
					});		
                });	
				//绑定岗位按钮
				$("i.icon-unlock,i.icon-lock").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						var classname = $(this).attr("class");
						var opt="冻结账户";
						if(classname=='icon-lock'){
							opt="解冻账户";
						}
						if(confirm("确定"+opt+"吗?")){
							$.post("/ucenter/users/frozen",{"id":id},function(data){
								if(data.status==0){
									if(classname=='icon-lock'){
										$(element).removeClass("icon-lock");
										$(element).addClass("icon-unlock");
										$(element).text("冻结");
									}
									else{
										$(element).removeClass("icon-unlock");
										$(element).addClass("icon-lock");
										$(element).text("解冻");
									}
								}
								else
									alert(data.msg);
							}); 
					      }else{
					         //取消 
					      }
					});		
                });	
			//crm开通
			$("i.icon-check-empty").each(function(index, element) {
					$(this).click(function(e){
						var id = $(this).parent().attr("data-id");
						if(confirm("确定开通CRM账号吗?")){
							$.post("/ucenter/users/crm",{"id":id},function(data){
								if(data.status==0){
									alert("开通crm成功！");
									location.reload();
								}
								else
									alert(data.msg);
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
			
			$("#batchImport").click(function(){
				document.getElementById('importFM').contentWindow.location.href="/views/import.jsp";
			});
		});

	</script>

</html>