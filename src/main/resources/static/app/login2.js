
let login2Module = angular.module("Login2Module",[]);

login2Module.config(function($stateProvider){
	$stateProvider
	.state("login", {
		url: "/login",
		templateUrl : "login.html"
	})
	.state("forbidden",{
		url: "/forbidden",
		templateUrl : "forbidden.html"
	})
})

login2Module.controller('Login2Controller', function($scope, $http, $rootScope, $cookies,$location){

	$rootScope.userLogin = undefined;
	
	console.log("tes");

	$scope.login = function() {
		$http.post("/api/user/login",$scope.user).then((response) => {
			$cookies.put("token",response.data.data.token);
			
			$cookies.put("user",JSON.stringify(response.data.data.user));
			$rootScope.userLogin = JSON.parse($cookies.get("user"));
			
			console.log($cookies.get("token"))
			$location.path("/")
		})
	}
	
	
	
	$scope.logout = function(){
		$cookies.put("token","");
		$location.path("/login");
	}
	
	
})
