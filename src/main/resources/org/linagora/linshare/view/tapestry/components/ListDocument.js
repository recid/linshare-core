function countCheckbox()
{

	//var elements = $$('input[type="checkbox"][id^="filesSelected"]').findAll(function(el)
		//{ return el.checked });

	//var allElements = $$('input[type="checkbox"][id^="filesSelected"]').findAll(function(el)
		//{ return true });
	//var result = "none";

	//if ((elements!=null)&&(elements.length>0)) {
		//result = "block";
	//}

	//if (elements.length<allElements.length) {
		//document.getElementById('selectAll').checked = false;
	//}
}


function checkAll()
{

	var chkboxelements = $$('input[type="checkbox"][id^="filesSelected"]').findAll(function(el)
		{ return true });
	if	(document.getElementById('selectAll').checked) {
		for (i=0;i<chkboxelements.length;i++) {
			chkboxelements[i].checked=true;
		}
	} else {
		for(i=0;i<chkboxelements.length;i++) {
			chkboxelements[i].checked=false;
		}
	}
	countCheckbox();
}
