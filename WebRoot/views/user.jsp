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
    
    <script src="/bower_components/lrz/dist/lrz.bundle.js" type="text/javascript"></script>
    
   
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

							<li><a href="/ucenter/users">用户管理</a><i class="icon-angle-right"></i>
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
											<c:if test="${empty user}">    
												<div class="control-group"> 
														<div class="controls"> 
														<span class="m-wrap span2" style="margin-top: 10px; "><b style="color:#FF003B;">*</b>登录名：</span> 
															<input placeholder="" class="m-wrap span6" type="text" data-label="登录名" must-be="true" data-length="15" data-length-min="1" name="login_name" id="login_name" value="">
														</div>
												</div> 
 
	                                                <div class="control-group"> 
														<div class="controls">
															<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>密码：</span> 
															<input placeholder="" class="m-wrap span6" type="password" data-label="密码" must-be="true" data-length="15" data-length-min="6" name="password" id="password" value="888888">
														</div>
	
													</div>
											</c:if> 
	                                           <c:if test="${!empty user}">  
	                                           	<div class="control-group"> 
														<div class="controls"> 
														<span class="m-wrap span2" style="margin-top: 10px; "><b style="color:#FF003B;">*</b>登录名：</span> 
															<input placeholder="" disabled="true" class="m-wrap span6" type="text" data-label="登录名" must-be="true" data-length="15" data-length-min="1" name="login_name" id="login_name" value="${user.loginName}">
														</div>
												</div> 
	                                                <div class="control-group"> 
														<div class="controls">
															<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>密码：</span> 
															<input placeholder="" class="m-wrap span6" type="password" data-label="密码" must-be="true"  data-length="15" data-length-min="6" name="password" id="password" value="888888">
														</div> 
													</div>
												</c:if>
												<div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;">工号：</span>  
														<input placeholder="员工工号" maxlength="50" class="m-wrap span6" type="text" data-label="工号" must-be="true" data-length="50" name="job_number" id="job_number" value="${user.jobNumber}">
													</div>
												</div>
													
                                                <div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>姓名：</span>  
														<input placeholder="必填项，顾问姓名，最多可输入40个字符。" maxlength="40" class="m-wrap span6" type="text" data-label="姓名" must-be="true" data-length="40" name="user_name" id="user_name" value="${user.userName}">
													</div>
												</div>
												<!-- 新增字段 -->
												<div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>主岗：</span>  
														<input placeholder="必填项，主岗，最多可输入20个字符。" maxlength="20" class="m-wrap span6" type="text" data-label="主岗" must-be="true" data-length="20" name="master_post" id="master_post" value="${user.masterPost}">
													</div>
												</div>
												<div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>职务级别：</span>  
														<input placeholder="必填项，职务级别，最多可输入20个字符。" maxlength="20" class="m-wrap span6" type="text" data-label="职务级别" must-be="true" data-length="20" name="post_level" id="post_level" value="${user.postLevel}">
													</div>
												</div>
												<div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;">座机：</span>  
														<input placeholder="座机电话，最多可输入20个字符。" maxlength="20" class="m-wrap span6" type="text" data-label="座机电话"  data-length="20" name="tel" id="tel" value="${user.tel}">
													</div>
												</div>
												<!-- 结束 -->
												 <div class="control-group">  
													<div class="controls">
												     	<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>英文名：</span>
														<input placeholder="必填项，顾问英文名，最多可输入40个字符。" maxlength="40" class="m-wrap span6" type="text" data-label="英文名" must-be="true" data-length="40" name="english_name" id="english_name" value="${user.englishName}">
													</div>
												</div>
											
												<div class="control-group">  
													<div class="controls">
													<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>性别：</span>   
                                                    ${gender}
													</div> 
												</div>
											
											
												<div class="control-group"> 
												 	<div class="controls">
												 		<span class="m-wrap span2" style="margin-top: 10px;">头像：</span>   
														<input id="pdtPic" name="pdtPic" multiple accept="image/*" type="file" style="display: none;">
														 <!-- <div  style="float: left;">
														 	<img   id="uploadShowImg" src="/images/addImg.jpg" style="height:100px;width:100px; border: 1px solid #C2C2C2;" >
														 </div> -->
														<div style="float: left;   width:100px;height:100px;overflow:hidden;position:relative;">
															<c:if test="${!empty user.headImg}">
																<img  id="showImg" src="${user.headImg}" style="position:absolute;left:-1px;top:-1px;width:102px;height:102px;">
															</c:if>
															<c:if test="${empty user.headImg}">
																<img  id="showImg" style="position:absolute;   top:2px;  right: -1px; left:1px; bottom:1px; width:97px;height:97px;" src="/images/headImg.png">
															</c:if>
															<input type="hidden" id="head_img" name="head_img" value="${user.headImg}" />
														 </div>   
														 <span style="margin-top: 75px;float:left;color:#A1A1A1;font-size: xx-small;">图片格式:支持<b style="color:#FF3B00;">jpg,png</b>格式图片，尺寸为<b style="color:#FF3B00;">156px*156px</b>以下,大小不超过<b style="color:#FF3B00;">50kb</b>.</span>
													</div>
												</div>
												
												<!-- 
												<div class="control-group"> 
													<div class="controls">  
												    	<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>头衔：</span>   
														<input placeholder="必填项，如“资深移民专家”，最大可输入10个汉字" maxlength="10" class="m-wrap span6" type="text" data-label="头衔"  must-be="true" data-length="10" name="job_title" id="job_title" value="${user.jobTitle}">
													</div> 
												</div>
												 -->
                                                <div class="control-group">
													<div class="controls"> 
                                                		<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>手机号：</span> 
														<input placeholder="必填项，请输入11位的数字合法手机号码。" maxlength="11"  class="m-wrap span6" type="text" data-label="手机号" data-type="mobile" must-be="true" data-length="11" name="phone" id="phone" value="${user.phone}">
													</div> 
												</div>
												
												 <div class="control-group">
													<div class="controls"> 
												 		<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>qq：</span> 
														<input placeholder="必填项，输入项是数字，最多可输入20个字符。" maxlength="20" class="m-wrap span6" type="text" data-label="qq" data-type="qq"  must-be="true" data-length="20" name="qq" id="qq" value="${user.qq}">
													</div> 
												</div>
												<div class="control-group">
													<div class="controls"> 
												 		<span class="m-wrap span2" style="margin-top: 10px;">座席号：</span> 
														<input placeholder="输入项是数字，最多可输入10个字符。" data-type="number" maxlength="10" class="m-wrap span6" type="text" data-label="座席号" data-length="10" name="seat_number" id="seat_number" value="${user.seatNumber}">
													</div> 
												</div>
												<!-- 
												<div class="control-group"> 
													<div class="controls">  
														<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>微信：</span>
														<input placeholder="必填项，最多可输入40个字符。" maxlength="40" class="m-wrap span6" type="text" data-label="微信" must-be="true" data-length="40" name="wechat" id="wechat" value="${user.wechat}">
													</div> 
												</div>
												 --> 
												 
												<div class="control-group">
													<div class="controls"> 
                                                		<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>电子邮箱：</span> 
														<input placeholder="必填项，请输入合法邮箱，最多可输入40个字符。" maxlength="40" class="m-wrap span6" type="text" data-type="email" data-label="电子邮箱" must-be="true" data-length="40" name="email" id="email" value="${user.email}">
													</div> 
												</div>
												<c:if test="${!empty user}"> 	
												<div class="control-group"> 
													<div class="controls"> 
                                                		<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>openid绑定状态：</span> 
													<select name="bind_status">
													 <c:choose>
													 		<c:when test="${'0'==user.bindStatus}">
													  			<option value="0" selected>未绑定</option>
													  		</c:when>
													  		<c:when test="${'1'==user.bindStatus}">
													  			<option value="1" selected>已绑定</option>
													  			<option value="2">已解绑</option>
													  		</c:when>
													  		<c:otherwise>
													  			<option value="2" selected>已解绑</option>
													  		</c:otherwise>
													 </c:choose>
													</select>
													</div> 
												</div>	
												</c:if>	
												<div class="control-group"> 
													<div class="controls"> 
                                                		<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>数据分流级别：</span> 
													<select name="data_range">
													 <c:forEach items="${dataRange.subDictionaries}" var="item">
													 	<c:choose>
													 		<c:when test="${item.value==user.dataRange}">
													  			<option value="${item.value}" selected>${item.name}</option>
													  		</c:when>
													  		<c:otherwise>
													  			<option value="${item.value}">${item.name}</option>
													  		</c:otherwise>
													  	</c:choose>
													  </c:forEach>
													</select>
													</div> 
												</div>	
												
												<div class="control-group"> 
													<div class="controls"> 
														<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>所在地区：</span> 
														<select  class="m-wrap span2" name="province_id" id="province_id">
															<c:forEach items="${provinces.subDictionaries}"	var="province">
																<c:choose>
																		<c:when test="${!empty user.provinceId && province.dictCode eq user.provinceId}">
																			<option value="${province.dictCode }" selected="selected">${province.name}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${province.dictCode }" >${province.name}</option>
																		</c:otherwise>
																</c:choose>
																
															</c:forEach>
														</select> 省/直辖市
														<select  class="m-wrap span2" name="city_id" id="city_id">
															<c:forEach items="${cities.subDictionaries}"	var="city">
																<c:choose>
																		<c:when test="${!empty user.cityId && city.dictCode eq user.cityId}">
																			<option value="${city.dictCode}" selected="selected">${city.name}</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${city.dictCode}" >${city.name}</option>
																		</c:otherwise>
																</c:choose>
															</c:forEach>
														</select> 市
													 
													 </div> 
												</div>	
												<!-- 
												<div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;"><b style="color:#FF003B;">*</b>所属岗位 ：</span> 
														${station}
													</div>
												</div>
												 -->
												
                                                <div class="control-group"> 
												  <div class="controls">所属部门 <font color="#FF0000" size="1">(务必选择其中一项)</font></div>

													<div class="controls portlet box" style="width:300px;height:300px; overflow-y:scroll">
														${tree}
														<input type="hidden" id="org_id" name="org_id" value="${user.depId}" />
													</div>
												</div>
												<!-- 
                                               	<div class="control-group"> 
													<div class="controls">
														<span class="m-wrap span2" style="margin-top: 10px;">个人简介：</span>  																												<%-- ${user.introduce} ${!empty user.provinceId}	${ provinces} --%>
														<textarea placeholder="选填项，一段文字介绍顾问，最多可输入100个汉字。"  maxlength="100" rows="7" class="m-wrap span6" type="text" data-label="个人简介" data-length="100" name="introduce" id="introduce">${user.introduce}</textarea>
													</div> 
												</div>
												 -->
												<div class="form-actions">
													<a id="subBtn" class="btn blue"><i class="icon-ok"></i> 保存</a>
													<a id="back"  class="btn">取消</a>
												</div>
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
			
		    $("#login_name").blur(function(e){
		    		if($(this).val()!=''){
			    		$.post("/ucenter/users/user/checkExist",{"loginname":$(this).val()},function(data){
			    			if(data.status>0){
			    				alert("当前用户名已存在");
			    				$("#login_name").val("");
			    				$("#login_name").focus();
			    			}
			    		});
		    		}
		    });
		    
		    $("#province_id").change(function(e){
			    	if($(this).val()!=''){
			    		$.post("/ucenter/users/user/cities",{"pcode":$(this).val()},function(data){
			    			
			    			if(data.status==0){
			    				$("#city_id").html(data.cities);
			    			}
			    			
			    		});
		    		}
		    });
		    
		    $("#job_number").blur(function(e){
		    		if($(this).val()!=''){
			    		$.post("/ucenter/users/user/checkJN",{"jobnumber":$(this).val()},function(data){
			    			if(data.status>0){
			    				alert("您填入的工号已经存在！");
			    				$("#job_number").val("");
			    				$("#job_number").focus();
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
                //额外特殊输入的验证
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
		
		
		$('#pdtPic').change(function() {
			var files = $('#pdtPic').prop('files');
			console.log(files);
			var imgUrl = $("#head_img").val();
			for(var m=0;m<files.length;m++){
			lrz(files[m],{quality :0.3})
			  .then(function (rst) {
				var filename = rst.origin.name;
				$.post("/fileupload", { "filename": filename, "content": rst.base64, "imgUrl":imgUrl},
					function(data){
						if(data.status==0){
							 $('#showImg').attr("src",data.picUrl);
							 alert('上传成功!');
							 $('#head_img').val(data.picUrl);
						}
						else {
							 $('#head_img').val('');
							alert(data.msg);
						}
					});
			  })
			  .always(function () {
				  // 不管是成功失败，都会执行
			  });
			}
		});
		$('#showImg').click(function(){ 
			$('#pdtPic').click(); 
		});  
	</script>

	<!-- END JAVASCRIPTS -->
<!-- END BODY -->

</html>