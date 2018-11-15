$.validator.setDefaults({
		highlight : function(a) {
			$(a).closest(".form-group").removeClass("has-success")
					.addClass("has-error")
		},
		success : function(a) {
			a.closest(".form-group").removeClass("has-error").addClass(
					"has-success")
		},
		errorElement : "span",
		errorPlacement : function(a, b) {
			if (b.is(":radio") || b.is(":checkbox")) {
				a.appendTo(b.parent().parent().parent())
			} else {
				a.appendTo(b.parent())
			}
		},
		errorClass : "help-block m-b-none",
		validClass : "help-block m-b-none"
	});
$().ready(function() {
			var a = "<i class='fa fa-times-circle'></i> ";
			$("#schoolForm").validate({
				rules : {
					name : {
						required : true,
						minlength : 2
					},	
					contactName : {
						required : true,
					},
					email : {
						email:true,
						required : true,
					},
					contactNumber:{
						required : true,
						isMobile : true
						// 自定义方法：校验手机号在数据库中是否存在
						// checkPhoneExist : true,
					}
				},
				messages : {
					name : {
						required : a + "请输入学校名称",
					},
					contactName : {
						required : a + "请输入联系人",
					},
					email : {
						required : a + "请输入正确邮箱",
					},
					contactNumber:{
						required : "请输入联系人手机",
						isMobile : "请输入正确的手机号"
					}

				}
			});

		});
	jQuery.validator.addMethod("isMobile", function(value, element) {
	    var length = value.length;
	    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
	    return this.optional(element) || (length == 11 && mobile.test(value));
	}, "手机号有问题");
	
	
	$(function(){
		var province =$("#hidprovince").val()==''?'上海市':$("#hidprovince").val();
		var city =$("#hidcity").val()==''?'上海市市直辖':$("#hidcity").val();
		var area =$("#hidarea").val()==''?'浦东新区':$("#hidarea").val();
		$('#distpicker').distpicker({
		    province: province,
		    city: city,
		    district: area
		  });
		
	})
	
	
	
	
	