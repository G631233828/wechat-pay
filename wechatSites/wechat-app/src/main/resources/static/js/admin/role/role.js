$(function(){
	
	$("#allAuthor").click(function() {
//		var flag = $("[name=authorIds]:checkbox").is(':checked');
		var flag = $("[name=allAuthor]:checkbox").is(':checked');
		
//		flag = flag == false ? true : false;
		$("[name=authorIds]:checkbox").each(function() {
			$(this).prop("checked", flag);
		})
	})	
})


$.validator.setDefaults({
	highlight : function(a) {
		$(a).closest(".form-group").removeClass("has-success").addClass(
				"has-error")
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
	$("#roleForm").validate({
		rules : {
			roleName : {
				required : true,
				minlength : 2,
				remote : {
					url :"role/ajaxgetRepletes",
					type : "POST",
					data : {
						roleName : function() {
								return  $("#roleName").val() ;
						}
					},
					dataType : "json",
					dataFilter : function(data, type) {
						
						var hidroleName = $("#hidroleName").val();
						var roleName = $("#roleName").val();
						if(roleName == hidroleName){
							return true;
						}
						
						var jsondata = $.parseJSON(data);
						if (jsondata.status == 200) {
							return true;
						}
						return false;
					}
				}
			},

		},
		messages : {
			roleName : {
				required : a + "请输入角色名称",
				minlength : a + "用户名必须两个字符以上",
				remote   : a + "不能添加重复的角色"
			},
		}
	});
});

function author(o) {
	
	$("input[name='authorIds']").attr("checked",false);
	$("input[name='allAuthor']").attr("checked",false);
	$("#id").val(o);
	
	$.ajax({
		type : 'POST',
		url : "getAuthor",
		data : "id="+o,
		dataType : "json",
		success : function(data) {
			
			$.each(data.data, function(index, item) {
				 $(":checkbox[id='"+item.id+"']").prop("checked",true);
			});
			
		}
	});
	
	$("#authorModal").modal('show');
	


}

function toAuthor(){
	var id = $("#id").val();
	
	//获取所有选中的权限
	
	var authorIds = $("input[name='authorIds']:checked");
	//获取所有的id执行删除操作，使用ajax
	var str = "";
	$(authorIds).each(function() {
		str += this.value + ",";
	});
	var checkallPermission = str.substring(0, str.length - 1);
	
	// 需要通过ajax加载对应的菜单列表
	$.ajax({
		type : 'POST',
		url : "author",
		data : "checkallPermission=" + checkallPermission+"&id="+id,
		dataType : "json",
		success : function(data) {
		$("#myModal").modal('show');
		$("#modalbody").text(data.msg);
		}
	});

	
	
	
	
}











