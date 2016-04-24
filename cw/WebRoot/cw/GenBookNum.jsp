<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>书号创建--五洲传播出版社</title>
    <link rel="shortcut icon" href="${ctx }/favicon.ico" />
    <link href="${ctx }/css/bootstrap-wizard.css" rel="stylesheet"/>
    <link href="${ctx }/css/bootstrap-slider.css" rel="stylesheet"/>

    <script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/bootstrap-wizard.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/bootstrap-slider.js" type="text/javascript"></script>
    <style>
        /*
        .wizard-card {
            border-top: 1px solid #EEE;
            display:none;
            padding:35px;
            padding-top:20px;
            overflow-y:inherit;
        }
    */
    .slider.slider-horizontal {
    	width:310px;
    }
    </style>

</head>

<body>
<jsp:include page="_header.jsp"></jsp:include>
<div class="wizard" id="some-wizard" data-title="书号创建">
    <div class="wizard-card" data-cardname="card1">
        <h3>填写书名</h3>
        <div class="wizard-input-section">
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" class="form-control" id="bookName" name="bookName" placeholder="书名" data-validate="validateBookName" data-is-valid="0" data-lookup="0" maxlength="100" />
                </div>
            </div>
        </div>
    </div>

    <div class="wizard-card" data-cardname="card2">
        <h3>出版时间</h3>
        <div class="wizard-input-section">
            <select id="publishTime" class="form-control">
                <option value="116" selected>2016</option>
                <option value="115">2015</option>
                <option value="114">2014</option>
                <option value="113">2013</option>
                <option value="112">2012</option>
                <option value="111">2011</option>
                <option value="110">2010</option>
                <option value="109">2009</option>
            </select>
        </div>
    </div>
    <div class="wizard-card" data-cardname="card3">
        <h3>图书 & 期刊</h3>
        <!-- 
        <div class="wizard-input-section">
            <p>&nbsp;</p>
            <div class="radio">
                <label>
                    <input type="radio" name="bookType" id="tushu" value="ts" checked> 图书
                </label>
            </div>
            <div class="radio">
                <label>
                    <input type="radio" name="bookType" id="qikan" value="qk"> 期刊
                </label>
            </div>
        </div>
         -->
        <div class="wizard-input-section">
            <p>期刊号/ISBN后五位</p>
            <div class="form-group">
                <div class="col-md-8">
                    <input type="text" class="form-control" id="isbn" name="isbn" placeholder="请输入期刊号或ISBN后5位" data-validate="validateISBN" maxlength="5" />
                </div>
            </div>
        </div>
        <div class="wizard-input-section">
            <p>文种</p>
            <div class="form-group">
                <div class="col-md-8">
                    <div class="input-group">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="">文种 <span class="caret"></span></span>
                            </button>
                            <ul class="dropdown-menu"  style="overflow-y:visible;">
                                <li><a href="javascript:initWenzhong('001--英文');">001--英文</a></li>
                                <li><a href="javascript:initWenzhong('002--西文');">002--西文</a></li>
                                <li><a href="javascript:initWenzhong('003--中文');">003--中文</a></li>
                                <li><a href="javascript:initWenzhong('004--法文');">004--法文</a></li>
                                <li><a href="javascript:initWenzhong('005--德语');">005--德语</a></li>
                                <li><a href="javascript:initWenzhong('006--阿语');">006--阿语</a></li>
                                <li><a href="javascript:initWenzhong('007--俄语');">007--俄语</a></li>
                                <li><a href="javascript:initWenzhong('008--土文');">008--土文</a></li>
                                <li><a href="javascript:initWenzhong('009--日语');">009--日语</a></li>
                                <li><a href="javascript:initWenzhong('010--韩语');">010--韩语</a></li>
                                <li><a href="javascript:initWenzhong('011--意大利语');">011--意大利语</a></li>
                                <li><a href="javascript:initWenzhong('012--印尼文');">012--印尼文</a></li>
                                <li><a href="javascript:initWenzhong('013--哈萨克斯坦文');">013--哈萨克斯坦文</a></li>
                                <li><a href="javascript:initWenzhong('014--蒙文');">014--蒙文</a></li>
                                <li><a href="javascript:initWenzhong('015--藏文');">015--藏文</a></li>
                                <li><a href="javascript:initWenzhong('016--波斯文');">016--波斯文</a></li>
                                <li><a href="javascript:initWenzhong('017--柯尔克孜文');">017--柯尔克孜文</a></li>
                                <li><a href="javascript:initWenzhong('500--双语对应');">500--双语对应</a></li>
                            </ul>
                        </div>
                        <input type="text" class="form-control" name="wenzhong" id="wenzhong" placeholder="请选择" data-validate="validateWenZhong" readonly>
                    </div>
                </div>
            </div>
        </div><!--
        <div class="wizard-input-section qk">
            <p>期刊号</p>
            <div class="form-group">
                <div class="col-md-8">
                    <div class="input-group">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                <span class="">期刊号 <span class="caret"></span></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="javascript:initQiKanHao('01');">01</a></li>
                                <li><a href="javascript:initQiKanHao('02');">02</a></li>
                                <li><a href="javascript:initQiKanHao('03');">03</a></li>
                                <li><a href="javascript:initQiKanHao('04');">04</a></li>
                                <li><a href="javascript:initQiKanHao('05');">05</a></li>
                                <li><a href="javascript:initQiKanHao('06');">06</a></li>
                                <li><a href="javascript:initQiKanHao('07');">07</a></li>
                                <li><a href="javascript:initQiKanHao('08');">08</a></li>
                                <li><a href="javascript:initQiKanHao('09');">09</a></li>
                                <li><a href="javascript:initQiKanHao('10');">10</a></li>
                                <li><a href="javascript:initQiKanHao('11');">11</a></li>
                                <li><a href="javascript:initQiKanHao('12');">12</a></li>
                            </ul>
                        </div>
                        <input type="text" class="form-control" name="qikanhao" id="qikanhao" placeholder="请选择期刊号" data-validate="validateQiKanHao" readonly>
                    </div>

                </div>
            </div>
        </div> -->
        <div class="wizard-input-section">
            <p>出版数量: <span id="ex1CurrentSliderValLabel"> &nbsp; <span id="ex1SliderVal">1</span></span></p>
            <div class="form-group">
                <div class="col-md-12">
                    <input id="publishCount" data-slider-id='ex1Slider' type="text" data-slider-min="1" data-slider-max="1" data-slider-step="1" data-slider-value="1"/>
                    
                </div>
            </div>
        </div>
    </div>
    <div class="wizard-card" data-cardname="card4">
        <h3>出版形式</h3>
        <div class="wizard-input-section">
            <div class="form-group">
                <div class="col-sm-8">
                    <label class="radio">
                        <input type="radio" name="pubType" value="1" checked> 纯E-only
                    </label>
                    <label class="radio">
                        <input type="radio" name="pubType" value="2"> 半E-only合并
                    </label>
                    <label class="radio">
                        <input type="radio" name="pubType" value="3"> 半E-only拆分
                    </label>
                    <hr />
                    <label class="radio">
                        <input type="radio" name="pubType" value="4"> 期刊
                    </label>
                    <hr />
                    <label class="radio">
                        <input type="radio" name="pubType" value="5"> 英文公版书
                    </label>
                    <label class="radio">
                        <input type="radio" name="pubType" value="6"> 阿文公版书
                    </label>
                    <label class="radio">
                        <input type="radio" name="pubType" value="7"> 西文公版书
                    </label>
                </div>
            </div>
        </div>
    </div>
    <div class="wizard-card" data-cardname="card5">
        <h3>生成书号</h3>
        <div class="wizard-input-section">
            <div class="form-group">
                <div class="col-sm-12">
                	<p>
                		<span id="bname"></span>
                		<hr />
                	</p>
                	
                    <p id="bookNum" style="width:430px;height:330px"></p>
                </div>
                </div>
        </div>
    </div>

</div>
<script>
    $(function() {
        var wizard = $("#some-wizard").wizard({
            keyboard: false,
            contentHeight: 600,
            contentWidth: 700,
            backdrop: 'static'
        });
        wizard.show();

        $('#bookName, #isbn').on('input', function() {
            if ($(this).val().length > 0) {
                $('#press').parents('.form-group').removeClass('has-error has-success');
            }
        });


        //初始化
        //$("#press").val("五洲传播出版社");
        $("#benshe").on('click', function() {
            $("#press").val("五洲传播出版社");
        });
        $("#qita").on('click', function() {
            $("#press").val("");
        });

        $(".qk").hide();//默认期刊不显示
        $("#tushu").on('click', function() {
            $(".qk").hide();
            $(".ts").show();
        });
        $("#qikan").on('click', function() {
            $(".ts").hide();
            $(".qk").show();
        });

        //初始化slider 出版数量
        $("#publishCount").slider();
        $("#publishCount").on("slide", function(slideEvt) {
            $("#ex1SliderVal").text(slideEvt.value);
        });


        wizard.on("submit", function(wizard) {
            var card1 = wizard.cards["card1"];
            var card2 = wizard.cards["card2"];
            var card3 = wizard.cards["card3"];
            var card4 = wizard.cards["card4"];
            //var card5 = wizard.cards["card5"];
            var bookName = $("#bookName").val();
            var bookNum="";
            var publishTime = $("#publishTime").val();
            var isbn = $("#isbn").val();
            var bookType = $("input[name='bookType']:checked").val();
            var pubCount = $("#ex1SliderVal").html(); //出版数量
            var wenzhong = $("#wenzhong").val();
            wenzhong=wenzhong.substr(0,3);
            var pubType = $("input[name='pubType']:checked").val(); //出版形式
            var pubCountInt = parseInt(pubCount);
           	for(var i = 0; i < pubCountInt; i++) {
           		var newIsbn = parseInt(isbn)+i
           		if(newIsbn<=99999) {
           			bookNum += checkISBN13(publishTime+newIsbn+wenzhong+pubType)+",";
           		}
           	}
            
            /*
            if(bookType=="ts") { //图书
                var wenzhong = $("#wenzhong").val();
                bookNum+=wenzhong.substr(0,3)+"-";
            } else { //期刊
                bookNum+="000"+"-";
            }
            */
            $("#bname").html("《"+bookName+"》");
            var bookHtml = "";
            for(var i = 1; i <= bookNum.split(",").length; i++) {
            	if(i%3==0) {
            		bookHtml+=bookNum.split(",")[i-1]+"&nbsp;&nbsp;<br />";
            	} else {
            		bookHtml+=bookNum.split(",")[i-1]+"&nbsp;&nbsp;";
            	}
            }
            $("#bookNum").html(bookHtml);
            $.ajax({
                url: "${ctx}/saveBookNum",
                type: "POST",
                data: {bookName:bookName.replace(/'/g, "’"), bookNum:bookNum},
                success: function(data) {
                	if(data=="-1") {
                		alert("参数获取失败");
                	} else if(data=="-2") {
                		alert("入库失败，原因：有已有相同书号！");
                	} else {
                		alert("共入库"+data+"个书号");
                		window.location.href = "${ctx}/bookList";
                	}
                    wizard.submitSuccess(); // displays the success card
                    wizard.hideButtons(); // hides the next and back buttons
                    wizard.updateProgressBar(0); // sets the progress meter to 0
                },
                error: function() {
                    wizard.submitError(); // display the error card
                    wizard.hideButtons(); // hides the next and back buttons
                }
            });
        });
    });


    function validateBookName(el) {
        var name = el.val();
        var retValue = {};
        if (name == "") {
            retValue.status = false;
            retValue.msg = "请输入书名";
        } else {
            retValue.status = true;
        }
        return retValue;
    }

    function validateISBN(el) {
        var name = el.val();
        var retValue = {};
        //if($(".ts").css("display")=="block") {
            if (name == "" || isNaN(name) || name.length != 5) {
                retValue.status = false;
                retValue.msg = "请输入5位数字";
            } else {
                retValue.status = true;
            }
        //} else {
        //    retValue.status = true;
        //}
        return retValue;
    }

    function validateQiKanHao(el) {
        var name = el.val();
        var retValue = {};
        if($(".qk").css("display")=="block") {
            if (name == "") {
                retValue.status = false;
                retValue.msg = "请选择期刊号";
            } else {
                retValue.status = true;
            }
        } else {
            retValue.status = true;
        }
        return retValue;
    }

    function validateWenZhong(el) {
        var name = el.val();
        var retValue = {};
        //if($(".ts").css("display")=="block") {
            if (name == "") {
                retValue.status = false;
                retValue.msg = "请选择文种";
            } else {
                retValue.status = true;
            }
        //} else {
        //    retValue.status = true;
        //}
        return retValue;
    }

    function initWenzhong(wz) {
        $("#wenzhong").val(wz);
    }

    function initQiKanHao(qkh) {
        $("#qikanhao").val(qkh);
    }
    
    /**
    * isbn前12位
    * 生成书号校验码
    */
    function checkISBN13(code){
        code=(code+'').replace(/[-\s]/g,'');
        if(!/^\d{12,13}$/.test(code))
            return "生成ISBN校验位错误";
        var i=1,c=0; // c:checksum
        for(;i<12;i+=2)
            c+=Math.floor(code.charAt(i));
        for(c*=3,i=0;i<12;i+=2)
            c+=Math.floor(code.charAt(i));
        c=(220-c)%10; // 220:大於(1*6+3*6)，%10==0即可。
        if(code.length==12)
            return code+c;
        return c==code.charAt(12);
    }
    

</script>
</body>
</html>
