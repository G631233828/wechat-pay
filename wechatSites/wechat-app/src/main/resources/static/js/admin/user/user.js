	
	
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
		$("#commentForm").validate();
		var a = "<i class='fa fa-times-circle'></i> ";
		$("#userForm").validate({
			rules : {
				userName : {
					required : true,
					minlength : 2
				},
				
				accountName : {
					required : true,
					minlength : 2,
					remote : {
						url :getRootPath()+ "/user/ajaxgetRepletes",
						type : "POST",
						data : {
							accountName : function() {
								return $("#accountName").val();
							}
						},
						dataType : "json",
						dataFilter : function(data, type) {
							var jsondata = $.parseJSON(data);
							if (jsondata.status == 200) {
								return true;
							}
							return false;
						}
					}
				},
				
				passWord : {
					required : true,
					minlength : 5
				},
				passWord2 : {
					required : true,
					minlength : 5,
					equalTo : "#passWord"
				},

				roleId : {
					required : true,
				},
			},
			messages : {
				userName : {
					required : a + "请输入您的姓名",
					minlength : a + "用户名必须两个字符以上"
				},
				accountName : {
					required : a + "请输入您的帐号",
					minlength : a + "帐号必须两个字符以上",
					remote : a + "该帐号已经被注册"
				},
				passWord : {
					required : a + "请输入您的密码",
					minlength : a + "密码必须5个字符以上",
				},
				passWord2 : {
					required : a + "请再次输入密码",
					minlength : a + "密码必须5个字符以上",
					equalTo : a + "两次输入的密码不一致",
				},
				roleId : {
					required : a + "请选择分配角色"
				}
			}
		});
	/* 	$("#username").focus(function() {
			var c = $("#firstname").val();
			var b = $("#lastname").val();
			if (c && b && !this.value) {
				this.value = c + "." + b
			}
		}) */
	});

	// 手机号码验证
	jQuery.validator.addMethod("isMobile", function(value, element) {
	    var length = value.length;
	    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
	    return this.optional(element) || (length == 11 && mobile.test(value));
	}, "请正确填写您的手机号码");








