const USA_lat = 37.0902;
const USA_lng = -95.7129;
let map;

function getInput() {
  var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
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
        var a = document.getElementById("similar");
        var link = document.createTextNode("here!"); 
        a.append(link);
        a.title = "here!";  
        //a.href = "https://www.quora.com/" + stats.mostSimilarQuestion.replace(/\s+/g, '-');  
        for (i = 0; i < stats.option.length; i++) {
          document
            .getElementById("voting")
            .appendChild(document.createElement("br"));
          var y = document.createElement("INPUT");
          y.setAttribute("type", "radio");
          y.setAttribute("name", "choice");
          // gets content portion of string if map type
          if (stats.questionType == "questionMap")
            y.setAttribute("value", stats.option[i].split(",")[2]);
          else
              y.setAttribute("value", stats.option[i]);

          y.classList = "form-control";
          var label = document.createElement("label");
          label.appendChild(y);
          if (stats.questionType == "questionPicture") {
            label.innerHTML+= "<span> <img src='" + stats.option[i] + "'>";
          } else if (stats.questionType == "questionMap") {
            label.innerHTML += "<span> " + stats.option[i].split(",")[2] + "</span><br>";
          } else {
            label.innerHTML += "<span> " + stats.option[i] + "</span><br>";
          }
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
        /*if (stats.questionType.equals("questionMap")) {
            map = new google.maps.Map(
                document.getElementById('map'),
                {center: {lat: USA_lat, lng: USA_lng}, zoom: 4});

            for (i = 0; i < stats.option.length; i++) {
                String [] vals = stats.option.length[i].split(",", 0);
                createDisplayMarker(vals[0], vals[1], vals[2]);
            }
            
        }*/
      });
  } catch {
    document.getElementById("title").innerHTML = "404";
  }
}

function Copy() {
  alert(
    "The URL of this page is: " +
      window.location.href +
      " and it was also copy in your clipboard!"
  );
  var Url = document.getElementById("url");
  Url.innerHTML = window.location.href;
  console.log(Url.innerHTML);
  Url.select();
  document.execCommand("copy");
}

function createDisplayMarker(lat, lng, content) {
  const marker =
      new google.maps.Marker({position: {lat: lat, lng: lng}, map: map});

  const infoWindow = new google.maps.InfoWindow({content: content});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}
