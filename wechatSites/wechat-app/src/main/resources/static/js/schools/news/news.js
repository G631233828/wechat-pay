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
			$("#newsForm").validate({
				rules : {
					 title: {
						required : true,
						minlength:2,
					},	
					author :{
						required:true,
					},
				},
				messages : {
					title : {
						required : a + "请输入新闻标题",
						minlength: a + "新闻标题太短！",
					},
					author : {
						required : a + "请输入作者",
					},
					

				}
			});

		});



function release(o){
	
	swal({
		title : "您确定要发布当前的信息吗？",
		text : "发布后将无法撤回，请谨慎操作！",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#DD6B55",
		confirmButtonText : "是的，我要发布！",
		cancelButtonText : "让我再考虑一下…",
		closeOnConfirm : false,
		closeOnCancel : false
	}, function(a) {
		if (a) {
			$.ajax({
				type:'get',
				url:'news/release/'+o,
				success:function(data){
					swal("发布成功", "您已经成功发送了一则新闻", "success")
				}
				
			})
		} else {
			swal("已取消", "您取消了发布操作！", "error")
		}
	})
	
}









	