
var app = angular.module("myModule", [
	'ui.router',
	'ngCookies', 
	'CustomerModule',
	'SupplierModule',
	'RekeningModule',
	'PersediaanModule',
	'JurnalModule',
	'PembelianModule',
	'PenjualanModule',
	'BarangMasukModule',
	'BarangKeluarModule',
	'LaporanModule',
	'UserModule',
	'Login2Module',
	'PembayaranUtangModule'
]);

app.factory('httpRequestInterceptor', function ($cookies,$location,$rootScope,$state) {

  return {
    request: function (config) {
      config.headers['Authorization'] = $cookies.get("token");
      $.ajaxSetup({
	    headers: { 'Authorization': $cookies.get("token") }
	  });
	  $(document).off().on('ajaxError', function(event, xhr) {
	      if (xhr.status === 401 || xhr.status === 403) {
			$state.go("login");
	      }
	  });
	  if($cookies.get("user") != undefined){
		$rootScope.userLogin = JSON.parse($cookies.get("user"));
	  }
	  
	  
	  $.fn.dataTable.ext.errMode = 'none';
      return config;
    },
    responseError: (response) => {
		if(response.data != undefined){
			if(response.data.status == 403) {
				$location.path('/forbidden');
				
	        }  
	        	
		}
		return response;
	}
  };
});

app.config(function($stateProvider,$httpProvider,$locationProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("dashboard", {
		url: "/",
		templateUrl : "dashboard.html"
	})
	$locationProvider.html5Mode(true);
})

app.service('AppService', function($http){

	this.upload = function(data) {
		document.getElementById("loadingData").style.visibility = "visible";
		return $http.post("/api/file/upload", data,{
			transformRequest: angular.identity,
			headers:{
				'Content-Type': undefined
			}
			
		}).then(function (result) {
			console.log("result.data.data : " + result.data.data);
			return result.data.data;
		},function(result){
   	    	Toast.fire({icon: 'warning',title: "Coba " + result.status})
		}).finally(function(){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	this.isAuthorized = () => {
		$http({
			method: 'get',
			url: '/api/user',

		}).then((response) => {
			console.log(response);			
		})
	}
	
})

app.controller('AppController', function($scope, $http, $rootScope, $cookies,$location){
	
	$rootScope.useTemplate =  true;
	
	$scope.logout = () => {
		$cookies.remove("token");
		$cookies.remove("user");
		$rootScope.userLogin = undefined;
		window.location.href = "/"
	}
	
	$rootScope.initFunction = () => {
		
	}
	
	
	$rootScope.isRole = (role) => {
		let user = $rootScope.userLogin;
		let status = false;
		if(user != undefined){
			let roleUser = user.roleUser;
			roleUser.forEach((ru) => {
				if(ru.role == role){
					status = true;
				}
			})	
		}
		return status;
	}
	
	
	
	$scope.batasSelect2 = 0;
	$rootScope.mySelect2 = (target) => {
		setTimeout(() => {
			if($(target).val()){
				$(target).select2();
			}else{
				if($scope.batasSelect2 < 500){
					$rootScope.mySelect2(target);
				}else{
					console.log('Select2 is failed')
				}
				$scope.batasSelect2++;
			}
		},10)	
	}
})


app.controller('DashboardController', function($scope, $http, $rootScope){

	$rootScope.menu = "Dashboard";
	$rootScope.subMenu = "";
	
	$http.get("/api/user")


})

app.controller('NotFoundController', function($scope, $http, $rootScope){

	$rootScope.menu = "Not Found";
	$rootScope.subMenu = "";


})

app.controller('LoginController', function($scope, $http, $rootScope){
	
})







