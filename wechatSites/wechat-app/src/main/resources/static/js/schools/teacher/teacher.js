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
	
	$("#file-pretty input[type='file']").prettyFile();
	
	
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
					teacherEmail  :{
						required : true,
						email  : true,
					},
					cardType :{
						required:true,
					},
					cardId:{
						required:true,
					}
					
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
					teacherEmail  :{
						required : a+ "请输入邮箱地址",
						email   : a+"请输入正确的邮箱地址",
					},
					cardType:{
						required: a+"请选择证件类型",
					},
					cardId:{
						required: a+"请输入证件号码",
					}

				}
			});

		});

jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "手机号有问题");




/**
 * 批量导入
 * @returns
 */
function batchImput(){
	
	$("#mybatchUpload").modal('show');
	
	
}



$(document)
.ready(
		function() {
			$('#submit')
					.bind(
							'click',
							function() {
								var eventFun = function() {
									$
											.ajax({
												type : 'GET',
												url : 'teacher/uploadprocess',
												data : {},
												dataType : 'json',
												success : function(
														data) {
													$("#proBar")
															.attr(
																	"style",
																	"width:"
																			+ (data.nownum / data.allnum)
																			* 100
																			+ '%');
													$('#proBar')
															.css(
																	'aria-valuenow',
																	data.nownum
																			+ '%');
													$('#proBar')
															.css(
																	'aria-valuemax',
																	data.allnum
																			+ '%');
													$(
															'#proBartext')
															.text(
																	"正在导入第"
																			+ data.nownum
																			+ "条记录，总共"
																			+ data.allnum
																			+ "条记录");
													if (data.nownum == data.allnum) {
														window
																.clearInterval(intId);
													}
												}
											});
								};
								var intId = window.setInterval(
										eventFun, 100);
							});
		});









	
	