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
			$("#sitesForm").validate({
				rules : {
					siteName : {
						required : true,
					},	
					x : {
						required : true,
						number:true,
					},
					y : {
						required : true,
						number:true,
					},
				},
				messages : {
					siteName : {
						required : a + "请输入站点名称",
					},
					x : {
						required : a + "请输入x坐标",
						number  : a + "请输入一个有效的x坐标",
					},
					y : {
						required : a + "请输入y坐标",
						number  : a + "请输入一个有效的y坐标",
					},

				}
			});

		});


	
	