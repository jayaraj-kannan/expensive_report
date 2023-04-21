var url = "http://localhost:8080";
var table;
var refresh = "refresh";
var load = "load";
function loadExpenses(id, option) {
  $.ajax({
    url: url + "/api/expenses/getall/" + id,
    cache: false,
    success: function (result) {
      if (option == load) {
        populateData(result);
      } else {
        table.destroy();
        populateData(result);
      }
    },
    error: function (err) {
      console.log(err);
    },
  });
}
function populateData(data) {
  table = $("#expense").DataTable({
    select: true,
    processing: true,
    data: data,
    columns: [
      { data: "date" },
      { data: "description" },
      { data: "expensiveSource" },
      { data: "expensiveCategory" },
      { data: "amount" },
    ],
  });
}

$(document).ready(function () {
  loadExpenses(userid, load);
  $("#expense tbody").on("click", "tr", function () {
    console.log(this);
    if ($(this).hasClass("selected")) {
      $(this).removeClass("selected");
    } else {
      table.$("tr.selected").removeClass("selected");
      $(this).addClass("selected");
    }
  });
  $("#reload").click(function () {
    loadExpenses(userid, refresh);
  });
  $("#delete").click(function () {
    var selectedRows = [];
    var data = table.rows(".selected").data();
    $.each(data, function (i, e) {
      selectedRows.push(e);
    });
    table.rows(".selected").remove().draw(false);
    console.log(selectedRows);
    removeExpense(selectedRows);
  });
  //add Expense ajax
  $("#myExpense").submit(function (event) {
    // Prevent default form submission
    event.preventDefault();

    $("#register").modal("toggle");
    // Get form data
    var formData = {
      expensiveSource: $("#expensiveSource").val(),
      amount: $("#amount").val(),
      description: $("#description").val(),
      expensiveCategory: $("#expensiveCategory").val(),
    };
    // Send AJAX request
    $.ajax({
      type: "POST",
      contentType: "application/json",
      url: url + "/api/expenses/add",
      data: JSON.stringify(formData),
      dataType: "json",
      cache: false,
      timeout: 600000,
      success: function (result) {
        if (result == "Success") {
          loadExpenses(userid, refresh);
        } else {
          console.log("Add Expense : Error=" + result);
        }
      },
      error: function (i, j, error) {
        loadExpenses(userid, refresh);
        console.log("Add Expense : error=" + error);
      },
    });
  });
});
function removeExpense(data) {
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: url + "/api/expenses/delete",
    data: JSON.stringify(data),
    dataType: "json",
    cache: false,
    timeout: 600000,
    success: function (data) {
      console.log(data);
    },
    error: function (e) {
      console.log(e);
    },
  });
}
