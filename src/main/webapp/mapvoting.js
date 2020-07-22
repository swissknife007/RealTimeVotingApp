function getInput() {
  var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
  console.log(id);
  var route = "/id?id=" + id;
  try {
    fetch(route)
      .then((response) => response.json())
      .then((stats) => {
        if (stats.error) {
          document.getElementById("title").innerHTML = "404";
          var myobj = document.getElementById("btn-register");
          myobj.remove();

          return;
        }
        document.getElementById("title").innerHTML = stats.question;
    
        for (i = 0; i < stats.option.length; i++) {
          document.getElementById("voting").appendChild(document.createElement("br"));
          var y = document.createElement("INPUT");
          y.setAttribute("type", "radio");
          y.setAttribute("name", "choice");
          y.setAttribute("value", stats.option[i]);
          y.classList = "form-control";
          var label = document.createElement("label");
          label.appendChild(y);
          label.innerHTML += "<span> " + stats.option[i] + "</span><br>";
          document.getElementById("voting").appendChild(label);
        }
        $.getJSON("https://extreme-ip-lookup.com/json/", function (data) {
          ip = data.query;
          var url_string = window.location.href;
          var url = new URL(url_string);
          var id = url.searchParams.get("id");
          var form = document.getElementById("voting");
          var z = document.createElement("INPUT");
          z.setAttribute("type", "hidden");
          z.setAttribute("name", "roomID");
          z.setAttribute("value", id);
          form.appendChild(z);
          var x = document.createElement("INPUT");
          x.setAttribute("type", "hidden");
          x.setAttribute("name", "IP");
          x.setAttribute("value", ip);
          form.appendChild(x);
        });
      });
  } catch {
    document.getElementById("title").innerHTML = "404";
  }
}



function Copy() {
    alert("The URL of this page is: " + window.location.href + " and it was also copy in your clipboard!");
  var Url = document.getElementById("url");
  Url.innerHTML = window.location.href;
  console.log(Url.innerHTML);
  Url.select();
  document.execCommand("copy");
}