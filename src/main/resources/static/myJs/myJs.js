class MyFetch{

	getData(url, loading = "") {
		if (loading != "") {
			document.getElementById(loading).style.visibility = "visible";
		    return fetch(url).then(response => response.json()).finally(function(){
		    	document.getElementById(loading).style.visibility = "hidden";
		    });
		}else{
			return fetch(url).then(response => response.json());
		}
	}

	getHTML(url,loading = "") {
		if (loading != "") {
			document.getElementById(loading).style.visibility = "visible";
			return fetch(url).then(response => response.text()).finally(function(){
				document.getElementById(loading).style.visibility = "hidden";
			});
		}else{
	    	return fetch(url).then(response => response.text());
		}
	}

	deleteData(url) {
	    return fetch(url).then(response => response.json());
	}

	postData(url,data,loading = "") {
		if (loading != "") {
			document.getElementById(loading).style.visibility = "visible";
			return fetch(url,{
		        method: 'post',
		        headers: {
		            'X-CSRF-Token': data.get('_csrf')
		        },
		        body: data
		    }).then(response => response.json()).finally(function(){
		    	document.getElementById(loading).style.visibility = "hidden";
		    });	
		}else{
		    return fetch(url,{
		        method: 'post',
		        headers: {
		            'X-CSRF-Token': data.get('_csrf')
		        },
		        body: data
		    }).then(response => response.json());
		}
	}

}

class MyCode {
	async loadPeriode(id) {
		let mf = new MyFetch();
		let awal = await mf.getData('/api/profil');
        let periode = document.getElementById(id);
        let th = new Date().getFullYear();
        periode.innerHTML = `<option>${th}</option>`;
        try {
            for (let i = th - 1; i >= awal.tahun; i--) {
                periode.innerHTML += `
                    <option>${i}</option>
                `;
            }
        	// return true;
        } catch(e) {
            alert(e);
        }
	}
}

class MyAjax{
	
	getData(url){
		let result;
		$.ajax({
	        type: "GET",
	        url: url,
	        async: false,
	        success: function(data) {
	        	result = data.data;
	        }
	    });
		return result;
	}
	
}
