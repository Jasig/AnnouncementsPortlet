<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%-- 
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.    
--%>
<c:set var="n"><portlet:namespace/></c:set>
<script src="<rs:resourceURL value="/rs/jquery/1.4.2/jquery-1.4.2.min.js"/>" type="text/javascript"></script>
<script src="<rs:resourceURL value="/rs/jqueryui/1.8/jquery-ui-1.8.min.js"/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value="/tinymce/tiny_mce.js"/>"></script>

<script type="text/javascript">
    var ${n} = ${n} || {}; //create a unique variable to assign our namespace too
    ${n}.jQuery = jQuery.noConflict(true); //assign jQuery to this namespace
    
    /*  runs when the document is finished loading.  This prevents things like the 'div' from being fully created */
    ${n}.jQuery(document).ready(function () { 
        var $ = ${n}.jQuery; //reassign $ for normal use of jQuery  
    	
    	$( "#${n}datepickerstart" ).datepicker({dateFormat: 'yy-mm-dd'});
    	$( "#${n}datepickerend" ).datepicker({dateFormat: 'yy-mm-dd'});
    });
    
</script>


<portlet:actionURL var="actionUrl">
	<portlet:param name="action" value="addAnnouncement"/>
	<portlet:param name="topicId" value="${topicId}"/>
</portlet:actionURL>

<div class="portlet-section-header"><spring:message code="addAnnouncement.header"/> <c:out value="${topicTitle}"/></div>

<form:form commandName="announcement" method="post" action="${actionUrl}">
<table width="100%" cellpadding="3">
	<tr>
		<td valign="top" width="20%">
			<spring:message code="addAnnouncement.title"/>
		</td>
		<td>
			<form:errors cssClass="portlet-msg-error" path="title"/>
	 		<form:input cssClass="portlet-form-input-field" path="title" size="30" maxlength="80"/>		
		</td>
	</tr>
	<tr>
		<td valign="top">
			<spring:message code="addAnnouncement.abstract"/>
		</td>
		<td>
			<form:errors cssClass="portlet-msg-error" path="abstractText"/>
			<script language="javascript" type="text/javascript">
			<!--
			window.onload = function() { 
				  var txts = document.getElementsByTagName('TEXTAREA') 

				  for(var i = 0, l = txts.length; i < l; i++) {
				    if(/^[0-9]+$/.test(txts[i].getAttribute("title"))) { 
				      var func = function() { 
				        var len = parseInt(this.getAttribute("title"), 10); 

				        if(this.value.length > len) { 
				          alert('Maximum length exceeded: ' + len); 
				          this.value = this.value.substr(0, len); 
				          return false; 
				        } 
				      }

				      txts[i].onkeyup = func;
				      txts[i].onblur = func;
				    } 
				  } 
				}
			-->
			</script>
			<form:textarea cssClass="portlet-form-input-field" path="abstractText" rows="2" cols="40" cssStyle="width:80%;" title="120" />
		</td>
	</tr>
	<tr>
		<td valign="top">
			<spring:message code="addAnnouncement.message"/>
		</td>
		<td>
			<form:errors cssClass="portlet-msg-error" path="message"/>
			<form:textarea cssClass="portlet-form-input-field mceEditor" path="message" rows="5" cols="30" cssStyle="width:100%;"/>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<spring:message code="addAnnouncement.link"/>
		</td>
		<td>
			<form:errors cssClass="portlet-msg-error" path="link"/>
			<form:input cssClass="portlet-form-input-field" path="link" size="30" maxlength="255"/>
		</td>
	</tr>
	<tr>
		<td valign="top">			
			<spring:message code="addAnnouncement.start"/>
			
		</td>
		<td>
			<form:errors cssClass="portlet-msg-error" path="startDisplay"/>
			<form:input path="startDisplay" id="${n}datepickerstart"></form:input>
		</td>
	</tr>
	<tr>
		<td valign="top">
			<spring:message code="addAnnouncement.end"/>			
		</td>
		<td>
			<form:errors cssClass="portlet-msg-error" path="endDisplay"/>
			<form:input path="endDisplay" id="${n}datepickerend"></form:input>
		</td>
	</tr>
</table>
<form:hidden path="id"/>
<form:hidden path="created"/>
<form:hidden path="author"/>
<form:hidden path="parent"/>
<button type="submit" class="portlet-form-button"><spring:message code="addAnnouncement.save"/></button>
</form:form>
<br/>
<a style="text-decoration:none;" href="<portlet:renderURL><portlet:param name="action" value="showTopic"/><portlet:param name="topicId" value="${topicId}"/></portlet:renderURL>">
<img src="<c:url value="/icons/arrow_left.png"/>" border="0" height="16" width="16" style="vertical-align:middle"/> <spring:message code="general.backtotopic"/></a>
&nbsp;&nbsp;
<a style="text-decoration:none;" href="<portlet:renderURL portletMode="view" windowState="normal"></portlet:renderURL>">
<img src="<c:url value="/icons/house.png"/>" border="0" height="16" width="16" style="vertical-align:middle"/> <spring:message code="general.adminhome"/></a>

<script type="text/javascript">
<!--
tinyMCE.init({
	mode : "textareas",
	editor_selector : "mceEditor",
	theme : "advanced",
	theme_advanced_buttons1 : "bold,separator,bullist,numlist,undo,redo,link,unlink",
	theme_advanced_buttons2 : "",
	theme_advanced_buttons3 : "",
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	extended_valid_elements : "a[name|href|target|title|onclick],span[class|align|style]"
});
//-->
</script>