
let accountLinkModule = angular.module("AccountLinkModule",[]);

accountLinkModule.config(function($routeProvider, $locationProvider,$httpProvider){
	$locationProvider.hashPrefix('');
	$routeProvider
	.when("/account-link", {
		templateUrl : "account-link.html"
	})
})

accountLinkModule.service('AccountLinkService', function($http){
	
	let apiPrevix = "/api/accountLink";
	
	this.getApi = function(url){
		document.getElementById("loadingData").style.visibility = "visible";
		return $http.get(apiPrevix + url).then(function(result){
			return result.data;
		}, function(result){
			Toast.fire({icon: "warning", title: result.status});
		}).finally(function(result){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	this.getAll = function(){
		return this.getApi("").then(result => {
			return result.data;
		});
	}
	
	this.getById = function(id){
		return this.getApi("/" + id).then(result => {
			return result.data;
		});
	}
	
	this.save = function(data) {
		document.getElementById("loading").style.visibility = "visible";
		return $http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
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
	
	this.delete = function(id){
		return $http.delete(apiPrevix + "/" + id).then(function(result){
			if(result.data.status == true){
				Toast.fire({icon: "success", title: result.data.messages});
			}else{
				Toast.fire({icon: "warning", title: result.data.messages});
			}
		}, function(result){
			Toast.fire({icon: "warning", title: result.status});
		})
			
	}
	
})

accountLinkModule.controller('AccountLinkController', function($scope, AccountLinkService, $rootScope){
	
	$rootScope.menu = "Account Link";
	$rootScope.subMenu = "";
	
	$scope.refreshData = function(){
		$('#example1').DataTable({
			destroy: true,
	    	serverSide : true,
	    	processing: true,
	    	ajax: "/api/accountLink/datatables",
	    	columns: [
		        { data: 'nama' },
		        { data: 'alamat' },
		        { data: 'telp' },
		        { mRender: function(data, type, row){
		        	return `
		        	<a class="btn btn-success btn-edit-accountLink" data-id="${row.id}">Edit</a>
		        	<button class="btn btn-danger btn-hapus-accountLink" data-id="${row.id}">Hapus</button>
		        	<a href="#accountLink/${row.id}/utang" class="btn btn-info">Buku Utang</a>`
		        }, orderable: false, width: '30%'}
		    ]
	    });
	}
	
	$scope.edit = function(id){
		AccountLinkService.getById(id).then(function(result){
			$scope.data = result;
			console.log(result);
		});
		$("#modal-default").modal('show');
	}
	
	$scope.save = function(){
		AccountLinkService.save($scope.data).then(function(){
			$scope.refreshData();
			$('#modal-default').modal('hide');
		})
	}
	
	$scope.remove = function(id){
		let confir = confirm("Hapus data accountLink?");
		if(confir){
			AccountLinkService.delete(id).then(function(){
				$scope.refreshData();
			})
		}
	}
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains("btn-hapus-accountLink")){
			$scope.remove(e.target.dataset.id);
		}else if(e.target.classList.contains("btn-edit-accountLink")){
			$scope.edit(e.target.dataset.id);
		}
	})
	
	$scope.refreshData();
	
});

accountLinkModule.controller('UtangController', function($http, $scope, AccountLinkService, $rootScope, $routeParams){
	
	$rootScope.menu = "Account Link";
	$rootScope.subMenu = "Buku Pembantu Utang";
	
	let apiPrevix = "/api/accountLink";
	
	$scope.getData = function(id){
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix + "/" + id + "/utang").then(function(result){
			$scope.data = result.data.data;
		}, function(result){
			Toast.fire({icon: "warning", title: result.title});
		}).finally(function(result){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			$scope.getData($routeParams.id);
		}else{
		
		}
		
	});
});