
let barangKeluarModule = angular.module("BarangKeluarModule",[]);

barangKeluarModule.config(function($stateProvider,$httpProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("/barang-keluar", {
		templateUrl : "barang_keluar.html"
	})
	.state("/barang-keluar/entry", {
		templateUrl : "barang_keluar_entry.html"
	})
	.state("/barang-keluar/entry/:id", {
		templateUrl : "barang_keluar_entry.html"
	})
})

barangKeluarModule.controller('BarangKeluarController', function($scope, $http, $rootScope){
	
	$rootScope.menu = "Barang Keluar";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/barang-keluar";
	
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
	
	$(document).off().on('click', (e) => {
		if(e.target.classList.contains("btn-hapus-bk")){
			$scope.remove(e.target.dataset.id);
		}
	})
	
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


barangKeluarModule.controller('BarangKeluarEntryController', function($scope, $http, $routeParams, $rootScope, $location, RekeningService){
	
	$rootScope.menu = "Barang Keluar";
	$rootScope.subMenu = "Barang Keluar Entry";
	
	
	let apiPrevix = "/api/barang-keluar";
	
	$scope.refreshData = function(){
		$scope.addOtherData();
	}
	
	$scope.getData = function(id){
		return $http.get(apiPrevix + "/" + id).then((result) => {
			let hasil = result.data.data;
			hasil.tanggal = new Date(hasil.tanggal);
			return hasil;
		})
	}
	
	$scope.addOtherData = async function(){
		$http.get("/api/persediaan").then(function(result){
			$scope.listBar = result.data.data;
		})
		
		RekeningService.getLeaf().then(result => {
			$scope.listRek = result;
		});
	}
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			$scope.getData($routeParams.id).then((result) => {$scope.data = result});
		}else{
			$scope.refreshData();
			$scope.data = {
				barangKeluarDetails: []
			}
		}
		
	});
	
	$scope.save = function(){
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		console.log(data);
		$http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
				$location.url('/barang-keluar')
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
		$scope.data.barangKeluarDetails.push({})
		$rootScope.mySelect2('.selBar');
		$rootScope.mySelect2('.selRek');
	}
	
	$rootScope.mySelect2('.selBar');
	$rootScope.mySelect2('.selRek');
	
	$scope.deleteItem = a => {
		const index = $scope.data.barangKeluarDetails.indexOf(a);
		if (index > -1) {
			$scope.data.barangKeluarDetails.splice(index, 1);
		}
		
	}
	
	
	$(document).off().on('change', (e) => {
		if(e.target.classList.contains('selBar')){
			let index = $('.selBar').index(e.target);
			$scope.data.barangKeluarDetails[index].barang = {id: e.target.value}
		}else if(e.target.classList.contains('selRek')){
			let index = $('.selRek').index(e.target);
			$scope.data.barangKeluarDetails[index].rekening = {id: e.target.value}
		}
		
	})
	
	$scope.refreshData();
});