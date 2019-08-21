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
			$("#clazzForm").validate({
				rules : {
					clazzYear : {
						required : true,
						digits:true,
						minlength:4,
						maxlength:4,
					},	
					clazzNum : {
						required : true,
						digits:true,
					},
				},
				messages : {
					clazzYear : {
						required : a + "请输入班级年份",
						digits   : a + "请输入有效数字",
						minlength: a + "请输入有效的年份",
						maxlength: a + "请输入有效的年份",
					},
					clazzNum : {
						required : a + "请输入班级号",
						digits  : a + "请输入有效数字",
					},

				}
			});

		});


	
	