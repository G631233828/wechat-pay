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
			$("#teacherForm").validate({
				rules : {
					 name: {
						required : true,
						minlength:2,
					},	
					contactsmobile : {
						required : true,
						isMobile : true,
					},
				},
				messages : {
					name : {
						required : a + "请输入班级年份",
						minlength: a + "姓名至少是2个字符",
					},
					contactsmobile : {
						required : a + "请输入班级号",
						isMobile  : a + "请输入正确的手机号",
					},

				}
			});

		});

jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "手机号有问题");


function batch(){
	
	$("#mybatch").modal('show');
	
	
}
	
	