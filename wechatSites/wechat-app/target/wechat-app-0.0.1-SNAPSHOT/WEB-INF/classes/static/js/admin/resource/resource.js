// 根据选择的目录生成菜单
function changeType() {
	var type = $("#type").val();
	if (type == 1) {
		$("#parent").show();
		$("#parentmenu").css("display", "none");
		$("#parentId").attr("disabled", false);
		$("#parentmenu").attr("disabled", "disabled");
	} else if (type == 2) {
		$("#parent").show();
		$("#parentmenu").show();
		$("#parentId").attr("disabled", false);
		$("#parentmenu").attr("disabled", false);
	} else if (type == 0) {
		$("#parent").css("display", "none");
		$("#parentmenu").css("display", "none");
		$("#parentId").attr("disabled", "disabled");
		$("#parentmenu").attr("disabled", "disabled");
	}
}

function getparentmenu() {
	var type = $("#type").val();
	var parentId = $("#parentId").val();
	if (type == 2) {
		// 需要通过ajax加载对应的菜单列表
		$.ajax({
			type : 'POST',
			url : "getparentmenu",
			data : "parentId=" + parentId,
			dataType : "json",
			success : function(data) {
				var sale = "<option value=''>----所属菜单----</option>";
				$.each(data.data, function(index, item) {
					sale += "<option value=" + item.id + ">" + item.name
							+ "</option>";
				});
				$("#parentSubId").html(sale)
			}
		});
	}
}


function selectIcon(){
	$("#iconModal").modal('show');
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
					minlength : a + "资源名称必须两个字符以上"
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











