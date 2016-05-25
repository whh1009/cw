<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<title>${title }</title>
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<style>
</style>




</head>
<body>

	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">ISBN</span>
					<input type="text" class="form-control" placeholder="ISBN" name="bookBaseModel.book_isbn" value="${bookBaseModel.book_isbn }">
				</div>
			</div>
			<div class="col-sm-8">
				<div class="input-group">
					<span class="input-group-addon">书名</span>
					<input type="text" class="form-control" placeholder="书名" name="bookBaseModel.book_name" value="${bookBaseModel.book_name }">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">责编</span>
					<input type="text" class="form-control" placeholder="责编" name="bookBaseModel.book_author" value="${bookBaseModel.book_author }">
				</div>
			</div>
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">文种</span>
					<input type="text" class="form-control" placeholder="文种" name="bookBaseModel.book_lang" value="${bookBaseModel.book_lang }">
				</div>
			</div>
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">时间</span>
					<input type="text" class="form-control" placeholder="时间" name="bookBaseModel.trans_time" value="${bookBaseModel.trans_time }" readonly>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">页数</span>
					<input type="text" class="form-control" placeholder="页数" name="bookBaseModel.page_count" value="${bookBaseModel.page_count }">
				</div>
			</div>
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">EPUB单价</span>
					<input type="text" class="form-control" placeholder="EPUB单价" name="bookBaseModel.epub_price" value="${bookBaseModel.epub_price }">
				</div>
			</div>
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">PDF单价</span>
					<input type="text" class="form-control" placeholder="PDF单价" name="bookBaseModel.pdf_price" value="${bookBaseModel.pdf_price }">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4">
				<div class="input-group">
					<span class="input-group-addon">作者版税</span>
					<input type="text" class="form-control" placeholder="作者版税" name="bookBaseModel.author_royalty" value="${bookBaseModel.author_royalty }">
				</div>
			</div>
		</div>
		
		<div class="row">
			<button class="btn btn-info pull-right">保存</button>
		</div>
	</div>
</body>
</html>
