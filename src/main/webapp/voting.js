function getInput() {
  var url_string = window.location.href;
  var url = new URL(url_string);
  var c = url.searchParams.get("id");
  console.log(c);

  fetch("/data?id=" + id)
    .then((response) => response.json())
    .then((stats) => {
      document.getElementById("feed").innerHTML = stats.title;
      var from = document.getElementById("vote-form");

      for (i = 0; i < stats.options.length; i++) {
        var y = document.createElement("INPUT");
        y.setAttribute("type", "radio");
        y.setAttribute("value", stats.option);
      }
    });
}
