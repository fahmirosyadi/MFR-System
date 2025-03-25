
let penjualanModule = angular.module("PenjualanModule",[]);

penjualanModule.config(function($stateProvider,$httpProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("/penjualan", {
		templateUrl : "penjualan.html"
	})
	.state("/penjualan/jurnal", {
		templateUrl : "penjualan_jurnal.html"
	})
	.state("/penjualan/entry", {
		templateUrl : "penjualan_entry.html"
	})
	.state("/penjualan/entry/:id", {
		templateUrl : "penjualan_entry.html"
	})
})

penjualanModule.controller('PenjualanController', function($scope, $http, $rootScope){
	
	$rootScope.menu = "Penjualan";
	$rootScope.subMenu = "";
	
	
	let apiPrevix = "/api/penjualan";
	
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


penjualanModule.controller('PenjualanEntryController', function($scope, $http, $routeParams, $location, $rootScope){
	
	$rootScope.menu = "Penjualan";
	$rootScope.subMenu = "Penjualan Entry";
	
	let apiPrevix = "/api/penjualan";
	
	$scope.refreshData = function(){
		$http.get("/api/persediaan").then(result => {
			$scope.listBar = result.data.data;
		})
	}
	
	$scope.hitungTotal = () => {
		let total = 0;
		if($scope.data.penjualanDetails){
			$scope.data.penjualanDetails.forEach(value => {
				total += value.jumlah * value.harga;
			})
		}
		$scope.totalPenjualan = total;
	}
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			$http.get(apiPrevix + "/" + $routeParams.id).then((result) => {
				let hasil = result.data.data;
				hasil.tanggal = new Date(hasil.tanggal);
				$scope.data = hasil
				$scope.hitungTotal();
				$('.selCus').html(`<option value=${$scope.data.customer.id}>${$scope.data.customer.nama}</option>`)
				$('.selCus').select2({
			    	ajax: {url: '/api/customer/select'}
			    })
			})
		}else{
			$scope.data = {
				penjualanDetails: []
			};
			$('.selCus').select2({
		    	ajax: {url: '/api/customer/select'}
		    })
		}
	});
	
	$scope.save = function(){
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		data['customer'] = {id: $('.selCus').val()}
		console.log(data);
		$http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
				$scope.refreshData();
				$location.url('/penjualan')
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
		$scope.data.penjualanDetails.push({})
		$rootScope.mySelect2('.selBar');
	}
	
	$rootScope.mySelect2('.selBar');
	
	$scope.deleteItem = a => {
		const index = $scope.data.penjualanDetails.indexOf(a);
		if (index > -1) {
			$scope.data.penjualanDetails.splice(index, 1);
		}
		
	}
	
	$(document).off().on('change','.selBar', (e) => {
		let index = $('.selBar').index(e.target);
		$scope.data.penjualanDetails[index].barang = {id: e.target.value}
	})
	
	$scope.refreshData();
});