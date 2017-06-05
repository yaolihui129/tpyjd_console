// JavaScript Document
//(function ($) {
!function ($) {
    $.fn.extend({
        formValidate: function (options) {
            //参数和默认值
            var defaults = {
            };

            var options = $.extend(defaults, options);

            return this.each(function () {
                var o = options;

                //将元素集合赋给变量 本例中是 ul对象 
                var obj = $(this);
				var inputs = obj.find("input");
				var flag=true;
				inputs.each(function(index, element) {
                    var type=$(element).attr("type");
					var mustbe = $(element).attr("must-be");
					var dataLen = $(element).attr("data-length");
					var dataLenMin = $(element).attr("data-length-min");
					var label = $(element).attr("data-label");
					var data_type = $(element).attr("data-type");
					var data_min = $(element).attr("data-min");
					var data_max = $(element).attr("data-max");
					
					if(mustbe==undefined)
						mustbe='';
					if(label==undefined)
						label='当前内容';
					
					if(mustbe=='true' && $(element).val()==''){
							alert(label+"不能为空！");
							flag=false;
							return false;
					}
					if(dataLen!=undefined && $(element).val().length>dataLen){
						alert(label+"字数不能超过"+dataLen+"!");
						flag=false;
						return false;
					}
					if(dataLenMin!=undefined && $(element).val().length<dataLenMin){
						alert(label+"字数不能小于"+dataLenMin+"!");
						flag=false;
						return false;
					}
					
					if(data_max!=undefined){
						var reg=/^[a-zA-Z0-9_]+$/; 
						if($(element).val().length!=0){    
							if(!reg.test($(element).val())){    
								alert(label+"应为数字类型!");
								flag=false;
								return false;   
							}
							else{
								if(parseInt($(element).val())>data_max){
									alert(label+"不能超过最大值"+data_max+"!");
									flag=false;
									return false; 
								}	
							}
						}
						else{
							alert(label+"不能为空!");
							flag=false;
							return false; 
						}  
					}
					
					if(data_min!=undefined){
						var reg=/^[a-zA-Z0-9_]+$/; 
						if($(element).val().length!=0){    
							if(!reg.test($(element).val())){    
								alert(label+"应为数字类型!");
								flag=false;
								return false;   
							}
							else{
								if(parseInt($(element).val())<data_min){
									alert(label+"不能小于最小值"+data_min+"!");
									flag=false;
									return false; 
								}	
							}
						}
						else{
							alert(label+"不能为空!");
							flag=false;
							return false; 
						}  
					}
					
					if(data_type!=undefined && $(element).val()!=''){
						if(data_type=='email'){
							if(!(/^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='number'){
							if(!(/^[0-9]+$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='mobile'){
							if(!(/^1[34578]\d{9}$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='tel'){
							if(!/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test($(element).val())){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='idcard'){
							if(!/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test($(element).val()) && !/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/.test($(element).val())){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='datetime'){
							//判断日期类型是否为YYYY-MM-DD格式的类型  
							var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
							//判断日期类型是否为YYYY-MM-DD hh:mm:ss格式的类型  
							var reg1 = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; 
							//判断日期类型是否为hh:mm:ss格式的类型    
							var reg2 = /^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
							
							if(!reg.test($(element).val()) && !reg1.test($(element).val()) && !reg2.test($(element).val())){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
							
						}
						else if(data_type=='ip'){
							if(!(/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='postcard'){
							if(!(/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type=='qq'){
							if(!(/^[1-9]\d{4,16}$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
						else if(data_type='url'){
							if(!(/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/.test($(element).val()))){
								alert(label+"格式不正确!");
								flag=false;
								return false;
							}
						}
					}
                });
				if(flag)
					obj.submit();
            });
        }
    });
} (window.jQuery);
//})(jQuery);