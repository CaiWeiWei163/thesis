//是否加载左边的菜单和选中顶部菜单
var checkBoolean = true;
$(function(){

	
	//加载用户信息
	getLoginSysUser();
	//加载主页面菜单
	getMenuFirst();
	
	
	if (checkBoolean == true) {
		localStorage.checkBoolean = "1";
	}
	
	if (localStorage.checkBoolean == "1") {
		//判断是否存在 顶部菜单
		var topMenuId = localStorage.topMenuId;
		
		//得到默认页面的地址
		var newPathName = window.location.pathname;
		var newPathNameCopy = newPathName.replace(TDFPath,"");
		//不相等则设置leftMenuUrl 的缓存
		if ((localStorage.leftMenuUrl != null && localStorage.leftMenuUrl != "#" && localStorage.leftMenuUrl!="") && (localStorage.leftMenuUrl != newPathNameCopy)) {
			
			//localStorage.leftMenuUrl = newPathNameCopy;
		}
		
		if (topMenuId) {
			$("#topMenu li").find("a").removeClass("layui-this")
			//$("#topMenu li").removeClass("layui-this");
		} else {
			topMenuId = $("#topMenu li").eq(0).find("a").attr("key");
			localStorage.topMenuId = topMenuId;
		}
		
		$("#topMenu li").find("a").each(function(i,obj){
			if($(obj).attr("key")==topMenuId){
				$(obj).addClass("layui-this");
				return false;
			}
		})
		getMenuChildren(topMenuId);
	}
	

	
	
	
})


function getMenuFirst(){
	
	$.ajax({
		url:TDFPath+"sysMenuInfo/ajax/getTopMenu",
		type:"get",
		cache:false, 
		async:false, 
		dateType:"json",
		success: function(data) {
			$("#topMenu").html("");
			var topMenuHtml="";
			for(var i=0;i<data.length;i++) {
				topMenuHtml+='<li class="layui-nav-item">';
				topMenuHtml+='<a href="javascript:void(0)" onClick="getMenuChildren(\''+data[i].id+'\')" key="'+data[i].id+'">'+data[i].menuname+'</a>'
				topMenuHtml+='</li>';
			}
			$("#topMenu").html(topMenuHtml);
			
		}
	});
}


function getMenuChildren(getMenuChildrenId){
	
	$("#leftMenu").html("");

	if (localStorage.topMenuId!=getMenuChildrenId) {
		localStorage.leftMenuUrl = ""
		localStorage.leftMenuUrlId ="";
	}
	$("#topMenu li").find("a").removeClass("layui-this");
	//$("#topMenu li").removeClass("layui-this");
	$("#topMenu li").find("a").each(function(i,obj){
		if($(obj).attr("key")==getMenuChildrenId){
			
			$(obj).addClass("layui-this");
			return false;
		}
	})


	$.ajax({
		url:TDFPath+"sysMenuInfo/ajax/getMenuChildren",
		data:{parentMenuId:getMenuChildrenId},
		type:"get",
		cache:false, 
		async:false, 
		dateType:"json",
		success: function(data) {
			localStorage.topMenuId = getMenuChildrenId;
			if (data.length>0) {
				if (localStorage.leftMenuUrl == "" && localStorage.leftMenuUrlId == "") {
					
					if (data[0].children.length>0) {
						leftFirstRecursion(data[0].id,data[0].children);
						leftMenuClick(leftFirstUrl,leftFirstId);
					} else {
						
						leftFirstUrl = data[0].url;
						leftFirstId = data[0].id;
						leftMenuClick(leftFirstUrl,leftFirstId);
					}
					leftMenu(data);
					$("#leftMenu").html("");
					
				} else {
					
					leftMenu(data);
					menuClickFirst();
				}
				
			} 
		}
	});

}
var leftFirstUrl="";
var leftFirstId ="";
function leftFirstRecursion(id, data) {
	if (data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].parentid == id) {
            	if (data[i].children.length>0) {
                	leftFirstRecursion(data[i].id, data[i].children)   //递归
                } else  {
                	if (leftFirstUrl == "") {
                		leftFirstUrl = data[i].url;
                		leftFirstId = data[i].id;
                	}
                	break;
            		
        		} 
                
              
               
            }
        }
    }
}



function leftMenu(menuInfo) {
	localStorage.checkBoolean = "1";
	$("#leftMenu").html("");
	var leftMenuVar = "";
	
	var leftMenuUrl =localStorage.leftMenuUrl;
	
	var leftMenuUrlId =localStorage.leftMenuUrlId;
	for(var i=0;i<menuInfo.length;i++) {
		var html="";
		menu = '';
		
		
		
		if ((menuInfo[i].url == "#" || menuInfo[i].url == "") && menuInfo[i].children.length<=0) {
			html +='<li class="layui-nav-item"> <a href="javascript:void(0);" key="'+menuInfo[i].url+'" key-id="'+menuInfo[i].id+'" onclick="leftMenuClick(\''+menuInfo[i].url+'\',\''+menuInfo[i].id+'\')">'+menuInfo[i].menuname+'</a>'
		} else if ((menuInfo[i].url == "#" || menuInfo[i].url == "") && menuInfo[i].children.length>0) {
			html +='<li class="layui-nav-item"> <a href="javascript:void(0);" key="'+menuInfo[i].url+'" key-id="'+menuInfo[i].id+'">'+menuInfo[i].menuname+'</a>'
		} else {
			if (leftMenuUrl == menuInfo[i].url && leftMenuUrlId == menuInfo[i].id) {
				html +='<li class="layui-nav-item layui-this"> <a href="javascript:void(0);" key="'+menuInfo[i].url+'" key-id="'+menuInfo[i].id+'" onclick="leftMenuClick(\''+menuInfo[i].url+'\',\''+menuInfo[i].id+'\')" >'+menuInfo[i].menuname+'</a>'
				
			} else {
				html +='<li class="layui-nav-item"> <a href="javascript:void(0);" key="'+menuInfo[i].url+'" key-id="'+menuInfo[i].id+'" onclick="leftMenuClick(\''+menuInfo[i].url+'\',\''+menuInfo[i].id+'\')" >'+menuInfo[i].menuname+'</a>'
				
			}
		}
		
		if (menuInfo[i].children.length>0) {
			html +=departmentRecursion(menuInfo[i].id,menuInfo[i].children);
		}
		html +='</li>';
		leftMenuVar+=html;
		
	}
		
		
	
	

	$("#leftMenu").html(leftMenuVar);
	layui.use( 'element', function() {
	      var element = layui.element; 
	      element.render()
	});
	
	$(".ddCheckClass").each(function(){
		$(this).parents("dd").addClass("layui-nav-itemed");
		$(this).parents("li").addClass("layui-nav-itemed");
	});
}






var menu = '';
function departmentRecursion(id, data) {
	if (data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].parentid == id) {
                menu += '<dl class="layui-nav-child">';
                var leftMenuUrl =localStorage.leftMenuUrl;
            	var leftMenuUrlId =localStorage.leftMenuUrlId;
                
                
            	if ((data[i].url == "#" || data[i].url == "") && data[i].children.length>0) {
            		menu += '<dd><a href="javascript:void(0);")">'+data[i].menuname+'</a>'
        		} else if((data[i].url == "#" || data[i].url == "") && data[i].children.length<=0) {
        			menu += '<dd><a href="javascript:void(0);" key="'+data[i].url+'" key-id="'+data[i].id+'" onclick="leftMenuClick(\''+data[i].url+'\',\''+data[i].id+'\',this)">'+data[i].menuname+'</a>'
        		} else {
        			if (leftMenuUrl == data[i].url && leftMenuUrlId == data[i].id) {
        				menu += '<dd class="layui-this ddCheckClass"><a href="javascript:void(0);" key="'+data[i].url+'" key-id="'+data[i].id+'" onclick="leftMenuClick(\''+data[i].url+'\',\''+data[i].id+'\',this)">'+data[i].menuname+'</a>'
        			} else {
        				menu += '<dd><a href="javascript:void(0);" key="'+data[i].url+'" key-id="'+data[i].id+'" onclick="leftMenuClick(\''+data[i].url+'\',\''+data[i].id+'\',this)">'+data[i].menuname+'</a>'
        				
        			}
        		}
                
              
                if (data[i].children.length>0) {
                	departmentRecursion(data[i].id, data[i].children)   //递归
                }
                menu += "</dd>"
                menu += "</dl>"
            }
        }
        return menu;
    }
}


//默认选中第一个
function menuClickFirst(){
	var leftMenuUrl =localStorage.leftMenuUrl;
	var leftId = localStorage.leftMenuUrlId;
	if (leftMenuUrl&&leftId) {
		$("#leftMenu li").find("a").each(function(){
			if(leftMenuUrl==$(this).attr("key") && leftId == $(this).attr("key-id")) {
				$(this).parent().addClass("layui-this");
				return false;
			} else {
				//$(this).parent().addClass("layui-nav-itemed");
			}
		})
	} else {
		$("#leftMenu li").eq(0).find("a").each(function(){
			var key=$(this).attr("key")
			if(key && key!=null && key!='null' && key!=''&& typeof key != "undefined"){
				$(this).parent().addClass("layui-this");
				//$(this).click();
				return false;
			} else {
				
				//$(this).parent().addClass("layui-nav-itemed");
			}
			
		})
	}
}

function leftMenuClick(url,id) {
	if(url && url!=null && url!='null' && url!='' && typeof url != "undefined"){
		
		localStorage.leftMenuUrl=url;
		if (id) {
			localStorage.leftMenuUrlId=id;
		} 
		
		
		window.location.href = TDFPath+url;
		
	} else {
		$("#mainContent").html("");
	}
}



function logout(){
	localStorage.checkBoolean = "";
	localStorage.leftMenuUrl="";
	localStorage.leftMenuUrlId="";
	localStorage.topMenuId = "";
	window.location.href=TDFPath+"logout";
}


function sysUsredata(name) {
	$(name).on("click",function(){
		$.ajax({
			url: TDFPath+'sysUser/ajax/getLoginSysUserRole',
			type:"get",
			cache:false,
			success:function(res){
				 layerOpenForm({
					title:"基本资料",
					content:$("#basicSysUsredataScript").html(),//使用模板
					formdata :res,   //name=>key
					filter:"basicSysUsredata"
					
				})
			}
			
		})
	})
}


function updateSysUsredata(name) {
	$(name).on("click",function(){
		$.ajax({
			url: TDFPath+'sysUser/ajax/getLoginSysUserRole',
			type:"get",
			cache:false,
			success:function(res){
				 layerOpenForm({
					title:"修改资料",
					content:$("#basicUpdateSysUsredataScript").html(),//使用模板
					formdata :res,   //name=>key
					filter:"basicUpdateSysUsredata",
					btnID:"editFormbtn",
					calbackfn:function(data){
					 	$.ajax({
					          url: TDFPath+'sysUser/ajax/updateSysUser',
					          type: 'post',
					          data: data.field,
					          success: function (res) {
					         	 if (res.code==0) {
					         		getLoginSysUser();
					         	 }
					          },
					          error:function(error){
					          }
					      });

					}
					
				 })
			}
		})
	})
}



//修改密码
function updatePwd(name) {
	$(name).on("click",function(){
		layerOpenForm({
			title:"修改密码",
			content:$("#basicUpdatePwdScript").html(),//使用模板
			filter:"basicUpdatePwddata",
			btnID:"editPwd",
			isAutoClose:false,
			calbackfn:function(data,index){
				var oldPwd= data.field.newPwd;
				var password = data.field.password;
				if (oldPwd != password) {
					
					layer.alert('两次密码不一致！清确认');
					//layer.close(index);
					return false;
				}
				
			 	$.ajax({
			          url: TDFPath+'sysUser/ajax/updatePersonalPwd',
			          type: 'post',
			          data: data.field,
			          success: function (res) {
			         	 if (res.code==0) {
			         		 
			         		logout();
			         	 }
			          },
			          error:function(error){
			          }
			      });

			}
			
		 })
	})
}



function getLoginSysUser(){
	$.ajax({
		url: TDFPath+'sysUser/ajax/getLoginSysUser',
		type:"get",
		cache:false,
		success:function(res){
			 $("#realnameId").html(res.realname)
		}
		
	})
}


function updateUrl() {
	//得到当前地址
	var domain = window.location.host;
	
	var stateObject = {};
    var title = "";
    var newUrl = 'http://'+domain+TDFPath+'index';
    //修改地址栏中的地址
    history.pushState(stateObject, title, newUrl);
   
}


function getIndexImage() {
	$.ajax({
		url: TDFPath+'sysItem/ajax/getItemKey',
		type:"get",
		data:{"itemKey":"image"},
		cache:false,
		success:function(res){
			if (res.code == 0 && res.data.length>0) {
				var varData = res.data;
				var html = '<img width="200px" height="60px;" src="'+TDFPath+varData[0].itemvalue+'">';
				
				$("#topImageId").html(html);
			}
			 
		}
		
	})
}


