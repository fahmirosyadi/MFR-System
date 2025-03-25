
let persediaanModule = angular.module("PersediaanModule",[]);

persediaanModule.config(function($stateProvider,$httpProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("persediaan", {
		url: "/persediaan",
		templateUrl : "persediaan.html"
	})
	.state("persediaanStok", {
		url: "/persediaan/:id/stok",
		templateUrl : "stok.html"
	})
})

persediaanModule.controller('PersediaanController', function($scope, $http, RekeningService, $rootScope){
	
	$rootScope.menu = "Persediaan";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/persediaan";
	
	$scope.refreshData = async function(){
		document.getElementById("loadingData").style.visibility = "visible";
		$scope.listRek = await RekeningService.getLeaf();
		$http.get(apiPrevix).then(function(result){
			$scope.listData = result.data.data;
			$('#tb-persediaan').DataTable({
				destroy: true,
		    	data: result.data.data,
		    	columns: [
			        { data: 'barang' },
			        { data: 'rekHpp.nama' },
			        { data: 'rekPenjualan.nama' },
			        { data: 'rekInventory.nama' },
			        { mRender: function(data, type, row){
			        	return `
			        	<button class="btn btn-success btn-edit-persediaan" data-id="${row.id}">Edit</button>
			        	<button class="btn btn-danger btn-hapus-persediaan" data-id="${row.id}">Hapus</button>
			        	<a href="#persediaan/` + row.id + `/stok" class="btn btn-info">Stok</a>`
			        }, orderable: false}
			    ]
		    });
		}, function(result){
			Toast.fire({icon: "warning", title: result.status});
		}).finally(function(){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains('btn-hapus-persediaan')){
			$scope.remove(e.target.dataset.id);
		}else if(e.target.classList.contains('btn-edit-persediaan')){
			$scope.edit(e.target.dataset.id);
		}
		
	})
	
	$scope.edit = function(id){
		document.getElementById("loading").style.visibility = "visible";
		$http.get(apiPrevix + "/" + id).then(function(result){
			let hasil = result.data.data;
			$scope.data = {
					id: hasil.id,
					barang: hasil.barang,
					rekHpp: hasil.rekHpp,
					rekPenjualan: hasil.rekPenjualan,
					rekInventory: hasil.rekInventory
			}
			$('#modal-default').modal('show');
		}).finally(function(){
			document.getElementById("loading").style.visibility = "hidden";
		})
	}
	
	$scope.create = function(){
		$scope.data = undefined;
		$('#modal-default').modal('show');
		
	}
	
	$rootScope.mySelect2('.selRek');
	
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
		let confir = confirm("Hapus data?");
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
	
	$(document).off().on('change','.selRek', (e) => {
		let index = $('.selRek').index(e.target);
		if(index == 0){
			$scope.data.rekHpp = {id: e.target.value}
		}else if(index == 1){
			$scope.data.rekPenjualan = {id: e.target.value}
		}else if(index == 2){
			$scope.data.rekInventory = {id: e.target.value}
		}
		
	})
	
	
	
	$scope.refreshData();
	
});

persediaanModule.controller('StokController', function($scope, $http, RekeningService, $rootScope, $routeParams){
	
	$rootScope.menu = "Persediaan";
	$rootScope.subMenu = "Stok";	
	
	let apiPrevix = "/api/persediaan";
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			$scope.refreshData($routeParams.id);
		}else{
		
		}
	});
	
	$scope.refreshData = (id) => {
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix + '/' + id + '/stok').then(function(result){
			$scope.data = result.data.data;
			console.log($scope.data)
		}, function(result){
			Toast.fire({icon: "warning", title: result.status});
		}).finally(function(){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
});