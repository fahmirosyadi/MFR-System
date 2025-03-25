
let userModule = angular.module("UserModule",[]);

userModule.config(function($stateProvider,$httpProvider){
	$httpProvider.interceptors.push('httpRequestInterceptor');
	$stateProvider
	.state("user", {
		url: "/user",
		templateUrl : "user.html"
	})
	.state("userDetails", {
		url: "/user/:id",
		templateUrl : "user-details.html"
	})
})

userModule.service('UserService', function($http,AppService){
	
	let apiPrevix = "/api/user";
	
	this.getApi = function(url){
		document.getElementById("loadingData").style.visibility = "visible";
		return $http.get(apiPrevix + url).then(function(result){
			return result.data;
		}, function(result){
			Toast.fire({icon: "warning", title: result.status});
		}).finally(function(result){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	this.getAll = function(){
		return this.getApi("").then(result => {
			return result.data;
		});
	}
	
	this.getById = function(id){
		return this.getApi("/" + id).then(result => {
			return result.data;
		});
	}
	
	this.save = function(data, dataFile) {
		AppService.upload(dataFile).then((fileName) => {
			document.getElementById("loadingData").style.visibility = "visible";
			data.foto = fileName;
			return $http.post(apiPrevix, data).then(function (result) {
				if(result.data.status == true){
					Toast.fire({icon: 'success',title: result.data.messages})
				}else{
					Toast.fire({icon: 'warning',title: result.data.messages})
				}
				
			},function(result){
	   	    	Toast.fire({icon: 'warning',title: result.status})
			}).finally(function(){
				document.getElementById("loadingData").style.visibility = "hidden";
			})
		})
		
	}
	
	
	
	this.delete = function(id){
		return $http.delete(apiPrevix + "/" + id).then(function(result){
			if(result.data.status == true){
				Toast.fire({icon: "success", title: result.data.messages});
			}else{
				Toast.fire({icon: "warning", title: result.data.messages});
			}
		}, function(result){
			Toast.fire({icon: "warning", title: result.status});
		})
			
	}
	
})

userModule.controller('UserController', function($scope, UserService, $rootScope,$routeParams){
	
	$rootScope.menu = "User";
	$rootScope.subMenu = "";
	
	$scope.refreshData = function(){
		$('#example1').DataTable({
			destroy: true,
	    	serverSide : true,
	    	processing: true,
	    	ajax: "/api/user/datatables",
	    	columns: [
		        { data: 'username' },
		        { data: 'email' },
		        { mRender: function(data, type, row){
		        	return `
		        	<a class="btn btn-success btn-edit-user" data-id="${row.id}" ui-sref="userDetails({id:${row.id}})">Edit</a>
		        	<button class="btn btn-danger btn-hapus-user" data-id="${row.id}">Hapus</button>`
		        }, orderable: false, width: '20%'}
		    ]
	    });
	}

	$scope.remove = function(id){
		let confir = confirm("Hapus data user?");
		if(confir){
			UserService.delete(id).then(function(){
				$scope.refreshData();
			})
		}
	}
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains("btn-hapus-user")){
			$scope.remove(e.target.dataset.id);
		}
	})
	
	$scope.refreshData();
	
})

userModule.controller('UserDetailsController', function($scope, UserService, $rootScope,$routeParams){
	
	$rootScope.menu = "User Details";
	$rootScope.subMenu = "";
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			UserService.getById($routeParams.id).then(function(result){
				$scope.data = result;
			});
		}
	});
	
	$scope.edit = function(id){
		UserService.getById(id).then(function(result){
			$scope.data = result;
		});
		$("#modal-default").modal('show');
	}
	
	$scope.save = function(){
		let mf = document.getElementById("mf");	
		let fd = new FormData(mf);
		fd.append("oldName",$scope.data.foto);
		fd.append("folder","user");
		UserService.save($scope.data,fd).then(function(){
			window.location.href = '#user';
		})
	}
	
	document.addEventListener("click", function(e){
		if(e.target.classList.contains("btn-hapus-user")){
			$scope.remove(e.target.dataset.id);
		}
	})
	
})
