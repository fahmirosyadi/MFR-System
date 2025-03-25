
let jurnalModule = angular.module("JurnalModule",[]);

jurnalModule.config(($stateProvider,$httpProvider) => {
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("jurnalUmum", {
		url: "/jurnal_umum",
		templateUrl : "jurnal.html"
	})
	.state("jurnalEntry", {
		url: "/jurnal/entry",
		templateUrl : "jurnal_entry.html"
	})
	.state("jurnalEdit", {
		url: "/jurnal/entry/:id",
		templateUrl : "jurnal_entry.html"
	})
})

jurnalModule.controller('JurnalController', ($scope, $http, $rootScope) => {
	
	$rootScope.menu = "Jurnal";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/jurnal";
	
	$scope.refreshData = () => {
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix).then(result => {
			$scope.listData = result.data.data;
		}, result => {
			Toast.fire({icon: "warning", title: result.title});
		}).finally(result => {
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	
	$scope.remove = (id) => {
		let confir = confirm("Hapus data");
		if(confir){
			$http.delete(apiPrevix + "/" + id).then(result => {
				if(result.data.status == true){
					$scope.refreshData();
					Toast.fire({icon: "success", title: result.data.messages});
				}else{
					Toast.fire({icon: "warning", title: result.data.messages});
				}
			}, result => {
				Toast.fire({icon: "warning", title: result.status});
			})
			
		}
	}
	
	$scope.refreshData();
	
});


jurnalModule.controller('JurnalEntryController', ($scope, $http, $routeParams, $location, $rootScope) => {
	
	$rootScope.menu = "Jurnal";
	$rootScope.subMenu = "Jurnal Entry";
	
	let apiPrevix = "/api/jurnal";
	
	$scope.refreshData = () => {
		$http.get("/api/rekening/findLeaf").then(function(result){
			$scope.listRek = result.data.data;
		})
	}
	
	$scope.$on('$routeChangeSuccess', () => {
		if($routeParams.id){
			$http.get(apiPrevix + "/" + $routeParams.id).then(result => {
				let hasil = result.data.data;
				hasil.tanggal = new Date(hasil.tanggal);
				$scope.data = hasil;
			})
		}else{
			$scope.data = {
				keterangan: "",
				tanggal: "",
				jurnalDetails: []
			};
		}
		
	});
	
	$scope.save = () => {
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		$http.post(apiPrevix, data).then(function (result) {
			if(result.data.status == true){
				$location.url("/jurnal");
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
		$scope.data.jurnalDetails.push({})
		$rootScope.mySelect2('.selRek');
	}
	
	$scope.deleteItem = a => {
		const index = $scope.data.jurnalDetails.indexOf(a);
		if (index > -1) {
			$scope.data.jurnalDetails.splice(index, 1);
		}
		
	}
	
	$rootScope.mySelect2('.selRek');
	
	$(document).off().on('change','.selRek', (e) => {
		let index = $('.selRek').index(e.target);
		$scope.data.jurnalDetails[index].rekening = {id: e.target.value}
	})
	
	$scope.refreshData();
	
});