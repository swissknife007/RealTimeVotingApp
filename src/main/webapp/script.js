// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

let map;
/* Editable marker that displays when a user clicks in the map. */
let editMarker;

async function getData() {
  throw "Impliment getData";
}

function createListElement(text) {
  const liElement = document.createElement("li");
  liElement.innerText = text;
  return liElement;
}

window.onload = function () {
  document.getElementById("addOption").onclick = function () {
    var optionCell = document.getElementById("optionCell");
    var input = document.createElement("input");
    input.type = "text";
    input.id = "Options";
    input.name = "option";
    input.classList = "form-control";
    input.required = true;
    var br = document.createElement("br");
    br.id = "added";
    optionCell.appendChild(input);
    optionCell.appendChild(br);
  };
  var btn = document.getElementById("btn");
  btn.onclick = function () {
    document.getElementById("Options").remove();
    document.getElementById("added").remove();
  };

  var map_btn = document.getElementById("map_btn");
  map_btn.onclick = function () {
    if (document.getElementById('map') == null) {
      var container = document.getElementById("container");
      var div_map = document.createElement("div");
      div_map.id = "map";
      container.appendChild(div_map);

      map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 38.5949, lng: -94.8923}, zoom: 4});

      map.addListener('click', (event) => {
        createMarkerForEdit(event.latLng.lat(), event.latLng.lng());
      });
      //fetchMarkers();
  }};

  var map_del_btn = document.getElementById("map_del_btn");
  map_del_btn.onclick = function () {
    document.getElementById("map").remove();
  };
};

function searchRoom() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    const parameter = "searchBar";
    var parameterValue = url.searchParams.get(parameter);
    console.log("ParameterValue is " + parameterValue);

    var route = "/search?" + parameter + "=" + parameterValue;
    console.log("Route is " + route);
    fetch(route)
        .then((response) => response.json())
        .then((stats) => {
            console.log(stats);
            let tag = document.createElement("h2");
            if (stats == null)
            {
                let result = document.createTextNode("No matches found");
                tag.appendChild(result);
                let div = document.getElementById("searchResults");
                div.appendChild(tag);
            }
            else
            {
                let address = location.protocol + '//' + location.host;
                let absoluteURL = new URL(address);
                absoluteURL.pathname = "votePage.html";
                let queryParameter = "?id=";
                let i = 0;
                let x = "<ul>";
                for(i in stats.RoomID)
                {   
                    x+= "<li><a href='"+absoluteURL + queryParameter + stats.RoomID[i].ID + "'/>" + stats.RoomID[i].Title +"</a></li>";
                    i++;
                }
                x+= "</ul>";
               document.getElementById("searchResults").innerHTML = x; 
                console.log("You have " + i + " questions found");
            }    
            });
}


//Creates a marker that shows a textbox the user can edit
function createMarkerForEdit(lat, lng) {
  // If we're already showing an editable marker, then remove it.
  if (editMarker) {
    editMarker.setMap(null);
  }

  editMarker =
    new google.maps.Marker({position: {lat: lat, lng: lng}, map: map});
}/*
  const infoWindow =
    new google.maps.InfoWindow({content: buildInfoWindowInput(lat, lng)});

  // When the user closes the editable info window, remove the marker.
  google.maps.event.addListener(infoWindow, 'closeclick', () => {
    editMarker.setMap(null);
  });

  infoWindow.open(map, editMarker);
}

//Creates an editable textbox and a submit button for marker
function buildInfoWindowInput(lat, lng) {
  const textBox = document.createElement('textarea');
  const button = document.createElement('button');
  button.appendChild(document.createTextNode('Submit Marker'));

  button.onclick = () => {
    postMarker(lat, lng, textBox.value);
    createMarkerForDisplay(lat, lng, textBox.value);
    editMarker.setMap(null);
  };

  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);

  return containerDiv;
}*/


