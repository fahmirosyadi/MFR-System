
let authModule = angular.module("AuthModule",[]);

authModule.config(function($routeProvider, $locationProvider,$httpProvider){
	$locationProvider.hashPrefix('');
	$routeProvider
	.when("/auth", {
		templateUrl : "auth.html"
	})
	.when("/auth/edit", {
		templateUrl : "auth.html"
	})
	.otherwise({
		templateUrl : "404.html"
	})
})

authModule.controller('AuthController', function($scope, $http){
	
	let apiPrevix = "/api/auth";
	
	$scope.refreshData = function(){
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix).then(function(result){
			$scope.listData = result.data.data;
			$('#example1').DataTable({
				destroy: true,
		    	data: result.data.data,
		    	columns: [
			        { data: 'nama' },
			        { data: 'alamat' },
			        { data: 'telp' },
			        { mRender: function(data, type, row){
			        	return `
			        	<a class="btn btn-success btn-edit" data-id="${row.id}">Edit</a>
			        	<button class="btn btn-danger btn-hapus" data-id="${row.id}">Hapus</button>
			        	<a href="/customer/` + row.id + `/utang" class="btn btn-info">Rincian Utang</a>`
			        }}
			    ]
		    });
		}, function(result){
			$('#table_id').DataTable();
			Toast.fire({icon: "warning", title: result.title});
		}).finally(function(result){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains("btn-hapus")){
			$scope.remove(e.target.dataset.id);
		}else if(e.target.classList.contains("btn-edit")){
			$scope.edit(e.target.dataset.id);
		}
	})
	
	$scope.edit = function(id){
		$http.get(apiPrevix + "/" + id).then(function(result){
			let hasil = result.data.data;
			$scope.data = {
					id: hasil.id,
					nama: hasil.nama,
					alamat: hasil.alamat,
					telp: hasil.telp,
					saldo: hasil.saldo
			}
			$("#modal-default").modal('show');
		})
	}
	
	$scope.create = function(){
		$scope.data = {
				id: null,
				nama: null,
				alamat: null,
				telp: null,
				saldo: null
		}
	}
	
	$scope.save = function(){
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		console.log(data);
		$http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
				$scope.refreshData();
				$('#modal-default').modal('hide');
				Toast.fire({icon: 'success',title: result.data.messages})
			}else{
				Toast.fire({icon: 'warning',title: result.data.messages})
			}
			
		},function(result){
   	    	Toast.fire({icon: 'warning',title: result.status})
		}).finally(function(){
			document.getElementById("loading").style.visibility = "hidden";
		})
	}
	
	$scope.remove = function(id){
		let confir = confirm("Hapus data");
		if(confir){
			$http.delete(apiPrevix + "/" + id).then(function(result){
				if(result.data.status == true){
					$scope.refreshData();
					Toast.fire({icon: "success", title: result.data.messages});
				}else{
					Toast.fire({icon: "warning", title: result.data.messages});
				}
			}, function(result){
				Toast.fire({icon: "warning", title: result.status});
			})
			
		}
	}
	
	$scope.refreshData();
	
});