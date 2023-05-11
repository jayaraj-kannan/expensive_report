var url = "http://localhost:8080";
var table;
var refresh = "refresh";
var load = "load";
var year = new Date().getFullYear();
$("#selectd_ytd").html("YTD - " + year);
function loadExpenses(id, option, year) {
  $.ajax({
    url: url + "/api/expenses/getbyyear/" + id + "/" + year,
    cache: false,
    success: function (result) {
      if (option == load) {
        populateData(result);
      } else {
        table.destroy();
        populateData(result);
      }
      getTotalExpenseAmount();
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
      { data: "expensiveType" },
      {
        data: "amount",
        render: function (data, type, row) {
          if (row.expensiveType === "CREDIT") {
            return "<span id='credit'> + " + data + "</span>";
          } else if (row.expensiveType === "DEBIT") {
            return "<span id='debit'> - " + data + "</span>";
          } else {
            return "<span>" + data + "</span>";
          }
        },
      },
    ],
  });
}

$(document).ready(function () {
  loadExpenses(userid, load, year);
  getTotalExpenseAmount();
  getCurrentBalance();
  //get Years basedon User
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: url + "/api/expenses/dist_years/" + userid,
    cache: false,
    timeout: 600000,
    success: function (response) {
      console.log("Years: ", response);
      $.each(response, function (index, value) {
        var option = $("<option>").attr("value", value).text(value);
        $("#years").append(option);
      });
      $("#years").val(year);
    },
    error: function (i, j, error) {
      console.log("Add Expense : error=" + error);
    },
  });
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
    loadExpenses(userid, refresh, year);
    getTotalExpenseAmount();
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
      expensiveType: $("#expensiveType").val(),
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
          loadExpenses(userid, refresh, year);
          getTotalExpenseAmount();
          getCurrentBalance();
        } else {
          console.log("Add Expense : Error=" + result);
        }
      },
      error: function (i, j, error) {
        loadExpenses(userid, refresh, year);
        getTotalExpenseAmount();
        getCurrentBalance();
        console.log("Add Expense : error=" + error);
      },
    });
  });
  //Based on Year selection
  $("#years").change(function () {
    year = $(this).children("option:selected").val();
    console.log("Selected option: " + year);
    loadExpenses(userid, refresh, year);
    $("#selectd_ytd").html("YTD - " + year);
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
      getTotalExpenseAmount();
      getCurrentBalance();
    },
    error: function (e, f, h) {
      console.log(e);
      getTotalExpenseAmount();
      getCurrentBalance();
    },
  });
}
function calculateUpdateTime(date) {
  var currentDate = new Date();
  var diff = Math.round((currentDate.getTime() - date.getTime()) / 1000);
  if (diff < 60) {
    return diff + " seconds ago";
  } else if (diff < 3600) {
    var minutes = Math.round(diff / 60);
    return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
  } else if (diff < 86400) {
    var hours = Math.round(diff / 3600);
    return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
  } else {
    return lastUpdated.toLocaleDateString();
  }
}
var total_updated_time;
var balance_updated_time;
function getTotalExpenseAmount() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: url + "/api/expenses/total/" + userid + "/" + year,
    cache: false,
    timeout: 600000,
    success: function (res) {
      console.log("Total Exps: ", res);
      $("#total").html('<i class="fa fa-inr"></i>' + res);
      total_updated_time = new Date();
      updateLastUpdated();
    },
    error: function (e) {
      console.log("ERROR: ", e);
    },
  });
}
function getCurrentBalance() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: url + "/api/blc/getbalance/" + userid,
    cache: false,
    timeout: 600000,
    success: function (res) {
      console.log("Total Exps: ", res);
      $("#current_balance").html('<i class="fa fa-inr"></i>' + res.total);
      balance_updated_time = new Date();
      updateLastUpdated();
    },
    error: function (e) {
      console.log("ERROR: ", e);
    },
  });
}
function updateLastUpdated() {
  $("#total_upteded_time").html(
    '<i class="feather icon-clock text-white f-14 m-r-10"></i>' +
      calculateUpdateTime(total_updated_time)
  );
  $("#balance_upteded_time").html(
    '<i class="feather icon-clock text-white f-14 m-r-10"></i>' +
      calculateUpdateTime(balance_updated_time)
  );
}

setInterval(updateLastUpdated, 10000);
