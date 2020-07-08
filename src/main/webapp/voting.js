function getInput() {
  var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
  console.log(id);
  var route = "/id?id=" + id;
  fetch(route)
    .then((response) => response.json())
    .then((stats) => {
      console.log(stats);
      document.getElementById("title").innerHTML = stats.question;
      var y = document.createElement("INPUT");
      y.setAttribute("type", "radio");
      y.setAttribute("value", stats.option);
      var label = document.createElement("label");
      label.appendChild(y);
      label.innerHTML += "<span> " + stats.option + "</span><br>";
      document.getElementById("vote-form").appendChild(label);

      // for (i = 0; i < stats.option.length; i++) {
      //   var y = document.createElement("INPUT");
      //   y.setAttribute("type", "radio");
      //   y.setAttribute("value", stats.option);
      // }
    });
}
