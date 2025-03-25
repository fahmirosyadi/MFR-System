class DataService{
	
	construct(){
		this.generateData = this.generateData.bind(this);
	}
	
	generateData(data){
		var self = this;
		let result = []
		let i = 0;
		for(let j = 0; j < data.length; j++){
			result[i++] = data[j];
			if(data[j].child){
				let child = self.generateData(data[j].child);
				for(let k = 0; k < child.length; k++){
					result[i++] = child[k];
				}
			}
		}
		return result;
	}

}

