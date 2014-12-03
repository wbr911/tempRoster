function clearJobTable(){
	$("input[id^=job_t").val('');
	PF("job_t").filter();
}
$(window).keydown(function(e){
	if(e.keyCode==13 &&e.ctrlKey){
		$("#searchBtn").click();
	}
	
});

$(function(){
	
	$(document).on('click' ,'#job_t td',function(e){
		if($(this).find('.ui-row-toggler').size()!=1 && $(this).find(".ui-chkbox").size()==0 && $(this).find(".glyphicon-edit").size()==0){
		$(this).parent().find('.ui-row-toggler').click();
		}
//		else if($(this).find(".glyphicon-edit").size()!=0){
//			e.stopPropagation();
//			$(this).find("a").click();
//			
//		}
	});
	$('.ui-autocomplete-input').each(function(){
		$(this).css('border','none');
		$(this).css('box-shadow','none');
	});
$('#requiredVar_panel').mouseup(function(e){
		$('#requiredVar_input').change();
	});
$('#optionalVar_panel').mouseup(function(e){
	$('#optionalVar_input').change();
});
$('#excludedVar_panel').mouseup(function(e){
	$('#excludedVar_input').change();
}); 
$('a:contains(Job)').click(function(){    // job tab click
	$('#hiddenHeaderClearBtn').click();
  $('#jobView_refresh_btn').click();
  
     });
updateHeader();
$(window).scroll(function(){

	  if($(window).scrollTop()>43){
	    $('#candDetailDialog').css('top','0px');
	  }else{
	      $('#candDetailDialog').css('top','40px');
	    }
	  }
	);
}); // END of $()

$(window).load(function(){
	clearTable();
	$(".ui-column-customfilter").click(function(event){event.stopPropagation();});
	this.refresh_header_interval = setInterval(refreshHeader,10000);
	}
	);

function refreshHeader(){
	$("#hiddenHeaderRefreshBtn").click();
	
}
function updateTable(tableName){
	PF(tableName).filter();
	$(".ui-column-customfilter").click(function(event){event.stopPropagation();});
	
}
$(window).resize(function(){
	$("#candDetailDialog").css('height' , $(window).height());
});

function clearTable(){
	$("input[id^=candidates_t]").val('');
	PF('candStatusFilter').selectValue('');
	PF('candTitleFilter').selectValue('');
	PF('candGenderFilter').selectValue('');
	PF('candEducationFilter').selectValue('');
	updateTable("candidates_t");
	$('#searchLabel').slideUp();
}
function initStatusLabel(){
	$('#candidates_t').find('label:contains(available)').addClass('label-success');
	$('#candidates_t').find('label:contains(rest)').addClass('label-default');
	$('#candidates_t').find('label:contains(chosen)').addClass('label-warning');
	$('#candidates_t').find('label:contains(placed)').addClass('label-info');
}
var intervalName;
var timeout;
var inputId;
var type;
function addTag(id,_type){
	inputId = id;
	type= _type;
	intervalName= setInterval(handle,10); 
	$('#' + inputId+'_input').val('~not exist');
}
function handle(){
	  var value = $('#' + inputId+'_input').val();
	  if(value!='~not exist'){
		  _addTag(inputId,type,value);
	      clearInterval(intervalName);
	  }
}
function _addTag(id,type ,value){
	 var tag="";
	if(value!=''){
		 switch (type){		
		 case 'certificates':
			 tag= '<label class="ui-outputlabel ui-widget label label-info" style="display:inline; float:left;" >'+value+'<a class="glyphicon glyphicon-remove" style="color:white;" onclick="removeTag(this)"></a>'+'</label>';
			 break;
		 case 'userDefine':
			 tag = '<label class="ui-outputlabel ui-widget label label-warning" style="display:inline; float:left;" >'+value+'<a class="glyphicon glyphicon-remove" style="color:white;" onclick="removeTag(this)"></a>'+'</label>';
			 break;
		 default:
			 tag = '<label class="ui-outputlabel ui-widget label label-primary" style="display:inline; float:left;" >'+value+'<a class="glyphicon glyphicon-remove" style="color:white;" onclick="removeTag(this)"></a>'+'</label>';
		 	break;
		 }
		  $('#' + id + '_input').val('');
		  $('#' + id + '_input').css('width','20px');
		  $('#' + id).before(tag);
		  }

}

function removeTag(div){
	$(div).parent().remove();
}
//----------------------------------------tag input---------------------------------------
function keyListener(event){
	if(event.keyCode==13){
		var id = '#' + event.target.id.replace('_input','')+'_panel';
		if($(id).css('display')!='block'){
		      _addTag(event.target.id.replace('_input',''),
		    		  $(event.target).parent().attr('id').replace('Var','') ,
		    		  $("#"+event.target.id).val());

		}else{
		$('#'+event.target.id).change();
		}
    
	event.preventDefault(); 
	window.event.returnValue = false;
	}
	if(event.keyCode==8){
		if($(event.target).val()==''){
		var tags = $(event.target).parent().prevAll('label');
		if(tags.length != 0){
			tags[0].remove();
		}
		}
	}
}
function changeLength(event){
	$('#'+event.target.id).css('width',20+$('#'+event.target.id).val().length*10+'px');
	}
//--------------------------------end of tag input---------------------------------------------
//------------------------------header----------------------------------------------------
function updateHeader(){
	var number = parseInt($('#newAcceptNumber').text());
	$('#newAcceptNumber').removeClass();
	$('#newAcceptNumber').removeAttr('style');
	$('#newRefuseNumber').removeClass();
	$('#newRefuseNumber').removeAttr('style');
	if( number !=0){
		$('#newAcceptNumber').addClass('badge');
		$('#newAcceptNumber').css('background-color','#5bc0de !important');
	}
	number = parseInt($('#newRefuseNumber').text());
	if(number !=0 ){
		$('#newRefuseNumber').addClass('badge');
		$('#newRefuseNumber').css('background-color','#f0ad4e !important');
	}
}

//-----------------------end of header----------------------------------------------------
//-------------------------------detail dialog-------------------------------------------
function showCandInfoEditView(){
	 $('#candDetailDialog').find('.input_').show();
	   $('#candDetailDialog').find('.output_').hide();
}
function hideCandInfoEditView(){
   $('#candDetailDialog').find('.input_').hide();
   $('#candDetailDialog').find('.output_').show();
}

function showCandDetail(){

	if($('#statusDetail').text() == 'available'){
		$('#candDetail_toolPanel').show();
	}
	$('#candDetailDialog').css('height' , $(window).height());
	$('#candDetailDialog').show();
	if($('#candDetailDialog').css('right')!='0px'){
	var width=$('#candDetailDialog').width()+$('#candDetailDialog').css('padding-left').replace('px','')*2;
	$('#candDetailDialog').css('right','-'+width+'px');}
	$('#candDetailDialog').animate({right:'0px' },500);
	_initCandDetail();
}
function showCreateCandView(){
	$('#candDetailDialog').find('.output_').hide();
	$('#candDetailDialog').find('.input_').show();
	$('#candDetailDialog').find('#createTool').show();
	$('#candDetail_closeBtn').hide();
	$('#candInfoEdit_tool').hide();
	
	$('#candCreateConfirm_tool').show();
	showCandDetail();
	$('#candDetail_toolPanel').hide();
	
	PF('mainViewBlock').show();
}

function closeCandDetail(){
	var width=$('#candDetailDialog').width()+$('#candDetailDialog').css('padding-left').replace('px','')*2;
	$('#candDetailDialog').animate({right:'-'+width+'px' },500);

}
function _initCandDetail(){
	switch ($('#statusDetail').text()){
	case 'available' :
		$('#statusDetail').addClass('label label-success');
	    break;
	case  'chosen':
		$('#statusDetail').addClass('label label-warning');
	    $('#chosenDetail').show();
	break;
	case 'placed':
		$('#statusDetail').addClass('label label-info');
	    $('#placedDetail').show();
	break;
	case 'rest':
		$('#statusDetail').addClass('label label-danger');
	break;
	}
	
	 show_matched_tags()
}
function show_matched_tags(){
	if($("#searchLabel").css('display')!="none"){
		var matchedTags = $("#matchedTagStr").text().toLowerCase();
		$("#tagsContainer").find("label").each(function(index){
			if(index!=0){
				if(matchedTags.indexOf($(this).text().toLowerCase()+"/")!=-1){
					$(this).css("border-left","10px solid red");
					$(this).prepend("<span class='glyphicon glyphicon-ok' style='color:red;line-height:0.8' ></span>");
				}
			}
		});
	}
}
function showTagsEditView(){
	$('#skillsVar_panel').mouseup(function(e){
		$('#skillsVar_input').change();
	});
$('#certificatesVar_panel').mouseup(function(e){
	$('#certificatesVar_input').change();
});
$('#userDefineVar_panel').mouseup(function(e){
	$('#userDefineVar_input').change();
});
	$('.ui-autocomplete-input').each(function(){
		$(this).css('border','none');
		$(this).css('box-shadow','none');
	});
	var width =$('#picContainer').width() +parseInt($('#picContainer').css('padding-left').replace('px',''))*2;
	$('#tagsEditView').css('left',width+'px');
	var min_height=$('#tagsContainer').parent().height() + 'px';

	$('#tagsEditView_content').css('min-height',min_height);
	$('#skillsVar').before($($('#tagsContainer').find('.row')[1]).html());
	$('#certificatesVar').before($($('#tagsContainer').find('.row')[2]).html());
	$('#userDefineVar').before($($('#tagsContainer').find('.row')[3]).html());
	$('#tagsEditView').find('.edit').each(function(){
		$(this).css('display','inline');
	});
	$('#tagsEditViewDetail').show();
	$('#tagsEditView').show();
	$('#tagsEditView').animate({'left':'0%','width':'100%','opacity':'1'} ,200);
	$('#skillsVar_input').focus();
	
}
function hideTagsEditView(){
	var width =$('#picContainer').width() +parseInt($('#picContainer').css('padding-left').replace('px',''))*2;
	$('#tagsEditView').animate({'left':width+'px','opacity':'0.5'},200,
			function(){
				$('#tagsEditView').hide();
			    $('#skillsVar').prevAll().remove();
				$('#certificatesVar').prevAll().remove();
				$('#userDefineVar').prevAll().remove();
			});
}
function parseCandTag(id,suffix){
	var tags = '';
	tags =_parseTag('#skillsVar'+suffix , tags);
	tags =_parseTag('#certificatesVar'+suffix , tags);
	tags =_parseTag('#userDefineVar'+suffix , tags);
	$('#'+id).val(tags);
	hideTagsEditView();
}
function _parseTag(id,tags){
	tags +='{';
		$(id).parent().find('.label').each(function(){
			tags += $(this).text() + ',';	
		});
		if(tags.charAt(tags.length-1)==','){
			tags = tags.substring(0,tags.length-1);
		}
	tags += '}';
	return tags;
}

//-------------------------------end of detail dialog-------------------------------------------
//--------------------------------advanced search-----------------------------------------------
function fillSearchTag(){
	remove_searchView_tags();
	var tags = $("#searchTags").text();
	if(tags==""){
		return;
	}
	tags = tags.replace(new RegExp("{","gm"),"");
	var tagsList = tags.split("}");
	var idList = new Array("requiredVar","optionalVar","excludedVar");
	for (var i = 0; i < 3; i++) {
		var tagStr = tagsList[i];
		if(tagStr.length==0){
			continue;
		}
		if(tagStr.charAt(tagStr.length-1)==","){
			tagStr = tagStr.substring(0,tagStr.length-1);
		}
		var tagsArray = tagStr.split(",");
		for(var key in tagsArray){
			_addTag(idList[i], "search" , tagsArray[key]);
		}
	};

}
function parseSearchTag(){
	var tags = '';
	tags =_parseTag('#requiredVar' , tags);
	tags =_parseTag('#optionalVar' , tags);
	tags =_parseTag('#excludedVar' , tags);
	$('#hiddenSearchTag').val(tags);
	//remove_searchView_tags();
}
function remove_searchView_tags(){
	$('#advanced_panel').find('.label.label-primary').remove();
}
//--------------------------end of advanced search-----------------------------------------------
//--------------------------job view-------------------------------------------------------------
function initJobStatusLabel(){
	$('#jobView').find('label:contains(accepted)').addClass('label-success');
	$('#jobView').find('label:contains(refused)').addClass('label-warning');
	$('#jobView').find('label:contains(no response)').addClass('label-info');
	
}
function clearJobTable(){
	$('input[id^=job_t]').val('');
	PF('jobTitleFilter').selectValue('');
	PF('jobClientFilter').selectValue('');
	PF('jobStatusFilter').selectValue('');
	updateTable('job_t');
}
function clickNext(e){
	var id =e.attr('source').replace(/:/g,"\\:");
	$("#"+id).next().click();
}
function initSubmitConfitmDilaog(){
	if($("#jobSubmitMsg").text().indexOf("wrong")!=-1){
	$("#submitConfirmDialog").find(".footerPanel").hide();}
}
function requirement_label_init(){
	$(".requirement-label").click(function(e){
		var target  =$(e.target);
		if(e.ctrlKey){
			target.attr("class","btn btn-danger requirement-label");
			return;
		}
		if(target.attr('class').indexOf("primary")!=-1){
			var newClass = target.attr('class').replace("primary","info");
			target.attr("class",newClass);
		}else if(target.attr('class').indexOf("info")!=-1){
			var newClass = target.attr('class').replace("info","default");
			target.attr("class",newClass);
		}else if(target.attr('class').indexOf("default")!=-1){
			var newClass = target.attr('class').replace("default","primary");
			target.attr("class",newClass);
		}else if(target.attr('class').indexOf("danger")!=-1){
			var newClass = target.attr('class').replace("danger","primary");
			target.attr("class",newClass);
		}
		
	});
	
}
function parse_requirement_tags(){
	var expanded_panel=$("#job_t").find(".ui-expanded-row-content");
	var hidden_parsed_tags=expanded_panel.find("input[id$=hidden_searchTag_in_jobView]");
	var des = expanded_panel.find("span[id$=requirement_des]");
	var tags = "{";
	des.find(".btn-primary").each(function(){
		tags+= $(this).text()+",";
	});
	tags+="}{";
	des.find(".btn-info").each(function(){
		tags+= $(this).text()+",";
	});
	tags+="}{";
	des.find(".btn-danger").each(function(){
		tags+= $(this).text()+",";
	});
	tags+="}{";
	des.find(".btn-default").each(function(){
		tags+= $(this).text()+",";
	});
	tags+="}";
	
	hidden_parsed_tags.val(tags);
	$("#searchTags").text(tags);
	
	
}
function fill_hidden_tagRequirement(){
	var expanded_panel=$("#job_t").find(".ui-expanded-row-content");
	var inputReq = expanded_panel.find("input[id$=hidden_tagRequirement]")
	inputReq.val(expanded_panel.find("span[id$=requirement_des]").html());
}

//--------------------------end job view---------------------------------------------------------
var ajaxCheck = true;
function checkAjaxStatus(){
	if(ajaxCheck){
		ajaxCheck = false;
		$("#ajaxStatusCheckBtn").click();
	}
}

function redirect(){
	if($("#ajaxStatusLabel").text()=="wrong"){
	 location.replace("http://localhost:9090/TempsRoster/userErrorPage.jsf");
	}
	else{
		ajaxCheck= true;
	}
	
}
function showBlockAjax(){
	$('#ajaxLoading').css('background','white');
	PF('mainViewBlock').show();
}


