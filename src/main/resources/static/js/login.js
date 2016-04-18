var linkToLogin = document.getElementById("loginLink");
var authorizeURI;

$.get("/login", function(responseJSON) {
  var responseObject = JSON.parse(responseJSON);
  authorizeURI = responseObject;
  console.log(authorizeURI);
  linkToLogin.href = authorizeURI;
});
