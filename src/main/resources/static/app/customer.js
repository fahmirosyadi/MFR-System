
let customerModule = angular.module("CustomerModule",[]);



customerModule.config(function($stateProvider,$httpProvider){
	$stateProvider
	.state("customer", {
		templateUrl : "customer.html"
	})
	.state("customerCreate", {
		templateUrl : "customer_form.html"
	})
	.state("customerPiutang", {
		url: "/customer/:id/piutang",
		templateUrl : "buku_pembantu_piutang.html"
	})
})

customerModule.service('CustomerService', function($http){

	let apiPrevix = "/api/customer";

})

customerModule.controller('CustomerController', function($scope, $http, $routeParams, $rootScope,CustomerService,$cookies){
	
	$rootScope.menu = "Customer";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/customer";
	
	$scope.refreshData = function(){
		
		if($cookies.get("token") != ""){
			$('#example1').DataTable({
				destroy: true,
		    	serverSide : true,
		    	processing: true,
		    	ajax:{
	                url: '/api/customer/datatables',
	                type: "GET"
	            },
		    	columns: [
			        { data: 'nama' },
			        { data: 'alamat' },
			        { data: 'telp' },
			        { mRender: function(data, type, row){
			        	return `
			        	<a class="btn btn-success btn-edit-customer" data-id="${row.id}">Edit</a>
			        	<button class="btn btn-danger btn-hapus-customer" data-id="${row.id}">Hapus</button>
			        	<a href="#customer/` + row.id + `/piutang" class="btn btn-info">Buku Piutang</a>`
			        }, orderable: false, width: '30%'}
			    ]
		    });	
		    
		    $("#example1").ajaxError(function(event, jqxhr, request, settings){
			   if(jqxhr.status == 403) alert("Validation Failure");    
			});
		}
			
	}
	
	
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains("btn-hapus-customer")){
			$scope.remove(e.target.dataset.id);
		}else if(e.target.classList.contains("btn-edit-customer")){
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
		$scope.data = undefined;	
		$("#modal-default").modal('show');
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
		let confir = confirm("Hapus data customer?");
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

customerModule.controller('PiutangController', function($scope, $http, $routeParams, $rootScope){
	
	$rootScope.menu = "Customer";
	$rootScope.subMenu = "Buku Pembantu Piutang";
	
	let apiPrevix = "/api/customer";
	
	$scope.getData = function(id){
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix + "/" + id + "/piutang").then(function(result){
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

})