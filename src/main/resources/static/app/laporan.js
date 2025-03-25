
let laporanModule = angular.module("LaporanModule",[]);

laporanModule.config(($stateProvider,$httpProvider) => {
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("/neraca", {
		templateUrl : "neraca.html"
	})
	.state("/neraca/:periode", {
		templateUrl : "neraca.html"
	})
	.state("/labarugi", {
		templateUrl : "laba-rugi.html"
	})
	.state("/labarugi/:periode", {
		templateUrl : "laba-rugi.html"
	})
})

laporanModule.controller('NeracaController', ($scope, $http, $rootScope, $routeParams) => {
	
	$rootScope.menu = "Neraca";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/laporan";
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.periode){
			$scope.refreshData($routeParams.periode);
		}else{
			$scope.refreshData();
		}
	});
	
	$scope.refreshData = (periode = "") => {
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix + '/neraca/' + periode).then(result => {
			$scope.data = result.data.data;
			console.log($scope.data);
		}, result => {
			Toast.fire({icon: "warning", title: result.title});
		}).finally(result => {
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	
	
});


laporanModule.controller('LabaRugiController', ($scope, $http, $location, $rootScope, $routeParams) => {
	
	$rootScope.menu = "Laba Rugi";
	$rootScope.subMenu = "";
	
	let apiPrevix = "/api/laporan";
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.periode){
			$scope.refreshData($routeParams.periode);
		}else{
			$scope.refreshData();
		}
	});
	
	$scope.refreshData = (periode = "") => {
		document.getElementById("loadingData").style.visibility = "visible";
		$http.get(apiPrevix + '/labarugi/' + periode).then(result => {
			$scope.data = result.data.data;
			console.log($scope.data);
		}, result => {
			Toast.fire({icon: "warning", title: result.title});
		}).finally(result => {
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
});