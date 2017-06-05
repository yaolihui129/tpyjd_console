<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>导入数据</title>
<style type="text/css">
body{
	font-size:12px;
	font-family: "Arial","Microsoft YaHei","黑体","宋体",sans-serif;
	}
</style>
</head>
<body>
<form id="importForm" method="post" action="/ucenter/users/importUsers" enctype="multipart/form-data">
<table width="100%" border="0" cellspacing="5" cellpadding="5">
  <tr>
    <td height="63">导入数据模版下载</td>
    <td><a href="/images/users.xls">下载</a></td>
  </tr>
  <tr>
    <td width="27%" height="63">数据文件：</td>
    <td width="73%"><label for="datafile"></label>
      <input type="file" name="datafile" id="datafile" accept="application/vnd.ms-excel"></td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td>仅支持.xls格式，且文件大小不能超过2M</td>
    </tr>
  <tr>
    <td height="39">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>导入重复数据处理:</td>
    <td><input type="radio" checked name="opt" value="0">
      跳过
        <input type="radio" name="opt" value="1">
      覆盖</td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input type="button" onclick="javascript:check()" name="button" id="button" value="立即导入"></td>
    </tr>
</table>
</form>

<script>
function check(){
	if(document.getElementById("datafile").value==''){
		alert("请选择导入数据文件!");
		return false;
	}
	
	document.getElementById("importForm").submit();
}
</script>
</body>
</html>