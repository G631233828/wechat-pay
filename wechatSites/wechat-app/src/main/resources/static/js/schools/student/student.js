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
			$("#studentForm").validate({
				rules : {
					 name: {
						required : true,
						minlength:2,
					},	
					clazz : {
						required : true,
					},
					code  :{
						required : true,
					},
					registerNumber  :{
						required : true,
						remote : {
							url  : getRootPath()+"/student/findStudentByRegisterNum",
							type : "POST",
							data : {
								registerNumber : function() {
									return $("#registerNumber").val();
								}
							},
							dataType : "json",
							dataFilter : function(data, type) {
								var jsondata = $.parseJSON(data);
								if (jsondata.status == 200 && jsondata.msg == 'false') {
									return true;
								}
								return false;
							}
						}
					},
				},
				messages : {
					name : {
						required : a + "请输入学生姓名",
						minlength: a + "姓名至少是2个字符",
					},
					clazz : {
						required : a + "请选择所属班级",
					},
					code  :{
						required : a+ "请输入班级学号",
					},
					registerNumber  :{
						required : a+ "请输入学籍号",
						remote: "该学籍号已经存在"
					},
				}
			});

		});



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
												url : 'student/uploadprocess',
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









	
	