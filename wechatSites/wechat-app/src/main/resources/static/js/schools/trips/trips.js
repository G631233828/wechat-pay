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
			$("#tripsForm").validate({
				rules : {
					"sites.id": {
						required : true,
					},	
					distance : {
						required : true,
						number:true
					},
					sorts : {
						required : true,
						number:true
					},
				},
				messages : {
					"sites.id" : {
						required : a + "请选择站点",
					},
					distance : {
						required : a + "请输入与上一站距离，起点请输0",
						number: a+"请输入一个合法的数字！",
					},
					sorts : {
						required : a + "请输入排序",
						number: a+"请输入一个合法的数字！",
					},

				}
			});

		});


function toSort(o){
	var sorts = $("#"+o+"_sorts").val();
	
	var flag = $.isNumeric(sorts);
	if(!flag){
		alert("请输入正确的数字！");
		return;
	}
	
	$.ajax({
		type : 'POST',
		url : "trips/edit",
		data : "id=" + o+"&sorts="+sorts,
		dataType : "json",
		success : function(data) {
			if(data.status == 200){
				window.location.href = "triplist"+data.msg;
			}else{
				alert("未能获取到数据，请刷新");
			}
			
			
		}
	});
	
	
}

function editDistance(o){
	var distance = $("#"+o+"_distance").val();
	var flag = $.isNumeric(distance);
	if(!flag){
		alert("请输入正确的数字！");
		return;
	}
	$.ajax({
		type : 'POST',
		url : "trips/edit",
		data : "id=" + o+"&distance="+distance,
		dataType : "json",
		success : function(data) {
			if(data.status == 200){
				window.location.href = "triplist"+data.msg;
			}else{
				alert("未能获取到数据，请刷新");
			}
		}
	});
	
}









	
	