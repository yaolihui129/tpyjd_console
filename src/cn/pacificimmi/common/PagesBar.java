package cn.pacificimmi.common;

public class PagesBar {
	public static String getPageBar(int currentPage,int totlePage,int pageSize,int rows,String query){
		StringBuffer pageBar = new StringBuffer();
		pageBar.append("<div class=\"row-fluid\"><div class=\"span6\"><div class=\"dataTables_info\">");
		int start=0,end=0;
		if(rows>0){
			start = (currentPage-1)*pageSize +1;
			end = currentPage*pageSize;
			if(end>rows)
				end = rows;
		}
		
		pageBar
		.append(start)
		.append("至")
		.append(end)
		.append(",共")
		.append(rows)
		.append("条记录</div></div>")
		.append("<div class=\"span6\"><div class=\"dataTables_paginate paging_bootstrap pagination\"><ul>");
		
		if(currentPage==1)
			pageBar.append("<li class=\"pages prev disabled\"><a>← <span class=\"hidden-480\">上一页</span></a></li>");
		else
			pageBar.append("<li class=\"pages prev\"  data-page=\""+(currentPage-1)+"\"><a>← <span class=\"hidden-480\">上一页</span></a></li>");
		
		for(int i=1;i<=totlePage;i++){
			if(i==currentPage)
				pageBar.append("<li class=\"pages active\"><a>"+i+"</a></li>");
			else
				pageBar.append("<li class=\"pages\"  data-page=\""+i+"\"><a>"+i+"</a></li>");
		}
		
		if(currentPage==totlePage)
			pageBar.append("<li class=\"pages next  disabled\"><a><span class=\"hidden-480\">下一页</span> → </a></li></ul></div></div></div>");
		else
			pageBar.append("<li class=\"pages next\" data-page=\""+(currentPage+1)+"\"><a><span class=\"hidden-480\">下一页</span> → </a></li></ul></div></div>");
		
		return pageBar.toString();
	}
	
	public static String getShortPageBar(int currentPage,int totlePage,int pageSize,int rows,int pages){
		StringBuffer pageBar = new StringBuffer();
		pageBar.append("<div class=\"row-fluid\"><div class=\"span6\"><div class=\"dataTables_info\">");
		int start=0,end=0;
		if(rows>0){
			start = (currentPage-1)*pageSize +1;
			end = currentPage*pageSize;
			if(end>rows)
				end = rows;
		}
		
		pageBar
		.append(start)
		.append("至")
		.append(end)
		.append(",共")
		.append(rows)
		.append("条记录</div></div>")
		.append("<div class=\"span6\"><div class=\"dataTables_paginate paging_bootstrap pagination\"><ul>");
		
		if(currentPage==1){
//			pageBar.append("<li class=\"pages prev disabled\"><a><span class=\"hidden-480\">首页</span></a></li>");
			pageBar.append("<li class=\"pages prev disabled\"><a><span class=\"hidden-480\">上一页</span></a></li>");
		}
		else{
			pageBar.append("<li class=\"pages prev\"  data-page=\"1\"><a><span class=\"hidden-480\">首页</span></a></li>");
			pageBar.append("<li class=\"pages prev\"  data-page=\""+(currentPage-1)+"\"><a><span class=\"hidden-480\">上一页</span></a></li>");
		}
		
		//只显示pages个页码
		if(pages>=totlePage){
			for(int i=1;i<=totlePage;i++){
				if(i==currentPage)
					pageBar.append("<li class=\"pages active\"><a>"+i+"</a></li>");
				else
					pageBar.append("<li class=\"pages\"  data-page=\""+i+"\"><a>"+i+"</a></li>");
			}
		}
		else{
			int half = pages/2;
			//显示省略号
			if(currentPage-half<=1){
				for(int i=1;i<=pages;i++){
					if(i==currentPage)
						pageBar.append("<li class=\"pages active\"><a>"+i+"</a></li>");
					else
						pageBar.append("<li class=\"pages\"  data-page=\""+i+"\"><a>"+i+"</a></li>");
				}
				pageBar.append("<li class=\"pages\" data-page=\""+(pages+1)+"\"><a>..</a></li>");
			}
			else if(currentPage+half>=totlePage){
				pageBar.append("<li class=\"pages\" data-page=\""+(totlePage-pages)+"\"><a>..</a></li>");
				for(int i=(totlePage-pages+1);i<=totlePage;i++){
					if(i==currentPage)
						pageBar.append("<li class=\"pages active\"><a>"+i+"</a></li>");
					else
						pageBar.append("<li class=\"pages\"  data-page=\""+i+"\"><a>"+i+"</a></li>");
				}
			}
			else{
				pageBar.append("<li class=\"pages\" data-page=\""+(currentPage-half-1)+"\"><a>..</a></li>");
				for(int i=currentPage-half;i<=currentPage+half;i++){
					if(i==currentPage)
						pageBar.append("<li class=\"pages active\"><a>"+i+"</a></li>");
					else
						pageBar.append("<li class=\"pages\"  data-page=\""+i+"\"><a>"+i+"</a></li>");
				}
				pageBar.append("<li class=\"pages\" data-page=\""+(currentPage+half+1)+"\"><a>..</a></li>");
			}
			
		}
		
		if(currentPage==totlePage)
			pageBar.append("<li class=\"pages next  disabled\"><a><span class=\"hidden-480\">下一页</span></a></li></ul></div></div></div>");
		else{
			pageBar.append("<li class=\"pages next\" data-page=\""+(currentPage+1)+"\"><a><span class=\"hidden-480\">下一页</span></a></li>");
			pageBar.append("<li class=\"pages next\" data-page=\""+(totlePage)+"\"><a><span class=\"hidden-480\">尾页</span></a></li>");
			pageBar.append("</ul></div></div>");
		}
		return pageBar.toString();
	}
}
