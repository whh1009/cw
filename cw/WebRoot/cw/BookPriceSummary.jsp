<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }--图书价格汇总</title>
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<style>
table {
	font-size: 90%;
}

.form-group {
	margin-right: 3em;
}
</style>

<link href="${ctx }/css/showLoading.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="all" href="${ctx }/css/daterangepicker-bs3.css" />
<script src="${ctx }/js/jquery.showLoading.min.js"></script>
<script src="${ctx }/js/moment.js"></script>
<script src="${ctx }/js/daterangepicker.js"></script>
<script type="text/javascript">
	
</script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3">
				<div class="input-group">
			    	<span class="input-group-addon">
			        	<label><input type="radio" name="timeRadio" value="0" checked onclick="timeSel(1)" />&nbsp;&nbsp;<i class="glyphicon glyphicon-calendar"></i></label>
			      	</span>
			      	<input type="text" style="width: 200px" name="singletime" id="singletime" class="form-control" value="" readonly />
	    		</div>
    		</div>
    		<div class="col-sm-5">
	    		<div class="input-group">
			    	<span class="input-group-addon">
			        	<label><input type="radio" name="timeRadio" value="1" onclick="timeSel(2)" />&nbsp;&nbsp;<i class="glyphicon glyphicon-calendar"></i> <i class="glyphicon glyphicon-calendar"></i></label>
			      	</span>
			      	<input type="text" style="width: 400px" name="reservationtime" id="reservationtime" class="form-control" value="" readonly />
	    		</div>
	    	</div>
	    	<script type="text/javascript">
				$(document).ready(function() {
					//取消readonly后退键
					$("input[readonly]").keydown(function(e) { 
						e.preventDefault();
					});
					$('#singletime').daterangepicker({
							format: 'YYYY-MM',
							singleDatePicker:true,
							startDate: moment(),
							maxDate: moment()
					});
					$('#reservationtime').daterangepicker({
							format: 'YYYY-MM',
							maxDate: moment()
						}
					);
				});
			</script>
			<div class="col-sm-4">
    			<button type="button" class="btn btn-info" onclick="initSearch(1)"><i class='glyphicon glyphicon-search'></i> 查询</button>
    		</div>
		</div>
		
		<div class="row" id="demo">
    		<div class="col-sm-4">
    			<label>检索条件：</label>
    			<select class="sType0 form-control">
    			</select>
    		</div>
    		<div class="col-sm-4">
    			<label>检索值：</label>
    			<input type="text" class="form-control sVal0" placeholder="模糊检索">
    		</div>
    		<div class="col-sm-4">
    			<br />
    			<small style="cursor:pointer" data-toggle="tooltip" data-placement="right" title="添加检索条件" onclick="addSearchConditionBtn()"><i class="glyphicon glyphicon-plus"></i></small>
    			<span id="span1">&nbsp;&nbsp;&nbsp;&nbsp;数量：0.0</span>&nbsp;
    			<span id="span2">&nbsp;&nbsp;&nbsp;金额：0.0</span><br />
    			<span id="span3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;版税：0.0</span>&nbsp;
    			<span id="span4">&nbsp;&nbsp;&nbsp;利润：0.0</span>
    		</div>
    	</div>
    	
		<div class="row">
			<table class="table table-hover" id="tableContent">
				<thead></thead>
				<tbody></tbody>
			</table>
			<div id="page"></div>
		</div>
	</div>
	<script>
		var _page = 1;
		var _mySearchSql= "";
		var searchConditionJson = ${searchConditionJson};
		var conditionCount=0;
		$(function() {
			//initPlatform();
			//initSaleTime();
			initTableHeader();
			
			$('[data-toggle="tooltip"]').tooltip();
	    	initSearchSel(".sType"+conditionCount);
	    	
		});
		
		//初始化第三方平台
		function initPlatform() {
			$.post("${ctx}/book/getDistinctPlatform", {}, function(data) {
				var platformSel = "<option value='0'>请选择平台</option>";
				if(data) {
					console.log(data);
					for(var i=0;i<data.length;i++){
						if(data[i].platform==1) {
							platformSel+="<option value='1'>基本表</option>";
						} else if(data[i].platform==2) {
							platformSel+="<option value='2'>亚马逊美国</option>";
						} else if(data[i].platform==3) {
							platformSel+="<option value='3'>亚马逊中国</option>";
						} else if(data[i].platform==4){
							platformSel+="<option value='4'>App Store</option>";
						} else if(data[i].platform==5) {
							platformSel+="<option value='5'>Over Drive</option>";
						}
					}
				}
				$("#platformSel").html(platformSel);
			});
		}
		
		function initSaleTime() {
			$.post("${ctx}/book/getDistinctSaleTime", {}, function(data) {
				var saleTimeSel = "<option value='0'>请选择月份</option>";
				if(data) {
					for(var i=0;i<data.length;i++){
						saleTimeSel+="<option value='"+data[i].sale_time+"'>"+data[i].sale_time+"</option>";
					}
				}
				$("#saleTimeSel").html(saleTimeSel);
			});
		}
		
		function initPriceCount() {
			$.post("${ctx}/book/getBookPriceCount", {mySearchSql:_mySearchSql}, function(data){
				console.log(data);
				$("#span1").html("&nbsp;&nbsp;&nbsp;&nbsp;数量："+data.scount);
				$("#span2").html("&nbsp;&nbsp;&nbsp;金额："+data.srmb);
				$("#span3").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;版税："+data.royalty);
				$("#span4").html("&nbsp;&nbsp;&nbsp;利润："+(data.srmb-data.royalty-data.cb).toFixed(2));
			});
		}
		
		//初始化表格标题
		function initTableHeader() {
			var header = "<tr><th>ISBN</th><th>书名</th><th>基本信息</th><th>销售时间</th><th>成本</th><th>销售金额</th><th>销售总量</th><th>利润</th></tr>";
			$("#tableContent thead").html(header);
		}
		
		
		
		function initSearch(page) {
			_mySearchSql = "";
			getSearchCondition();
			console.log("condition:"+_mySearchSql);
			$.ajax({
				url:"${ctx}/book/getBookSaleByPlatform",
				method:"POST",
				data:{page:page, mySearchSql: _mySearchSql},
				beforeSend:function() {
					$("#tableContent").showLoading();
				},
				success:function(data) {
					$("#tableContent tbody").html("");
					$("#tableContent").hideLoading();
					if(data) {
						if(data.totalRow>0) {
							var content= "";
							console.log(data);
							var basePrice = 0.0; //转码成本
							var lirun = 0.0; //利润
							var banshui = 0.0 //版税成本
							for(var i = 0; i<data.list.length; i++) {
								basePrice = (data.list[i].pdf_price+data.list[i].epub_price+data.list[i].ad_price+data.list[i].yz_price+data.list[i].ext_price1+data.list[i].ext_price2+data.list[i].ext_price3+data.list[i].ext_price4+data.list[i].ext_price5).toFixed(2);
								banshui = ((data.list[i].author_royalty/100.00)*data.list[i].srmb).toFixed(2);
								lirun = (data.list[i].srmb-basePrice-banshui).toFixed(2);
								content+="<tr><td style='vertical-align: middle;'>"+data.list[i].book_isbn+"</td>";
								content+=    "<td style='vertical-align: middle;'>"+data.list[i].book_name+"</td>";
								content+=    "<td style='vertical-align: middle;'>作者："+data.list[i].book_author+"<br />文种："+data.list[i].book_lang+"<br />转码公司："+data.list[i].trans_company+"<br />出版社："+data.list[i].is_self+"</td>";
								content+=    "<td style='vertical-align: middle;'>"+data.list[i].stime+"</td>";								
								content+=    "<td style='vertical-align: middle;'>版税："+banshui+"<br />转码："+basePrice+"</td>";
								content+=    "<td style='vertical-align: middle;'>人民币："+data.list[i].srmb+"<br />美　元："+data.list[i].sdollar+"</td>";
								content+=    "<td style='vertical-align: middle;'><b>"+data.list[i].scount+"</b></td>";
								if(lirun>0) {
									content+=    "<td style='vertical-align: middle;'><b>"+lirun+"</b></td>";
								} else{
									content+=    "<td style='vertical-align: middle;'><b><font color='red'>"+lirun+"</font></b></td>";
								}
								/*
								if(data.list[i].platform==2) {
									content+="<td style='vertical-align: middle;'>亚马逊美国</td>";
								} else if(data.list[i].platform==3) {
									content+="<td style='vertical-align: middle;'>亚马逊中国</td>";
								} else if(data.list[i].platform==4) {
									content+="<td style='vertical-align: middle;'>App Store</td>";
								} else if(data.list[i].platform=5) {
									content+="<td style='vertical-align: middle;'>Over Drive</td>";
								}
								*/
								content+="</tr>";
							}
							$("#tableContent tbody").html(content);
							initPage(data.pageNumber, data.totalPage, data.totalRow);
							initPriceCount();
						} else {
							$("#tableContent tbody").html("<tr><td><font color='red'>暂无数据</font></td></tr>");
							$("#page").html("");
						}
					}
				},
				error:function() {
					$("#tableContent").hideLoading();
					console.log("---error---");
				}
			});
		}
		
		//初始化分页
	    function initPage(pageNumber, totalPage, totalRow) {
			var page = "<nav><ul class='pagination'><li><span>共 "+totalRow+" 条，"+totalPage+" 页</span></li>";
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
			
	  //初始化检索条件select
	    function initSearchSel(arg) {
	    	if(searchConditionJson) {
	    		var option = "<option value='0' selected>请选择</option>";
	    		for(key in searchConditionJson) {
	    			option+="<option value='"+key+"'>"+searchConditionJson[key]+"</option>";
	    		}
	    		$(arg).html(option);
	    	}
	    }
	  
	  //添加检索条件框
	function addSearchConditionBtn() {
    	conditionCount++;
    	var content = "<div class='row'>";
    	content+="<div class='col-sm-4'><select class='sType"+conditionCount+" form-control'><option value='0'>请选择</option></select></div>";
    	content+="<div class='col-sm-4'><input type='text' class='form-control sVal"+conditionCount+"' placeholder='模糊检索'></div>";
    	content+="<div class='col-sm-4'><small style='cursor:pointer' data-toggle='tooltip' data-placement='top' title='删除检索条件' onclick='removeSearchConditionBtn(this)'><i class='glyphicon glyphicon-minus'></i></small></div>";
    	content+="</div>";
    	$("#demo").after(content);
    	$('[data-toggle="tooltip"]').tooltip();
    	initSearchSel(".sType"+conditionCount);
    	
    }
    
    //删除检索条件框
    function removeSearchConditionBtn(arg) {
    	$(arg).parent().parent().remove();
    }
		
  	//检索
    function getSearchCondition(){
    	var timeSel = $("input[name='timeRadio']:checked").val();
    	if(timeSel=="0") {
    		var singleTime = $("#singletime").val();
    		if(singleTime!="") {
    			_mySearchSql+=" and s.sale_time = '"+singleTime+"'";
    		}
    	} else {
    		var reservationTime = $("#reservationtime").val();
    		if(reservationTime!="") {
    			_mySearchSql+=" and s.sale_time between '"+reservationTime.split(" \- ")[0]+"' and '"+reservationTime.split(" \- ")[1]+"'";
    		}
    	}
    	$("select[class^=sType]").each(function() {
    		var s = $(this).val();
    		var temp = $(this).attr("class").replace(" form-control","").replace("sType", "");
    		var v = $(".sVal"+temp).val();
    		if(s!="0") {
    			if(s=="book_isbn"||s=="book_name") {
    				_mySearchSql+=" and b."+s+" like '%"+v+"%'";
    			} else {
    				_mySearchSql+=" and s."+s+" like '%"+v+"%'";
    			}
    			
    		}
    	});
    }
  
	function timeSel(arg){
		if(arg==1) {
			$("input[name='timeRadio']").each(function(i){
				if(i>0){
					$("#reservationtime").val("");
				}
			});
		} else {
			$("input[name='timeRadio']").each(function(i){
				if(i<1){
					$("#singletime").val("");
				}
			});
		}
  }
  </script>
</body>
</html>
