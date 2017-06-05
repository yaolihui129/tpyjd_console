<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*" errorPage="" %>
<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="navbar-inner">
			<div class="container-fluid">
				<!-- BEGIN LOGO -->
				<a class="brand" href="/">
				<img src="/images/logo.png" alt="logo"/>
				</a>

				<!-- END LOGO -->
				<a href="javascript:;" class="btn-navbar collapsed" data-toggle="collapse" data-target=".nav-collapse">
				<img src="/views/media/image/menu-toggler.png" alt="" />

				</a>          

				<!-- END RESPONSIVE MENU TOGGLER -->            

				<!-- BEGIN TOP NAVIGATION MENU -->              

				<ul class="nav pull-right">
				<!-- BEGIN  CHANGE PLATFORM DROPDOWN -->

					<li class="dropdown">

						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<i class="icon-refresh"></i>
						<span class="username">平台切换</span>
							<i class="icon-angle-down"></i>
						</a>

						<ul class="dropdown-menu">
							<c:forEach items="${platforms}" var="item">
								<li><a href="${item.oathurl}?token=${LoginUserInfo.token}">${item.name}</a></li>
							</c:forEach>
						</ul>

					</li>

					<!-- END CHANGE PLATFORM DROPDOWN -->
					<!-- BEGIN USER LOGIN DROPDOWN -->

					<li class="dropdown user">

						<a href="#" class="dropdown-toggle" data-toggle="dropdown">

						<img alt="" width="30" src="${LoginUserInfo.headImg}" />

						<span class="username">${LoginUserInfo.userName}</span>

						<i class="icon-angle-down"></i>

						</a>

						<ul class="dropdown-menu">

							<li><a data-toggle="modal" href="#passBox"><i class="icon-user"></i> 密码修改</a></li>

							<li class="divider"></li>

							<li><a href="/loginOut"><i class="icon-key"></i> 退出</a></li>

						</ul>

					</li>

					<!-- END USER LOGIN DROPDOWN -->

				</ul>

				<!-- END TOP NAVIGATION MENU --> 

			</div>

		</div>

		<!-- END TOP NAVIGATION BAR -->

	</div>
    
    <div id="passBox" class="modal hide fade" data-focus-on="input:first">
    <form>
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
				<h3 id="boxTitle">修改密码</h3>
			</div>

			<div class="modal-body">
				<table>
					<tr>
						<td width="100" align="right">原密码：</td>
						<td>
							<input name="password0" id="password0" class="m-wrap" type="password" data-tabindex="1" placeholder="" />
						</td>
					</tr>
					<tr>
						<td width="100" align="right">新密码：</td>
						<td>
							<input name="password1" id="password1" class="m-wrap" maxlength="20" type="password" data-tabindex="1" placeholder="" />
						</td>
					</tr>
					<tr>
						<td width="100" align="right">确认密码：</td>
						<td>
							<input name="password2" id="password2" class="m-wrap" maxlength="20" type="password" data-tabindex="1" placeholder="" />
						</td>
					</tr>
					<tr>
						<td width="100" align="right" valign="top"><b>提示：</b></td>
						<td>
							1.至少为字母、数字、符号任意两者的组合;<br>
							2.不少于6位，不大于20位;<br>
							3.不可与原密码相同;
						</td>
					</tr>
				</table>
   			</div>

			<div class="modal-footer">
				<a data-dismiss="modal" class="btn">取消</a>
				<a id="submitBtn" class="btn red">确定</a>
			</div>
		</form>
	</div>