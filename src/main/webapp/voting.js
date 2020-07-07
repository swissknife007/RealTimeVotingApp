function getInput() {
  var url_string = window.location.href;
  var url = new URL(url_string);
  var c = url.searchParams.get("id");
  console.log(c);

  fetch("/data?id=" + id)
    .then((response) => response.json())
    .then((stats) => {
      console.log(stats);
    });
}
