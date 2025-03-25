
let supplierModule = angular.module("SupplierModule",[]);

supplierModule.config(function($stateProvider,$httpProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("supplier", {
		url: "/supplier",
		templateUrl : "supplier.html"
	})
	.state("supplierUtang", {
		url: "/supplier/:id/utang",
		templateUrl : "buku_pembantu_utang.html"
	})
})

supplierModule.service('SupplierService', function($http){
	
	let apiPrevix = "/api/supplier";
	
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

supplierModule.controller('SupplierController', function($scope, SupplierService, $rootScope){
	
	$rootScope.menu = "Supplier";
	$rootScope.subMenu = "";
	
	$scope.refreshData = function(){
		$('#example1').DataTable({
			destroy: true,
	    	serverSide : true,
	    	processing: true,
	    	ajax: "/api/supplier/datatables",
	    	columns: [
		        { data: 'nama' },
		        { data: 'alamat' },
		        { data: 'telp' },
		        { mRender: function(data, type, row){
		        	return `
		        	<a class="btn btn-success btn-edit-supplier" data-id="${row.id}">Edit</a>
		        	<button class="btn btn-danger btn-hapus-supplier" data-id="${row.id}">Hapus</button>
		        	<a href="#supplier/${row.id}/utang" class="btn btn-info">Buku Utang</a>`
		        }, orderable: false, width: '30%'}
		    ]
	    });
	}
	
	$scope.edit = function(id){
		SupplierService.getById(id).then(function(result){
			$scope.data = result;
			console.log(result);
		});
		$("#modal-default").modal('show');
	}
	
	$scope.create = function(){
		$scope.data = undefined;	
		$("#modal-default").modal('show');
	}
	
	$scope.save = function(){
		SupplierService.save($scope.data).then(function(){
			$scope.refreshData();
			$('#modal-default').modal('hide');
		})
	}
	
	$scope.remove = function(id){
		let confir = confirm("Hapus data supplier?");
		if(confir){
			SupplierService.delete(id).then(function(){
				$scope.refreshData();
			})
		}
	}
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains("btn-hapus-supplier")){
			$scope.remove(e.target.dataset.id);
		}else if(e.target.classList.contains("btn-edit-supplier")){
			$scope.edit(e.target.dataset.id);
		}
	})
	
	$scope.refreshData();
	
});

supplierModule.controller('UtangController', function($http, $scope, SupplierService, $rootScope, $routeParams){
	
	$rootScope.menu = "Supplier";
	$rootScope.subMenu = "Buku Pembantu Utang";
	
	let apiPrevix = "/api/supplier";
	
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