var url ='http://localhost:8080';
$(document).ready(function () {
        $.ajax({
					url: url+'/api/expenses/getall/11',
					cache: false,
					dataSrc: function(res){
					 return res;
					},
					success: function(result) {

					    populateData(result);
					},
					error: function(err) {
						console.log(err)
					}
				})
});
function populateData(data){
$('#expense').DataTable({
    data : data,
    columns: [
            { data: 'id' },
            { data: 'date' },
            { data: 'description' },
            { data: 'expensiveSource' },
            { data: 'expensiveCategory' },
            { data: 'amount' },
        ],
});
}