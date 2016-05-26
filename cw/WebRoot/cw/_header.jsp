<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="shortcut icon" href="${ctx }/favicon.ico" />

<link href="${ctx }/css/bootstrap.min.css" rel="stylesheet" />
<script src="${ctx }/js/bootstrap.min.js" type="text/javascript"></script>
<link href="${ctx }/css/header.css" rel="stylesheet" />
<style>
.container-fluid {
	padding-left: 55px;
	padding-right: 55px;
}
.glyphicon-trash, .fontRed{
	color:red;
}

</style>
<script>
$(function() {
	
})

function checkFile() {
		 var file = $("#file").val();
		 if(file==null||file==undefined||file=="") {
			 alert("请至少选择一个excel");
			 return false;
		 } else {
			 var ext = file.substring(file.lastIndexOf(".")+1, file.length).toLowerCase();
			 if(ext=="xlsx"||ext=="xls") {
				 return true;
			 } else {
				 alert("亲，建议上传07版本以上excel哦");
				 return false;
			 }
		 }
	 }
</script>
<!-- http://work.smarchal.com/twbscolor/css/e74c3cc0392becf0f1ffbbbc0 -->
	<nav class="navbar navbar-default" role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${ctx }/" style="padding:5px"><img class="" style="height:40" src="${ctx }/images/logo.png" title=" ${brand }" /> &nbsp;五洲传播出版社&nbsp;&nbsp;&nbsp;</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="${ctx }/">首页 <span class="sr-only">(current)</span></a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">数据导入 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="${ctx }/import/importBaseBookPage"><i class='glyphicon glyphicon-import'></i>&nbsp;图书基本信息</a></li>
							<li><a href="${ctx }/import/importAmazonCNBookPage"><i class='glyphicon glyphicon-import'></i>&nbsp;亚马逊（中国）</a></li>
							<li><a href="${ctx }/import/importAmazonUSBookPage"><i class='glyphicon glyphicon-import'></i>&nbsp;亚马逊（美国）</a></li>
							<li><a href="${ctx }/import/importAppStorePage"><i class='glyphicon glyphicon-import'></i>&nbsp;AppStore</a></li>
							<!-- <li><a href="${ctx }/import/importOverDrivePage"><i class='glyphicon glyphicon-import'></i>&nbsp;OverDrive</a></li> -->
						</ul>
					</li>
					<li><a href="${ctx }/book/bookList"><i class='glyphicon glyphicon-tasks'></i>&nbsp;图书列表</a></li>
					<li><a href="${ctx }/book/bookPriceList"><i class='glyphicon glyphicon-signal'></i>&nbsp;图书价格列表</a></li>
					<li><a href="${ctx }/import/manageExcelList"><i class='glyphicon glyphicon-remove'></i>&nbsp;清理上传数据</a></li>
				</ul>
				<!-- 
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
				 -->
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">欢迎您 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="${ctx }/user/signout">退出</a></li>
						</ul></li>
				</ul>
				
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
