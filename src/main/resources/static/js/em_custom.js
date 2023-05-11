$(document).ready(function () {
  var currentUrl = window.location.href;

  // iterate through each submenu item
  $(".pcoded-item .li li").each(function () {
    // check if the link href matches the current URL
    if ($(this).find("a").attr("href") == currentUrl) {
      // if it matches, add the active class to this submenu item
      $(this).addClass("active");

      // add the active class to the parent menu item as well
      $(this).parents(".pcoded-hasmenu").addClass("active");
    } else {
      // if it does not match, remove the active class from this submenu item
      $(this).removeClass("active");
    }
  });
});
