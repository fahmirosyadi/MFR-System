
let pembayaranUtangModule = angular.module("PembayaranUtangModule",[]);

pembayaranUtangModule.config(function($stateProvider,$httpProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("/pembayaran_utang", {
		templateUrl : "pembayaran_utang.html"
	})
	.state("/pembayaran_utang/entry", {
		templateUrl : "pembelian_utang_entry.html"
	})
	.state("/pembayaran_utang/entry/:id", {
		templateUrl : "pembelian_utang_entry.html"
	})
})

pembayaranUtangModule.controller('PembayaranUtangController', function($scope, $http, $rootScope){
	
	$rootScope.menu = "Pembayaran Utang";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/pembayaran_utang";
	
	$scope.refreshData = function(){
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix).then(function(result){
			$scope.listData = result.data.data;
		}, function(result){
			Toast.fire({icon: "warning", title: result.title});
		}).finally(function(result){
			document.getElementById("loadingData").style.visibility = "hidden";
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


pembayaranUtangModule.controller('PembayaranUtangEntryController', function($scope, $http, $routeParams, $location, $rootScope){

	$rootScope.menu = "Pembayaran Utang";
	$rootScope.subMenu = "Pembayaran Utang Entry";
	

	let apiPrevix = "/api/pembayaran_utang";

	$scope.getData = function(id){
		return $http.get(apiPrevix + "/" + id).then((result) => {
			let hasil = result.data.data;
			hasil.tanggal = new Date(hasil.tanggal);
			return hasil;
		})
	}
	
	$scope.refreshData = function(){
		$http.get("/api/persediaan").then(function(result){
			$scope.listBar = result.data.data;
		})
		$http.get("/api/supplier?length=5&search=").then(function(result){
			$scope.listSup = result.data.data;
		})
	}
	
	$scope.hitungTotal = () => {
		let total = 0;
		if($scope.data.pembelianDetails){
			$scope.data.pembelianDetails.forEach(value => {
				total += value.jumlah * value.harga;
			})
		}
		$scope.totalPembelian = total;
	}
	
	
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			$scope.getData($routeParams.id).then((result) => {
				$scope.data = result
				$scope.hitungTotal();
				$('.selSup').html(`<option value=${$scope.data.supplier.id}>${$scope.data.supplier.nama}</option>`)
				$('.selSup').select2({
			    	ajax: {url: '/api/supplier/select'}
			    })
				
			});
			
		}else{
			$('.selSup').select2({
		    	ajax: {url: '/api/supplier/select'}
		    })
			$scope.data = {
				pembelianDetails: []
			}
		}
		
		
	});
	
	
	
	$scope.save = function(){
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		data['supplier'] = {id: $('.selSup').val()}
		console.log(data);
		$http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
				$scope.refreshData();
				$location.url("/pembelian");
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
	
	$scope.addItem = () => {
		$scope.data.pembelianDetails.push({})
		$rootScope.mySelect2('.selBar');
	}
	
	$rootScope.mySelect2('.selBar');
	
	$scope.deleteItem = a => {
		const index = $scope.data.pembelianDetails.indexOf(a);
		if (index > -1) {
			$scope.data.pembelianDetails.splice(index, 1);
		}
		
	}
	
	$(document).off().on('change','.selBar', (e) => {
		let index = $('.selBar').index(e.target);
		$scope.data.pembelianDetails[index].barang = {id: e.target.value}
	})
	
	$scope.refreshData();
});