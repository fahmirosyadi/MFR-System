
let rekeningModule = angular.module("RekeningModule",[]);

rekeningModule.config(function($stateProvider,$httpProvider){
	$stateProvider
	.state("rekening", {
		url: "/rekening",
		templateUrl : "rekening.html"
	})
	.state("rekeningCreate", {
		url: "/rekening/create/:pid",
		templateUrl : "rekening_form.html"
	})
	.state("rekeningEdit", {
		url: "/rekening/edit/:id",
		templateUrl : "rekening_form.html"
	})
	.state("rekeningSaldoAwal", {
		url: "/rekening/saldo_awal",
		templateUrl : "rekening-sa.html"
	})
})

rekeningModule.service('RekeningService', function($http){

	let apiPrevix = "/api/rekening";
	let dataService = new DataService();
	
	this.getLeaf = function() {
		return $http.get(apiPrevix + "/findLeaf").then(function(result){
			return result.data.data;
		})
	}
})

rekeningModule.controller('RekeningController', function($scope, $http, $routeParams, RekeningService, $rootScope){
	
	$rootScope.menu = "Rekening";
	$rootScope.subMenu = "";	
	
	let apiPrevix = "/api/rekening";
	let dataService = new DataService();
	
	$scope.getAll = function(){
		document.getElementById("loadingData").style.visibility = "visible";
		return $http.get(apiPrevix + '/toList').then(function(result){
			return result.data.data;
		}).finally(function(){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	$scope.indentString = (indent) => {
		let result = "";
		for(let i = 0; i < indent; i++){
			result += "&nbsp&nbsp&nbsp&nbsp&nbsp";
		}
		return result;
	}
	
	$scope.refreshData = async function(){
		$('#tb-rekening').DataTable({
			destroy: true,
	    	data: await $scope.getAll(),
	    	ordering: false,
	    	bPaginate: false,
	    	columns: [
		        { mRender: function(data, type, row){
		        	let i = $scope.indentString(row.indent);
		 			
		        	return `<span>${i}${row.nomor}&nbsp&nbsp&nbsp${row.nama}</span>`
		        }},
		        { data: 'jenis',className:'text-center' },
		        { data: 'defaultAccount', className:'text-center'},
		        { data: 'saldo',className:'text-right', render: $.fn.dataTable.render.number(',','.')},
		        { mRender: function(data, type, row){
		        	return `
		        	<center>
		        	<a href="#rekening/create/${row.id}" class="btn btn-primary">Tambah</a>
					<a href="#rekening/edit/${row.id}" class="btn btn-warning">Edit</a>
		            <button class="btn btn-danger btn-hapus-rekening" data-id="${row.id}">Hapus</button>
		            </center>`
		        }, width: '30%'}
		    ]
	    });
	}
	

	$(document).off().on('click','.btn-hapus-rekening', (e) => {
		$scope.remove(e.target.dataset.id)
	})
	
	$scope.getData = function(id){
		return $http.get(apiPrevix + "/" + id).then((result) => {
			let hasil = result.data.data;
			hasil.nomor = hasil.nomor.substring(2);
			return hasil;
		})
	}
	
	$scope.$on('$routeChangeSuccess', function() {
		if($routeParams.id){
			$scope.getData($routeParams.id).then((result) => {$scope.data = result});
		}else if($routeParams.pid){
			$scope.getData($routeParams.pid).then((result) => {
				$scope.data = {
					jenis: result.jenis,
					parent: result
				}
			});
			
		}else{
			$scope.refreshData();
		}
		
	});
	
	$scope.save = function(){
		document.getElementById("loading").style.visibility = "visible";
		let data = $scope.data;
		if(data.jenis == 'assets'){
			data.nomor = '1-' + data.nomor;
		}else if(data.jenis == 'utang'){
			data.nomor = '2-' + data.nomor;
		}else if(data.jenis == 'modal'){
			data.nomor = '3-' + data.nomor;
		}else if(data.jenis == 'penjualan'){
			data.nomor = '4-' + data.nomor;
		}else if(data.jenis == 'hpp'){
			data.nomor = '5-' + data.nomor;
		}else if(data.jenis == 'beban'){
			data.nomor = '6-' + data.nomor;
		}else if(data.jenis == 'rekening' || data.jenis == 'neraca' || data.jenis == 'labarugi'){
			data.nomor = '0-' + data.nomor;
		}
		console.log(data);
		$http.post(apiPrevix, data).then((result) => {
			if(result.data.status == true){
				window.location.href = '#rekening';
				$scope.refreshData();
				Toast.fire({icon: "success", title: "Data berhasil disimpan"});
			}else{
				Toast.fire({icon: "warning", title: result.data.messages});
			}
		}, (result) => {
			alert(result.status);
		}).finally((result) => {
			document.getElementById("loading").style.visibility = "hidden";
		});
	}
	
	$scope.remove = function(id){
		let confir = confirm("Hapus data rekening?");
		if(confir){
			$http.delete(apiPrevix + "/" + id).then(function(){
				$scope.refreshData();
			})
		}
	}
	
	
	
});

rekeningModule.controller('RekeningSaController', function($scope, $http, $routeParams, RekeningService, $rootScope){
	
	$rootScope.menu = "Rekening";
	$rootScope.subMenu = "Saldo Awal";	
	
	let apiPrevix = "/api/rekening";
	let dataService = new DataService();
	
	$scope.getAll = function(){
		document.getElementById("loadingData").style.visibility = "visible";
		return $http.get(apiPrevix + '/findLeaf').then(function(result){
			$scope.datalist = result.data.data;
		}).finally(function(){
			document.getElementById("loadingData").style.visibility = "hidden";
		})
	}
	
	$scope.save = function(){
		document.getElementById("loadingData").style.visibility = "visible";
		$http.post(apiPrevix + "/saveAll", $scope.datalist).then((result) => {
			if(result.data.status == true){
				window.location.href = '#rekening';
				Toast.fire({icon: "success", title: result.data.messages});
			}else{
				Toast.fire({icon: "warning", title: result.data.messages});
			}
		}, (result) => {
			alert(result.status);
		}).finally((result) => {
			document.getElementById("loadingData").style.visibility = "hidden";
		});
	}
	
	$scope.getAll();
	
});