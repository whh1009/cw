<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>${title }--回收站</title>
    <link rel="shortcut icon" href="${ctx }/favicon.ico" />
    <script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
	<style>
		.cen{text-align:center;}
		table{
			font-size:90%;
		}
	</style>
	<script type="text/javascript">
	var userJson = ${userJson};
	var _page = 1;
	var _searchCondition = "";
	var _searchVal = "";
	var _userId = "";
	$(function() {
		var user = "<option value='0'>请选择</option>";
		for(var i=0;i<userJson.length;i++) {
			user+="<option value='"+userJson[i].user_id+"'>"+userJson[i].nick_name+"</option>";
		}
		$("#userSel").html(user);
		initSearch(1);
	});
	
	function search() {
		_searchCondition=$("#sType").val();
		_searchVal = $("#searchVal").val();
		_userId = $("#userSel").val();
		initSearch(1);
	}
	
	function initSearch(page) {
		_page = page;
		$.post("${ctx}/getDelBookList", {searchCondition:_searchCondition, searchVal:_searchVal, userId:_userId, page:_page}, function(data) {
			var content="";
			if(data&&data.list.length) {
				for(var i = 0; i < data.list.length; i++) {
					content+="<tr>";
					content+="<td>"+data.list[i].id+"</td>";
					content+="<td>"+data.list[i].book_name+"</td>";
					content+="<td>"+data.list[i].book_num+"</td>";
					content+="<td>"+data.list[i].nick_name+"</td>";
					content+="<td>"+data.list[i].add_time+"</td>";
					content+="<td><a href=\"javascript:deleteBook('"+data.list[i].id+"')\" title='删除'><span class='glyphicon glyphicon-trash' style='color:red'></span> </a>　<a href=\"javascript:recoveryBook('"+data.list[i].id+"')\" title='恢复'><span class='glyphicon glyphicon-send' style='color:darkviolet'></span><a></td>";
					content+="</tr>";
				}
				initPage(data.pageNumber, data.totalPage);
			}
			$("#content").html(content);
		});
	}
	
	function initPage(pageNumber, totalPage) {
		var page = "<nav><ul class='pagination'>";
		if (pageNumber == 1) {
			page += "<li class='disabled'><a href='javascript:void();' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
		} else {
			page += "<li><a href='javascript:initSearch(1)' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
		}
		if (pageNumber <= 5) {
			var endPage = 10;
			if (endPage <= totalPage) {
				endPage = 10;
			} else {
				endPage = totalPage;
			}
			for (var i = 1; i <= endPage; i++) {
				if (i == pageNumber) {
					page += "<li class='active'><a href='javascript:initSearch(" + i + ")'>" + i + " <span class='sr-only'></span></a></li>";
				} else {
					page += "<li><a href='javascript:initSearch(" + i + ")'>" + i + "</a></li>";
				}
			}
		} else {
			var startPage = pageNumber - 5;
			var endPage = pageNumber + 5;
			if (endPage >= totalPage) {
				endPage = totalPage;
			}
			for (var i = startPage; i <= endPage; i++) {
				if (i == pageNumber) {
					page += "<li class='active'><a href='javascript:initSearch(" + i + ")'>" + i + " <span class='sr-only'></span></a></li>";
				} else {
					page += "<li><a href='javascript:initSearch(" + i + ")'>" + i + "</a></li>";
				}
			}
		}
		if (pageNumber == totalPage) {
			page += "<li class='disabled'><a href='javascript:void();' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
		} else {
			page += "<li><a href='javascript:initSearch(" + totalPage + ")' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
		}
		page += "</ul></nav>";
		$("#page").html(page);
	}
	
	//
	function deleteBook(bId) {
		if(confirm("确定要删除吗？")) {
			$.post("${ctx}/deleteBookById", {bookId:bId}, function(data) {
				if(data=="1") {
					alert("删除成功");
					initSearch(_page);
				} else if(data=="-1") {
					alert("删除异常");
				} else if(data=="2") {
					alert("删除失败");
				} else {
					alert("未获取到图书ID");
				}
			});
		}
	}
	
	function recoveryBook(bId) {
		if(confirm("确定要恢复吗？")) {
			$.post("${ctx}/recoveryBookById", {bookId:bId}, function(data) {
				if(data=="1") {
					alert("恢复成功");
					initSearch(_page);
				} else if(data=="-1") {
					alert("恢复异常");
				} else if(data=="2") {
					alert("恢复失败");
				} else {
					alert("未获取到图书ID");
				}
			});
		}
	}
	</script>
  </head>
  
  <body>
  	<jsp:include page="_header.jsp"></jsp:include>
    <div class="container">
    	<div class="row">
    		<div class="col-lg-3">
				<select class="form-control" id="userSel">
					<option></option>
				</select>
			</div>
    		<div class="col-lg-3 col-sm-3">
    			<select class="form-control" id="sType">
    				<option value="book_name">书名</option>
    				<option value="book_num">书号</option>
    			</select>
    		</div>
    		<div class="col-lg-6 col-sm-7">
		    	<div class="input-group">
			      	<input type="text" id="searchVal" class="form-control" placeholder="模糊检索">
			      	<span class="input-group-btn">
			        	<button class="btn btn-success" type="button" onclick="search(1)"><span class="glyphicon glyphicon-search"></span>&nbsp;检索</button>
			      	</span>
			    </div>
			</div>
			
		</div>
		<div class="row">
			<table class="table table-hover table-bordered" style="text-align:center">
				<thead>
					<tr>
						<th class='cen'>编号</th>
						<th class='cen'>书名</th>
						<th class='cen'>书号</th>
						<th class='cen'>录入</th>
						<th class='cen'>创建时间</th>
						<th class='cen' style="width:80px">操作</th>
					</tr>
				</thead>
				<tbody id="content"></tbody>
			</table>
		</div>
		<div id="page"></div>
    </div>
  </body>
</html>
