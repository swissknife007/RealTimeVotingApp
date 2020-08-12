const USA_lat = 37.0902;
const USA_lng = -95.7129;
let map;

function getInput() {
  var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
  var route = "/data?id=" + id;
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
        console.log(getUniqueString());
        document.getElementById("title").innerHTML = stats.question;
        var a = document.getElementById("similar");
        var link = document.createTextNode("here!");
        a.append(link);
        a.title = "here!";
        a.href =
          "https://www.quora.com/" +
          stats.mostSimilarQuestion.replace(/\s+/g, "-");
        for (i = 0; i < stats.option.length; i++) {
          document
            .getElementById("voting")
            .appendChild(document.createElement("br"));
          var y = document.createElement("INPUT");
          y.setAttribute("type", "radio");
          y.setAttribute("name", "choice");
          y.setAttribute("onclick", "enableSubmit()");
          // gets content portion of string if map type
          if (stats.questionType == "questionMap")
            y.setAttribute("value", stats.option[i].split(",")[2]);
          else y.setAttribute("value", stats.option[i]);

          y.classList = "form-control";
          var label = document.createElement("label");
          label.appendChild(y);
          if (stats.questionType == "questionPicture") {
            label.innerHTML += "<span> <img src='" + stats.option[i] + "'>";
          } else if (stats.questionType == "questionMap") {
            label.innerHTML +=
              "<span> " + stats.option[i].split(",")[2] + "</span><br>";
          } else {
            label.innerHTML += "<span> " + stats.option[i] + "</span><br>";
          }
          document.getElementById("voting").appendChild(label);
        }
        ip = getUniqueString();
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

        if (stats.questionType == "questionMap") {
          map = new google.maps.Map(document.getElementById("map"), {
            center: { lat: USA_lat, lng: USA_lng },
            zoom: 4,
          });

          for (i = 0; i < stats.option.length; i++) {
            var vals = stats.option[i].split(",");
            createDisplayMarker(vals[0], vals[1], vals[2]);
          }
        } else document.getElementById("map").remove();
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
  const marker = new google.maps.Marker({
    position: { lat: parseFloat(lat), lng: parseFloat(lng) },
    map: map,
  });

  const infoWindow = new google.maps.InfoWindow({ content: content });
  marker.addListener("click", () => {
    infoWindow.open(map, marker);
  });
}
function enableSubmit() {
  var submitButton = document.getElementById("vote-register");
  submitButton.removeAttribute("disabled");
}

function getUniqueString() {
  "use strict";

  var module = {
    options: [],
    header: [
      navigator.platform,
      navigator.userAgent,
      navigator.appVersion,
      navigator.vendor,
      window.opera,
    ],
    dataos: [
      { name: "Windows Phone", value: "Windows Phone", version: "OS" },
      { name: "Windows", value: "Win", version: "NT" },
      { name: "iPhone", value: "iPhone", version: "OS" },
      { name: "iPad", value: "iPad", version: "OS" },
      { name: "Kindle", value: "Silk", version: "Silk" },
      { name: "Android", value: "Android", version: "Android" },
      { name: "PlayBook", value: "PlayBook", version: "OS" },
      { name: "BlackBerry", value: "BlackBerry", version: "/" },
      { name: "Macintosh", value: "Mac", version: "OS X" },
      { name: "Linux", value: "Linux", version: "rv" },
      { name: "Palm", value: "Palm", version: "PalmOS" },
    ],
    databrowser: [
      { name: "Chrome", value: "Chrome", version: "Chrome" },
      { name: "Firefox", value: "Firefox", version: "Firefox" },
      { name: "Safari", value: "Safari", version: "Version" },
      { name: "Internet Explorer", value: "MSIE", version: "MSIE" },
      { name: "Opera", value: "Opera", version: "Opera" },
      { name: "BlackBerry", value: "CLDC", version: "CLDC" },
      { name: "Mozilla", value: "Mozilla", version: "Mozilla" },
    ],
    init: function () {
      var agent = this.header.join(" "),
        os = this.matchItem(agent, this.dataos),
        browser = this.matchItem(agent, this.databrowser);

      return { os: os, browser: browser };
    },
    matchItem: function (string, data) {
      var i = 0,
        j = 0,
        html = "",
        regex,
        regexv,
        match,
        matches,
        version;

      for (i = 0; i < data.length; i += 1) {
        regex = new RegExp(data[i].value, "i");
        match = regex.test(string);
        if (match) {
          regexv = new RegExp(data[i].version + "[- /:;]([\\d._]+)", "i");
          matches = string.match(regexv);
          version = "";
          if (matches) {
            if (matches[1]) {
              matches = matches[1];
            }
          }
          if (matches) {
            matches = matches.split(/[._]+/);
            for (j = 0; j < matches.length; j += 1) {
              if (j === 0) {
                version += matches[j] + ".";
              } else {
                version += matches[j];
              }
            }
          } else {
            version = "0";
          }
          return {
            name: data[i].name,
            version: parseFloat(version),
          };
        }
      }
      return { name: "unknown", version: 0 };
    },
  };

  var e = module.init(),
    debug = "";

  debug += e.os.name;
  debug += e.os.version;
  debug += e.browser.name;
  debug += e.browser.version;
  debug += navigator.userAgent;
  debug += navigator.appVersion;
  debug += navigator.platform;
  debug += navigator.vendor;
  return debug;
}
