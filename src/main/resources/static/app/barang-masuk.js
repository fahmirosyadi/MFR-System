
let barangMasukModule = angular.module("BarangMasukModule",[]);

barangMasukModule.config(function($stateProvider,$httpProvider){
	$stateProvider
	.state("/barang-masuk", {
		templateUrl : "barang_masuk.html"
	})
	.state("/barang-masuk/entry", {
		templateUrl : "barang_masuk_entry.html"
	})
	.state("/barang-masuk/entry/:id", {
		templateUrl : "barang_masuk_entry.html"
	})
})

barangMasukModule.controller('BarangMasukController', function($scope, $http, $rootScope){
	
	$rootScope.menu = "Barang Masuk";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/barang-masuk";
	
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
		if(e.target.classList.contains("btn-hapus-bm")){
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


barangMasukModule.controller('BarangMasukEntryController', function($scope, $http, $routeParams, $rootScope, $location, RekeningService){
	
	$rootScope.menu = "Barang Masuk";
	$rootScope.subMenu = "Barang Masuk Entry";
	
	let apiPrevix = "/api/barang-masuk";
	
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
				barangMasukDetails: []
			}
		}
		
	});
	
	$scope.save = function(){
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		console.log(data);
		$http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
				$location.url('/barang-masuk')
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
		$scope.data.barangMasukDetails.push({})
		$rootScope.mySelect2('.selBar');
		$rootScope.mySelect2('.selRek');
	}
	
	$rootScope.mySelect2('.selBar');
	$rootScope.mySelect2('.selRek');
	
	$scope.deleteItem = a => {
		const index = $scope.data.barangMasukDetails.indexOf(a);
		if (index > -1) {
			$scope.data.barangMasukDetails.splice(index, 1);
		}
		
	}
	
	$(document).off().on('change', (e) => {
		if(e.target.classList.contains('selBar')){
			let index = $('.selBar').index(e.target);
			$scope.data.barangMasukDetails[index].barang = {id: e.target.value}
		}else if(e.target.classList.contains('selRek')){
			let index = $('.selRek').index(e.target);
			$scope.data.barangMasukDetails[index].rekening = {id: e.target.value}
		}
		
	})
	
	$scope.refreshData();
});