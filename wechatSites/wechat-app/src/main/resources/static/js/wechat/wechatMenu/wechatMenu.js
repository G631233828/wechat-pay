$(function() {
	// activate Nestable for list 1
	$('#nestable').nestable({
		group : 1,
		maxDepth : 2,
	});
	$('#nestable2').nestable({
		group : 2,
		maxDepth : 2
	});
	$('#nestable3').nestable({
		group : 1,
		maxDepth : 2
	});
})

function add(o) {
	if(o == 0){
		$("#parentId").val(0);
		$("#wechatMenuTitle").text("添加主菜单");
	}else{
		$("#parentId").val(o);
		$("#wechatMenuTitle").text("添加子菜单");
	}
	$("#wechatMenuAddModal").modal('show');

}

function edit(o,o1) {
	if(o == 0){
		$("#wechatMenuTitle").text("修改主菜单");
	}else if(o == 1){
		$("#wechatMenuTitle").text("修改子菜单");
	}
	$.ajax({
		type : 'POST',
		url : "wechatmenu/editMenu",
		data : "id=" + o1,
		dataType : "json",
		success : function(data) {
			
			if(data.status == 200){
				var data = data.data;
				$("#name").val(data.name);
				$("#parentId").val(data.parentId);
				$("#sort").val(data.sort);
				var type = data.type;
				$("#type").find("option[value='"+type+"']").attr("selected",true);
				 $("#view").val(data.key);
				 $("#id").val(data.id);
				 if(type == 'click'){
					 $("#click").show();
					 $("#url").val('');
					 $("#view").hide();
					 $("#url").attr("disabled","disabled");
					 $("#key").attr("disabled",false);
					 $("#key").val(data.key);
				 }else if(type =='view'){
					 $("#view").show();
					 $("#key").val('');
					 $("#click").hide();
					 $("#key").attr("disabled","disabled");
					 $("#url").attr("disabled",false);
					 $("#url").val(data.url);
				 }
				 $("#wechatMenuAddModal").modal('show'); 
			}else{
				
				$("#myModal").modal('show');
				$("#modalbody").text(data.msg);
				
			}
		}
	});
}


function selectType(){
	
 var type = $("#type").val();
 if(type == 'click'){
	 $("#click").show();
	 $("#url").attr("disabled","disabled");
	 $("#key").attr("disabled",false);
	 $("#view").hide();
 }else if(type =='view'){
	 $("#view").show();
	 $("#key").attr("disabled","disabled");
	 $("#url").attr("disabled",false);
	 $("#click").hide();
 }
	
	
}


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
	$("#resourceForm").validate({
		rules : {
			name : {
				required : true,
				minlength : 2
			},
			resKey : {
				required : true,
			},
			resUrl : {
				required : true,
			},

		},
		messages : {
			name : {
				required : a + "请输入资源名称",
				minlength : a + "用户名必须两个字符以上"
			},
			resKey : {
				required : a + "请输入资源权限标识"
			},
			resUrl : {
				required : a + "请输入访问路径"
			}
		}
	});
});

function release(){
	$.ajax({
		type : 'POST',
		url : "wechatmenu/release",
		dataType : "json",
		success : function(data) {
			$("#myModal").modal('show');
			$("#modalbody").text(data.msg);
			
		}
	});
}




